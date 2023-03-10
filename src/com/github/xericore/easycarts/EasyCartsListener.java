package com.github.xericore.easycarts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

public class EasyCartsListener implements Listener
{

	public final Logger logger = Logger.getLogger("Minecraft");
	public static EasyCarts easyCartsPlugin;
	private static FileConfiguration config = null;

	// Max speed that a minecart can have before it derails in curves or stops on upward slopes
	private static final double MAX_SAFE_DERAIL_SPEED = 0.4D;
	// Max speed that a minecart can have before it detection of intersection fails
	private static final double MAX_SAFE_INTERSECTION_SPEED = 1.0D;
	private static final int BLOCKS_LOOK_AHEAD = 3;

	private HashMap<UUID, Double> previousSpeed = new HashMap<UUID, Double>();
	private HashSet<UUID> slowedCarts = new HashSet<UUID>();

	// Needed to automatically delete newly created carts on intersections.
	private HashSet<UUID> removeOnExitMinecartIds = new HashSet<UUID>();

	private HashMap<UUID, SpeedAndYaw> stoppedCarts = new HashMap<UUID, SpeedAndYaw>();

	public EasyCartsListener(EasyCarts theInstance)
	{
		easyCartsPlugin = theInstance;
		config = easyCartsPlugin.getConfig();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMyVehicleCreate(VehicleCreateEvent event)
	{
		RideableMinecart cart = Utils.getValidMineCart(event.getVehicle(), false);
		if (cart == null)
			return;

		if (config.getBoolean("RemoveMinecartOnExit"))
			removeOnExitMinecartIds.add(cart.getUniqueId());

		if (config.getDouble("MaxSpeedPercent") > 0)
		{
			cart.setMaxSpeed(Utils.MINECART_VANILLA_MAX_SPEED * config.getDouble("MaxSpeedPercent") / 100);
		}

		cart.setSlowWhenEmpty(config.getBoolean("SlowWhenEmpty"));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMyVehicleMove(VehicleMoveEvent event)
	{

		try
		{
			RideableMinecart cart = Utils.getValidMineCart(event.getVehicle(), true);
			if (cart == null)
				return;

			Vector cartVelocity = cart.getVelocity();

			UUID cartId = cart.getUniqueId();
			Location cartLocation = cart.getLocation();
			Block blockUnderCart = cartLocation.getBlock();

			// We won't do anything if there's no rail under the cart
			Rails railUnderCart = null;
			try
			{
				railUnderCart = (Rails) blockUnderCart.getState().getData();
			} catch (ClassCastException e)
			{
				return;
			}

			if (!config.getBoolean("MinecartCollisions"))
			{
				pushNearbyEntities(cart, cartLocation);
			}

			// ------------------------------- SLOW DOWN CART IF CART IS APPROACHING A SLOPE OR A CURVE -----------------------------

			if (!slowedCarts.contains(cartId))
			{
				Location locationInFront = cartLocation.clone();
				Vector cartDirection = cartVelocity.clone().normalize();
				for (int i = 1; i < BLOCKS_LOOK_AHEAD; i++)
				{
					locationInFront.add(cartDirection.multiply(i));
					Rails railInFront = Utils.getRailInFront(locationInFront);

					if (railInFront != null)
					{
							if (railInFront.isCurve() || railInFront.isOnSlope()) {
								if (railUnderCart.isOnSlope() && Utils.isMovingDown(event)) {
									// Don't do anything if we are on a downward slope
									return;
								} else {
									previousSpeed.put(cartId, cart.getVelocity().length());
									slowedCarts.add(cartId);
									slowDownCart(cart, MAX_SAFE_DERAIL_SPEED);
									return;
								}
							} else if ((cartVelocity.length() > MAX_SAFE_INTERSECTION_SPEED)
									&& Utils.isIntersection(locationInFront, cartDirection)) {
								// Slow down before intersections, otherwise the VehicleMoveEvent might
								// come too late and we will miss the intersection
								slowDownCart(cart, MAX_SAFE_INTERSECTION_SPEED);
								return;
							}
						}
				}
			}

			if (railUnderCart.isCurve() || railUnderCart.isOnSlope())
			{
				if (config.getBoolean("AutoBoostOnSlope") && railUnderCart.isOnSlope() && Utils.isMovingUp(event))
				{
					// Speed up carts on rising slopes so they don't slow down as quickly.
					cart.setVelocity(cartVelocity.multiply(config.getDouble("MaxPushSpeedPercent")));
				}
				slowedCarts.remove(cartId);
				return;
			} else if (!slowedCarts.contains(cartId) && previousSpeed.containsKey(cartId))
			{
				// If it is passed, set it back to its original speed
				cart.setMaxSpeed(Utils.MINECART_VANILLA_MAX_SPEED * config.getDouble("MaxSpeedPercent") / 100);
				Vector newVel = cart.getVelocity().normalize().multiply(previousSpeed.get(cartId));
				cart.setVelocity(newVel);
				previousSpeed.remove(cartId);
			}

			boostCartOnPoweredRails(cart, blockUnderCart);

			// _/\__/\__/\__/\__/\__/\__/\__/\_ STOP MINECARTS AT INTERSECTIONS _/\__/\__/\__/\__/\__/\__/\__/\__/\__/\__/\__/\__/\_

			if (Double.isNaN(cartVelocity.length()))
				return; // Otherwise we will not stop at intersection

			// X+ = EAST | X- = WEST
			// Z+ = SOUTH | Z- = NORTH

			if (stoppedCarts.containsKey(cart.getUniqueId()) && (cartVelocity.lengthSquared() > 0))
			{
				continueCartAfterIntersection(cart);
				return;
			}

			if(!config.getBoolean("ContinueOnCurve")) {
				if (Utils.isIntersection(cartLocation, cartVelocity)) {
					stopCartAndShowMessageToPlayer(cart);
				}
			}
		} catch (Exception e)
		{
			logger.severe("Error in onMyVehicleMove.");
			logger.severe(e.toString());
		}
	}

	private void pushNearbyEntities(RideableMinecart cart, Location cartLocation)
	{
		// To avoid collision, the entity must be located at least 1.0 block away from the cart.
		// The entities will be moved to this distance if they are within the search box when the cart is moving.
		// We actually move the entity a little bit further, to avoid it moving right back into the search box.

		// We cannot use cart.getVelocity() because on diagonal rails, this returns +x,0,0 then 0,0,+z and then diagonal
		// (depending on movement direction).
		// Then it looks like we are moving e.g. left, then right, then diagonal and we cannot distinguish between this
		// and a real straight movement.
		// Luckily, the getLocation().getDirection() is unaffected by this. However, for some unknown reason we have to
		// rotate that vector 90 degree clockwise to get the correct direction.
		Vector cartVector = (new Vector(-cart.getLocation().getDirection().getZ(), 0, cart.getLocation().getDirection().getX()))
				.normalize();

		Vector velocityNormalRight = new Vector(-cartVector.getZ(), 0, cartVector.getX());
		Vector velocityNormalLeft = new Vector(cartVector.getZ(), 0, -cartVector.getX());

		// Adjust size of box to be between 1-2, depending on movement direction
		List<Entity> nearbyEntities = cart.getNearbyEntities(1 + Math.abs(cartVector.getX()), 1, 1 + Math.abs(cartVector.getZ()));

		for (Entity entity : nearbyEntities)
		{
			if (((entity instanceof Monster) || (entity instanceof Animals) || (entity instanceof NPC)))
			{
				// Only move monsters, animals and NPCs, not players
				if (!entity.isInsideVehicle())
				{
					// Entity is not in a minecart, thus we can move it

					Location entityLocation = entity.getLocation();

					// The vector between the current cart location and the entity location, needed to determine which direction to
					// move the entity to.
					Vector cartToEntity = new Vector(entityLocation.getX() - cartLocation.getX(), 0,
							entityLocation.getZ() - cartLocation.getZ());

					// The cross product vector will point up- or downwards depending on the location of the second vector
					if (cartVector.crossProduct(cartToEntity).getY() > 0)
					{
						entity.teleport(entityLocation.add(velocityNormalLeft.multiply(0.5)));
					} else
					{
						entity.teleport(entityLocation.add(velocityNormalRight.multiply(0.5)));
					}
				}
			} else if ((entity instanceof Minecart) && entity.isEmpty())
			{
				// Remove empty minecarts still on track
				entity.remove();
			}
		}
	}

	private void slowDownCart(RideableMinecart cart, double maxSpeed)
	{
		cart.setVelocity(cart.getVelocity().clone().normalize().multiply(maxSpeed));
		cart.setMaxSpeed(maxSpeed);
	}

	private void continueCartAfterIntersection(RideableMinecart cart)
	{
		// Cart must jump one block in the desired direction
		// Desired direction is player look direction
		SpeedAndYaw beforeStop = stoppedCarts.get(cart.getUniqueId());
		Entity firstPassenger = Utils.GetFirstPassenger(cart);

		if (firstPassenger == null)
			return;

		Vector locationOffset = Utils.getUnitVectorFromYaw(firstPassenger.getLocation().getYaw());

		// The new cart location will be the old location +1 block in the player look direction.
		Location newCartLocation = cart.getLocation().clone().add(locationOffset);

		if (newCartLocation.getBlock().getState().getType() == Material.RAIL)
		{
			if (beforeStop.getSpeed() < 0.1d)
			{
				beforeStop.setSpeed(0.1d);
			}
			cart.setVelocity(locationOffset.clone().multiply(beforeStop.getSpeed()));
			teleportMineCart(cart, newCartLocation, firstPassenger.getLocation(), beforeStop.getDirection());
			stoppedCarts.remove(cart.getUniqueId());
		}
	}

	private void boostCartOnPoweredRails(RideableMinecart cart, Block blockUnderCart)
	{
		Vector cartVelocity = cart.getVelocity();
		Double cartSpeed = cartVelocity.length();

		boolean isPoweredBlock = (blockUnderCart.getType() == Material.POWERED_RAIL);
		// Only boost carts if they have not been slowed down by slopes already.
		// This disables boosters that are placed within BLOCKS_LOOK_AHEAD blocks before slopes or curves.
		// Also, boosters on slopes will only apply the default minecraft boost, because if we make them stronger here, the cart
		// reverses.
		if (isPoweredBlock && !slowedCarts.contains(cart.getUniqueId()))
		{
			cart.setMaxSpeed(Utils.MINECART_VANILLA_MAX_SPEED * config.getDouble("MaxSpeedPercent") / 100);
			cartVelocity.multiply(config.getDouble("PoweredRailBoostPercent") / 100);
			cart.setVelocity(cartVelocity);
		} else if (cartSpeed < (Utils.MINECART_VANILLA_PUSH_SPEED * config.getDouble("MaxPushSpeedPercent") / 100))
		{
			// Boost default/auto minecart speed
			cart.setVelocity(cartVelocity.multiply(config.getDouble("MaxPushSpeedPercent") / 100));
		}
	}

	private void stopCartAndShowMessageToPlayer(RideableMinecart cart)
	{
		stoppedCarts.put(cart.getUniqueId(), new SpeedAndYaw(cart.getVelocity().length(), cart.getVelocity().normalize()));
		cart.setVelocity(new Vector(0, 0, 0));
		if (config.getBoolean("ShowIntersectionMessage"))
		{
			Entity firstPassenger = Utils.GetFirstPassenger(cart);
			if (firstPassenger == null)
				return;

			Player player = (Player) firstPassenger;
			TextComponent actionbarText = new TextComponent(config.getString("IntersectionMessageText"));
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbarText);
		}
	}

	private void teleportMineCart(final Minecart cart, Location destination, Location oldPlayerLocation, Vector oldDirection)
	{
		// Teleport destination must be in the center of a minecart track to prevent the cart from derailing.
		// Thus depending on the facing of the destination either X or Z values must be n,5.
		Vector destinationVector = new Vector(destination.getX(), destination.getY(), destination.getZ());
		destinationVector.multiply(oldDirection); // 0, 0, -138.7

		// Only X or Z are relevant
		// Make sure new cart location is on center of a minecart rail
		destination.setX(new Double(destination.getX()).intValue() + (destination.getX() >= 0 ? 0.5 : -0.5));
		destination.setZ(new Double(destination.getZ()).intValue() + (destination.getZ() >= 0 ? 0.5 : -0.5)); // -138,7 -> -138,5

		final Minecart toCart = cart.getWorld().spawn(destination, RideableMinecart.class);
		final Entity passenger = Utils.GetFirstPassenger(cart);
		if (passenger == null)
			return;

		if (passenger != null)
		{
			cart.eject();
			destination.setY(oldPlayerLocation.getY()); // prevents Y-bump when teleporting
			destination.setPitch(oldPlayerLocation.getPitch()); // preserve player orientation after teleport
			destination.setYaw(oldPlayerLocation.getYaw());

			passenger.teleport(destination);
			Bukkit.getScheduler().runTask(easyCartsPlugin, new Runnable()
			{
				@Override
				public void run()
				{
					toCart.addPassenger(passenger);
					passenger.setVelocity(cart.getVelocity());
				}
			});
		}
		toCart.getLocation().setYaw(cart.getLocation().getYaw());
		toCart.getLocation().setPitch(cart.getLocation().getPitch());
		toCart.setVelocity(cart.getVelocity()); // speed of newly spawned minecart will be speed of old cart before the stop.
		// Make sure newly spawned cart after intesection will be correctly removed.
		if (config.getBoolean("RemoveMinecartOnExit"))
			removeOnExitMinecartIds.add(toCart.getUniqueId());
		// Old cart must be removed from list as well, otherwise it will stay in memory.
		removeOnExitMinecartIds.remove(cart.getUniqueId());
		cart.remove();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDismount(final VehicleExitEvent event)
	{

		if (!(event.getVehicle() instanceof RideableMinecart))
			return;

		UUID cartId = event.getVehicle().getUniqueId();
		// Clean up data to avoid having dead entries in the maps when user dismounts at intersections or on slopes
		previousSpeed.remove(cartId);
		slowedCarts.remove(cartId);
		stoppedCarts.remove(cartId);

		if (removeOnExitMinecartIds.contains(cartId) && config.getBoolean("RemoveMinecartOnExit"))
		{
			Bukkit.getScheduler().runTaskLater(easyCartsPlugin, new Runnable()
			{
				@Override
				public void run()
				{
					event.getVehicle().remove();
					removeOnExitMinecartIds.remove(event.getVehicle().getUniqueId());
				}
			}, 2L);
		}
	}

	/**
	 * Unfortunately at this point the cart's speed is already 0. Thus it isn't possible to determine the speed of the cart before the
	 * collision (at least not without resource intense saving of speed every tick in VehicleMoveEvent).
	 * 
	 * The work around is to move the entities before they collide with the cart. Other players will not be moved. However, if a player
	 * blocks the path, the cart will come to a stop.
	 * 
	 * See https://bukkit.org/threads/trouble-cancelling-vehicleentitycollisionevent.285269/
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onVehicleCollision(final VehicleEntityCollisionEvent event)
	{
		RideableMinecart cart = Utils.getValidMineCart(event.getVehicle(), true);
		if (cart == null)
		{
			return;
		}
		if (!config.getBoolean("MinecartCollisions") && event.getEntity() instanceof Player)
		{
			// This will cause the cart to stop
			event.setCancelled(true);
			event.setCollisionCancelled(true);
			event.setPickupCancelled(true);
		}
	}

}

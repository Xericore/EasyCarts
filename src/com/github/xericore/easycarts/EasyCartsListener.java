package com.github.xericore.easycarts;

import com.github.xericore.easycarts.data.RailsAhead;
import com.github.xericore.easycarts.data.TracedRail;
import com.github.xericore.easycarts.utilities.CartSpeed;
import com.github.xericore.easycarts.utilities.RailTracer;
import com.github.xericore.easycarts.utilities.RailUtils;
import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class EasyCartsListener implements Listener
{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static EasyCarts easyCartsPlugin;
	private static FileConfiguration config = null;
	private final RailTracer _railTracer;

	private HashMap<UUID, Double> previousSpeed = new HashMap<UUID, Double>();

	// Needed to automatically delete newly created carts on intersections.
	private HashSet<UUID> removeOnExitMinecartIds = new HashSet<UUID>();
	private HashMap<UUID, SpeedAndYaw> cartsAtIntersection = new HashMap<UUID, SpeedAndYaw>();


	public EasyCartsListener(EasyCarts theInstance)
	{
		easyCartsPlugin = theInstance;
		config = easyCartsPlugin.getConfig();
		_railTracer = new RailTracer();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMyVehicleCreate(VehicleCreateEvent event)
	{
		RideableMinecart cart = Utils.getRideableMineCart(event.getVehicle());
		if (cart == null)
			return;

		if (config.getBoolean("RemoveMinecartOnExit"))
			removeOnExitMinecartIds.add(cart.getUniqueId());

		if (config.getDouble("MaxSpeedPercent") > 0)
		{
			cart.setMaxSpeed(CartSpeed.MINECART_VANILLA_MAX_SPEED * config.getDouble("MaxSpeedPercent") / 100);
		}

		cart.setSlowWhenEmpty(config.getBoolean("SlowWhenEmpty"));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMyVehicleMove(VehicleMoveEvent event)
	{
		try
		{
			RideableMinecart cart = Utils.getRideableMineCartWithPlayerInside(event.getVehicle());

			if(cart == null || !RailUtils.isCartOnRails(cart))
				return;

			if (!config.getBoolean("MinecartCollisions"))
				Utils.pushNearbyEntities(cart);

			if(Utils.isCartSlowAndDirectionUnknown(cart))
			{
				autoPushCart(cart);
				return;
			}

			boolean isCartStoppedAtIntersection = handleCartsAtIntersection(cart);
			if (isCartStoppedAtIntersection)
				return;

			BlockFace cartFacing = Utils.getCartBlockFaceDirection(cart);

			if(cartFacing == null)
				return;

			Block blockUnderCart = cart.getLocation().getBlock();

			List<TracedRail> tracedRails = _railTracer.traceRails(blockUnderCart, cartFacing, 5);

			RailsAhead railsAhead = RailUtils.getRailsAhead(tracedRails, 3);

			switch (railsAhead)
			{
				case Derailing:
                    // Wait for next vehicle move event if we are on a downward slope
					if (RailUtils.isSlopedRail(blockUnderCart) && Utils.isCartMovingDown(event))
						return;

					if(!isCartSlowedDown(cart))
					{
						slowDownCart(cart);
						return;
					}
					break;
				case Intersection:
					// Slow down before intersections, otherwise the VehicleMoveEvent might
					// come too late and we will miss the intersection
					CartSpeed.setCartSpeedToAvoidMissingIntersection(cart);
					break;
                case SafeForSpeedup:
                    if (isCartSlowedDown(cart))
                    {
                        restoreCartSpeed(cart);
                        return;
                    }
                    else
                    {
						if (blockUnderCart.getType() == Material.POWERED_RAIL)
							boostCartOnPoweredRail(cart);
						else
							autoPushCart(cart);
                    }
                    break;
			}

			if (RailUtils.isSlopedRail(blockUnderCart))
			{
				if (config.getBoolean("AutoBoostOnSlope") && Utils.isMovingUp(event))
				{
					// Speed up carts on rising slopes so they don't slow down as quickly.
					cart.setVelocity(cart.getVelocity().multiply(config.getDouble("MaxPushSpeedPercent")));
				}
				previousSpeed.remove(cart.getUniqueId());
				return;
			}
		}
		catch (Exception e)
		{
			logger.severe("Error in onMyVehicleMove.");
			logger.severe(e.toString());
		}
	}

	private void boostCartOnPoweredRail(RideableMinecart cart)
	{
		double maxSpeed = CartSpeed.MINECART_VANILLA_MAX_SPEED * config.getDouble("MaxSpeedPercent") / 100;
		cart.setMaxSpeed(maxSpeed);
		Vector boostedVelocity = cart.getVelocity().clone().multiply(config.getDouble("PoweredRailBoostPercent") / 100);

		boostedVelocity = boostedVelocity.length() > maxSpeed ? boostedVelocity.normalize().multiply(maxSpeed) : boostedVelocity;

		cart.setVelocity(boostedVelocity);

		previousSpeed.remove(cart.getUniqueId());
	}

	private boolean handleCartsAtIntersection(RideableMinecart cart)
	{
		Vector cartVelocity = cart.getVelocity();

		// Otherwise we will not stop at intersection
		if (Double.isNaN(cartVelocity.length()))
			return true;

		boolean didPlayerNudgeCartForward = cartVelocity.lengthSquared() > 0;

		if (isCartAtIntersection(cart) && didPlayerNudgeCartForward)
		{
			continueCartAfterIntersection(cart);
			return true;
		}

		if (RailUtils.isIntersection(cart.getLocation()))
			stopCartAndShowMessageToPlayer(cart);

		return false;
	}

	private void slowDownCart(RideableMinecart cart)
	{
		previousSpeed.put(cart.getUniqueId(), cart.getVelocity().length());
		CartSpeed.setCartSpeedToAvoidDerailing(cart);
	}

	private boolean isCartSlowedDown(RideableMinecart cart)
	{
		return previousSpeed.containsKey(cart.getUniqueId());
	}

	private boolean isCartAtIntersection(RideableMinecart cart)
	{
		return cartsAtIntersection.containsKey(cart.getUniqueId());
	}

	private void restoreCartSpeed(RideableMinecart cart)
	{
		UUID cartId = cart.getUniqueId();

		if(!previousSpeed.containsKey(cartId))
			return;

		cart.setMaxSpeed(CartSpeed.MINECART_VANILLA_MAX_SPEED * config.getDouble("MaxSpeedPercent") / 100);
		Vector newVel = cart.getVelocity().normalize().multiply(previousSpeed.get(cartId));
		cart.setVelocity(newVel);

		previousSpeed.remove(cartId);
	}

	private void continueCartAfterIntersection(RideableMinecart cart)
	{
		// Cart must jump one block in the desired direction
		// Desired direction is player look direction
		SpeedAndYaw beforeStop = cartsAtIntersection.get(cart.getUniqueId());
		Entity firstPassenger = Utils.GetFirstPassenger(cart);

		if (firstPassenger == null)
			return;

		Vector locationOffset = Utils.getStraightUnitVectorFromYaw(firstPassenger.getLocation().getYaw());

		// The new cart location will be the old location +1 block in the player look direction.
		Location newCartLocation = cart.getLocation().clone().add(locationOffset);

		if (newCartLocation.getBlock().getState().getType() == Material.RAIL)
		{
			if (beforeStop.getSpeed() < 0.1d)
				beforeStop.setSpeed(0.1d);

			cart.setVelocity(locationOffset.clone().multiply(beforeStop.getSpeed()));
			teleportMineCart(cart, newCartLocation, firstPassenger.getLocation(), beforeStop.getDirection());
			cartsAtIntersection.remove(cart.getUniqueId());
		}
	}

	private void autoPushCart(RideableMinecart cart)
	{
		Double cartSpeed = cart.getVelocity().length();

		double autoPushMultiplier = config.getDouble("MaxPushSpeedPercent") / 100;

		if (cartSpeed < CartSpeed.MINECART_VANILLA_PUSH_SPEED * autoPushMultiplier)
			cart.setVelocity(cart.getVelocity().clone().multiply(autoPushMultiplier));
	}

	private void stopCartAndShowMessageToPlayer(RideableMinecart cart)
	{
		cartsAtIntersection.put(cart.getUniqueId(), new SpeedAndYaw(cart.getVelocity().length(), cart.getVelocity().normalize()));
		cart.setVelocity(new Vector(0, 0, 0));
		if (config.getBoolean("ShowIntersectionMessage"))
		{
			Entity firstPassenger = Utils.GetFirstPassenger(cart);
			if (firstPassenger == null)
				return;

			firstPassenger.sendMessage(ChatColor.GRAY + config.getString("IntersectionMessageText"));
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
		// Make sure newly spawned cart after intersection will be correctly removed.
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
		cartsAtIntersection.remove(cartId);

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
}

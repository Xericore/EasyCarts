package com.github.xericore.easycarts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

public class EasyCartsListener implements Listener {

	public final Logger logger = Logger.getLogger("Minecraft");
	public static EasyCarts eCarts;

	private static final double MINECART_VANILLA_PUSH_SPEED = 0.2D;
	private static final double MINECART_VANILLA_MAX_SPEED = 0.4D;
	
	// Max speed that a minecart can have before it derails in curves or stops on upward slopes
	private static final double MAX_SAFE_DERAIL_SPEED = 0.499D;
	// Max speed that a minecart can have before it detection of intersection fails
	private static final double MAX_SAFE_INTERSECTION_SPEED = 1.0D;
	private static final int BLOCKS_LOOK_AHEAD = 3;

	private HashMap<UUID, Double> previousSpeed = new HashMap<UUID, Double>();
	private HashSet<UUID> slowedCarts = new HashSet<UUID>();

	// Needed to automatically delete newly created carts on intersections.
	private HashSet<UUID> removeOnExitMinecartIds = new HashSet<UUID>();
	
	private HashMap<UUID, SpeedAndYaw> stoppedCarts = new HashMap<UUID, SpeedAndYaw>();

	public EasyCartsListener(EasyCarts theInstance) {
		eCarts = theInstance;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMyVehicleCreate(VehicleCreateEvent event) {
		RideableMinecart cart = getValidMineCart(event.getVehicle(), false);
		if (cart == null)
			return;

		if (eCarts.getConfig().getDouble("MaxSpeedPercent") > 0) {
			cart.setMaxSpeed(MINECART_VANILLA_MAX_SPEED * eCarts.getConfig().getDouble("MaxSpeedPercent") / 100);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMyVehicleMove(VehicleMoveEvent event) {

		try {
			RideableMinecart cart = getValidMineCart(event.getVehicle(), true);
			if (cart == null)
				return;

			Vector cartVelocity = cart.getVelocity();
			Double currentSpeed = cartVelocity.length();

			UUID id = cart.getUniqueId();
			Location cartLocation = cart.getLocation();
			Block blockUnderCart = cartLocation.getBlock();

			// ------------------------------- 	SLOW DOWN CART IF CART IS APPROACHING A SLOPE OR A CURVE 	-----------------------------

			Rails railUnderCart = null;
			try {
				railUnderCart = (Rails) blockUnderCart.getState().getData();
			} catch (ClassCastException e) {
				railUnderCart = null;
			}
			if (railUnderCart != null) {
				if (!slowedCarts.contains(id)) {
					Location testLoc = cartLocation.clone();
					Location testLocUnder;
					Vector tempVelocity = cartVelocity.clone().normalize();
					for (int i = 1; i < BLOCKS_LOOK_AHEAD; i++) {
						testLoc.add(tempVelocity.multiply(i));
						// Slopes that go down/fall have the blocks underneath the current y-level
						testLocUnder = testLoc.clone().subtract(0, 1, 0);
						Rails testRail = null;
						try {
							if (testLoc.getBlock().getType() == Material.RAILS) {
								// Detects rising slope
								testRail = (Rails) testLoc.getBlock().getState().getData();
							} else if (testLocUnder.getBlock().getType() == Material.RAILS) {
								// Detects falling slope
								testRail = (Rails) testLocUnder.getBlock().getState().getData();
							}
						} catch (ClassCastException e) {
							break;
						}

						if (testRail != null) {
							// all of this code tests if there is a curve or slope slightly ahead of the cart.
							if (testRail.isCurve() || testRail.isOnSlope()) {
								previousSpeed.put(id, currentSpeed);
								slowedCarts.add(id);
								cart.setVelocity(cartVelocity.clone().normalize().multiply(MAX_SAFE_DERAIL_SPEED));
								cart.setMaxSpeed(MAX_SAFE_DERAIL_SPEED);
								return;
							} else if ((currentSpeed > MAX_SAFE_INTERSECTION_SPEED) && isIntersection(testLoc, tempVelocity)) {
								// Slow down before intersections, otherwise the VehicleMoveEvent might 
								// come too late and we will miss the intersection
								cart.setVelocity(cartVelocity.clone().normalize().multiply(MAX_SAFE_INTERSECTION_SPEED));
								cart.setMaxSpeed(MAX_SAFE_INTERSECTION_SPEED);
								return;
							}
						}
					}
				}

				Double originalSpeed = null;
				try {
					originalSpeed = previousSpeed.get(id);
				} catch (NullPointerException e) {
					originalSpeed = null;
				}

				if (railUnderCart.isCurve() || railUnderCart.isOnSlope()) {
					// Speed up carts on slopes so they don't slow down as quickly. 
					cart.setVelocity(cartVelocity.multiply(eCarts.getConfig().getDouble("MaxPushSpeedPercent")));
					if (slowedCarts.contains(id)) {
						slowedCarts.remove(id);
					}
					return;
				} else if (!slowedCarts.contains(id) && originalSpeed != null) {
					// If it is passed, set it back to its original speed
					cart.setMaxSpeed(MINECART_VANILLA_MAX_SPEED * eCarts.getConfig().getDouble("MaxSpeedPercent") / 100);
					Vector newVel = cart.getVelocity().normalize().multiply(originalSpeed);
					cart.setVelocity(newVel);
					previousSpeed.remove(id);
				}
			}

			// ----------------------------------------------------------------------------------------------------------------------------

			if (blockUnderCart == null || (blockUnderCart.getType() != Material.RAILS && blockUnderCart.getType() != Material.POWERED_RAIL))
				return;

			// Boost default minecart speed
			if (cartVelocity.length() < (MINECART_VANILLA_PUSH_SPEED * eCarts.getConfig().getDouble("MaxPushSpeedPercent") / 100)) { 
				cart.setVelocity(cartVelocity.multiply(eCarts.getConfig().getDouble("MaxPushSpeedPercent") / 100));
			}

			// ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ 		Boost minecart when it passes over a powered rail ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~  
			boolean isPowered = (blockUnderCart.getData() & 8) != 0;
			if (isPowered) {
				cartVelocity.multiply(eCarts.getConfig().getDouble("PoweredRailBoostPercent") / 100);
				cart.setMaxSpeed(MINECART_VANILLA_MAX_SPEED * eCarts.getConfig().getDouble("MaxSpeedPercent") / 100);
				cart.setVelocity(cartVelocity);
				return;
			}
			
			// _/\__/\__/\__/\__/\__/\__/\__/\_		STOP MINECARTS AT INTERSECTIONS		_/\__/\__/\__/\__/\__/\__/\__/\__/\__/\__/\__/\__/\_

			if (Double.isNaN(cartVelocity.length()))
				return; // Otherwise we will not stop at intersection

			// X+ = EAST | X- = WEST
			// Z+ = SOUTH | Z- = NORTH

			if (stoppedCarts.containsKey(cart.getUniqueId()) && (cartVelocity.lengthSquared() > 0)) {
				// Cart must jump one block in the desired direction
				// Desired direction is player look direction
				SpeedAndYaw beforeStop = stoppedCarts.get(cart.getUniqueId());
				Vector locationOffset = Utils.getUnitVectorFromYaw(cart.getPassenger().getLocation().getYaw());
				
				// The new cart location will be the old location +1 block in the player look direction.
				Location newCartLocation = cart.getLocation().clone().add(locationOffset);

				if (newCartLocation.getBlock().getState().getType() == Material.RAILS) {
					if (beforeStop.getSpeed() < 0.1d) {
						beforeStop.setSpeed(0.1d);
					}
					cart.setVelocity(locationOffset.clone().multiply(beforeStop.getSpeed()));
					teleportMineCart(cart, newCartLocation, cart.getPassenger().getLocation(), beforeStop.getDirection());
					stoppedCarts.remove(cart.getUniqueId());
				}
				return;
			}

			if (isIntersection(cartLocation, cartVelocity)) {
				stoppedCarts.put(cart.getUniqueId(), new SpeedAndYaw(cart.getVelocity().length(), cartVelocity.normalize()));
				cart.setVelocity(new Vector(0, 0, 0));
				if (eCarts.getConfig().getBoolean("ShowIntersectionMessage")) {
					event.getVehicle().getPassenger().sendMessage(ChatColor.GRAY + eCarts.getConfig().getString("IntersectionMessageText"));
				}
			}
		} catch (Exception e) {
			logger.severe("Error in onMyVehicleMove.");
			logger.severe(e.toString());
		}

		return;
	}

	/**
	 * It's not a valid intersection if the rail left or right to our location is not normal to the rail we are moving/standing on.
	 * 
	 * @param myLocation
	 * @param movementDirection
	 * @return
	 */
	private boolean isIntersection(Location myLocation, Vector movementDirection) {
		// Search for intersection
		Location front = myLocation.clone().add(movementDirection.normalize());
		Location back = myLocation.clone().subtract(movementDirection.normalize());
		Location left = myLocation.clone().add(movementDirection.getZ(), 0, -movementDirection.getX()); // go one left
		Location right = myLocation.clone().add(-movementDirection.getZ(), 0, movementDirection.getX()); // go one right

		if (Utils.isFlatRail(myLocation)) {
			if (Utils.isRailNormal(myLocation, left) && Utils.isRailNormal(myLocation, right)) {
				return true;
			} else if ((Utils.isRailNormal(myLocation, left) && (Utils.isRailParallel(myLocation, front) || Utils.isRailParallel(myLocation, back)))
					|| (Utils.isRailNormal(myLocation, right) && (Utils.isRailParallel(myLocation, front) || Utils.isRailParallel(myLocation, back)))) {
				return true;
			} else if ((Utils.isRailParallel(myLocation, left) && (Utils.isRailNormal(myLocation, front) || Utils.isRailNormal(myLocation, back)))
					|| (Utils.isRailParallel(myLocation, right) && (Utils.isRailNormal(myLocation, front) || Utils.isRailNormal(myLocation, back)))) {
				return true;
			}
		}
		return false;
	}

	private RideableMinecart getValidMineCart(Vehicle vehicle, boolean mustHavePassenger) {
		RideableMinecart cart = null;
		try {
			if (!(vehicle instanceof RideableMinecart))
				return null;
			cart = (RideableMinecart) vehicle;

			if (mustHavePassenger && (cart.isEmpty() || !(cart.getPassenger() instanceof Player)))
				return null;

		} catch (Exception e) {
			logger.warning("EasyCarts: Couldn't get valid minecart with player in it.");
		}
		return cart;
	}

	private void teleportMineCart(final Minecart cart, Location destination, Location oldPlayerLocation, Vector oldDirection) {

		// Teleport destination must be in the center of a minecart track to prevent the cart from derailing.
		// Thus depending on the facing of the destination either X or Z values must be n,5.
		Vector destinationVector = new Vector(destination.getX(), destination.getY(), destination.getZ());
		destinationVector.multiply(oldDirection); // 0, 0, -138.7

		// Only X or Z are relevant
		// Make sure new cart location is on center of a minecart rail
		destination.setX(new Double(destination.getX()).intValue() + (destination.getX() >= 0 ? 0.5 : -0.5));
		destination.setZ(new Double(destination.getZ()).intValue() + (destination.getZ() >= 0 ? 0.5 : -0.5)); // -138,7 -> -138,5

		final Minecart toCart = cart.getWorld().spawn(destination, RideableMinecart.class);
		final Entity passenger = cart.getPassenger();

		if (passenger != null) {
			cart.eject();
			destination.setY(oldPlayerLocation.getY()); // prevents Y-bump when teleporting
			destination.setPitch(oldPlayerLocation.getPitch()); // preserve player orientation after teleport
			destination.setYaw(oldPlayerLocation.getYaw());

			passenger.teleport(destination);
			Bukkit.getScheduler().runTask(eCarts, new Runnable() {
				@Override
				public void run() {
					toCart.setPassenger(passenger);
					passenger.setVelocity(cart.getVelocity());
				}
			});
		}
		toCart.getLocation().setYaw(cart.getLocation().getYaw());
		toCart.getLocation().setPitch(cart.getLocation().getPitch());
		toCart.setVelocity(cart.getVelocity()); // speed of newly spawned minecart will be speed of old cart before the stop
		if(eCarts.getConfig().getBoolean("RemoveMinecartOnExit")) removeOnExitMinecartIds.add(toCart.getUniqueId());
		cart.remove();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDismount(final VehicleExitEvent event) {

		if (!(event.getVehicle() instanceof RideableMinecart))
			return;
		
		UUID cartId = event.getVehicle().getUniqueId();
		// Clean up data to avoid having dead entries in the maps when user dismounts at intersections or on slopes
		previousSpeed.remove(cartId);
		slowedCarts.remove(cartId);
		stoppedCarts.remove(cartId);

		if (!removeOnExitMinecartIds.contains(event.getVehicle().getUniqueId()))
			return;
		
		if(!eCarts.getConfig().getBoolean("RemoveMinecartOnExit"))
			return;

		Bukkit.getScheduler().runTaskLater(eCarts, new Runnable() {
			@Override
			public void run() {
				removeOnExitMinecartIds.remove(event.getVehicle().getUniqueId());
				event.getVehicle().remove();
			}
		}, 2L);
	}

}

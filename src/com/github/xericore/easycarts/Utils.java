package com.github.xericore.easycarts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.material.PoweredRail;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

public class Utils {

	public static final double MINECART_VANILLA_PUSH_SPEED = 0.2D;
	public static final double MINECART_VANILLA_MAX_SPEED = 0.4D;

	public static RideableMinecart getValidMineCart(Vehicle vehicle, boolean mustHavePassenger) {
		RideableMinecart cart = null;

		if (!(vehicle instanceof RideableMinecart))
			return null;
		cart = (RideableMinecart) vehicle;

		if (mustHavePassenger && (cart.isEmpty() || !(cart.getPassenger() instanceof Player)))
			return null;

		return cart;
	}

	/**
	 * Includes curves, but not slopes.
	 * 
	 * @param location
	 * @return
	 */
	public static boolean isFlatRail(Location location) {
		if (location.getBlock().getType() == Material.RAILS) {
			Rails testRail = (Rails) location.getBlock().getState().getData();
			if (!testRail.isOnSlope()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isRailPerpendicular(Location myLocation, Location otherLocation) {
		Block myBlock = myLocation.getBlock();
		Block otherBlock = otherLocation.getBlock();
		if (otherBlock.getType() == Material.RAILS) {
			if (myBlock.getData() == (byte) 0 && otherBlock.getData() == (byte) 1) {
				return true;
			} else if (myBlock.getData() == (byte) 1 && otherBlock.getData() == (byte) 0) {
				return true;
			}
		}
		return false;
	}

	public static boolean isRailParallel(Location myLocation, Location otherLocation) {
		Block myBlock = myLocation.getBlock();
		Block otherBlock = otherLocation.getBlock();
		if (otherBlock.getType() == Material.RAILS) {
			if (myBlock.getData() == otherBlock.getData()) {
				return true;
			}
		}
		return false;
	}

	public static Vector getUnitVectorFromYaw(float yaw) {
		BlockFace facing = getBlockFaceFromYaw(yaw);
		switch (facing) {
		case SOUTH:
			return new Vector(0, 0, 1);
		case WEST:
			return new Vector(-1, 0, 0);
		case NORTH:
			return new Vector(0, 0, -1);
		default: // EAST
			return new Vector(1, 0, 0);
		}
	}

	/**
	 * It's not a valid intersection if the rail left or right to our location is not normal to the rail we are moving/standing on.
	 * 
	 * @param myLocation
	 * @param movementDirection
	 * @return
	 */
	public static boolean isIntersection(Location myLocation, Vector movementDirection) {
		if (Utils.isFlatRail(myLocation)) {
			// Search for intersection
			Location front = myLocation.clone().add(movementDirection.normalize());
			Location back = myLocation.clone().subtract(movementDirection.normalize());
			Location left = myLocation.clone().add(movementDirection.getZ(), 0, -movementDirection.getX()); // go one left
			Location right = myLocation.clone().add(-movementDirection.getZ(), 0, movementDirection.getX()); // go one right

			if (Utils.isRailPerpendicular(myLocation, left) && Utils.isRailPerpendicular(myLocation, right)) {
				return true;
			} else if ((Utils.isRailPerpendicular(myLocation, left) && (Utils.isRailParallel(myLocation, front) || Utils
					.isRailParallel(myLocation, back)))
					|| (Utils.isRailPerpendicular(myLocation, right) && (Utils.isRailParallel(myLocation, front) || Utils
							.isRailParallel(myLocation, back)))) {
				return true;
			} else if ((Utils.isRailParallel(myLocation, left) && (Utils.isRailPerpendicular(myLocation, front) || Utils
					.isRailPerpendicular(myLocation, back)))
					|| (Utils.isRailParallel(myLocation, right) && (Utils.isRailPerpendicular(myLocation, front) || Utils
							.isRailPerpendicular(myLocation, back)))) {
				return true;
			}
		}
		return false;
	}

	public static BlockFace getBlockFaceFromYaw(float yaw) {

		if (yaw < 0) { // Map all negative values to positives. E.g. -45° = +315°
			yaw = yaw + 360;
		}
		yaw = yaw % 360; // crop value, e.g. if it's 460° --> 100°

		float straightAngle = 90;

		if ((yaw >= 0) && (yaw < (straightAngle / 2)) || (yaw >= (360 - (straightAngle / 2)))) {
			return BlockFace.SOUTH;
		} else if ((yaw >= (straightAngle / 2)) && (yaw < 135)) {
			return BlockFace.WEST;
		} else if ((yaw >= 135) && (yaw < (360 - (straightAngle * 1.5)))) {
			return BlockFace.NORTH;
		} else {
			return BlockFace.EAST;
		}
	}

	public static boolean isMovingUp(VehicleMoveEvent event) {
		return event.getTo().getY() - event.getFrom().getY() > 0;
	}

	public static boolean isMovingDown(VehicleMoveEvent event) {
		return event.getTo().getY() - event.getFrom().getY() < 0;
	}

	public static Rails getRailInFront(Location testLoc) {
		try {
			// Slopes that go down/fall have the blocks underneath the current y-level
			Location testLocUnder = testLoc.clone().subtract(0, 1, 0);

			if (testLoc.getBlock().getType() == Material.RAILS) {
				// Detects rising slope
				return (Rails) testLoc.getBlock().getState().getData();
			} else if (testLocUnder.getBlock().getType() == Material.RAILS) {
				// Detects falling slope
				return (Rails) testLocUnder.getBlock().getState().getData();
			} else if (testLoc.getBlock().getType() == Material.POWERED_RAIL) {
				return (PoweredRail) testLoc.getBlock().getState().getData();
			} else if (testLocUnder.getBlock().getType() == Material.POWERED_RAIL) {
				return (PoweredRail) testLocUnder.getBlock().getState().getData();
			}
		} catch (ClassCastException e) {
			// no valid rail found
		}
		return null;
	}
}

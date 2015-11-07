package com.github.xericore.easycarts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

public class Utils {

	public static final double MINECART_VANILLA_PUSH_SPEED = 0.2D;
	public static final double MINECART_VANILLA_MAX_SPEED = 0.4D;

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
}

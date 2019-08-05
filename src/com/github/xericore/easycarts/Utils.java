package com.github.xericore.easycarts;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.material.PoweredRail;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

class Utils
{

	public static RideableMinecart getValidMineCart(Vehicle vehicle, boolean mustHavePassenger)
	{
		RideableMinecart cart = null;

		if (!(vehicle instanceof RideableMinecart))
			return null;
		cart = (RideableMinecart) vehicle;

		Entity firstPassenger = GetFirstPassenger(cart);
		if (firstPassenger == null)
			return null;

		if (mustHavePassenger && (cart.isEmpty() || !(firstPassenger instanceof Player)))
			return null;

		return cart;
	}

	public static Entity GetFirstPassenger(Minecart toCart)
	{
		List<Entity> passengers = toCart.getPassengers();

		if (passengers.isEmpty())
			return null;

		return passengers.get(0);
	}

	/**
	 * Includes curves, but not slopes.
	 * 
	 * @param location
	 * @return
	 */
	public static boolean isFlatRail(Location location)
	{
		if (location.getBlock().getType() == Material.RAIL)
		{
			Rails testRail = (Rails) location.getBlock().getState().getData();
			if (!testRail.isOnSlope())
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isRailPerpendicular(Location myLocation, Location otherLocation)
	{
		Block myBlock = myLocation.getBlock();
		Block otherBlock = otherLocation.getBlock();
		if (otherBlock.getType() == Material.RAIL)
		{
			if (myBlock.getData() == (byte) 0 && otherBlock.getData() == (byte) 1)
			{
				return true;
			} else if (myBlock.getData() == (byte) 1 && otherBlock.getData() == (byte) 0)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isRailParallel(Location myLocation, Location otherLocation)
	{
		Block myBlock = myLocation.getBlock();
		Block otherBlock = otherLocation.getBlock();
		if (otherBlock.getType() == Material.RAIL)
		{
			if (myBlock.getData() == otherBlock.getData())
			{
				return true;
			}
		}
		return false;
	}

	public static Vector getStraightUnitVectorFromYaw(float yaw)
	{
		BlockFace facing = getStraightBlockFaceFromYaw(yaw);
		switch (facing)
		{
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
	public static boolean isIntersection(Location myLocation, Vector movementDirection)
	{
		if (Utils.isFlatRail(myLocation))
		{
			// Search for intersection
			Location front = myLocation.clone().add(movementDirection.normalize());
			Location back = myLocation.clone().subtract(movementDirection.normalize());
			Location left = myLocation.clone().add(movementDirection.getZ(), 0, -movementDirection.getX()); // go one left
			Location right = myLocation.clone().add(-movementDirection.getZ(), 0, movementDirection.getX()); // go one right

			if (Utils.isRailPerpendicular(myLocation, left) && Utils.isRailPerpendicular(myLocation, right))
			{
				return true;
			} else if ((Utils.isRailPerpendicular(myLocation, left)
					&& (Utils.isRailParallel(myLocation, front) || Utils.isRailParallel(myLocation, back)))
					|| (Utils.isRailPerpendicular(myLocation, right)
							&& (Utils.isRailParallel(myLocation, front) || Utils.isRailParallel(myLocation, back))))
			{
				return true;
			} else if ((Utils.isRailParallel(myLocation, left)
					&& (Utils.isRailPerpendicular(myLocation, front) || Utils.isRailPerpendicular(myLocation, back)))
					|| (Utils.isRailParallel(myLocation, right)
							&& (Utils.isRailPerpendicular(myLocation, front) || Utils.isRailPerpendicular(myLocation, back))))
			{
				return true;
			}
		}
		return false;
	}

	public static BlockFace getStraightBlockFaceFromYaw(float yaw)
	{
		if (yaw < 0)
		{ // Map all negative values to positives. E.g. -45° = +315°
			yaw = yaw + 360;
		}
		yaw = yaw % 360; // crop value, e.g. if it's 460° --> 100°

		float straightAngle = 90;

		if ((yaw >= 0) && (yaw < (straightAngle / 2)) || (yaw >= (360 - (straightAngle / 2))))
		{
			return BlockFace.SOUTH;
		} else if ((yaw >= (straightAngle / 2)) && (yaw < 135))
		{
			return BlockFace.WEST;
		} else if ((yaw >= 135) && (yaw < (360 - (straightAngle * 1.5))))
		{
			return BlockFace.NORTH;
		} else
		{
			return BlockFace.EAST;
		}
	}

	public static boolean isMovingUp(VehicleMoveEvent event)
	{
		return event.getTo().getY() - event.getFrom().getY() > 0;
	}

	public static boolean isMovingDown(VehicleMoveEvent event)
	{
		return event.getTo().getY() - event.getFrom().getY() < 0;
	}

	public static boolean isStraightRail(Rails rails)
	{
		return !rails.isCurve();
	}

	public static Rails getRailInFront(Location testLoc)
	{
		try
		{
			// Slopes that go down/fall have the blocks underneath the current y-level
			Location testLocUnder = testLoc.clone().subtract(0, 1, 0);

			if (testLoc.getBlock().getType() == Material.RAIL)
			{
				// Detects rising slope
				return (Rails) testLoc.getBlock().getState().getData();
			}
			else if (testLocUnder.getBlock().getType() == Material.RAIL)
			{
				// Detects falling slope
				return (Rails) testLocUnder.getBlock().getState().getData();
			}
			else if (testLoc.getBlock().getType() == Material.POWERED_RAIL)
			{
				return (PoweredRail) testLoc.getBlock().getState().getData();
			}
			else if (testLocUnder.getBlock().getType() == Material.POWERED_RAIL)
			{
				return (PoweredRail) testLocUnder.getBlock().getState().getData();
			}
		} catch (ClassCastException e)
		{
			// no valid rail found
		}
		return null;
	}

	public static void pushNearbyEntities(RideableMinecart cart, Location cartLocation)
	{
		// To avoid collision, the entity must be located at least 1.0 block away from the cart.
		// The entities will be moved to this distance if they are within the search box when the cart is moving.
		// We actually move the entity a little bit further, to avoid it moving right back into the search box.

		// We cannot use cart.getVelocity() because on diagonal rails, this returns +x,0,0 then 0,0,+z and then diagonal
		// (depending on movement direction).
		// Then it looks like we are moving e.g. left, then right, then diagonal and we cannot distinguish between this
		// and a real straight movement.
		// Luckily, the getLocation().getDirection() is unaffected by this. However, for some unknown reason we have to
		// rotate that vector 90° clockwise to get the correct direction.
		Vector cartVector = (new Vector(-cart.getLocation().getDirection().getZ(), 0, cart.getLocation().getDirection().getX()))
				.normalize();

		Vector velocityNormalRight = new Vector(-cartVector.getZ(), 0, cartVector.getX());
		Vector velocityNormalLeft = new Vector(cartVector.getZ(), 0, -cartVector.getX());

		// Adjust size of box to be between 1-2, depending on movement direction
		List<Entity> nearbyEntities = cart.getNearbyEntities(1 + Math.abs(cartVector.getX()), 1, 1 + Math.abs(cartVector.getZ()));

		for (Entity entity : nearbyEntities)
		{
			// Remove empty minecarts still on track
			if ((entity instanceof Minecart) && entity.isEmpty())
			{
				entity.remove();
				continue;
			}

			// Only move monsters, animals and NPCs, not players
			if ((entity instanceof Monster) || (entity instanceof Animals) || (entity instanceof NPC) || (entity instanceof Player))
			{
				// Entity is not in a minecart, thus we can move it
				if (entity.isInsideVehicle())
					continue;

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
		}
	}
}

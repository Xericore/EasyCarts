package com.github.xericore.easycarts.utilities;

import com.github.xericore.easycarts.RailsAhead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.material.PoweredRail;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class RailUtils
{
    private static final int BLOCKS_LOOK_AHEAD = 3;

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

    /**
     * It's not a valid intersection if the rail left or right to our location is not normal to the rail we are moving/standing on.
     *
     * @param myLocation
     * @param movementDirection
     * @return
     */
    public static boolean isIntersection(Location myLocation, Vector movementDirection)
    {
        if (isFlatRail(myLocation))
        {
            // Search for intersection
            Location front = myLocation.clone().add(movementDirection.normalize());
            Location back = myLocation.clone().subtract(movementDirection.normalize());
            Location left = myLocation.clone().add(movementDirection.getZ(), 0, -movementDirection.getX()); // go one left
            Location right = myLocation.clone().add(-movementDirection.getZ(), 0, movementDirection.getX()); // go one right

            if (isRailPerpendicular(myLocation, left) && isRailPerpendicular(myLocation, right))
            {
                return true;
            } else if ((isRailPerpendicular(myLocation, left)
                    && (isRailParallel(myLocation, front) || isRailParallel(myLocation, back)))
                    || (isRailPerpendicular(myLocation, right)
                    && (isRailParallel(myLocation, front) || isRailParallel(myLocation, back))))
            {
                return true;
            } else if ((isRailParallel(myLocation, left)
                    && (isRailPerpendicular(myLocation, front) || isRailPerpendicular(myLocation, back)))
                    || (isRailParallel(myLocation, right)
                    && (isRailPerpendicular(myLocation, front) || isRailPerpendicular(myLocation, back))))
            {
                return true;
            }
        }
        return false;
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


    public static RailsAhead getRailsAhead(RideableMinecart cart)
    {
        Location cartLocation = cart.getLocation();
        Block blockUnderCart = cartLocation.getBlock();

        Location locationInFront = cartLocation.clone();

        BlockFace cartFacing = Utils.getCartBlockFaceDirection(cart);

        if(cartFacing == null)
            return null;

        Vector cartDirection = Utils.getDirectionFromBlockFace(cartFacing);

        // We won't do anything if there's no rail under the cart
        Rails railUnderCart = null;
        try
        {
            railUnderCart = (Rails) blockUnderCart.getState().getData();
        } catch (ClassCastException e)
        {
            return RailsAhead.Derailing;
        }

        for (int i = 1; i < BLOCKS_LOOK_AHEAD; i++)
        {
            locationInFront.add(cartDirection.multiply(i));
            Rails railInFront = getRailInFront(locationInFront);

            if (railInFront == null)
                continue;

            if(isDirectionChangeAhead(railUnderCart, railInFront))
            {
                return RailsAhead.Derailing;
            }
            else if (CartSpeed.isCartTooFastToDetectIntersection(cart) && isIntersection(locationInFront, cartDirection))
            {
                return RailsAhead.Intersection;
            }
        }

        return RailsAhead.SafeForSpeedup;
    }

    public static boolean isDirectionChangeAhead(Rails railUnderCart, Rails railInFront)
    {
        return (isStraightRail(railUnderCart) && railInFront.isCurve()) ||
                (railUnderCart.isCurve() && isStraightRail(railInFront)) ||
                railInFront.isOnSlope();
    }
}

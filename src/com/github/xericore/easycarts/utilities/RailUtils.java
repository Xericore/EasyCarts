package com.github.xericore.easycarts.utilities;

import com.github.xericore.easycarts.data.RailsAhead;
import com.github.xericore.easycarts.data.TracedRail;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.material.PoweredRail;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

import java.util.List;

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

    public static boolean isSlopedRail(Block blockUnderCart)
    {
        Rail.Shape railShape = ((Rail) blockUnderCart.getBlockData()).getShape();

        switch (railShape)
        {
            case ASCENDING_EAST:
            case ASCENDING_WEST:
            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
                return true;
            default:
                return false;
        }
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
     * It's not a valid intersection if the rail left or right to our location is not normal (90°) to the rail we are moving/standing on.
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

    public static boolean areAllRailsConnectedStraightOrDiagonal(List<TracedRail> tracedRails)
    {
        int tracedRailsCount = 0;

        for (int i = 0; i < tracedRails.size()-1; i++)
        {
            tracedRailsCount++;

            if(RailUtils.areRailsConnectedStraight(tracedRails.get(i).getShape(), tracedRails.get(i+1).getShape()) == false)
                return false;
        }

        return tracedRailsCount >= tracedRails.size() - 1;
    }

    public static boolean areRailsConnectedStraight(Rail.Shape thisShape, Rail.Shape otherShape)
    {
        switch (thisShape) {
            case NORTH_SOUTH:
                return otherShape == Rail.Shape.NORTH_SOUTH;
            case EAST_WEST:
                return otherShape == Rail.Shape.EAST_WEST;
            case SOUTH_EAST:
                return otherShape == Rail.Shape.NORTH_WEST;
            case SOUTH_WEST:
                return otherShape == Rail.Shape.NORTH_EAST;
            case NORTH_WEST:
                return otherShape == Rail.Shape.SOUTH_EAST;
            case NORTH_EAST:
                return otherShape == Rail.Shape.SOUTH_WEST;
        }

        return false;
    }

    public static boolean isCartOnRails(RideableMinecart cart)
    {
        if(isRail(cart.getLocation().getBlock()))
            return true;

        return false;
    }

    public static boolean isRail(Block block)
    {
        switch (block.getBlockData().getMaterial())
        {
            case RAIL:
            case ACTIVATOR_RAIL:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
                return true;
            default:
                return false;
        }
    }

    public static RailsAhead getRailsAhead(List<TracedRail> tracedRails)
    {
        RailsAhead railsAhead;
        railsAhead = RailUtils.areAllRailsConnectedStraightOrDiagonal(tracedRails) ? RailsAhead.SafeForSpeedup : RailsAhead.Derailing;

        if(tracedRails.size() <= 3)
            railsAhead = RailsAhead.Derailing;

        for (TracedRail tracedRail : tracedRails)
        {
            if(tracedRail.isIntersection())
                return RailsAhead.Intersection;
        }

        return railsAhead;
    }

    public static boolean isDirectionChangeAhead(Rails railUnderCart, Rails railInFront)
    {
        return (isStraightRail(railUnderCart) && railInFront.isCurve()) ||
                (railUnderCart.isCurve() && isStraightRail(railInFront)) ||
                railInFront.isOnSlope();
    }
}

package com.github.xericore.easycarts.utilities;

import com.github.xericore.easycarts.data.RailsAhead;
import com.github.xericore.easycarts.data.TracedRail;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

import java.util.List;

import static org.bukkit.block.data.Rail.Shape.*;

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

    public static boolean isIntersection(Location location)
    {
        switch (getRailShapeFromBlock(location.getBlock()))
        {
            case NORTH_SOUTH:
                Block eastBlock = location.clone().add(Utils.getDirectionFromBlockFace(BlockFace.EAST)).getBlock();
                Block westBlock = location.clone().add(Utils.getDirectionFromBlockFace(BlockFace.WEST)).getBlock();

                if(getRailShapeFromBlock(eastBlock) == EAST_WEST ||
                        getRailShapeFromBlock(westBlock) == EAST_WEST)
                    return true;

                break;
            case EAST_WEST:
                Block northBlock = location.clone().add(Utils.getDirectionFromBlockFace(BlockFace.NORTH)).getBlock();
                Block southBlock = location.clone().add(Utils.getDirectionFromBlockFace(BlockFace.SOUTH)).getBlock();

                if(getRailShapeFromBlock(northBlock) == NORTH_SOUTH ||
                        getRailShapeFromBlock(southBlock) == NORTH_SOUTH)
                    return true;

                break;
            case ASCENDING_EAST:
            case ASCENDING_WEST:
            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            case SOUTH_EAST:
            case SOUTH_WEST:
            case NORTH_WEST:
            case NORTH_EAST:
        }

        return false;
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
                return otherShape == NORTH_SOUTH;
            case EAST_WEST:
                return otherShape == EAST_WEST;
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

    public static Rail.Shape getRailShapeFromBlock(Block block)
    {
        if(!RailUtils.isRail(block))
            return null;

        return ((Rail) block.getBlockData()).getShape();
    }
}

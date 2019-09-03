package com.github.xericore.easycarts.data;

import com.github.xericore.easycarts.utilities.RailUtils;
import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;

public class TracedRail
{
    private Rail.Shape Shape;
    private Location Location;

    public TracedRail(Rail.Shape shape, Location location)
    {
        Shape = shape;
        Location = location;
    }

    public TracedRail(Block block)
    {
        Shape = getRailShapeFromLocation(block);
        Location = block.getLocation();
    }

    public Rail.Shape getShape()
    {
        return Shape;
    }

    public org.bukkit.Location getLocation()
    {
        return Location;
    }

    public boolean isIntersection()
    {
        switch (Shape)
        {
            case NORTH_SOUTH:
                Block eastBlock = Location.clone().add(Utils.getDirectionFromBlockFace(BlockFace.EAST)).getBlock();
                Block westBlock = Location.clone().add(Utils.getDirectionFromBlockFace(BlockFace.WEST)).getBlock();

                if(getRailShapeFromLocation(eastBlock) == Rail.Shape.EAST_WEST ||
                        getRailShapeFromLocation(westBlock) == Rail.Shape.EAST_WEST)
                    return true;

                break;
            case EAST_WEST:
                Block northBlock = Location.clone().add(Utils.getDirectionFromBlockFace(BlockFace.NORTH)).getBlock();
                Block southBlock = Location.clone().add(Utils.getDirectionFromBlockFace(BlockFace.SOUTH)).getBlock();

                if(getRailShapeFromLocation(northBlock) == Rail.Shape.EAST_WEST ||
                        getRailShapeFromLocation(southBlock) == Rail.Shape.EAST_WEST)
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

    private Rail.Shape getRailShapeFromLocation(Block block)
    {
        if(!RailUtils.isRail(block))
            return null;

        return ((Rail) block.getBlockData()).getShape();
    }
}

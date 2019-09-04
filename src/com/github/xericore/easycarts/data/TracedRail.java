package com.github.xericore.easycarts.data;

import com.github.xericore.easycarts.utilities.RailUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
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
        Shape = RailUtils.getRailShapeFromBlock(block);
        Location = block.getLocation();
    }

    public Rail.Shape getShape()
    {
        return Shape;
    }

    public boolean isIntersection()
    {
        return RailUtils.isIntersection(Location);
    }
}

package com.github.xericore.easycarts.test;

import com.github.xericore.easycarts.utilities.RailUtils;
import org.bukkit.block.data.Rail;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class RailsConnectedTest
{
    @Test
    public void areRailsConnectedFlat_given_EW_EW_returnsTrue()
    {
        Assert.assertEquals(true, RailUtils.areRailsConnectedFlat(Rail.Shape.EAST_WEST, Rail.Shape.EAST_WEST));
    }

    @Test
    public void areRailsConnectedFlat_given_NS_NS_returnsTrue()
    {
        Assert.assertEquals(true, RailUtils.areRailsConnectedFlat(Rail.Shape.NORTH_SOUTH, Rail.Shape.NORTH_SOUTH));
    }
}

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
        Assert.assertEquals(true, RailUtils.areRailsConnectedStraight(Rail.Shape.EAST_WEST, Rail.Shape.EAST_WEST));
    }

    @Test
    public void areRailsConnectedFlat_given_NS_NS_returnsTrue()
    {
        Assert.assertEquals(true, RailUtils.areRailsConnectedStraight(Rail.Shape.NORTH_SOUTH, Rail.Shape.NORTH_SOUTH));
    }

    @Test
    public void areRailsConnectedFlat_given_NW_SE_returnsTrue()
    {
        Assert.assertEquals(true, RailUtils.areRailsConnectedStraight(Rail.Shape.NORTH_WEST, Rail.Shape.SOUTH_EAST));
    }

    @Test
    public void areRailsConnectedFlat_given_SE_NW_returnsTrue()
    {
        Assert.assertEquals(true, RailUtils.areRailsConnectedStraight(Rail.Shape.SOUTH_EAST, Rail.Shape.NORTH_WEST));
    }

    @Test
    public void areRailsConnectedFlat_given_NE_SW_returnsTrue()
    {
        Assert.assertEquals(true, RailUtils.areRailsConnectedStraight(Rail.Shape.NORTH_EAST, Rail.Shape.SOUTH_WEST));
    }

    @Test
    public void areRailsConnectedFlat_given_SW_NE_returnsTrue()
    {
        Assert.assertEquals(true, RailUtils.areRailsConnectedStraight(Rail.Shape.SOUTH_WEST, Rail.Shape.NORTH_EAST));
    }

    @Test
    public void areRailsConnectedFlat_given_EW_Ascending_returnsFalse()
    {
        Assert.assertEquals(false, RailUtils.areRailsConnectedStraight(Rail.Shape.EAST_WEST, Rail.Shape.ASCENDING_EAST));
    }

    @Test
    public void areRailsConnectedFlat_given_NS_Ascending_returnsFalse()
    {
        Assert.assertEquals(false, RailUtils.areRailsConnectedStraight(Rail.Shape.NORTH_SOUTH, Rail.Shape.ASCENDING_NORTH));
    }
}

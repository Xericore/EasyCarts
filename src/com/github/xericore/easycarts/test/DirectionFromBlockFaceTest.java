package com.github.xericore.easycarts.test;

import com.github.xericore.easycarts.test.mocking.MockedLocation;
import com.github.xericore.easycarts.test.mocking.MockedRideableMinecart;
import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class DirectionFromBlockFaceTest
{
    @Test
    public void getDirectionFromBlockFace_given_North_returns_0_0_minus1()
    {
        Assert.assertEquals(new Vector(0,0,-1), Utils.getDirectionFromBlockFace(BlockFace.NORTH));
    }

    @Test
    public void getDirectionFromBlockFace_given_East_returns_1_0_0()
    {
        Assert.assertEquals(new Vector(1,0,0), Utils.getDirectionFromBlockFace(BlockFace.EAST));
    }

    @Test
    public void getDirectionFromBlockFace_given_South_returns_0_0_1()
    {
        Assert.assertEquals(new Vector(0,0,1), Utils.getDirectionFromBlockFace(BlockFace.SOUTH));
    }

    @Test
    public void getDirectionFromBlockFace_given_West_returns_minus1_0_0()
    {
        Assert.assertEquals(new Vector(-1,0,0), Utils.getDirectionFromBlockFace(BlockFace.WEST));
    }

    @Test
    public void getDirectionFromBlockFace_given_NorthEast_returns_1_0_minus1()
    {
        Assert.assertEquals(new Vector(1,0,-1), Utils.getDirectionFromBlockFace(BlockFace.NORTH_EAST));
    }

    @Test
    public void getDirectionFromBlockFace_given_SouthEast_returns_1_0_1()
    {
        Assert.assertEquals(new Vector(1,0,1), Utils.getDirectionFromBlockFace(BlockFace.SOUTH_EAST));
    }

    @Test
    public void getDirectionFromBlockFace_given_SouthWest_returns_minus1_0_1()
    {
        Assert.assertEquals(new Vector(-1,0,1), Utils.getDirectionFromBlockFace(BlockFace.SOUTH_WEST));
    }

    @Test
    public void getDirectionFromBlockFace_given_NorthWest_returns_minus1_0_minus1()
    {
        Assert.assertEquals(new Vector(-1,0,-1), Utils.getDirectionFromBlockFace(BlockFace.NORTH_WEST));
    }
}

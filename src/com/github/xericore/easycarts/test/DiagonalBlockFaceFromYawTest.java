package com.github.xericore.easycarts.test;

import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.block.BlockFace;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class DiagonalBlockFaceFromYawTest
{
    @Test
    public void getDiagonalBlockFaceFromYaw_given_0_returns_South()
    {
        Assert.assertEquals(BlockFace.SOUTH, Utils.getDiagonalBlockFaceFromYaw(0));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_22_5_returns_South()
    {
        Assert.assertEquals(BlockFace.SOUTH, Utils.getDiagonalBlockFaceFromYaw(22.5f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_22_51_returns_SouthWest()
    {
        Assert.assertEquals(BlockFace.SOUTH_WEST, Utils.getDiagonalBlockFaceFromYaw(22.51f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_45_returns_SouthWest()
    {
        Assert.assertEquals(BlockFace.SOUTH_WEST, Utils.getDiagonalBlockFaceFromYaw(45f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_67_5_returns_SouthWest()
    {
        Assert.assertEquals(BlockFace.SOUTH_WEST, Utils.getDiagonalBlockFaceFromYaw(67.5f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_67_51_returns_West()
    {
        Assert.assertEquals(BlockFace.WEST, Utils.getDiagonalBlockFaceFromYaw(67.51f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_90_returns_West()
    {
        Assert.assertEquals(BlockFace.WEST, Utils.getDiagonalBlockFaceFromYaw(90));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_112_5_returns_West()
    {
        Assert.assertEquals(BlockFace.WEST, Utils.getDiagonalBlockFaceFromYaw(112.5f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_112_51_returns_NorthWest()
    {
        Assert.assertEquals(BlockFace.NORTH_WEST, Utils.getDiagonalBlockFaceFromYaw(112.51f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_135_returns_NorthWest()
    {
        Assert.assertEquals(BlockFace.NORTH_WEST, Utils.getDiagonalBlockFaceFromYaw(135));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_157_5_returns_NorthWest()
    {
        Assert.assertEquals(BlockFace.NORTH_WEST, Utils.getDiagonalBlockFaceFromYaw(157.5f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_157_51_returns_North()
    {
        Assert.assertEquals(BlockFace.NORTH, Utils.getDiagonalBlockFaceFromYaw(157.51f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_180_returns_North()
    {
        Assert.assertEquals(BlockFace.NORTH, Utils.getDiagonalBlockFaceFromYaw(180));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_202_5_returns_North()
    {
        Assert.assertEquals(BlockFace.NORTH, Utils.getDiagonalBlockFaceFromYaw(202.5f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_202_51_returns_NorthEast()
    {
        Assert.assertEquals(BlockFace.NORTH_EAST, Utils.getDiagonalBlockFaceFromYaw(202.51f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_225_returns_NorthEast()
    {
        Assert.assertEquals(BlockFace.NORTH_EAST, Utils.getDiagonalBlockFaceFromYaw(225));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_247_5_returns_NorthEast()
    {
        Assert.assertEquals(BlockFace.NORTH_EAST, Utils.getDiagonalBlockFaceFromYaw(247.5f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_247_51_returns_East()
    {
        Assert.assertEquals(BlockFace.EAST, Utils.getDiagonalBlockFaceFromYaw(247.51f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_270_returns_East()
    {
        Assert.assertEquals(BlockFace.EAST, Utils.getDiagonalBlockFaceFromYaw(270));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_292_5_returns_East()
    {
        Assert.assertEquals(BlockFace.EAST, Utils.getDiagonalBlockFaceFromYaw(292.5f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_292_51_returns_SouthEast()
    {
        Assert.assertEquals(BlockFace.SOUTH_EAST, Utils.getDiagonalBlockFaceFromYaw(292.51f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_315_returns_SouthEast()
    {
        Assert.assertEquals(BlockFace.SOUTH_EAST, Utils.getDiagonalBlockFaceFromYaw(315));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_337_5_returns_SouthEast()
    {
        Assert.assertEquals(BlockFace.SOUTH_EAST, Utils.getDiagonalBlockFaceFromYaw(337.5f));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_337_51_returns_South()
    {
        Assert.assertEquals(BlockFace.SOUTH, Utils.getDiagonalBlockFaceFromYaw(337.51f));
    }
}

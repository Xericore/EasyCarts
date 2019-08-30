package com.github.xericore.easycarts.test;

import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.block.BlockFace;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class DiagonalBlockFaceFromNegativeYawTest
{
    @Test
    public void getDiagonalBlockFaceFromYaw_given_minus360_returns_South()
    {
        Assert.assertEquals(BlockFace.SOUTH, Utils.getDiagonalBlockFaceFromYaw(-360));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_minus315_returns_SouthWest()
    {
        Assert.assertEquals(BlockFace.SOUTH_WEST, Utils.getDiagonalBlockFaceFromYaw(-315));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_minus270_returns_West()
    {
        Assert.assertEquals(BlockFace.WEST, Utils.getDiagonalBlockFaceFromYaw(-270));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_minus225_returns_NorthWest()
    {
        Assert.assertEquals(BlockFace.NORTH_WEST, Utils.getDiagonalBlockFaceFromYaw(-225));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_minus180_returns_North()
    {
        Assert.assertEquals(BlockFace.NORTH, Utils.getDiagonalBlockFaceFromYaw(-180));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_minus135_returns_NorthEast()
    {
        Assert.assertEquals(BlockFace.NORTH_EAST, Utils.getDiagonalBlockFaceFromYaw(-135));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_minus90_returns_East()
    {
        Assert.assertEquals(BlockFace.EAST, Utils.getDiagonalBlockFaceFromYaw(-90));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_minus45_returns_SouthEast()
    {
        Assert.assertEquals(BlockFace.SOUTH_EAST, Utils.getDiagonalBlockFaceFromYaw(-45));
    }

    @Test
    public void getDiagonalBlockFaceFromYaw_given_minus0_01_returns_South()
    {
        Assert.assertEquals(BlockFace.SOUTH, Utils.getDiagonalBlockFaceFromYaw(-0.01f));
    }

}

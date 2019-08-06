package com.github.xericore.easycarts.test;

import com.github.xericore.easycarts.test.mocking.MockedLocationWithDirection;
import com.github.xericore.easycarts.test.mocking.MockedRideableMinecart;
import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CartDirectionTest
{
    @Test
    public void getCartBlockFaceDirection_returnsNorth()
    {
        Vector direction = new Vector(0,0,0);

        MockedLocationWithDirection locationWithDirection = new MockedLocationWithDirection(direction);

        MockedRideableMinecart mockedCart = new MockedRideableMinecart(locationWithDirection);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH, cartBlockFaceDirection);
    }
}

package com.github.xericore.easycarts.test;

import com.github.xericore.easycarts.test.mocking.MockedLocation;
import com.github.xericore.easycarts.test.mocking.MockedRideableMinecart;
import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CartStraightDirectionTest
{
    @Test
    public void getCartBlockFaceDirection_given_yaw_90_velocity_0_minus1_returnsNorth()
    {
        float cartYaw = 90;
        Vector cartVelocity = new Vector(0,0,-1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_minus90_velocity_0_minus1_returnsNorth()
    {
        float cartYaw = -90;
        Vector cartVelocity = new Vector(0,0,-1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_270_velocity_0_minus1_returnsNorth()
    {
        float cartYaw = 270;
        Vector cartVelocity = new Vector(0,0,-1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_90_velocity_0_1_returnsSouth()
    {
        float cartYaw = 90;
        Vector cartVelocity = new Vector(0,0,1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_minus90_velocity_0_1_returnsSouth()
    {
        float cartYaw = -90;
        Vector cartVelocity = new Vector(0,0,1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_270_velocity_0_1_returnsSouth()
    {
        float cartYaw = 270;
        Vector cartVelocity = new Vector(0,0,1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_0_velocity_1_0_returnsEast()
    {
        float cartYaw = 0;
        Vector cartVelocity = new Vector(1,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_180_velocity_1_0_returnsEast()
    {
        float cartYaw = 180;
        Vector cartVelocity = new Vector(1,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_0_velocity_minus1_0_returnsWest()
    {
        float cartYaw = 0;
        Vector cartVelocity = new Vector(-1,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.WEST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_180_velocity_minus1_0_returnsWest()
    {
        float cartYaw = 0;
        Vector cartVelocity = new Vector(-1,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.WEST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_0_velocity_005_0_returnsNull()
    {
        float cartYaw = 0;
        Vector cartVelocity = new Vector(0.05,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(null, cartBlockFaceDirection);
    }
}

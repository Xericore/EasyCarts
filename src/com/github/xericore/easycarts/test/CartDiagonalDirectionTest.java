package com.github.xericore.easycarts.test;

import com.github.xericore.easycarts.test.mocking.MockedLocation;
import com.github.xericore.easycarts.test.mocking.MockedRideableMinecart;
import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CartDiagonalDirectionTest
{
    @Test
    public void getCartBlockFaceDirection_given_yaw_315_velocity_0_0_returnsNorthEast()
    {
        float cartYaw = 315;
        Vector cartVelocity = new Vector(0,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH_EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_minus45_velocity_0_0_returnsSouthWest()
    {
        float cartYaw = -45;
        Vector cartVelocity = new Vector(0,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH_WEST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_225_velocity_0_0_returnsNorthWest()
    {
        float cartYaw = 225;
        Vector cartVelocity = new Vector(0,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH_WEST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_minus135_velocity_0_0_returnsSouthEast()
    {
        float cartYaw = -135;
        Vector cartVelocity = new Vector(0,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH_EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_135_velocity_07_minus07_returnsNorthEast()
    {
        float cartYaw = 135;
        Vector cartVelocity = new Vector(Utils.Sqrt2Half,0,-Utils.Sqrt2Half);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH_EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_135_velocity_1_0_returnsNorthEast()
    {
        float cartYaw = 135;
        Vector cartVelocity = new Vector(1,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH_EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_135_velocity_0_minus1_returnsNorthEast()
    {
        float cartYaw = 135;
        Vector cartVelocity = new Vector(0,0,-1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH_EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_45_velocity_07_07_returnsSouthEast()
    {
        float cartYaw = 45;
        Vector cartVelocity = new Vector(Utils.Sqrt2Half,0,Utils.Sqrt2Half);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH_EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_45_velocity_1_0_returnsSouthEast()
    {
        float cartYaw = 45;
        Vector cartVelocity = new Vector(1,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH_EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_45_velocity_0_1_returnsSouthEast()
    {
        float cartYaw = 45;
        Vector cartVelocity = new Vector(0,0,1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH_EAST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_135_velocity_minus07_07_returnsSouthWest()
    {
        float cartYaw = 135;
        Vector cartVelocity = new Vector(-Utils.Sqrt2Half,0,Utils.Sqrt2Half);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH_WEST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_135_velocity_0_1_returnsSouthWest()
    {
        float cartYaw = 135;
        Vector cartVelocity = new Vector(0,0,1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH_WEST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_135_velocity_minus1_0_returnsSouthWest()
    {
        float cartYaw = 135;
        Vector cartVelocity = new Vector(-1,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.SOUTH_WEST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_45_velocity_minus07_minus07_returnsNorthWest()
    {
        float cartYaw = 45;
        Vector cartVelocity = new Vector(-Utils.Sqrt2Half,0,-Utils.Sqrt2Half);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH_WEST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_45_velocity_0_minus1_returnsNorthWest()
    {
        float cartYaw = 45;
        Vector cartVelocity = new Vector(0,0,-1);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH_WEST, cartBlockFaceDirection);
    }

    @Test
    public void getCartBlockFaceDirection_given_yaw_45_velocity_minus1_0_returnsNorthWest()
    {
        float cartYaw = 45;
        Vector cartVelocity = new Vector(-1,0,0);
        MockedLocation mockedLocation = new MockedLocation(cartYaw);
        MockedRideableMinecart mockedCart = new MockedRideableMinecart(mockedLocation, cartVelocity);

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(BlockFace.NORTH_WEST, cartBlockFaceDirection);
    }

}

package com.github.xericore.easycarts.test;

import com.github.xericore.easycarts.test.mocking.MockedRideableMinecart;
import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CartDirectionTest
{
    @Test
    public void getCartBlockFaceDirection_returnsNorth()
    {
        MockedRideableMinecart mockedCart = new MockedRideableMinecart();

        mockedCart.setLocation(new Vector(0,0,0));

        BlockFace cartBlockFaceDirection = Utils.getCartBlockFaceDirection(mockedCart);

        Assert.assertEquals(cartBlockFaceDirection, BlockFace.NORTH);
    }
}

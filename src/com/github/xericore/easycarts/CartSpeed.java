package com.github.xericore.easycarts;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.logging.Logger;

class CartSpeed
{
    public static final double MINECART_VANILLA_PUSH_SPEED = 0.2D;
    public static final double MINECART_VANILLA_MAX_SPEED = 0.4D;

    // Max speed that a minecart can have before it derails in curves or stops on upward slopes
    private static final double MAX_SAFE_DERAIL_SPEED = 0.4D;
    // Max speed that a minecart can have before detection of intersection fails
    private static final double MAX_SAFE_INTERSECTION_SPEED = 1.0D;

    private static final int BLOCKS_LOOK_AHEAD = 3;

    public static void setCartSpeedToAvoidDerailing(RideableMinecart cart)
    {
        setCartSpeed(cart, MAX_SAFE_DERAIL_SPEED);
    }

    public static void setCartSpeedToAvoidMissingIntersection(RideableMinecart cart)
    {
        setCartSpeed(cart, MAX_SAFE_DERAIL_SPEED);
    }

    private static void setCartSpeed(RideableMinecart cart, double maxSpeed)
    {
        cart.setVelocity(cart.getVelocity().clone().normalize().multiply(maxSpeed));
        cart.setMaxSpeed(maxSpeed);
    }

    public static boolean isCartTooFastToDetectIntersection(RideableMinecart cart)
    {
        return cart.getVelocity().length() > MAX_SAFE_INTERSECTION_SPEED;
    }

    public static RailsAhead getRailsAhead(RideableMinecart cart, Logger logger)
    {
        Location cartLocation = cart.getLocation();
        Block blockUnderCart = cartLocation.getBlock();

        Location locationInFront = cartLocation.clone();
        Vector cartDirection = cart.getVelocity().clone().normalize();

        DecimalFormat df = new DecimalFormat("#.#");

        logger.info("cartDirection: " +
                df.format(cart.getLocation().getDirection().clone().getX()) + ", " +
                df.format(cart.getLocation().getDirection().clone().getZ()));
        //logger.info("cartVelocity:  " + cart.getVelocity().clone());

        // We won't do anything if there's no rail under the cart
        Rails railUnderCart = null;
        try
        {
            railUnderCart = (Rails) blockUnderCart.getState().getData();
        } catch (ClassCastException e)
        {
            return RailsAhead.Derailing;
        }

        for (int i = 1; i < BLOCKS_LOOK_AHEAD; i++)
        {
            /*if(i==1)
                locationInFront.clone().subtract(0,1,0).getBlock().setType(Material.QUARTZ_BLOCK);
            if(i==2)
                locationInFront.clone().subtract(0,1,0).getBlock().setType(Material.GOLD_BLOCK);*/

            locationInFront.add(cartDirection.multiply(i));
            Rails railInFront = Utils.getRailInFront(locationInFront);

            if (railInFront == null)
                continue;

            if(CartSpeed.isDirectionChangeAhead(railUnderCart, railInFront))
            {
                return RailsAhead.Derailing;
            }
            else if (CartSpeed.isCartTooFastToDetectIntersection(cart) && Utils.isIntersection(locationInFront, cartDirection))
            {
                return RailsAhead.Intersection;
            }
        }

        return RailsAhead.SafeForSpeedup;
    }

    public static boolean isDirectionChangeAhead(Rails railUnderCart, Rails railInFront)
    {
        return (Utils.isStraightRail(railUnderCart) && railInFront.isCurve()) ||
                (railUnderCart.isCurve() && Utils.isStraightRail(railInFront)) ||
                railInFront.isOnSlope();
    }

    public static boolean isCartTooFast(RideableMinecart cart)
    {
        return cart.getVelocity().clone().lengthSquared() > MAX_SAFE_DERAIL_SPEED;
    }
}

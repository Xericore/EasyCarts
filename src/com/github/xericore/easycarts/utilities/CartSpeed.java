package com.github.xericore.easycarts.utilities;

import com.github.xericore.easycarts.RailsAhead;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class CartSpeed
{
    public static final double MINECART_VANILLA_PUSH_SPEED = 0.2D;
    public static final double MINECART_VANILLA_MAX_SPEED = 0.4D;

    // Max speed that a minecart can have before it derails in curves or stops on upward slopes
    private static final double MAX_SAFE_DERAIL_SPEED = 0.4D;
    // Max speed that a minecart can have before detection of intersection fails
    private static final double MAX_SAFE_INTERSECTION_SPEED = 1.0D;

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

    public static boolean isCartTooFast(RideableMinecart cart)
    {
        return cart.getVelocity().clone().lengthSquared() > MAX_SAFE_DERAIL_SPEED;
    }
}

package com.github.xericore.easycarts.test.mocking;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class MockedLocationWithDirection extends Location
{
    private Vector _direction;

    public MockedLocationWithDirection(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public MockedLocationWithDirection(World world, double x, double y, double z, Vector direction)
    {
        super(world, x, y, z);
        setDirection(direction);
    }

    public MockedLocationWithDirection(Vector direction)
    {
        super(null, 0, 0, 0);
        setDirection(direction);
    }

    public Location setDirection(Vector direction)
    {
        _direction = direction;
        return this;
    }

    public Vector getDirection()
    {
        return _direction;
    }
}

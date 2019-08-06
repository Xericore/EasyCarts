package com.github.xericore.easycarts.test.mocking;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class MockedLocation extends Location
{
    private Vector _direction;

    public MockedLocation(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public MockedLocation(World world, double x, double y, double z, Vector direction)
    {
        super(world, x, y, z);
        setDirection(direction);
    }

    public MockedLocation(Vector direction)
    {
        super(null, 0, 0, 0);
        setDirection(direction);
    }

    public MockedLocation(float yaw)
    {
        super(null, 0, 0, 0);
        setYaw(yaw);
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

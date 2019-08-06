package com.github.xericore.easycarts.test.mocking;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MockedRideableMinecart implements RideableMinecart
{
    private double _maxSpeed;
    private boolean _isSlowWhenEmpty;
    private Vector _velocity;
    private Entity _passenger;
    private MockedLocation _mockedLocation;

    public MockedRideableMinecart(MockedLocation mockedLocation)
    {
        _mockedLocation = mockedLocation;
    }

    public MockedRideableMinecart(MockedLocation mockedLocation, Vector velocity)
    {
        _mockedLocation = mockedLocation;
        _velocity = velocity;
    }

    @Override
    public void setDamage(double v)
    {

    }

    @Override
    public double getDamage()
    {
        return 0;
    }

    @Override
    public double getMaxSpeed()
    {
        return _maxSpeed;
    }

    @Override
    public void setMaxSpeed(double v)
    {
        _maxSpeed = v;
    }

    @Override
    public boolean isSlowWhenEmpty()
    {
        return _isSlowWhenEmpty;
    }

    @Override
    public void setSlowWhenEmpty(boolean b)
    {
        _isSlowWhenEmpty = b;
    }

    @Override
    public Vector getFlyingVelocityMod()
    {
        return null;
    }

    @Override
    public void setFlyingVelocityMod(Vector vector)
    {

    }

    @Override
    public Vector getDerailedVelocityMod()
    {
        return null;
    }

    @Override
    public void setDerailedVelocityMod(Vector vector)
    {

    }

    @Override
    public void setDisplayBlock(MaterialData materialData)
    {

    }

    @Override
    public MaterialData getDisplayBlock()
    {
        return null;
    }

    @Override
    public void setDisplayBlockData(BlockData blockData)
    {

    }

    @Override
    public BlockData getDisplayBlockData()
    {
        return null;
    }

    @Override
    public void setDisplayBlockOffset(int i)
    {

    }

    @Override
    public int getDisplayBlockOffset()
    {
        return 0;
    }

    @Override
    public Vector getVelocity()
    {
        return _velocity;
    }

    @Override
    public double getHeight()
    {
        return 0;
    }

    @Override
    public double getWidth()
    {
        return 0;
    }

    @Override
    public BoundingBox getBoundingBox()
    {
        return null;
    }

    @Override
    public boolean isOnGround()
    {
        return false;
    }

    @Override
    public World getWorld()
    {
        return null;
    }

    @Override
    public void setRotation(float v, float v1)
    {

    }

    @Override
    public boolean teleport(Location location)
    {
        return false;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause)
    {
        return false;
    }

    @Override
    public boolean teleport(Entity entity)
    {
        return false;
    }

    @Override
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause)
    {
        return false;
    }

    @Override
    public List<Entity> getNearbyEntities(double v, double v1, double v2)
    {
        return null;
    }

    @Override
    public int getEntityId()
    {
        return 123456;
    }

    @Override
    public int getFireTicks()
    {
        return 0;
    }

    @Override
    public int getMaxFireTicks()
    {
        return 0;
    }

    @Override
    public void setFireTicks(int i)
    {

    }

    @Override
    public void remove()
    {

    }

    @Override
    public boolean isDead()
    {
        return false;
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    @Override
    public void sendMessage(String s)
    {

    }

    @Override
    public void sendMessage(String[] strings)
    {

    }

    @Override
    public Server getServer()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return "MockedRidableCart";
    }

    @Override
    public boolean isPersistent()
    {
        return false;
    }

    @Override
    public void setPersistent(boolean b)
    {

    }

    @Override
    public Entity getPassenger()
    {
        return _passenger;
    }

    @Override
    public boolean setPassenger(Entity entity)
    {
        _passenger = entity;
        return true;
    }

    @Override
    public List<Entity> getPassengers()
    {
        List<Entity> passengers = new ArrayList<Entity>();
        passengers.add(_passenger);
        return passengers;
    }

    @Override
    public boolean addPassenger(Entity entity)
    {
        return false;
    }

    @Override
    public boolean removePassenger(Entity entity)
    {
        return false;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean eject()
    {
        setPassenger(null);
        return true;
    }

    @Override
    public float getFallDistance()
    {
        return 0;
    }

    @Override
    public void setFallDistance(float v)
    {

    }

    @Override
    public void setLastDamageCause(EntityDamageEvent entityDamageEvent)
    {

    }

    @Override
    public EntityDamageEvent getLastDamageCause()
    {
        return null;
    }

    @Override
    public UUID getUniqueId()
    {
        return UUID.randomUUID();
    }

    @Override
    public int getTicksLived()
    {
        return 0;
    }

    @Override
    public void setTicksLived(int i)
    {

    }

    @Override
    public void playEffect(EntityEffect entityEffect)
    {

    }

    @Override
    public EntityType getType()
    {
        return EntityType.MINECART;
    }

    @Override
    public boolean isInsideVehicle()
    {
        return false;
    }

    @Override
    public boolean leaveVehicle()
    {
        return false;
    }

    @Override
    public Entity getVehicle()
    {
        return this;
    }

    @Override
    public void setCustomNameVisible(boolean b)
    {

    }

    @Override
    public boolean isCustomNameVisible()
    {
        return false;
    }

    @Override
    public void setGlowing(boolean b)
    {

    }

    @Override
    public boolean isGlowing()
    {
        return false;
    }

    @Override
    public void setInvulnerable(boolean b)
    {

    }

    @Override
    public boolean isInvulnerable()
    {
        return false;
    }

    @Override
    public boolean isSilent()
    {
        return false;
    }

    @Override
    public void setSilent(boolean b)
    {

    }

    @Override
    public boolean hasGravity()
    {
        return false;
    }

    @Override
    public void setGravity(boolean b)
    {

    }

    @Override
    public int getPortalCooldown()
    {
        return 0;
    }

    @Override
    public void setPortalCooldown(int i)
    {

    }

    @Override
    public Set<String> getScoreboardTags()
    {
        return null;
    }

    @Override
    public boolean addScoreboardTag(String s)
    {
        return false;
    }

    @Override
    public boolean removeScoreboardTag(String s)
    {
        return false;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction()
    {
        return null;
    }

    @Override
    public BlockFace getFacing()
    {
        return null;
    }

    @Override
    public Pose getPose()
    {
        return null;
    }

    @Override
    public Spigot spigot()
    {
        return null;
    }

    @Override
    public Location getLocation()
    {
        if(_mockedLocation == null)
            _mockedLocation = new MockedLocation(null, 0,0,0);

        return _mockedLocation;
    }

    public void setLocation(MockedLocation newLocation)
    {
        _mockedLocation = newLocation;
    }

    @Override
    public Location getLocation(Location location)
    {
        return null;
    }

    @Override
    public void setVelocity(Vector velocity)
    {
        _velocity = velocity;
    }

    @Override
    public String getCustomName()
    {
        return null;
    }

    @Override
    public void setCustomName(String s)
    {

    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue)
    {

    }

    @Override
    public List<MetadataValue> getMetadata(String s)
    {
        return null;
    }

    @Override
    public boolean hasMetadata(String s)
    {
        return false;
    }

    @Override
    public void removeMetadata(String s, Plugin plugin)
    {

    }

    @Override
    public boolean isPermissionSet(String s)
    {
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission permission)
    {
        return false;
    }

    @Override
    public boolean hasPermission(String s)
    {
        return false;
    }

    @Override
    public boolean hasPermission(Permission permission)
    {
        return false;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b)
    {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin)
    {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i)
    {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i)
    {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment)
    {

    }

    @Override
    public void recalculatePermissions()
    {

    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions()
    {
        return null;
    }

    @Override
    public boolean isOp()
    {
        return false;
    }

    @Override
    public void setOp(boolean b)
    {

    }

    @Override
    public PersistentDataContainer getPersistentDataContainer()
    {
        return null;
    }
}

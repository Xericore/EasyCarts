package com.github.xericore.easycarts;

import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

public class MyVehicleCollisionListener implements Listener
{
    private static FileConfiguration config = null;

    public MyVehicleCollisionListener(EasyCarts easyCartsPlugin)
    {
        config = easyCartsPlugin.getConfig();
    }

    /**
     * Unfortunately at this point the cart's speed is already 0. Thus it isn't possible to determine the speed of the cart before the
     * collision (at least not without resource intense saving of speed every tick in VehicleMoveEvent).
     *
     * The work around is to move the entities before they collide with the cart. Other players will not be moved. However, if a player
     * blocks the path, the cart will come to a stop.
     *
     * @see https://bukkit.org/threads/trouble-cancelling-vehicleentitycollisionevent.285269/
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onVehicleCollision(final VehicleEntityCollisionEvent event)
    {
        RideableMinecart cart = Utils.getValidMineCart(event.getVehicle(), true);
        if (cart == null)
        {
            return;
        }
        if (!config.getBoolean("MinecartCollisions") && event.getEntity() instanceof Player)
        {
            // This will cause the cart to stop
            event.setCancelled(true);
            event.setCollisionCancelled(true);
            event.setPickupCancelled(true);
        }
    }
}

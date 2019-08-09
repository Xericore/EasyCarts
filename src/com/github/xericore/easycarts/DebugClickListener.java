package com.github.xericore.easycarts;

import org.bukkit.Material;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DebugClickListener implements Listener
{
    public static EasyCarts easyCartsPlugin;

    public DebugClickListener(EasyCarts theInstance)
    {
        easyCartsPlugin = theInstance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMyPlayerInteract(PlayerInteractEvent event)
    {
        Player player = (Player) event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            easyCartsPlugin.logger.info("");
            easyCartsPlugin.logger.info("BlockData: " + event.getClickedBlock().getBlockData());
            easyCartsPlugin.logger.info("Material: " + event.getClickedBlock().getBlockData().getMaterial());

            switch (event.getClickedBlock().getBlockData().getMaterial())
            {
                case RAIL:
                    break;
                case ACTIVATOR_RAIL:
                    break;
                case POWERED_RAIL:
                    break;
                case DETECTOR_RAIL:
                    break;
                default:
                    return;
            }

            Rail railData = (Rail) event.getClickedBlock().getBlockData();
            easyCartsPlugin.logger.info("Shape: " + railData.getShape());
        }
    }
}

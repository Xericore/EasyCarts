package com.github.xericore.easycarts;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.data.Rail;
import org.bukkit.util.Vector;

/**
 * Class to re-orient rails when right clicked by a stick
 * @author crazygmr101
 */
public class PlayerRailInteractListener implements Listener
{

	public static EasyCarts easyCartsPlugin;
	private static FileConfiguration config = null;

	public PlayerRailInteractListener(EasyCarts theInstance)
	{
		easyCartsPlugin = theInstance;
		config = easyCartsPlugin.getConfig();
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent event){
		if (!Utils.isRail(event.getClickedBlock().getType())) return;
		if (event.getItem() == null) return;
		if (event.getItem().getType() != Material.STICK) return;
		Block block = event.getClickedBlock();
		block.setBlockData(
				(BlockData)Utils.changeRailDirection((Rail)event.getClickedBlock().getBlockData(), event.getPlayer().isSneaking())
		);
	}
}

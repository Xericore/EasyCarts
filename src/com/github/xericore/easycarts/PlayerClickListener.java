package com.github.xericore.easycarts;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerClickListener implements Listener {

	public static EasyCarts easyCartsPlugin;
	private static FileConfiguration config = null;

	public PlayerClickListener(EasyCarts theInstance) {
		easyCartsPlugin = theInstance;
		config = easyCartsPlugin.getConfig();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMyPlayerInteract(PlayerInteractEvent event) {

		if (!config.getBoolean("StopStartOnLeftClick"))
			return;

		Player player = (Player) event.getPlayer();

		if (!player.isInsideVehicle()) {
			return;
		}

		Vehicle cart = (Vehicle) player.getVehicle();

		if (event.getAction() == Action.LEFT_CLICK_AIR) {

			if (cart.getVelocity().length() <= 0) {
				cart.setVelocity(Utils.getUnitVectorFromYaw(player.getLocation().getYaw()).multiply(
						(Utils.MINECART_VANILLA_PUSH_SPEED * config.getDouble("MaxPushSpeedPercent") / 100)));
			} else {
				cart.setVelocity(new Vector(0, 0, 0));
			}
		}
	}
}

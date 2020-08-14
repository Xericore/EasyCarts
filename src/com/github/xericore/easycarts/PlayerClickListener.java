package com.github.xericore.easycarts;

import com.github.xericore.easycarts.utilities.CartSpeed;
import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerClickListener implements Listener
{
	public static EasyCarts easyCartsPlugin;
	private static FileConfiguration config = null;

	public PlayerClickListener(EasyCarts theInstance)
	{
		easyCartsPlugin = theInstance;
		config = easyCartsPlugin.getConfig();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onMyPlayerInteract(PlayerInteractEvent event)
	{
		if (!config.getBoolean("StopStartOnLeftClick"))
			return;

		Player player = event.getPlayer();

		if (!player.isInsideVehicle())
			return;

		if (event.getAction() != Action.LEFT_CLICK_AIR)
			return;

		if (!(player.getVehicle() instanceof RideableMinecart))
			return;

		RideableMinecart cart = (RideableMinecart) player.getVehicle();

		if (cart.getVelocity().length() <= 0)
		{
			cart.setVelocity(Utils.getStraightUnitVectorFromYaw(player.getLocation().getYaw())
					.multiply((CartSpeed.MINECART_VANILLA_PUSH_SPEED * config.getDouble("MaxPushSpeedPercent") / 100)));
		}
		else
		{
			cart.setVelocity(new Vector(0, 0, 0));
		}
	}
}

package com.github.xericore.easycarts;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Xericore
 */
public class EasyCarts extends JavaPlugin
{

	// public static MinecartIntersectionTest plugin;
	public final Logger logger = Logger.getLogger("Minecraft");

	// Define Listener classes
	public final EasyCartsListener myMinecartListener = new EasyCartsListener(this);
	public final PlayerClickListener myPlayerClickListener = new PlayerClickListener(this);

	public void onEnable()
	{
		// Save a copy of the default config.yml if one is not there
		this.saveDefaultConfig();

		registerEvents();

		addMetrics();
	}

	private void registerEvents()
	{
		getServer().getPluginManager().registerEvents(this.myMinecartListener, this);

		if (getConfig().getBoolean("StopStartOnLeftClick"))
			getServer().getPluginManager().registerEvents(this.myPlayerClickListener, this);
	}

	private void addMetrics()
	{
		try
		{
			Metrics metrics = new Metrics(this);
		}
		catch (Exception e)
		{
			logger.info("Couldn't submit metrics to bStats.org.");
		}
	}

	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " has been disabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{

		if (cmd.getName().equalsIgnoreCase("easycarts"))
		{ // If the player typed /easycarts then do the following...
			if (sender instanceof Player)
			{
				Player player = (Player) sender;
				if (!player.hasPermission("easycarts.admin"))
				{
					player.sendMessage(ChatColor.RED + "You don't have the permission to execute that command.");
					return true;
				}
			}

			try
			{
				switch (args[0])
				{
				case "push":
					if (args.length >= 2)
						this.getConfig().set("MaxPushSpeedPercent", Double.parseDouble(args[1]));
					sender.sendMessage(
							ChatColor.GRAY + "MaxPushSpeedPercent is set to " + this.getConfig().getDouble("MaxPushSpeedPercent"));
					break;
				case "boost":
					if (args.length >= 2)
						this.getConfig().set("PoweredRailBoostPercent", Double.parseDouble(args[1]));
					sender.sendMessage(
							ChatColor.GRAY + "PoweredRailBoostPercent is set to " + this.getConfig().getDouble("PoweredRailBoostPercent"));
					break;
				case "maxspeed":
					if (args.length >= 2)
						this.getConfig().set("MaxSpeedPercent", Double.parseDouble(args[1]));
					sender.sendMessage(ChatColor.GRAY + "MaxSpeedPercent is set to " + this.getConfig().getDouble("MaxSpeedPercent"));
					break;
				case "slowwhenempty":
					if (args.length >= 2)
						switch (args[1])
						{
						case "true":
							this.getConfig().set("SlowWhenEmpty", true);
							break;
						case "false":
							this.getConfig().set("SlowWhenEmpty", false);
							break;
						case "toggle":
							this.getConfig().set("SlowWhenEmpty", !this.getConfig().getBoolean("SlowWhenEmpty"));
							break;
						default:
							throw new IllegalArgumentException("Use true, false or toggle");
						}
					sender.sendMessage(ChatColor.GRAY + "SlowWhenEmpty is set to " + this.getConfig().getBoolean("SlowWhenEmpty"));
					break;
				case "reload":
					this.reloadConfig();
					sender.sendMessage(ChatColor.GREEN + "EasyCarts config successfully reloaded.");
					return true;
				default:
					throw new IllegalArgumentException();
				}
				this.saveConfig();
				return true;

			} catch (IllegalArgumentException illEx)
			{
				sender.sendMessage(ChatColor.RED + "EasyCarts: Sorry, I didn't understand that command. " + illEx.getMessage());
				return false;
			} catch (Exception e)
			{
				sender.sendMessage(ChatColor.RED + "EasyCarts: Sorry, I didn't understand that command.");
				return false;
			}
		}
		return false;
	}
}

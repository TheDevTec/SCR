package me.devtec.scr.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Formatters;
import me.devtec.scr.commands.teleport.spawn.Spawn;
import me.devtec.scr.commands.teleport.warps.WarpManager;

public class CommandsManager {
	public static boolean waitingCooldown(CommandHolder command, CommandSender sender) {
		return false;
	}
	
	public static void load() {
		WarpManager.load();
		Spawn.spawn=(ConfigManager.data.getString("spawn")==null||ConfigManager.data.getString("spawn").trim().isEmpty())?Bukkit.getWorlds().get(0).getSpawnLocation():Formatters.locFromString(ConfigManager.data.getString("spawn"));
	}
	
	public static void unload() {
		WarpManager.unload();
		ConfigManager.data.set("spawn", Formatters.locToString(Spawn.spawn));
		ConfigManager.data.save();
	}
}

package me.devtec.scr.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Formatters;
import me.devtec.scr.Loader;
import me.devtec.scr.commands.teleport.spawn.Spawn;
import me.devtec.scr.commands.teleport.warps.WarpManager;
import me.devtec.scr.modules.Module;
import me.devtec.theapi.configapi.Config;

public class CommandsManager implements Module {
	private boolean isLoaded;
	public static boolean waitingCooldown(CommandHolder command, CommandSender sender) {
		return false;
	}
	
	public Module load() {
		if(isLoaded)return this;
		isLoaded=true;
		Loader.config=Config.loadConfig(Loader.plugin, "files/config.yml", "SCR/config.yml");
		WarpManager.load();
		Spawn.spawn=(ConfigManager.data.getString("spawn")==null||ConfigManager.data.getString("spawn").trim().isEmpty())?Bukkit.getWorlds().get(0).getSpawnLocation():Formatters.locFromString(ConfigManager.data.getString("spawn"));
		for(String val : Loader.config.getStringList("positive"))
			Loader.positive.add(val.toLowerCase());
		for(String val : Loader.config.getStringList("negative"))
			Loader.negative.add(val.toLowerCase());
		return this;
	}
	
	public Module unload() {
		if(!isLoaded)return this;
		isLoaded=false;
		WarpManager.unload();
		Loader.positive.clear();
		Loader.negative.clear();
		ConfigManager.data.set("spawn", Formatters.locToString(Spawn.spawn));
		ConfigManager.data.save();
		return this;
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}
}

package me.devtec.scr.modules.events;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import me.devtec.scr.Loader;

public class Listeners {
	
	
	public static void load() {
		Bukkit.getPluginManager().registerEvents(new AsyncChat(), Loader.plugin);
	}
	
	public static void unload() {
		HandlerList.unregisterAll(Loader.plugin);
	}
}

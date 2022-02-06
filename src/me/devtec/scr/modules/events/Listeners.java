package me.devtec.scr.modules.events;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import me.devtec.scr.Loader;
import me.devtec.scr.modules.Module;

public class Listeners implements Module {
	private boolean isLoaded;
	
	public Module load() {
		if(isLoaded)return this;
		isLoaded=true;
		Bukkit.getPluginManager().registerEvents(new JoinQuit(), Loader.plugin);
		Bukkit.getPluginManager().registerEvents(new AsyncChat(), Loader.plugin);
		Bukkit.getPluginManager().registerEvents(new AnvilPrepare(), Loader.plugin);
		Bukkit.getPluginManager().registerEvents(new SignEdit(), Loader.plugin);
		Bukkit.getPluginManager().registerEvents(new BookEdit(), Loader.plugin);
		Bukkit.getPluginManager().registerEvents(new ItemPickup(), Loader.plugin);
		Bukkit.getPluginManager().registerEvents(new ItemDrop(), Loader.plugin);
		return this;
	}
	
	public Module unload() {
		if(!isLoaded)return this;
		isLoaded=false;
		HandlerList.unregisterAll(Loader.plugin);
		return this;
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}
}

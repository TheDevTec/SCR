package me.devtec.scr.commands.teleport.warps;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.utils.Formatters;

public class WarpManager {
	public static class Instance {
		
		public Instance(String name) {
			this.name=name;
		}
		
		public String name;
		public Location spawn;
		public double cost;
		public boolean perms;
	}
	
	public static Map<String, Instance> map = new HashMap<>();
	
	public static void load() {
		for(String warpName : ConfigManager.data.getKeys("warp")) {
			Instance warp = create(warpName);
			warp.cost=ConfigManager.data.getDouble("warp."+warpName+".cost");
			warp.perms=ConfigManager.data.getBoolean("warp."+warpName+".perms");
			warp.spawn=Formatters.locFromString(ConfigManager.data.getString("warp."+warpName+".spawn"));
		}
	}
	
	public static void unload() {
		for(Instance warp : values()) {
			ConfigManager.data.set("warp."+warp.name+".cost", warp.cost);
			ConfigManager.data.set("warp."+warp.name+".perms", warp.perms);
			ConfigManager.data.set("warp."+warp.name+".spawn", Formatters.locToString(warp.spawn));
		}
		ConfigManager.data.save();
	}
	
	public static Instance create(String name) {
		Instance instance = new Instance(name);
		map.put(name, instance);
		return instance;
	}
	
	public static Instance delete(String name) {
		return map.remove(name.toLowerCase());
	}
	
	public static Instance get(String name) {
		return map.get(name.toLowerCase());
	}

	public static Set<String> names() {
		return map.keySet();
	}

	public static Collection<Instance> values() {
		return map.values();
	}

	public static Instance getFrom(String warp, Set<Instance> availableWarps) {
		Instance get = get(warp);
		if(availableWarps.contains(get))return get;
		return null;
	}
}

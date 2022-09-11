package me.devtec.scr.functions.guis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.shared.dataholder.Config;

public class GUIManager {

	public static Map<String, GUIMaker> guis = new HashMap<>();
	public static List<String> loaded = new ArrayList<>();

	public static void load() {
		if (loaded != null && !loaded.isEmpty())
			loaded.clear();
		File file = new File("plugins/SCR/guis");
		if (file.exists() && file.isDirectory())
			for (File f : file.listFiles())
				loadFile(f);
		/*
		 * Config config = new Config(f); String name = f.getName().substring(0,
		 * f.getName().length()-4); GUIMaker maker = null;
		 * 
		 * if(guis.containsKey(name)) { maker = guis.get(name); maker.newConfig(config);
		 * // Updating config } else maker = new GUIMaker(config); //Creating new
		 * GUIMaker
		 * 
		 * if(config.getBoolean("enabled")) { //Putting or replacing maker in map
		 * maker.createCommand(); if(guis.containsKey(name)) guis.replace(name, maker);
		 * else guis.put(name, maker); } else if(guis.containsKey(name)) //Removing if
		 * loaded guis.remove(name);
		 */
	}

	// TODO - check jesttli není třeba dvakrát stejný file... XD
	private static void loadFile(File file) {
		if (file.exists() && file.isDirectory())
			for (File f : file.listFiles())
				loadFile(f);
		else {
			Config config = new Config(file);
			String name = file.getName().substring(0, file.getName().length() - 4);
			GUIMaker maker = null;
			if (!loaded.contains(name)) {

				if (guis.containsKey(name)) {
					maker = guis.get(name);
					maker.newConfig(config); // Updating config
				} else
					maker = new GUIMaker(config); // Creating new GUIMaker

				if (config.getBoolean("enabled", true)) { // Putting or replacing maker in map
					maker.createCommand();
					if (guis.containsKey(name))
						guis.replace(name, maker);
					else
						guis.put(name, maker);
					loaded.add(name);
					Loader.plugin.getLogger().info("[GUI] Loaded GUI " + name);
				} else if (guis.containsKey(name)) // Removing if loaded
					guis.remove(name);

			}
		}
	}

	public static void open(Player player, String guiname) {
		GUIMaker maker = guis.get(guiname);
		if (maker != null)
			maker.open(player);
		else
			Loader.plugin.getLogger().warning("GUI " + guiname + " does not exist!");
	}
}

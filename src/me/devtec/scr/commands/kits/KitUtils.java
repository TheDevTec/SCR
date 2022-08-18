package me.devtec.scr.commands.kits;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.devtec.scr.Loader;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.game.ItemMaker;

public class KitUtils {

	public static HashMap<String, Kit> loaded_kits = new HashMap<>();
	
	public static void loadKits() {
		//Utility
		File file = new File("plugins/SCR/kits");
		if(file.exists() && file.isDirectory()) {
			for(File f : file.listFiles()) {
				Config c = new Config(f);
				if(c.getBoolean("enabled")) {
					if(!loaded_kits.containsKey(c.getString("name"))) {
						loaded_kits.put(c.getString("name"), new Kit(c));
						Loader.plugin.getLogger().info("[Kits] Loaded kit "+c.getString("name"));
					}
				}
			}
		}
	}
	public static List<String> getKitsFor(CommandSender player){
		if(player instanceof Player) {
			List<String> list = new ArrayList<>();
			for(Kit kit : loaded_kits.values()) {
				if(player.hasPermission(kit.permission()))
					list.add(kit.config.getString("name"));
			}
			return list;
		}
		return Collections.emptyList();
	}
	
	
	public static class Kit {
		
		public Kit(Config c) {
			this.config=c;
		}
		
		public Config config;

		public String getName() {
			return config.getString("name");
		}
		public String displayName() {
			return StringUtils.colorize(config.getString("displayName"));
		}
		public String permission() {
			return config.getString("permission");
		}
		public String getCooldownTime() {
			if(config.exists("cooldown"))
				return config.getString("cooldown");
			else
				return "0";
		}
		public String getCost() {
			if(config.exists("cost"))
				return config.getString("cost");
			else
				return "0";
		}
		
		// path: items.<add | set>.<name | slot>
		public ItemStack getItem(String path) {
			return ItemMaker.loadFromConfig(config, path).clone();
		}

		// path: items.<add | set>.<name | slot>
		public void setItem(String path, ItemStack item) {
			ItemMaker.saveToConfig(config, path, item);
			config.save();
		}
		
		public void giveItems(CommandSender sender) {
			if( !(sender instanceof Player) )
				return;
			Player p = (Player) sender;
			if(config.exists("items.set")) {
				for(String sslot : config.getKeys("items.set")) {
					int slot = StringUtils.getInt(sslot);
					p.getInventory().setItem(slot, getItem("items.set."+sslot));
				}
			}
			if(config.exists("items.add")) {
				for(String item : config.getKeys("items.add")) {
					p.getInventory().addItem(getItem("items.set."+item));
				}
			}
		}
	}
}

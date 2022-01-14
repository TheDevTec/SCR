package me.devtec.scr.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;

public class ActionBar {
	public static boolean isLoaded;
	private static int task;
	public static List<String> disabledWorlds;
	public static Map<UUID, Long> affected = new HashMap<>();
	public static List<UUID> disabledToggle = new ArrayList<>();
	
	public static void load(List<String> dWorlds, long time) {
		if(isLoaded)return;
		isLoaded=true;
		disabledWorlds=dWorlds;
		if(time>0)
		task = new Tasker() {
			public void run() {
				for(Player player : TheAPI.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(affected.getOrDefault(player.getUniqueId(), 0L)-System.currentTimeMillis()/1000 > 0)
							disable(player);
						continue;
					}
					if(toggled(player)) {
						if(canToggle(player)) {
							if(disabledToggle.contains(player.getUniqueId())) {
								disabledToggle.remove(player.getUniqueId());
							}else {
								disable(player);
							}
							continue;
						}else {
							if(!disabledToggle.contains(player.getUniqueId())) {
								disabledToggle.add(player.getUniqueId());

								String text = text(player);
								
								int fadeIn = fadeIn(player);
								int stay = stay(player);
								int fadeOut = fadeOut(player);

								affected.put(player.getUniqueId(), System.currentTimeMillis()/1000+fadeIn/20+stay/20+fadeOut/20);
								TheAPI.sendActionBar(player, text, fadeIn, stay, fadeOut);
							}
							continue;
						}
					}else {
						disabledToggle.remove(player.getUniqueId());
					}
					String text = text(player);
					
					int fadeIn = fadeIn(player);
					int stay = stay(player);
					int fadeOut = fadeOut(player);
					
					affected.put(player.getUniqueId(), System.currentTimeMillis()/1000+fadeIn/20+stay/20+fadeOut/20);
					TheAPI.sendActionBar(player, text, fadeIn, stay, fadeOut);
				}
			}
		}.runRepeating(10, time);
	}
	
	protected static int fadeIn(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.actionbar.exists(path)) {
			//players
			if(ConfigManager.actionbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.actionbar.getString(path+".players."+player.getName()+".fadeIn");
				if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
			}
			//groups
			if(ConfigManager.actionbar.exists(path+".groups."+group)) {
				String get = ConfigManager.actionbar.getString(path+".groups."+group+".fadeIn");
				if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
			}
			//global
			String get = ConfigManager.actionbar.getString(path+".fadeIn");
			if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.actionbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.actionbar.getString(path+".fadeIn");
			if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.actionbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.actionbar.getString(path+".fadeIn");
			if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 4) global
		 */
		return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, ConfigManager.actionbar.getString("fadeIn")));
	}
	
	protected static int fadeOut(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.actionbar.exists(path)) {
			//players
			if(ConfigManager.actionbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.actionbar.getString(path+".players."+player.getName()+".fadeOut");
				if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
			}
			//groups
			if(ConfigManager.actionbar.exists(path+".groups."+group)) {
				String get = ConfigManager.actionbar.getString(path+".groups."+group+".fadeOut");
				if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
			}
			//global
			String get = ConfigManager.actionbar.getString(path+".fadeOut");
			if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.actionbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.actionbar.getString(path+".fadeOut");
			if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.actionbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.actionbar.getString(path+".fadeOut");
			if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 4) global
		 */
		return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, ConfigManager.actionbar.getString("fadeOut")));
	}
	
	protected static int stay(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.actionbar.exists(path)) {
			//players
			if(ConfigManager.actionbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.actionbar.getString(path+".players."+player.getName()+".stay");
				if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
			}
			//groups
			if(ConfigManager.actionbar.exists(path+".groups."+group)) {
				String get = ConfigManager.actionbar.getString(path+".groups."+group+".stay");
				if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
			}
			//global
			String get = ConfigManager.actionbar.getString(path+".stay");
			if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.actionbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.actionbar.getString(path+".stay");
			if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.actionbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.actionbar.getString(path+".stay");
			if(get!=null)return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 4) global
		 */
		return (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, ConfigManager.actionbar.getString("fadeOut")));
	}
	
	protected static String text(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.actionbar.exists(path)) {
			//players
			if(ConfigManager.actionbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.actionbar.getString(path+".players."+player.getName()+".text");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//groups
			if(ConfigManager.actionbar.exists(path+".groups."+group)) {
				String get = ConfigManager.actionbar.getString(path+".groups."+group+".text");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//global
			String get = ConfigManager.actionbar.getString(path+".text");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.actionbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.actionbar.getString(path+".text");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.actionbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.actionbar.getString(path+".text");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 4) global
		 */
		return PlaceholderAPI.setPlaceholders(player, ConfigManager.actionbar.getString("text"));
	}
	
	public static boolean toggled(Player player) {
		return TheAPI.getUser(player).getBoolean("scr.scoreboard");
	}
	
	public static void setToggled(Player player, boolean status) {
		TheAPI.getUser(player).setAndSave("scr.scoreboard",status);
	}
	
	protected static boolean canToggle(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.actionbar.exists(path)) {
			//players
			if(ConfigManager.actionbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.actionbar.getString(path+".players."+player.getName()+".toggleable");
				if(get!=null)return StringUtils.getBoolean(get);
			}
			//groups
			if(ConfigManager.actionbar.exists(path+".groups."+group)) {
				String get = ConfigManager.actionbar.getString(path+".groups."+group+".toggleable");
				if(get!=null)return StringUtils.getBoolean(get);
			}
			//global
			String get = ConfigManager.actionbar.getString(path+".toggleable");
			if(get!=null)return StringUtils.getBoolean(get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.actionbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.actionbar.getString(path+".toggleable");
			if(get!=null)return StringUtils.getBoolean(get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.actionbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.actionbar.getString(path+".toggleable");
			if(get!=null)return StringUtils.getBoolean(get);
		}
		/*
		 * 4) global
		 */
		return ConfigManager.actionbar.getBoolean("toggleable");
	}
	
	public static void disable(Player player) {
		Long time = affected.remove(player.getUniqueId());
		if(time != null && time-System.currentTimeMillis()/1000 > 0) {
			TheAPI.sendActionBar(player, "", 10, 20, 10); //Reset
		}
	}

	public static void unload() {
		if(!isLoaded)return;
		isLoaded=false;
		for(Entry<UUID, Long> uuid : affected.entrySet()) {
			if(uuid.getValue()-System.currentTimeMillis()/1000 > 0) {
				TheAPI.sendActionBar(Bukkit.getPlayer(uuid.getKey()), "", 10, 20, 10); //Reset
			}
		}
		affected.clear();
		Scheduler.cancelTask(task);
	}
}
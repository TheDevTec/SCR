package me.devtec.scr.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.scr.modules.bossbar.BossBarManager;
import me.devtec.scr.modules.bossbar.SBossBar;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;

public class BossBar {
	public static boolean isLoaded;
	private static int task;
	public static List<String> disabledWorlds;
	public static Map<UUID, SBossBar> scores = new HashMap<>();
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
						if(scores.containsKey(player.getUniqueId()))
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
								SBossBar score = scores.get(player.getUniqueId());
								if(score==null)
									scores.put(player.getUniqueId(), score=BossBarManager.create(player));
								score.setTitle(title(player));
								score.setProgress(progress(player));
								score.setStyle(style(player));
								score.setColor(color(player));
							}
							continue;
						}
					}else {
						disabledToggle.remove(player.getUniqueId());
					}
					SBossBar score = scores.get(player.getUniqueId());
					if(score==null)
						scores.put(player.getUniqueId(), score=BossBarManager.create(player));
					score.setTitle(title(player));
					score.setProgress(progress(player));
					score.setStyle(style(player));
					score.setColor(color(player));
				}
			}
		}.runRepeating(10, time);
	}
	
	protected static String title(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.bossbar.exists(path)) {
			//players
			if(ConfigManager.bossbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.bossbar.getString(path+".players."+player.getName()+".title");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//groups
			if(ConfigManager.bossbar.exists(path+".groups."+group)) {
				String get = ConfigManager.bossbar.getString(path+".groups."+group+".title");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//global
			String get = ConfigManager.bossbar.getString(path+".title");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.bossbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.bossbar.getString(path+".title");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.bossbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.bossbar.getString(path+".title");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 4) global
		 */
		return PlaceholderAPI.setPlaceholders(player, ConfigManager.bossbar.getString("title"));
	}
	
	protected static String color(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.bossbar.exists(path)) {
			//players
			if(ConfigManager.bossbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.bossbar.getString(path+".players."+player.getName()+".color");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//groups
			if(ConfigManager.bossbar.exists(path+".groups."+group)) {
				String get = ConfigManager.bossbar.getString(path+".groups."+group+".color");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//global
			String get = ConfigManager.bossbar.getString(path+".color");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.bossbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.bossbar.getString(path+".color");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.bossbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.bossbar.getString(path+".color");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 4) global
		 */
		return PlaceholderAPI.setPlaceholders(player, ConfigManager.bossbar.getString("color"));
	}
	
	protected static String style(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.bossbar.exists(path)) {
			//players
			if(ConfigManager.bossbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.bossbar.getString(path+".players."+player.getName()+".style");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//groups
			if(ConfigManager.bossbar.exists(path+".groups."+group)) {
				String get = ConfigManager.bossbar.getString(path+".groups."+group+".style");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//global
			String get = ConfigManager.bossbar.getString(path+".style");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.bossbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.bossbar.getString(path+".style");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.bossbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.bossbar.getString(path+".style");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 4) global
		 */
		return PlaceholderAPI.setPlaceholders(player, ConfigManager.bossbar.getString("style"));
	}
	
	protected static double progress(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.bossbar.exists(path)) {
			//players
			if(ConfigManager.bossbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.bossbar.getString(path+".players."+player.getName()+".progress");
				if(get!=null)return StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
			}
			//groups
			if(ConfigManager.bossbar.exists(path+".groups."+group)) {
				String get = ConfigManager.bossbar.getString(path+".groups."+group+".progress");
				if(get!=null)return StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
			}
			//global
			String get = ConfigManager.bossbar.getString(path+".progress");
			if(get!=null)return StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.bossbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.bossbar.getString(path+".progress");
			if(get!=null)return StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.bossbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.bossbar.getString(path+".progress");
			if(get!=null)return StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 4) global
		 */
		return StringUtils.calculate(PlaceholderAPI.setPlaceholders(player, ConfigManager.bossbar.getString("progress")));
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
		if(ConfigManager.bossbar.exists(path)) {
			//players
			if(ConfigManager.bossbar.exists(path+".players."+player.getName())) {
				String get = ConfigManager.bossbar.getString(path+".players."+player.getName()+".toggleable");
				if(get!=null)return StringUtils.getBoolean(get);
			}
			//groups
			if(ConfigManager.bossbar.exists(path+".groups."+group)) {
				String get = ConfigManager.bossbar.getString(path+".groups."+group+".toggleable");
				if(get!=null)return StringUtils.getBoolean(get);
			}
			//global
			String get = ConfigManager.bossbar.getString(path+".toggleable");
			if(get!=null)return StringUtils.getBoolean(get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.bossbar.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.bossbar.getString(path+".toggleable");
			if(get!=null)return StringUtils.getBoolean(get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.bossbar.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.bossbar.getString(path+".toggleable");
			if(get!=null)return StringUtils.getBoolean(get);
		}
		/*
		 * 4) global
		 */
		return ConfigManager.bossbar.getBoolean("toggleable");
	}
	
	public static void disable(Player player) {
		SBossBar bar = scores.remove(player.getUniqueId());
		if(bar!=null)
			bar.remove();
	}

	public static void unload() {
		if(!isLoaded)return;
		isLoaded=false;
		for(SBossBar bar : scores.values())
			bar.remove();
		scores.clear();
		Scheduler.cancelTask(task);
	}
}

package me.devtec.scr.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.scoreboardapi.SimpleScore;
import me.devtec.theapi.utils.StringUtils;

public class Scoreboard implements Module {
	private boolean isLoaded;
	private int task;
	public List<String> disabledWorlds;
	public Map<UUID, SimpleScore> scores = new HashMap<>();
	public List<UUID> disabledToggle = new ArrayList<>();
	
	public Module load() {
		load(ConfigManager.scoreboard.getStringList("settings.disabledWorlds"), (long)StringUtils.calculate(ConfigManager.scoreboard.getString("settings.reflesh")));
		return this;
	}
	
	public void load(List<String> dWorlds, long time) {
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
								SimpleScore score = scores.get(player.getUniqueId());
								if(score==null)
									scores.put(player.getUniqueId(), score=new SimpleScore());
								score.setTitle(title(player));
								score.addLines(lines(player));
								score.send(player);
							}
							continue;
						}
					}else {
						disabledToggle.remove(player.getUniqueId());
					}
					SimpleScore score = scores.get(player.getUniqueId());
					if(score==null)
						scores.put(player.getUniqueId(), score=new SimpleScore());
					score.setTitle(title(player));
					score.addLines(lines(player));
					score.send(player);
				}
			}
		}.runRepeating(10, time);
	}

	protected static List<String> lines(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.scoreboard.exists(path)) {
			//players
			if(ConfigManager.scoreboard.exists(path+".players."+player.getName())) {
				List<String> get = ConfigManager.scoreboard.getStringList(path+".players."+player.getName()+".lines");
				if(!get.isEmpty())return PlaceholderAPI.setPlaceholders(player, get);
			}
			//groups
			if(ConfigManager.scoreboard.exists(path+".groups."+group)) {
				List<String> get = ConfigManager.scoreboard.getStringList(path+".groups."+group+".lines");
				if(!get.isEmpty())return PlaceholderAPI.setPlaceholders(player, get);
			}
			//global
			List<String> get = ConfigManager.scoreboard.getStringList(path+".lines");
			if(!get.isEmpty())return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.scoreboard.exists(path="players."+player.getName())) {
			//global
			List<String> get = ConfigManager.scoreboard.getStringList(path+".lines");
			if(!get.isEmpty())return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.scoreboard.exists(path="groups."+group)) {
			//global
			List<String> get = ConfigManager.scoreboard.getStringList(path+".lines");
			if(!get.isEmpty())return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 4) global
		 */
		return PlaceholderAPI.setPlaceholders(player, ConfigManager.scoreboard.getStringList("lines"));
	}
	protected static String title(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.scoreboard.exists(path)) {
			//players
			if(ConfigManager.scoreboard.exists(path+".players."+player.getName())) {
				String get = ConfigManager.scoreboard.getString(path+".players."+player.getName()+".title");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//groups
			if(ConfigManager.scoreboard.exists(path+".groups."+group)) {
				String get = ConfigManager.scoreboard.getString(path+".groups."+group+".title");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//global
			String get = ConfigManager.scoreboard.getString(path+".title");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.scoreboard.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.scoreboard.getString(path+".title");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.scoreboard.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.scoreboard.getString(path+".title");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 4) global
		 */
		return PlaceholderAPI.setPlaceholders(player, ConfigManager.scoreboard.getString("title"));
	}
	
	public static boolean toggled(Player player) {
		return TheAPI.getUser(player).getBoolean("scr.scoreboard");
	}
	
	public static void setToggled(Player player, boolean status) {
		TheAPI.getUser(player).setAndSave("scr.scoreboard",status);
	}
	
	protected static boolean canToggle(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.scoreboard.exists(path)) {
			//players
			if(ConfigManager.scoreboard.exists(path+".players."+player.getName())) {
				String get = ConfigManager.scoreboard.getString(path+".players."+player.getName()+".toggleable");
				if(get!=null)return StringUtils.getBoolean(get);
			}
			//groups
			if(ConfigManager.scoreboard.exists(path+".groups."+group)) {
				String get = ConfigManager.scoreboard.getString(path+".groups."+group+".toggleable");
				if(get!=null)return StringUtils.getBoolean(get);
			}
			//global
			String get = ConfigManager.scoreboard.getString(path+".toggleable");
			if(get!=null)return StringUtils.getBoolean(get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.scoreboard.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.scoreboard.getString(path+".toggleable");
			if(get!=null)return StringUtils.getBoolean(get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.scoreboard.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.scoreboard.getString(path+".toggleable");
			if(get!=null)return StringUtils.getBoolean(get);
		}
		/*
		 * 4) global
		 */
		return ConfigManager.scoreboard.getBoolean("toggleable");
	}
	
	public void disable(Player player) {
		if(scores.remove(player.getUniqueId())!=null)
			SimpleScore.scores.remove(player.getName()).destroy();
	}

	public Module unload() {
		if(!isLoaded)return this;
		isLoaded=false;
		for(UUID uuid : scores.keySet()) {
			SimpleScore.scores.remove(Bukkit.getPlayer(uuid).getName()).destroy();
		}
		scores.clear();
		Scheduler.cancelTask(task);
		return this;
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}
}

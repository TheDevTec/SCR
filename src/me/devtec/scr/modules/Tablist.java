package me.devtec.scr.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.TabListAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.components.ComponentAPI;
import me.devtec.theapi.utils.nms.NmsProvider.Action;
import me.devtec.theapi.utils.nms.NmsProvider.DisplayType;
import me.devtec.theapi.utils.nms.NmsProvider.PlayerInfoType;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class Tablist {
	public static boolean isLoaded;
	private static int task, task2, task3, task4;
	public static Map<Player, NameTagAPI> nameTags = new HashMap<>();
	public static List<Player> applied = new ArrayList<>();
	private static DisplayType displayType;
	
	public static void load(List<String> disabledWorlds, long headerFooter, long name, long nametag, long yellownumber) {
		if(isLoaded)return;
		isLoaded=true;
		//headerFooter
		if(headerFooter>0)
		task = new Tasker() {
			public void run() {
				for(Player player : TheAPI.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(applied.contains(player))
							disable(player);
						continue;
					}
					if(!applied.contains(player))
						applied.add(player);
					TabListAPI.setHeaderFooter(player, header(player), footer(player));
				}
			}
		}.runRepeating(10, headerFooter);
		//playerName
		if(name>0)
		task2 = new Tasker() {
			public void run() {
				for(Player player : TheAPI.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(applied.contains(player))
							disable(player);
						continue;
					}
					if(!applied.contains(player))
						applied.add(player);
					Object obj = TheAPI.getNmsProvider().packetPlayerInfo(PlayerInfoType.UPDATE_DISPLAY_NAME, player);
					Ref.set(((List<?>)Ref.get(obj,"b")).get(0), "d", ComponentAPI.toIChatBaseComponent(ComponentAPI.toComponent(StringUtils.colorize(playerListName(player)), true)));
					Ref.sendPacket(player, obj);
				}
			}
		}.runRepeating(15, name);
		//nametag
		if(nametag>0)
		task3 = new Tasker() {
			public void run() {
				for(Player player : TheAPI.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(applied.contains(player))
							disable(player);
						continue;
					}
					if(!applied.contains(player)) {
						applied.add(player);
					}
					NameTagAPI tag;
					String sort = sorting(player);
					nameTags.put(player, tag = new NameTagAPI(player, sort));
					if(!tag.name.equals(sort))
						tag.setName(sort);
					String prefix = nameTagPrefix(player);
					tag.set(getColor(StringUtils.getLastColors(prefix)), prefix, nameTagSuffix(player));
					tag.send();
				}
			}

			private ChatColor getColor(String lastColors) {
				if(lastColors.isEmpty())return null;
				return ChatColor.getByChar(lastColors.charAt(1));
			}
		}.runRepeating(20, nametag);
		//yellownumber
		if(yellownumber>0)
		task4 = new Tasker() {
			public void run() {
				for(Player player : TheAPI.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(applied.contains(player))
							disable(player);
						continue;
					}
					int number = yellowNumber(player);
					if(!applied.contains(player)) {
						applied.add(player);
						Ref.sendPacket(player, createObjectivePacket(0,"ping",displayType));
						Ref.sendPacket(player, createObjectivePacket(0,player.getName(),displayType));
						Object packet = LoaderClass.nmsProvider.packetScoreboardDisplayObjective(0, null);
						Ref.set(packet, "b", "ping");
						Ref.sendPacket(player, packet);
					}
					Ref.sendPacket(player, LoaderClass.nmsProvider.packetScoreboardScore(Action.CHANGE, "ping", player.getName(), number));
				}
			}
		}.runRepeating(20, yellownumber);
	}
	
	/*
	 * Search Priorities
	 * 1) worlds
	 *   1) players
	 *   2) groups
	 *   3) global - world
	 * 2) players
	 * 3) groups
	 * 4) global
	 */
	
	protected static int yellowNumber(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms.getPrimaryGroup(player);
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.tablist.exists(path)) {
			//players
			if(ConfigManager.tablist.exists(path+".players."+player.getName())) {
				String get = ConfigManager.tablist.getString(path+".players."+player.getName()+".yellow-number");
				if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				String get = ConfigManager.tablist.getString(path+".groups."+group+".yellow-number");
				if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
			}
			//global
			String get = ConfigManager.tablist.getString(path+".yellow-number");
			if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.tablist.getString(path+".yellow-number");
			if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.tablist.getString(path+".yellow-number");
			if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 4) global
		 */
		return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, ConfigManager.tablist.getString("yellow-number")));
	}

	protected static String footer(Player player) {
		return "VEEERRYYY LOONG FOOTER";
	}

	protected static String header(Player player) {
		return "VEEERRYYY LOONG HEADER";
	}

	protected static String playerListName(Player player) {
		return player.getName();
	}

	protected static String nameTagPrefix(Player player) {
		return "§cVEEERRYYY LOONG PREFIX";
	}

	protected static String nameTagSuffix(Player player) {
		return "VEEERRYYY LOONG SUUUFIX";
	}

	protected static String sorting(Player player) {
		return player.getName().startsWith("S")?"x":"a";
	}

	public static void disable(Player player) {
		TabListAPI.setHeaderFooter(player, "", "");
		player.setPlayerListName(null);
		nameTags.remove(player).reset();
		applied.remove(player);
		Ref.sendPacket(player, createObjectivePacket(1, TheAPI.isNewVersion()?null:"", displayType));
	}
	
	private static Object createObjectivePacket(int mode, String displayName, DisplayType type) {
		Object packet = LoaderClass.nmsProvider.packetScoreboardObjective();
		if(TheAPI.isNewerThan(16)) {
			Ref.set(packet, "d", "ping");
			Ref.set(packet, "e", Ref.IChatBaseComponentJson("{\"text\":\""+displayName+"\"}"));
			Ref.set(packet, "f", LoaderClass.nmsProvider.getEnumScoreboardHealthDisplay(type));
			Ref.set(packet, "g", mode);
		}else {
			Ref.set(packet, "a", "ping");
			Ref.set(packet, "b", Ref.IChatBaseComponentJson("{\"text\":\""+displayName+"\"}"));
			if(TheAPI.isNewerThan(7)) {
				Ref.set(packet, "c", LoaderClass.nmsProvider.getEnumScoreboardHealthDisplay(type));
				Ref.set(packet, "d", mode);
			}else
				Ref.set(packet, "c", mode);
		}
		return packet;
	}

	public static void unload() {
		if(!isLoaded)return;
		isLoaded=false;
		for(Player player : applied)
			disable(player);
		applied.clear();
		Scheduler.cancelTask(task);
		Scheduler.cancelTask(task2);
		Scheduler.cancelTask(task3);
		Scheduler.cancelTask(task4);
	}
}

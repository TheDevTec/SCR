package me.devtec.scr.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	public static List<String> disabledWorlds;
	public static Map<UUID, NameTagAPI> nameTags = new HashMap<>();
	public static Map<UUID, DisplayType> dType = new HashMap<>();
	
	// Map<groupName, sortingValue>
	public static Map<String, String> sorting = new HashMap<>();
	
	public static List<UUID> 
	        appliedHF = new ArrayList<>(), //header footer
	    	appliedTN = new ArrayList<>(), //tab name
			appliedNT = new ArrayList<>(), //name tag
			appliedYN = new ArrayList<>(); //yellow number
	
	private static void generate(List<String> value, int size) {
		int current = 0;
		
		int length = (""+size).length()+1;
		for(int i = 0; i < size; ++i) {
			String s = "";
			int limit = length-(i+"").length();
			for(int d = 0; d < limit; ++d)
				s+="0";
			s+=i;
			Tablist.sorting.put(value.get(current++), s);
		}
	}
	
	public static void load(List<String> sorting, List<String> dWorlds, long headerFooter, long name, long nametag, long yellownumber) {
		if(isLoaded)return;
		isLoaded=true;
		disabledWorlds=dWorlds;
		generate(sorting, sorting.size());
		//headerFooter
		if(headerFooter>0)
		task = new Tasker() {
			public void run() {
				for(Player player : TheAPI.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(appliedHF.contains(player.getUniqueId()))
							disable(player, dType.remove(player.getUniqueId()));
						continue;
					}
					if(!appliedHF.contains(player.getUniqueId()))
						appliedHF.add(player.getUniqueId());
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
						if(appliedTN.contains(player.getUniqueId()))
							disable(player, dType.remove(player.getUniqueId()));
						continue;
					}
					if(!appliedTN.contains(player.getUniqueId()))
						appliedTN.add(player.getUniqueId());
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
						if(appliedNT.contains(player.getUniqueId()))
							disable(player, dType.remove(player.getUniqueId()));
						continue;
					}
					if(!appliedNT.contains(player.getUniqueId())) {
						appliedNT.add(player.getUniqueId());
					}
					NameTagAPI tag;
					String sort = sorting(player);
					nameTags.put(player.getUniqueId(), tag = new NameTagAPI(player, sort));
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
						if(appliedYN.contains(player.getUniqueId())) {
							disable(player, dType.remove(player.getUniqueId()));
						}
						continue;
					}
					int number = yellowNumber(player);
					DisplayType displayType = yellowNumberDisplay(player);
					DisplayType previous = dType.get(player.getUniqueId());
					if(!appliedYN.contains(player.getUniqueId())) {
						appliedYN.add(player.getUniqueId());
						Ref.sendPacket(player, createObjectivePacket(0,"ping",displayType));
						Ref.sendPacket(player, createObjectivePacket(0,player.getName(),displayType));
						Object packet = LoaderClass.nmsProvider.packetScoreboardDisplayObjective(0, null);
						Ref.set(packet, "b", "ping");
						Ref.sendPacket(player, packet);
					}else {
						if(previous!=displayType) {
							dType.put(player.getUniqueId(), displayType);
							Ref.sendPacket(player, createObjectivePacket(2,"ping",previous));
							Ref.sendPacket(player, createObjectivePacket(2,player.getName(),previous));
							Ref.sendPacket(player, createObjectivePacket(0,"ping",displayType));
							Ref.sendPacket(player, createObjectivePacket(0,player.getName(),displayType));
							Object packet = LoaderClass.nmsProvider.packetScoreboardDisplayObjective(0, null);
							Ref.set(packet, "b", "ping");
							Ref.sendPacket(player, packet);
						}
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
				String get = ConfigManager.tablist.getString(path+".players."+player.getName()+".yellow-number.value");
				if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				String get = ConfigManager.tablist.getString(path+".groups."+group+".yellow-number.value");
				if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
			}
			//global
			String get = ConfigManager.tablist.getString(path+".yellow-number.value");
			if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.tablist.getString(path+".yellow-number.value");
			if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.tablist.getString(path+".yellow-number.value");
			if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 4) global
		 */
		return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, ConfigManager.tablist.getString("yellow-number.value")));
	}

	protected static DisplayType yellowNumberDisplay(Player player) {
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
				String get = ConfigManager.tablist.getString(path+".players."+player.getName()+".yellow-number.show_type");
				if(get!=null)return showType(get);
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				String get = ConfigManager.tablist.getString(path+".groups."+group+".yellow-number.show_type");
				if(get!=null)return showType(get);
			}
			//global
			String get = ConfigManager.tablist.getString(path+".yellow-number.show_type");
			if(get!=null)return showType(get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.tablist.getString(path+".yellow-number.show_type");
			if(get!=null)return showType(get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.tablist.getString(path+".yellow-number.show_type");
			if(get!=null)return showType(get);
		}
		/*
		 * 4) global
		 */
		return showType(ConfigManager.tablist.getString("yellow-number.show_type"));
	}
	
	private static DisplayType showType(String showType) {
		return showType.equalsIgnoreCase("heart")||showType.equalsIgnoreCase("hearts")||showType.equalsIgnoreCase("hp")?DisplayType.HEARTS:DisplayType.INTEGER;
	}

	protected static String footer(Player player) {
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
				List<String> get = ConfigManager.tablist.getStringList(path+".players."+player.getName()+".footer");
				if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				List<String> get = ConfigManager.tablist.getStringList(path+".groups."+group+".footer");
				if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
			}
			//global
			List<String> get = ConfigManager.tablist.getStringList(path+".footer");
			if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			List<String> get = ConfigManager.tablist.getStringList(path+".footer");
			if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			List<String> get = ConfigManager.tablist.getStringList(path+".footer");
			if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
		}
		/*
		 * 4) global
		 */
		return StringUtils.join(PlaceholderAPI.setPlaceholders(player, ConfigManager.tablist.getStringList("footer")), "\n");
	}

	protected static String header(Player player) {
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
				List<String> get = ConfigManager.tablist.getStringList(path+".players."+player.getName()+".header");
				if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				List<String> get = ConfigManager.tablist.getStringList(path+".groups."+group+".header");
				if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
			}
			//global
			List<String> get = ConfigManager.tablist.getStringList(path+".header");
			if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			List<String> get = ConfigManager.tablist.getStringList(path+".header");
			if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			List<String> get = ConfigManager.tablist.getStringList(path+".header");
			if(!get.isEmpty())return StringUtils.join(PlaceholderAPI.setPlaceholders(player, get), "\n");
		}
		/*
		 * 4) global
		 */
		return StringUtils.join(PlaceholderAPI.setPlaceholders(player, ConfigManager.tablist.getStringList("header")), "\n");
	}

	protected static String playerListName(Player player) {
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
				String get = ConfigManager.tablist.getString(path+".players."+player.getName()+".tab-name");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				String get = ConfigManager.tablist.getString(path+".groups."+group+".tab-name");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//global
			String get = ConfigManager.tablist.getString(path+".tab-name");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.tablist.getString(path+".tab-name");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.tablist.getString(path+".tab-name");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 4) global
		 */
		return PlaceholderAPI.setPlaceholders(player, ConfigManager.tablist.getString("tab-name"));
	}

	protected static String nameTagPrefix(Player player) {
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
				String get = ConfigManager.tablist.getString(path+".players."+player.getName()+".tag-prefix");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				String get = ConfigManager.tablist.getString(path+".groups."+group+".tag-prefix");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//global
			String get = ConfigManager.tablist.getString(path+".tag-prefix");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.tablist.getString(path+".tag-prefix");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.tablist.getString(path+".tag-prefix");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 4) global
		 */
		return PlaceholderAPI.setPlaceholders(player, ConfigManager.tablist.getString("tag-prefix"));
	}

	protected static String nameTagSuffix(Player player) {
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
				String get = ConfigManager.tablist.getString(path+".players."+player.getName()+".tag-suffix");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				String get = ConfigManager.tablist.getString(path+".groups."+group+".tag-suffix");
				if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
			}
			//global
			String get = ConfigManager.tablist.getString(path+".tag-suffix");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.tablist.getString(path+".tag-suffix");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.tablist.getString(path+".tag-suffix");
			if(get!=null)return PlaceholderAPI.setPlaceholders(player, get);
		}
		/*
		 * 4) global
		 */
		return PlaceholderAPI.setPlaceholders(player, ConfigManager.tablist.getString("tag-suffix"));
	}

	protected static String sorting(Player player) {
		String def = Loader.perms!=null?sorting.get(Loader.perms.getPrimaryGroup(player)):player.getName();
		return def==null?player.getName():def;
	}

	public static void disable(Player player, DisplayType displayType) {
		TabListAPI.setHeaderFooter(player, "", "");
		player.setPlayerListName(null);
		NameTagAPI tag = nameTags.remove(player.getUniqueId());
		if(tag!=null)tag.reset();
		appliedHF.remove(player.getUniqueId());
		appliedNT.remove(player.getUniqueId());
		appliedTN.remove(player.getUniqueId());
		appliedYN.remove(player.getUniqueId());
		if(displayType!=null)
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
		Scheduler.cancelTask(task);
		Scheduler.cancelTask(task2);
		Scheduler.cancelTask(task3);
		Scheduler.cancelTask(task4);
		sorting.clear();
		for(Player player : TheAPI.getOnlinePlayers())
			if(!disabledWorlds.contains(player.getWorld().getName()))
				disable(player, dType.remove(player.getUniqueId()));
	}
}

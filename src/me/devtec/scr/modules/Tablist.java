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
import me.devtec.theapi.apis.NameTagAPI;
import me.devtec.theapi.apis.TabListAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NmsProvider.Action;
import me.devtec.theapi.utils.nms.NmsProvider.DisplayType;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.utils.theapiutils.BukkitLoader;

public class Tablist implements Module {
	private boolean isLoaded;
	private List<Integer> tasks = new ArrayList<>();
	public List<String> disabledWorlds;
	public Map<UUID, NameTagAPI> nameTags = new HashMap<>();
	public Map<UUID, DisplayType> dType = new HashMap<>(), bType = new HashMap<>();
	
	// Map<groupName, sortingValue>
	public Map<String, String> sorting = new HashMap<>();
	
	public List<UUID> 
	        appliedHF = new ArrayList<>(), //header footer
	    	appliedTN = new ArrayList<>(), //tab name
			appliedNT = new ArrayList<>(), //name tag
			appliedBN = new ArrayList<>(), //below name
			appliedYN = new ArrayList<>(); //yellow number
	
	private void generate(List<String> value, int size) {
		int current = 0;
		
		int length = (""+size).length()+1;
		for(int i = 0; i < size; ++i) {
			String s = "";
			int limit = length-(i+"").length();
			for(int d = 0; d < limit; ++d)
				s+="0";
			s+=i;
			sorting.put(value.get(current++), s);
		}
	}
	
	public Module load() {
		load(ConfigManager.tablist.getStringList("sorting"), ConfigManager.tablist.getStringList("settings.disabledWorlds"), (long)StringUtils.calculate(ConfigManager.tablist.getString("settings.reflesh.header-footer"))
					, (long)StringUtils.calculate(ConfigManager.tablist.getString("settings.reflesh.tablist-name"))
					, (long)StringUtils.calculate(ConfigManager.tablist.getString("settings.reflesh.nametag"))
					, (long)StringUtils.calculate(ConfigManager.tablist.getString("settings.reflesh.yellow-number"))
					, (long)StringUtils.calculate(ConfigManager.tablist.getString("settings.reflesh.below-name")));
		return this;
	}
	
	public void load(List<String> sorting, List<String> dWorlds, long headerFooter, long name, long nametag, long yellownumber, long belowname) {
		if(isLoaded)return;
		isLoaded=true;
		disabledWorlds=dWorlds;
		generate(sorting, sorting.size());
		//headerFooter
		if(headerFooter>0)
			tasks.add(new Tasker() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(appliedHF.contains(player.getUniqueId()))
							disable(player, dType.remove(player.getUniqueId()), bType.remove(player.getUniqueId()));
						continue;
					}
					if(!appliedHF.contains(player.getUniqueId()))
						appliedHF.add(player.getUniqueId());
					TabListAPI.setHeaderFooter(player, header(player), footer(player));
				}
			}
		}.runRepeating(10, headerFooter));
		//playerName
		if(name>0)
			tasks.add(new Tasker() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(appliedTN.contains(player.getUniqueId()))
							disable(player, dType.remove(player.getUniqueId()), bType.remove(player.getUniqueId()));
						continue;
					}
					if(!appliedTN.contains(player.getUniqueId()))
						appliedTN.add(player.getUniqueId());
					TabListAPI.setTabListName(player, StringUtils.colorize(playerListName(player)));
				}
			}
		}.runRepeating(15, name));
		//nametag
		if(nametag>0)
			tasks.add(new Tasker() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(appliedNT.contains(player.getUniqueId()))
							disable(player, dType.remove(player.getUniqueId()), bType.remove(player.getUniqueId()));
						continue;
					}
					if(!appliedNT.contains(player.getUniqueId())) {
						appliedNT.add(player.getUniqueId());
					}
					NameTagAPI tag;
					String sort = sorting(player);
					nameTags.put(player.getUniqueId(), tag = new NameTagAPI(player, sort));
					if(!tag.getTeamName().equals(sort))
						tag.setName(sort);
					String prefix = nameTagPrefix(player);
					tag.set(getColor(StringUtils.getLastColors(prefix)), prefix, nameTagSuffix(player));
					tag.send(Loader.onlinePlayers(player).toArray(new Player[0]));
				}
			}

			private ChatColor getColor(String lastColors) {
				if(lastColors.isEmpty())return null;
				return ChatColor.getByChar(lastColors.charAt(1));
			}
		}.runRepeating(20, nametag));
		//yellownumber
		if(yellownumber>0)
			tasks.add(new Tasker() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(appliedYN.contains(player.getUniqueId())) {
							disable(player, dType.remove(player.getUniqueId()), bType.remove(player.getUniqueId()));
						}
						continue;
					}
					int number = yellowNumber(player);
					DisplayType displayType = yellowNumberDisplay(player);
					DisplayType previous = dType.get(player.getUniqueId());
					if(!appliedYN.contains(player.getUniqueId())) {
						appliedYN.add(player.getUniqueId());
						BukkitLoader.getPacketHandler().send(player, createObjectivePacket(0, "ping","ping",displayType));
						BukkitLoader.getPacketHandler().send(player, createObjectivePacket(0, "ping",player.getName(),displayType));
						Object packet = BukkitLoader.getNmsProvider().packetScoreboardDisplayObjective(0, null);
						Ref.set(packet, "b", "ping");
						BukkitLoader.getPacketHandler().send(player, packet);
					}else {
						if(previous!=displayType) {
							dType.put(player.getUniqueId(), displayType);
							BukkitLoader.getPacketHandler().send(player, createObjectivePacket(2, "ping","ping",previous));
							BukkitLoader.getPacketHandler().send(player, createObjectivePacket(2, "ping",player.getName(),previous));
							BukkitLoader.getPacketHandler().send(player, createObjectivePacket(0, "ping","ping",displayType));
							BukkitLoader.getPacketHandler().send(player, createObjectivePacket(0, "ping",player.getName(),displayType));
							Object packet = BukkitLoader.getNmsProvider().packetScoreboardDisplayObjective(0, null);
							Ref.set(packet, "b", "ping");
							BukkitLoader.getPacketHandler().send(player, packet);
						}
					}
					BukkitLoader.getPacketHandler().send(player, BukkitLoader.getNmsProvider().packetScoreboardScore(Action.CHANGE, "ping", player.getName(), number));
				}
			}
		}.runRepeating(20, yellownumber));
		//belowname
		if(belowname>0)
			tasks.add(new Tasker() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(appliedBN.contains(player.getUniqueId())) {
							disable(player, dType.remove(player.getUniqueId()), bType.remove(player.getUniqueId()));
						}
						continue;
					}
					int number = belowName(player);
					DisplayType displayType = belowNameDisplay(player);
					DisplayType previous = bType.get(player.getUniqueId());
					if(!appliedBN.contains(player.getUniqueId())) {
						appliedBN.add(player.getUniqueId());
						BukkitLoader.getPacketHandler().send(player, createObjectivePacket(0,"below","below",displayType));
						BukkitLoader.getPacketHandler().send(player, createObjectivePacket(0,"below",player.getName(),displayType));
						Object packet = BukkitLoader.getNmsProvider().packetScoreboardDisplayObjective(1, null);
						Ref.set(packet, "b", "below");
						BukkitLoader.getPacketHandler().send(player, packet);
					}else {
						if(previous!=displayType) {
							bType.put(player.getUniqueId(), displayType);
							BukkitLoader.getPacketHandler().send(player, createObjectivePacket(2,"below","below",previous));
							BukkitLoader.getPacketHandler().send(player, createObjectivePacket(2,"below",player.getName(),previous));
							BukkitLoader.getPacketHandler().send(player, createObjectivePacket(0,"below","below",displayType));
							BukkitLoader.getPacketHandler().send(player, createObjectivePacket(0,"below",player.getName(),displayType));
							Object packet = BukkitLoader.getNmsProvider().packetScoreboardDisplayObjective(1, null);
							Ref.set(packet, "b", "below");
							BukkitLoader.getPacketHandler().send(player, packet);
						}
					}
					BukkitLoader.getPacketHandler().send(player, BukkitLoader.getNmsProvider().packetScoreboardScore(Action.CHANGE, "below", player.getName(), number));
				}
			}
		}.runRepeating(20, belowname));
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

	protected static int belowName(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.tablist.exists(path)) {
			//players
			if(ConfigManager.tablist.exists(path+".players."+player.getName())) {
				String get = ConfigManager.tablist.getString(path+".players."+player.getName()+".below-name.value");
				if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				String get = ConfigManager.tablist.getString(path+".groups."+group+".below-name.value");
				if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
			}
			//global
			String get = ConfigManager.tablist.getString(path+".below-name.value");
			if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.tablist.getString(path+".below-name.value");
			if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.tablist.getString(path+".below-name.value");
			if(get!=null)return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, get));
		}
		/*
		 * 4) global
		 */
		return StringUtils.getInt(PlaceholderAPI.setPlaceholders(player, ConfigManager.tablist.getString("below-name.value")));
	}

	protected static DisplayType belowNameDisplay(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
		/*
		 * 1) worlds
		 *   1) players
		 *   2) groups
		 *   3) global - world
		 */
		if(ConfigManager.tablist.exists(path)) {
			//players
			if(ConfigManager.tablist.exists(path+".players."+player.getName())) {
				String get = ConfigManager.tablist.getString(path+".players."+player.getName()+".below-name.show_type");
				if(get!=null)return showType(get);
			}
			//groups
			if(ConfigManager.tablist.exists(path+".groups."+group)) {
				String get = ConfigManager.tablist.getString(path+".groups."+group+".below-name.show_type");
				if(get!=null)return showType(get);
			}
			//global
			String get = ConfigManager.tablist.getString(path+".below-name.show_type");
			if(get!=null)return showType(get);
		}
		/*
		 * 2) players
		 */
		if(ConfigManager.tablist.exists(path="players."+player.getName())) {
			//global
			String get = ConfigManager.tablist.getString(path+".below-name.show_type");
			if(get!=null)return showType(get);
		}
		/*
		 * 3) groups
		 */
		if(ConfigManager.tablist.exists(path="groups."+group)) {
			//global
			String get = ConfigManager.tablist.getString(path+".below-name.show_type");
			if(get!=null)return showType(get);
		}
		/*
		 * 4) global
		 */
		return showType(ConfigManager.tablist.getString("below-name.show_type"));
	}
	
	protected static int yellowNumber(Player player) {
		String path = "worlds."+player.getWorld().getName();
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
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
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
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
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
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
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
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
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
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
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
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
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup(player):"default";
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

	protected String sorting(Player player) {
		String def = Loader.perms!=null?sorting.get(Loader.perms.getPrimaryGroup(player)):player.getName();
		return def==null?player.getName():def;
	}

	public void disable(Player player, DisplayType displayType, DisplayType displayTypeBelow) {
		TabListAPI.setHeaderFooter(player, "", "");
		player.setPlayerListName(null);
		NameTagAPI tag = nameTags.remove(player.getUniqueId());
		if(tag!=null)tag.reset(Loader.onlinePlayers(player).toArray(new Player[0]));
		appliedHF.remove(player.getUniqueId());
		appliedNT.remove(player.getUniqueId());
		appliedTN.remove(player.getUniqueId());
		appliedYN.remove(player.getUniqueId());
		appliedBN.remove(player.getUniqueId());
		if(displayType!=null)
		BukkitLoader.getPacketHandler().send(player, createObjectivePacket(1, "ping", TheAPI.isNewVersion()?null:"", displayType));
		if(displayTypeBelow!=null)
		BukkitLoader.getPacketHandler().send(player, createObjectivePacket(1, "below", TheAPI.isNewVersion()?null:"", displayTypeBelow));
	}
	
	private static Object createObjectivePacket(int mode, String name, String displayName, DisplayType type) {
		Object packet = BukkitLoader.getNmsProvider().packetScoreboardObjective();
		if(TheAPI.isNewerThan(16)) {
			Ref.set(packet, "d", name);
			Ref.set(packet, "e", Ref.IChatBaseComponentJson("{\"text\":\""+displayName+"\"}"));
			Ref.set(packet, "f", BukkitLoader.getNmsProvider().getEnumScoreboardHealthDisplay(type));
			Ref.set(packet, "g", mode);
		}else {
			Ref.set(packet, "a", name);
			Ref.set(packet, "b", Ref.IChatBaseComponentJson("{\"text\":\""+displayName+"\"}"));
			if(TheAPI.isNewerThan(7)) {
				Ref.set(packet, "c", BukkitLoader.getNmsProvider().getEnumScoreboardHealthDisplay(type));
				Ref.set(packet, "d", mode);
			}else
				Ref.set(packet, "c", mode);
		}
		return packet;
	}

	public Module unload() {
		if(!isLoaded)return this;
		isLoaded=false;
		tasks.forEach(task -> Scheduler.cancelTask(task));
		tasks.clear();
		sorting.clear();
		for(Player player : Bukkit.getOnlinePlayers())
			if(!disabledWorlds.contains(player.getWorld().getName()))
				disable(player, dType.remove(player.getUniqueId()), bType.remove(player.getUniqueId()));
		return this;
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}
}

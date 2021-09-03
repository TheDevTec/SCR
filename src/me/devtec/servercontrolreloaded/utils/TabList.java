package me.devtec.servercontrolreloaded.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.economy.EcoTop;
import me.devtec.servercontrolreloaded.commands.info.Staff;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Item;
import me.devtec.servercontrolreloaded.utils.playtime.PlayTimeUtils;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.MemoryAPI;
import me.devtec.theapi.apis.TabListAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;

public class TabList {
	// GROUP, PRIORITE
	protected static final HashMap<String, String> sorting = new HashMap<>();

	public static void reload() {
		sorting.clear();
		List<String> sd = Loader.tab.getStringList("Options.Sorting-Groups");
		List<String> priorites = generate(sd.size());
		int current = 0;
		for(String a : sd)
			sorting.put(a, priorites.get(current++));
		
	}
	
	//limit 1000 groups
	private static List<String> generate(int size) {
		List<String> a = new LinkedList<>();
		for(int i = 0; i < size; ++i) {
			String s = "";
			int limit = 4-(i+"").length();
			for(int d = 0; d < limit; ++d)
				s+="0";
			s+=i;
			a.add(s);
		}
		return a;
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static String getPrefix(String path, boolean nametag, int type) {
		return Loader.tab.getString((type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups."))  +path+ "."+(nametag?"NameTag":"TabList")+".Prefix");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static String getSuffix(String path, boolean nametag, int type) {
		return Loader.tab.getString((type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups.")) + path+"."+(nametag?"NameTag":"TabList")+".Suffix");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static String getNameFormat(String path, int type) {
		return Loader.tab.getString((type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups.")) +path+ ".Format");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New prefix
	 */
	public static void setPrefix(String path, boolean nametag, int type, String value) {
		Loader.tab.set((type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups."))  +path+ "."+(nametag?"NameTag":"TabList")+".Prefix", value);
		Loader.tab.save();
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New prefix
	 */
	public static void setSuffix(String path, boolean nametag, int type, String value) {
		Loader.tab.set((type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups.")) + path+"."+(nametag?"NameTag":"TabList")+".Suffix", value);
		Loader.tab.save();
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New header
	 */
	public static void setHeader(String path, int type, List<String> value) {
		Loader.tab.set(path==null?"Header":(type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups.")) + path+".Header", value);
		Loader.tab.save();
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New header
	 */
	public static void setFooter(String path, int type, List<String> value) {
		Loader.tab.set(path==null?"Footer":(type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups.")) + path+".Footer", value);
		Loader.tab.save();
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static List<String> getHeader(String path, int type) {
		return Loader.tab.getStringList(path==null?"Header":(type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups.")) + path+".Header");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 */
	public static List<String> getFooter(String path, int type) {
		return Loader.tab.getStringList(path==null?"Footer":(type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups.")) + path+".Footer");
	}

	/**
	 * 
	 * @param path Name
	 * @param type 0 - player, 1 - world, 2 - group
	 * @param value New prefix
	 */
	public static void setNameFormat(String path, int type, String value) {
		Loader.tab.set((type==0?"PerPlayer.":(type==1?"PerWorld.":"Groups.")) +path+ ".Format", value);
		Loader.tab.save();
	}

	public static String getPrefix(Player p, boolean nametag) {
		if(Loader.tab==null)return null;
		if (Loader.tab.exists("PerPlayer." + p.getName() + "."+(nametag?"NameTag":"TabList")+".Prefix"))
			return replace(Loader.tab.getString("PerPlayer." + p.getName() + "."+(nametag?"NameTag":"TabList")+".Prefix"), p, true);
		if (Loader.tab.exists("PerWorld." + p.getWorld().getName() + "."+(nametag?"NameTag":"TabList")+".Prefix"))
			return replace(Loader.tab.getString("PerWorld." + p.getWorld().getName() + "."+(nametag?"NameTag":"TabList")+".Prefix"), p, true);
		String g = Staff.getGroup(p);
		if (Loader.tab.exists("Groups." + g + "."+(nametag?"NameTag":"TabList")+".Prefix"))
			return replace(Loader.tab.getString("Groups." + g + "."+(nametag?"NameTag":"TabList")+".Prefix"), p, true);
		return null;
	}

	public static String getSuffix(Player p, boolean nametag) {
		if(Loader.tab==null)return null;
		if (Loader.tab.exists("PerPlayer." + p.getName() + "."+(nametag?"NameTag":"TabList")+".Suffix"))
			return replace(Loader.tab.getString("PerPlayer." + p.getName() + "."+(nametag?"NameTag":"TabList")+".Suffix"), p, true);
		if (Loader.tab.exists("PerWorld." + p.getWorld().getName() + "."+(nametag?"NameTag":"TabList")+".Suffix"))
			return replace(Loader.tab.getString("PerWorld." + p.getWorld().getName() + "."+(nametag?"NameTag":"TabList")+".Suffix"), p, true);
		String g = Staff.getGroup(p);
		if (Loader.tab.exists("Groups." + g + "."+(nametag?"NameTag":"TabList")+".Suffix"))
			return replace(Loader.tab.getString("Groups." + g + "."+(nametag?"NameTag":"TabList")+".Suffix"), p, true);
		return null;
	}

	public static String getNameFormat(Player p) {
		if(Loader.tab==null)return p.getName();
		if (Loader.tab.exists("PerPlayer." + p.getName() + ".Format"))
			return replace(Loader.tab.getString("PerPlayer." + p.getName() + ".Format"), p, true);
		if (Loader.tab.exists("PerWorld." + p.getWorld().getName() + ".Format"))
			return replace(Loader.tab.getString("PerWorld." + p.getWorld().getName() + ".Format"), p, true);
		String g = Staff.getGroup(p);
		if (Loader.tab.exists("Groups." + g + ".Format"))
			return replace(Loader.tab.getString("Groups." + g + ".Format"), p, true);
		return "%tab_prefix% "+p.getName()+" %tab_suffix%";
	}
	static Pattern playtimetop = Pattern.compile("\\%playtime_top_([0-9]+)\\%")
			, playtime_player = Pattern.compile("\\%playtime_[_A-Za-z0-9]+\\%")
					, playtime_worldOrGm = Pattern.compile("\\%playtime_([_A-Za-z0-9]+)_(.+?)\\%")
					, playtime_world_gm = Pattern.compile("\\%playtime_([_A-Za-z0-9]+)_(.+?)_(SURVIVAL|CREATIVE|ADVENTURE|SPECTATOR)\\%", Pattern.CASE_INSENSITIVE)
					, playtime_worldOrGmMe = Pattern.compile("\\%playtime_(.+?)\\%")
					, playtime_world_gmMe = Pattern.compile("\\%playtime_(.+?)_(SURVIVAL|CREATIVE|ADVENTURE|SPECTATOR)\\%", Pattern.CASE_INSENSITIVE)
					, balancetop = Pattern.compile("\\%balancetop_([0-9]+)_(name|money|format_money|formatted_money)\\%", Pattern.CASE_INSENSITIVE);
	
	public static String replace(String header, Player p, boolean color) {
		if(p!=null) {
		if(header.contains("%money%"))
				header=header.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), false));
		if(header.contains("%formatted_money%"))
			header=header.replace("%formatted_money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), true));
		if(header.contains("%format_money%"))
			header=header.replace("%format_money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), true));
		if(header.contains("%online%")) {
			int seen = 0;
			for(Player s : TheAPI.getPlayers())
				if(API.canSee(p,s.getName()))++seen;
			header=header.replace("%online%", seen + "");
		}
		/*
		 * Playtime Placeholders
		 * 
		 *  %scr_playtime%
		 *  %scr_playtime_<player>%
		 *  %scr_playtime_<player>_<world>%
		 *  %scr_playtime_<player_<world>_<GAMEMODE>%
		 *  %scr_playtime_<player>_<GAMEMODE>
		 *  %scr_playtime_top_<position>%
		 *  
		 */
		if(header.contains("%playtime%")) {
			header=header.replace("%playtime%", StringUtils.timeToString(PlayTimeUtils.playtime(p)));
		}
		if(header.contains("%raw_playtime%")) {
			header=header.replace("%raw_playtime%", PlayTimeUtils.playtime(p)+"");
		}
		if(header.contains("%playtime_")) {
			if(header.contains("%playtime_top_")) {
				Matcher m = playtimetop.matcher(header);
				while(m.find())
					header=header.replace(m.group(), PlayTimeUtils.getTop(StringUtils.getInt(m.group(1))));
			}
			String player = null;
			GameMode mode = null;
			World world = null;
			//%playtime_<player>_<world>_<GAMEMODE>%
			Matcher m = playtime_world_gm.matcher(header);
			while(m.find()) {
				if(TheAPI.existsUser(m.group(1))) {
					player = m.group(1);
				}else continue;
				if(Bukkit.getWorld(m.group(2))!=null) {
					world = Bukkit.getWorld(m.group(2));
				}else continue;
				if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
					mode = GameMode.valueOf(m.group(3).toUpperCase());
				}else continue;
				header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(player, mode, world)));
			}
			//%playtime_<player>_<world/GAMEMODE>%
			m = playtime_worldOrGm.matcher(header);
			while(m.find()) {
				if(TheAPI.existsUser(m.group(1))) {
					player = m.group(1);
				}else continue;
				if(Bukkit.getWorld(m.group(2))!=null) {
					world = Bukkit.getWorld(m.group(2));
				}
				if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
					mode = GameMode.valueOf(m.group(3).toUpperCase());
				}
				if(world ==null && mode == null)continue;
				header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(player, mode, world)));
			}
			//%playtime_<world>_<GAMEMODE>%
			m = playtime_world_gmMe.matcher(header);
			while(m.find()) {
				if(TheAPI.existsUser(m.group(1))) {
					player = m.group(1);
				}else continue;
				if(Bukkit.getWorld(m.group(2))!=null) {
					world = Bukkit.getWorld(m.group(2));
				}else continue;
				if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
					mode = GameMode.valueOf(m.group(3).toUpperCase());
				}else continue;
				header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(p, mode, world)));
			}
			//%playtime_<world/GAMEMODE>%
			m = playtime_worldOrGmMe.matcher(header);
			while(m.find()) {
				if(TheAPI.existsUser(m.group(1))) {
					player = m.group(1);
				}else continue;
				if(Bukkit.getWorld(m.group(2))!=null) {
					world = Bukkit.getWorld(m.group(2));
				}
				if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
					mode = GameMode.valueOf(m.group(3).toUpperCase());
				}
				if(world ==null && mode == null)continue;
				header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(p, mode, world)));
			}
		}
		
		if(header.contains("%ping%"))
			header=header.replace("%ping%", Loader.getInstance.pingPlayer(p));
		if(header.contains("%world%"))
			header=header.replace("%world%", p.getWorld().getName());
		if(header.contains("%hp%"))
			header=header.replace("%hp%", StringUtils.fixedFormatDouble(((Damageable)p).getHealth()));
		if(header.contains("%health%"))
			header=header.replace("%health%", StringUtils.fixedFormatDouble(((Damageable)p).getHealth()));
		if(header.contains("%food%"))
			header=header.replace("%food%", p.getFoodLevel() + "");
		if(header.contains("%x%"))
			header=header.replace("%x%", StringUtils.fixedFormatDouble(p.getLocation().getX()));
		if(header.contains("%y%"))
			header=header.replace("%y%", StringUtils.fixedFormatDouble(p.getLocation().getY()));
		if(header.contains("%z%"))
			header=header.replace("%z%", StringUtils.fixedFormatDouble(p.getLocation().getZ()));
		if(header.contains("%vault_group%"))
			header=header.replace("%vault_group%", API.getGroup(p));
		if(header.contains("%vault_prefix%"))
			header=header.replace("%vault_prefix%", ""+TheAPI.colorize(Loader.getChatFormat(p, Item.PREFIX)));
		if(header.contains("%vault_suffix%"))
			header=header.replace("%vault_suffix%", ""+TheAPI.colorize(Loader.getChatFormat(p, Item.SUFFIX)));
		if(header.contains("%group%"))
			header=header.replace("%group%", Staff.getGroup(p));
		if(header.contains("%kills%"))
			header=header.replace("%kills%", "" + p.getStatistic(Statistic.PLAYER_KILLS));
		if(header.contains("%deaths%"))
			header=header.replace("%deaths%", "" + p.getStatistic(Statistic.DEATHS));
		if(header.contains("%deaths%"))
			header=header.replace("%deaths%", "" + p.getStatistic(Statistic.DEATHS));
		if(header.contains("%player%"))
			header=header.replace("%player%", p.getName());
		if(header.contains("%xp%"))
			header=header.replace("%xp%", p.getTotalExperience()+"");
		if(header.contains("%level%"))
			header=header.replace("%level%", p.getLevel()+"");
		if(header.contains("%exp%"))
			header=header.replace("%exp%", p.getTotalExperience()+"");
		if(header.contains("%playername%")) {
			String displayname = p.getName();
			if (p.getDisplayName() != null)
				displayname = p.getDisplayName();
			header=header.replace("%playername%", StringUtils.colorize(displayname));
		}
		if(header.contains("%customname%")) {
			String customname = p.getName();
		if (p.getCustomName() != null)
			customname = p.getCustomName();
		if(TheAPI.getUser(p).exists("DisplayName"))
			customname = TheAPI.getUser(p).getString("DisplayName");
			header=header.replace("%customname%", StringUtils.colorize(customname));
		}
		if(header.contains("%afk%"))
			header=header.replace("%afk%", Loader.getAFK(p));
		if(header.contains("%vanish%"))
			header=header.replace("%vanish%", Loader.getElse("Vanish", API.hasVanish(p)));
		if(header.contains("%fly%"))
			header=header.replace("%fly%", Loader.getElse("Fly", API.getSPlayer(p).hasFlyEnabled(true)||API.getSPlayer(p).hasTempFlyEnabled()));
		if(header.contains("%god%"))
			header=header.replace("%god%", Loader.getElse("God", API.getSPlayer(p).hasGodEnabled()));
		}else {
			if(header.contains("%online%"))
				header=header.replace("%online%", TheAPI.getOnlineCount()+"");
			if(header.contains("%playtime_")) {
				if(header.contains("%playtime_top_")) {
					Matcher m = playtimetop.matcher(header);
					while(m.find())
						header=header.replace(m.group(), PlayTimeUtils.getTop(StringUtils.getInt(m.group(1))));
				}
				String player = null;
				GameMode mode = null;
				World world = null;
				//%playtime_<player>_<world>_<GAMEMODE>%
				Matcher m = playtime_world_gm.matcher(header);
				while(m.find()) {
					if(TheAPI.existsUser(m.group(1))) {
						player = m.group(1);
					}else continue;
					if(Bukkit.getWorld(m.group(2))!=null) {
						world = Bukkit.getWorld(m.group(2));
					}else continue;
					if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
						mode = GameMode.valueOf(m.group(3).toUpperCase());
					}else continue;
					header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(player, mode, world)));
				}
				//%playtime_<player>_<world/GAMEMODE>%
				m = playtime_worldOrGm.matcher(header);
				while(m.find()) {
					if(TheAPI.existsUser(m.group(1))) {
						player = m.group(1);
					}else continue;
					if(Bukkit.getWorld(m.group(2))!=null) {
						world = Bukkit.getWorld(m.group(2));
					}
					if(GameMode.valueOf(m.group(3).toUpperCase())!=null) {
						mode = GameMode.valueOf(m.group(3).toUpperCase());
					}
					if(world ==null && mode == null)continue;
					header=header.replace(m.group(), StringUtils.timeToString(PlayTimeUtils.playtime(player, mode, world)));
				}
			}
		}
		if(header.contains("%balancetop_")) {
			Matcher m = balancetop.matcher(header);
			while(m.find()) {
				int pos = StringUtils.getInt(m.group(1));
				String world = Eco.getEconomyGroupByWorld(Bukkit.getWorlds().get(0).getName());
				if(p!=null && p instanceof Player)
					world = Eco.getEconomyGroupByWorld((p).getWorld().getName());
				if(EcoTop.h==null || EcoTop.h.isEmpty())
					EcoTop.reload(p);
				
				int i = 0; 
				Entry<Double, String> user = null;
				Iterator<Entry<Double, String>> it = EcoTop.h.get(world).entrySet().iterator();
				while(it.hasNext()) {
					Entry<Double, String> next = it.next();
					if(++i==pos) {
						user=next;
						break;
					}
				}
				if(user==null)return null;
				switch(m.group(2).toLowerCase()) {
				case "format_money":
				case "formatted_money":
					return API.setMoneyFormat(user.getKey(), true);
				case "money":
					return user.getKey()+"";
				case "name":
					return user.getValue();
				}
			}
		}
		if(header.contains("%players_max%"))
			header=header.replace("%players_max%", TheAPI.getMaxPlayers() + "");
		if(header.contains("%online_max%"))
			header=header.replace("%online_max%", TheAPI.getMaxPlayers() + "");
		if(header.contains("%max_online%"))
			header=header.replace("%max_online%", TheAPI.getMaxPlayers() + "");
		if(header.contains("%max_players%"))
			header=header.replace("%max_players%", TheAPI.getMaxPlayers() + "");
		if(header.contains("%time%"))
			header=header.replace("%time%", new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
		;if(header.contains("%date%"))
			header=header.replace("%date%", new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()));
		if(header.contains("%tps%"))
			header=header.replace("%tps%", TheAPI.getServerTPS() + "")
		;if(header.contains("%ram_free%"))
			header=header.replace("%ram_free%", MemoryAPI.getFreeMemory(false) + "")
		;if(header.contains("%ram_free_percentage%"))
			header=header.replace("%ram_free_percentage%", MemoryAPI.getFreeMemory(true) + "%")
		;if(header.contains("%ram_usage%"))
			header=header.replace("%ram_usage%", MemoryAPI.getUsedMemory(false) + "")
		;if(header.contains("%ram_usage_percentage%"))
			header=header.replace("%ram_usage_percentage%", MemoryAPI.getUsedMemory(true) + "%")
		;if(header.contains("%ram_max%"))
			header=header.replace("%ram_max%", MemoryAPI.getMaxMemory() + "").replace("%ram_max_percentage%", "100%");
		header = PlaceholderAPI.setPlaceholders(p, header);
		if(color)
			header=TheAPI.colorize(header);
		return header;
	}
	
	public static final AnimationManager aset = new AnimationManager();
	
	public static void update() {
		aset.update();
	}
	
	public static String getTabListName(Player p) {
		String p2 = getPrefix(p, false), s2 = getSuffix(p, false);
		return getNameFormat(p).replace("%tab_prefix%", (p2!=null?replace(p2, p, true):"")).replace("%tab_suffix%", (s2!=null?replace(s2, p, true):""));
	}
	
	public static void setNameTag(Player p) {
		String p1 = getPrefix(p, true), s1 = getSuffix(p, true);
		if(setting.tab_nametag)
			NameTagChanger.setNameTag(p, p1!=null?aset.replaceWithoutColors(p,replace(p1, p, false)):"", s1!=null?aset.replaceWithoutColors(p,replace(s1, p, false)):"");
	}
	
	public static void setTabName(Player p) {
		if(setting.tab_name)
			p.setPlayerListName(getTabListName(p));
	}
	
	public static final Object empty = NMSAPI.getPacketPlayOutPlayerListHeaderFooter(NMSAPI.getIChatBaseComponentText(""),NMSAPI.getIChatBaseComponentText(""));
	
	static int test;
	public static void removeTab() {
		for (Player p : TheAPI.getOnlinePlayers()) {
			NameTagChanger.remove(p);
			p.setPlayerListName(p.getName());
			Ref.sendPacket(p, empty);
		}
	}

	private static String get(String path, Player p) {
		if (setting.tab_header || setting.tab_footer)
			if (!Loader.tab.getStringList(path).isEmpty())
				return StringUtils.join(Loader.tab.getStringList(path), "\n");
		return "";
	}

	private static String getPath(Player p, String what) {
		if (what.equalsIgnoreCase("footer") && setting.tab_footer
				|| what.equalsIgnoreCase("header") && setting.tab_header) {
			if (Loader.tab.exists("PerPlayer." + p.getName() + "." + what))
				return get("PerPlayer." + p.getName() + "." + what, p);
			else if (Loader.tab.exists("PerWorld." + p.getWorld().getName() + "." + what))
				return get("PerWorld." + p.getWorld().getName() + "." + what, p);
			else if (Loader.tab.exists("PerGroup." + Staff.getGroup(p) + "." + what))
				return get("PerGroup." + Staff.getGroup(p) + "." + what, p);
			else {
				return get(what, p);
			}
		}
		return "";
	}
	
	public static void setFooterHeader(Player p) {
		TabListAPI.setHeaderFooter(p, aset.replaceWithoutColors(p,getPath(p, "Header")), aset.replaceWithoutColors(p,getPath(p, "Footer")));
	}

	public static List<String> replace(List<String> lore, Player a, boolean color) {
		lore.replaceAll(as -> replace(as,a,color));
		return lore;
	}
}

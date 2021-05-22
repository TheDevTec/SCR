package me.devtec.servercontrolreloaded.scr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.commands.other.Trash;
import me.devtec.servercontrolreloaded.commands.other.mirror.MirrorEvents;
import me.devtec.servercontrolreloaded.events.AFkPlayerEvents;
import me.devtec.servercontrolreloaded.events.ChatFormat;
import me.devtec.servercontrolreloaded.events.CreatePortal;
import me.devtec.servercontrolreloaded.events.DeathEvent;
import me.devtec.servercontrolreloaded.events.DisableItems;
import me.devtec.servercontrolreloaded.events.EntitySpawn;
import me.devtec.servercontrolreloaded.events.FarmingSystem;
import me.devtec.servercontrolreloaded.events.ItemUse;
import me.devtec.servercontrolreloaded.events.LoginEvent;
import me.devtec.servercontrolreloaded.events.OnPlayerJoin;
import me.devtec.servercontrolreloaded.events.RewardsListenerChat;
import me.devtec.servercontrolreloaded.events.SecurityListenerCooldowns;
import me.devtec.servercontrolreloaded.events.SecurityListenerV4;
import me.devtec.servercontrolreloaded.events.Signs;
import me.devtec.servercontrolreloaded.events.WorldChange;
import me.devtec.servercontrolreloaded.utils.BungeeListener;
import me.devtec.servercontrolreloaded.utils.Configs;
import me.devtec.servercontrolreloaded.utils.Converter;
import me.devtec.servercontrolreloaded.utils.DisplayManager;
import me.devtec.servercontrolreloaded.utils.Eco;
import me.devtec.servercontrolreloaded.utils.Kit;
import me.devtec.servercontrolreloaded.utils.MultiWorldsGUI;
import me.devtec.servercontrolreloaded.utils.MultiWorldsUtils;
import me.devtec.servercontrolreloaded.utils.Portal;
import me.devtec.servercontrolreloaded.utils.Rule;
import me.devtec.servercontrolreloaded.utils.SPlayer;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.Tasks;
import me.devtec.servercontrolreloaded.utils.XMaterial;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.servercontrolreloaded.utils.metrics.Metrics;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.placeholderapi.PlaceholderRegister;
import me.devtec.theapi.placeholderapi.ThePlaceholder;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.ChatMessage;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.Data;
import me.devtec.theapi.utils.datakeeper.DataType;
import me.devtec.theapi.utils.json.Reader;
import me.devtec.theapi.utils.json.Writer;
import me.devtec.theapi.utils.listener.Listener;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class Loader extends JavaPlugin implements Listener {
	public static Config config, plac, sb, tab, mw, kit, trans, events, cmds, anim, ac, bb,guicreator,portals,customCmds;
	public static List<Rule> rules = new ArrayList<>();
	private int task;
	private long time, rkick;
	public static String[] colorsText, rulesText;
	public static Object econ;
	public static Loader getInstance;
	public static Config english;
	private static UpdateChecker updater;
	public static Chat vault = null;
	public static Permission perms = null;
	private static int aad = 0;
	
	public static class Placeholder {
		private final HashMap<String, String> set = new HashMap<>();
		public Placeholder add(String placeholder, Object replace) {
			set.put(placeholder, replace+"");
			return this;
		}
		
		public static Placeholder c() {
			return new Placeholder();
		}

		public Placeholder replace(String placeholder, Object replace) {
			return add(placeholder, replace);
		}
	}
	
	public static void Help(CommandSender s, String cmd, String section) {
		Object o = cmds.get(section+"."+cmd+".Help");
		if(o==null)return;
		if(o instanceof Collection) {
			for(Object d : (Collection<?>)o)
			TheAPI.msg(d+"", s);
		}else
			if(!(o+"").isEmpty())
			TheAPI.msg(o+"", s);
	}
	public static void advancedHelp(CommandSender s, String cmd, String section, String subCommand) {
		Object o = cmds.get(section+"."+cmd+".AdvancedHelp."+subCommand);
		if(o==null)return;
		if(o instanceof Collection) {
			for(Object d : (Collection<?>)o)
			TheAPI.msg(d+"", s);
		}else
			if(!(o+"").isEmpty())
			TheAPI.msg(o+"", s);
	}
	public static void advancedHelp(CommandSender s, String cmd, String section,String underSection ,String subCommand) {
		Object o = cmds.get(section+"."+cmd+".AdvancedHelp."+underSection+"."+subCommand);
		if(o==null)return;
		if(o instanceof Collection) {
			for(Object d : (Collection<?>)o)
			TheAPI.msg(d+"", s);
		}else
			if(!(o+"").isEmpty())
			TheAPI.msg(o+"", s);
	}
	
	public static String placeholder(CommandSender sender, String string, Placeholder placeholders) {
		if(setting.prefix!=null)
			string=string.replace("%prefix%", setting.prefix);
		if(placeholders!=null)
		for(Entry<String, String> placeholder : placeholders.set.entrySet())
			string=string.replace(placeholder.getKey()+"", placeholder.getValue()+"");
		if(sender!=null) {
		if(sender instanceof Player)
			string=TabList.replace(string, (Player)sender, true);
		else
			string=TabList.replace(string.replace("%player%", sender.getName())
					.replace("%playername%", sender.getName())
					.replace("%customname%", sender.getName()),null,true);
		string=string.replace("%op%", ""+sender.isOp());
		}
		return PlaceholderAPI.setPlaceholders(sender instanceof Player ? (Player)sender : null, string);
	}
	
	public static Object getTranslation(String path) {
		if(trans==null || !trans.exists(path)) {
			if(english.exists(path)) {
				if(english.get(path) instanceof Collection) {
					return english.getStringList(path);
				}else
					if(!english.getString(path).trim().isEmpty())
						return english.getString(path).replace("%prefix%",english.getString("Prefix"));
			}
		}else {
		if(trans.get(path) instanceof Collection) {
			return trans.getStringList(path);
		}else
			if(!trans.getString(path).trim().isEmpty())
				return trans.getString(path).replace("%prefix%", trans.getString("Prefix"));
		}
		return null;
	}
	
	public static String getTranslationAsString(String path) {
		if(trans==null || !trans.exists(path)) {
			if(english.exists(path)) {
				return english.getString(path);
			}
		}else {
			return trans.getString(path);
		}
		return null;
	}
	
	public static Object getTranslationAsObject(String path) {
		if(trans==null || !trans.exists(path)) {
			if(english.exists(path)) {
				return english.get(path);
			}
		}else {
			return trans.get(path);
		}
		return null;
	}
	
	public static boolean existsTranslation(String path) {
		return trans==null || !trans.exists(path)?english.exists(path):trans.exists(path);
	}
	
	public static void sendMessages(CommandSender to, String path) {
		sendMessages(to, path, null);
	}
	
	public static void sendMessages(CommandSender to, CommandSender replace, String path) {
		sendMessages(to, replace, path, null);
	}
	
	public static void sendBroadcasts(CommandSender whoIsSelected, String path) {
		sendBroadcasts(whoIsSelected, path, null);
	}
	
	@SuppressWarnings("unchecked")
	public static void sendMessages(CommandSender to, String path, Placeholder placeholders) {
		Object o = getTranslation(path);
		if(o==null) {
			Bukkit.getLogger().severe("[BUG] Missing configuration path [Translations]!");
			Bukkit.getLogger().severe("[BUG] Report this to the DevTec discord:");
			Bukkit.getLogger().severe("[BUG] Missing path: "+path);
			return;
		}
		if(o instanceof Collection || o instanceof Map) { //json?
			String sf = getTranslationAsString(path);
			if(sf!=null)
				if(sf.startsWith("[") && sf.endsWith("]")||sf.startsWith("{") && sf.endsWith("}")) {
					Object old = o;
					o=getTranslationAsObject(path);
					Object json;
					if(o instanceof Collection) {
						json = colorizeList((Collection<?>)o,(d)->placeholder(to,d,placeholders));
					}else
						json = colorizeMap((Map<String, Object>)o,(d)->placeholder(to,d,placeholders));
					if(json!=null) {
						Object formatt = json;
						if (formatt instanceof Map || formatt instanceof Collection) {
							List<Map<String,Object>> oo = new ArrayList<>();
							if(formatt instanceof Map) {
								oo.add((Map<String, Object>) formatt);
								formatt=oo;
							}else {
								for(Object w : ((Collection<Object>)formatt)) {
									if(w instanceof String)w=Reader.read((String)w);
									if(w instanceof Map) {
										oo.add((Map<String, Object>) w);
									}else {
										Map<String, Object> g = new HashMap<>();
										g.put("text", w+"");
										oo.add(g);
									}
								}
								formatt=oo;
							}
							if(to instanceof Player) {
								Ref.sendPacket((Player)to,NMSAPI.getPacketPlayOutChat(NMSAPI.ChatType.SYSTEM, NMSAPI.getIChatBaseComponentJson(Writer.write((List<Map<String,Object>>)formatt))));
							}else {
								to.sendMessage(convertToLegacy(ChatMessage.fixListMap((List<Map<String,Object>>)formatt)));
							}
							return;
						}
					} // fallback to default
					o=old;
				}
		}
		if(o instanceof Collection) {
			for(Object d : (Collection<?>)o)
				TheAPI.msg(placeholder(to, d+"", placeholders), to);
		}else
			if(!(o+"").isEmpty())
		TheAPI.msg(placeholder(to, o+"", placeholders), to);
	}
	
	private static String convertToLegacy(List<Map<String, Object>> list) {
		StringBuilder b = new StringBuilder();
		for(Map<String, Object> text : list)
			b.append(StringUtils.colorize(getColor(""+text.getOrDefault("color",""))+getStats(text)+text.get("text")));
		return b.toString();
	}
	
	private static String getStats(Map<String, Object> text) {
		String s = "";
		if(text.containsKey("bold") && (boolean)text.get("bold"))s+="&l";
		if(text.containsKey("italic") && (boolean)text.get("italic"))s+="&o";
		if(text.containsKey("strikethrough") && (boolean)text.get("strikethrough"))s+="&m";
		if(text.containsKey("underlined") && (boolean)text.get("underlined"))s+="&n";
		if(text.containsKey("obfuscated") && (boolean)text.get("obfuscated"))s+="&k";
		return s;
	}
	
	static String getColor(String color) {
		if(color.trim().isEmpty())return "";
		if(color.startsWith("#"))return color;
		try {
		return ChatColor.valueOf(color.toUpperCase())+"";
		}catch(Exception | NoSuchFieldError err) {
			return "";
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void sendMessages(CommandSender to, CommandSender replace, String path, Placeholder placeholders) {
		Object o = getTranslation(path);
		if(o==null) {
			Bukkit.getLogger().severe("[BUG] Missing configuration path [Translations]!");
			Bukkit.getLogger().severe("[BUG] Report this to the DevTec discord to channel #scr-bugs:");
			Bukkit.getLogger().severe("[BUG] Missing path: "+path);
			return;
		}
		if(o instanceof Collection || o instanceof Map) { //json?
			String sf = getTranslationAsString(path);
			if(sf!=null)
				if(sf.startsWith("[") && sf.endsWith("]")||sf.startsWith("{") && sf.endsWith("}")) {
					Object old = o;
					o=getTranslationAsObject(path);
					Object json;
					if(o instanceof Collection) {
						json = colorizeList((Collection<?>)o,(d)->placeholder(replace,d,placeholders));
					}else
						json = colorizeMap((Map<String, Object>)o,(d)->placeholder(replace,d,placeholders));
					if(json!=null) {
						Object formatt = json;
						if (formatt instanceof Map || formatt instanceof Collection) {
							List<Map<String,Object>> oo = new ArrayList<>();
							if(formatt instanceof Map) {
								oo.add((Map<String, Object>) formatt);
								formatt=oo;
							}else {
								for(Object w : ((Collection<Object>)formatt)) {
									if(w instanceof String)w=Reader.read((String)w);
									if(w instanceof Map) {
										oo.add((Map<String, Object>) w);
									}else {
										Map<String, Object> g = new HashMap<>();
										g.put("text", w+"");
										oo.add(g);
									}
								}
								formatt=oo;
							}
							if(to instanceof Player) {
								Ref.sendPacket((Player)to,NMSAPI.getPacketPlayOutChat(NMSAPI.ChatType.SYSTEM, NMSAPI.getIChatBaseComponentJson(Writer.write((List<Map<String,Object>>)formatt))));
							}else {
								to.sendMessage(convertToLegacy(ChatMessage.fixListMap((List<Map<String,Object>>)formatt)));
							}
							return;
						}
					} // fallback to default
					o=old;
				}
		}
		if(o instanceof Collection) {
			for(Object d : (Collection<?>)o)
				TheAPI.msg(placeholder(replace, d+"", placeholders), to);
		}else
			if(!(o+"").isEmpty())
		TheAPI.msg(placeholder(replace, o+"", placeholders), to);
	}
	
	@SuppressWarnings("unchecked")
	public static void sendBroadcasts(CommandSender whoIsSelected, String path, Placeholder placeholders) {
		Object o = getTranslation(path);
		if(o==null)return;
		if(o instanceof Collection || o instanceof Map) { //json?
			String sf = getTranslationAsString(path);
			if(sf!=null)
				if(sf.startsWith("[") && sf.endsWith("]")||sf.startsWith("{") && sf.endsWith("}")) {
					Object old = o;
					o=getTranslationAsObject(path);
					Object json;
					if(o instanceof Collection) {
						json = colorizeList((Collection<?>)o,(d)->placeholder(whoIsSelected,d,placeholders));
					}else
						json = colorizeMap((Map<String, Object>)o,(d)->placeholder(whoIsSelected,d,placeholders));
					if(json!=null) {
						Object formatt = json;
						if (formatt instanceof Map || formatt instanceof Collection) {
							List<Map<String,Object>> oo = new ArrayList<>();
							if(formatt instanceof Map) {
								oo.add((Map<String, Object>) formatt);
								formatt=oo;
							}else {
								for(Object w : ((Collection<Object>)formatt)) {
									if(w instanceof String)w=Reader.read((String)w);
									if(w instanceof Map) {
										oo.add((Map<String, Object>) w);
									}else {
										Map<String, Object> g = new HashMap<>();
										g.put("text", w+"");
										oo.add(g);
									}
								}
								formatt=oo;
							}
							Ref.sendPacket(TheAPI.getOnlinePlayers(),NMSAPI.getPacketPlayOutChat(NMSAPI.ChatType.SYSTEM, NMSAPI.getIChatBaseComponentJson(Writer.write((List<Map<String,Object>>)formatt))));
							TheAPI.getConsole().sendMessage(convertToLegacy(ChatMessage.fixListMap((List<Map<String,Object>>)formatt)));
							return;
						}
					} // fallback to default
					o=old;
				}
		}
		if(o instanceof List) {
			for(String s : (List<String>)o)
				TheAPI.broadcastMessage(placeholder(whoIsSelected, s, placeholders));
		}else
		TheAPI.bcMsg(placeholder(whoIsSelected, o.toString(), placeholders));
	}

	@SuppressWarnings("unchecked")
	public static void sendBroadcasts(CommandSender whoIsSelected, String path, Placeholder placeholders, String perms) {
		Object o = getTranslation(path);
		if(o==null)return;
		if(o instanceof Collection || o instanceof Map) { //json?
			String sf = getTranslationAsString(path);
			if(sf!=null)
				if(sf.startsWith("[") && sf.endsWith("]")||sf.startsWith("{") && sf.endsWith("}")) {
					Object old = o;
					o=getTranslationAsObject(path);
					Object json;
					if(o instanceof Collection) {
						json = colorizeList((Collection<?>)o,(d)->placeholder(whoIsSelected,d,placeholders));
					}else
						json = colorizeMap((Map<String, Object>)o,(d)->placeholder(whoIsSelected,d,placeholders));
					if(json!=null) {
						Object formatt = json;
						if (formatt instanceof Map || formatt instanceof Collection) {
							List<Map<String,Object>> oo = new ArrayList<>();
							if(formatt instanceof Map) {
								oo.add((Map<String, Object>) formatt);
								formatt=oo;
							}else {
								for(Object w : ((Collection<Object>)formatt)) {
									if(w instanceof String)w=Reader.read((String)w);
									if(w instanceof Map) {
										oo.add((Map<String, Object>) w);
									}else {
										Map<String, Object> g = new HashMap<>();
										g.put("text", w+"");
										oo.add(g);
									}
								}
								formatt=oo;
							}
							List<Player> p = TheAPI.getOnlinePlayers();
							Iterator<Player> f = p.iterator();
							while(f.hasNext())
								if(!f.next().hasPermission(perms))f.remove();
							Ref.sendPacket(p,NMSAPI.getPacketPlayOutChat(NMSAPI.ChatType.SYSTEM, NMSAPI.getIChatBaseComponentJson(Writer.write((List<Map<String,Object>>)formatt))));
							if(TheAPI.getConsole().hasPermission(perms))
								TheAPI.getConsole().sendMessage(convertToLegacy(ChatMessage.fixListMap((List<Map<String,Object>>)formatt)));
							return;
						}
					} // fallback to default
					o=old;
				}
		}
		
		if(o instanceof List) {
			for(String s : (List<String>)o)
				TheAPI.broadcast(placeholder(whoIsSelected, s, placeholders), perms);
		}else
		TheAPI.bc(placeholder(whoIsSelected, o.toString(), placeholders), perms);
	}
	static Pattern colorPattern = Pattern.compile("[XxA-Fa-fUu0-9]");

	public static interface Replacer {
		public String replace(String s);
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<?> colorizeList(Collection<?> json, Replacer c) {
		ArrayList<Object> colorized = new ArrayList<>(json.size());
		for (Object e : json) {
			if (e instanceof Collection) {
				colorized.add(colorizeList((Collection<?>) e,c));
				continue;
			}
			if (e instanceof Map) {
				colorized.add(colorizeMap((Map<String, Object>) e,c));
				continue;
			}
			if (e instanceof String) {
				colorized.add(r((String)e,c));
				continue;
			}
			colorized.add(e);
		}
		return colorized;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> colorizeMap(Map<String, Object> jj, Replacer c) {
		HashMap<String, Object> json = new HashMap<>(jj.size());
		for (Entry<String, Object> e : jj.entrySet()) {
			if (e.getValue() instanceof Collection) {
				json.put(e.getKey(), colorizeList((Collection<?>) e.getValue(), c));
				continue;
			}
			if (e.getValue() instanceof Map) {
				json.put(e.getKey(), colorizeMap((Map<String, Object>) e.getValue(),c ));
				continue;
			}
			if (e.getValue() instanceof String && !e.getKey().equals("color")) {
				json.put(e.getKey(), r((String) e.getValue(), c));
				continue;
			}
			json.put(e.getKey(), e.getValue());
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	public static Object r(Object s, Replacer c) {
		if((s+"").trim().isEmpty())return s;
		try {
			if(s instanceof Map && s!=null) {
				return colorizeMap((Map<String, Object>) s, c);
			} //else continue in code below
			if(s instanceof Collection && s!=null) {
				return colorizeList((Collection<Object>) s, c);
			} //else continue in code below
		}catch(Exception err) {}
		String orig = (String)s;
		orig=c.replace((String)s);
		if(orig!=null)s=orig;
		return s;
	}
	
	public static enum Item {
		PREFIX,
		SUFFIX
	}
	public static String getChatFormat(Player p, Item type) {
		switch(type) {
		case PREFIX:
			if(PluginManagerAPI.isEnabledPlugin("LuckPerms"))
				return LuckPermsProvider.get().getUserManager().getUser(p.getUniqueId()).getCachedData().getMetaData().getPrefix();
			if (vault != null) {
				String prefix = vault.getPlayerPrefix(p);
				if(prefix==null)
					prefix=vault.getGroupPrefix(p.getWorld(), API.getGroup(p));
				return prefix==null?"":prefix;
			}
			return "";
		case SUFFIX:
			if(PluginManagerAPI.isEnabledPlugin("LuckPerms"))
				return LuckPermsProvider.get().getUserManager().getUser(p.getUniqueId()).getCachedData().getMetaData().getSuffix();
			if (vault != null) {
				String prefix = vault.getPlayerSuffix(p);
				if(prefix==null)
					prefix=vault.getGroupSuffix(p.getWorld(), API.getGroup(p));
				return prefix==null?"":prefix;
			}
			return "";
		}
		return null;
	}
	
	public static String getAFK(Player p) {
		return Loader.getElse("AFK", API.getSPlayer(p).isAFK());
	}

	private String getColoredPing(Player p) {
		int s = TheAPI.getPlayerPing(p);
		if (s >= 500)
			return TheAPI.colorize("&c" + s);
		if (s >= 200 && s < 500)
			return TheAPI.colorize("&e" + s);
		if (s >= 0 && s < 200)
			return TheAPI.colorize("&a" + s);
		return TheAPI.colorize("&4" + s);
	}

	public String pingPlayer(Player who) {
		if (tab.getBoolean("Colored-Ping"))
			return getColoredPing(who);
		return String.valueOf(TheAPI.getPlayerPing(who));
	}
	
	boolean disable = false;
	
	@Override
	public void onLoad() {
		if(VersionChecker.getVersion(PluginManagerAPI.getVersion("TheAPI"), "5.9.9")==VersionChecker.Version.NEW) {
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &4SECURITY: &cYou are running on outdated version of plugin TheAPI", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &4SECURITY: &cPlease update plugin TheAPI to latest version.", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "        &6https://www.spigotmc.org/resources/72679/", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			disable=true;
			return;
		}
		XMaterial.matchXMaterial("STONE"); //intialize ids on load
        boolean save = false;
        Data c = new Data("plugins/bStats/config.yml");
        if(c.setIfAbsent("enabled", true))save=true;
        if(c.setIfAbsent("serverUuid", UUID.randomUUID().toString()))save=true;
        if(c.setIfAbsent("logFailedRequests", false))save=true;
        if(c.setIfAbsent("logSentData", false))save=true;
        if(c.setIfAbsent("logResponseStatusText", false))save=true;
        c.setHeader(Arrays.asList(
                "# bStats collects some data for plugin authors like how many servers are using their plugins.",
                        "# To honor their work, you should not disable it.",
                        "# This has nearly no effect on the server performance!",
                        "# Check out https://bStats.org/ to learn more :)"));
        if(save)
        c.save(DataType.YAML);
		getInstance = this;
		Configs.load(false);
		if (PluginManagerAPI.getPlugin("Vault")!=null) {
			setupEco();
		} else {
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &eINFO: &7Missing Vault plugin for Economy.", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
		}
	}
	
	private static long loading;
	public static boolean hasBungee;

	private Object reg;

	@Override
	public void onEnable() {
		if(disable) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		//LOAD PLACEHOLDERS
		if(PluginManagerAPI.isEnabledPlugin("PlaceholderAPI")) {
			reg=new PlaceholderRegister("scr", "DevTec", getDescription().getVersion()) {
				
				@Override
				public String onRequest(Player player, String params) {
					params='%'+params+'%';
					String f = TabList.replace(params, player, true);
					return params.equals(f)?null:f;
				}
			};
			((PlaceholderRegister)reg).register();
		}else {
			reg=new ThePlaceholder("ServerControlReloaded") {
				
				@Override
				public String onRequest(Player player, String placeholder) {
					if(!placeholder.toLowerCase().startsWith("scr_"))return null;
					placeholder=placeholder.substring(4);
					placeholder='%'+placeholder+'%';
					return TabList.replace(placeholder, player, true);
				}
			};
			((ThePlaceholder)reg).register();
		}
		
		MultiWorldsUtils.loadWorlds();
		if(Ref.getClass("net.md_5.bungee.api.ChatColor")!=null) {
			if(new Data("spigot.yml").getBoolean("settings.bungeecord")) {
				hasBungee=true;
				Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
				Bukkit.getMessenger().registerOutgoingPluginChannel(this, "scr:community");
				Bukkit.getMessenger().registerIncomingPluginChannel(this, "scr:community", new BungeeListener());
			}
		}
		EventsRegister();
		updater = new UpdateChecker();
		switch(updater.checkForUpdates()) {
		case UKNOWN:
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &eUpdate checker: &7Unable to connect to spigot, check internet connection.", TheAPI.getConsole());
			updater=null; //close updater
			break;
		case NEW:
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &eUpdate checker: &7Found new version of SCR.", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "        https://www.spigotmc.org/resources/71147/", TheAPI.getConsole());
			break;
		case OLD:
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &eUpdate checker: &7You are using the BETA version of SCR, report bugs to our Discord.", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "        https://discord.io/spigotdevtec", TheAPI.getConsole());
			break;
		default:
			break;
		}
		new Metrics(this, 10560);
		if(updater!=null)
		new Tasker() {
			public void run() {
				switch(updater.checkForUpdates()) {
					case UKNOWN:
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &eUpdate checker: &7Unable to connect to spigot, check internet connection.", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						updater=null; //close updater
						cancel(); //destroy task
						break;
					case NEW:
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &eUpdate checker: &7Found new version of SCR.", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + "        https://www.spigotmc.org/resources/71147/", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						break;
					case OLD:
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &eUpdate checker: &7You are using the BETA version of SCR, report bugs to our Discord.", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + "        https://discord.io/spigotdevtec", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						break;
				default:
					break;
				}
			}
		}.runRepeating(144000, 144000);
		reload();
		TheAPI.msg(setting.prefix + " &eINFO: &7Newest versions of &eTheAPI &7can be found on Spigot or Discord:", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + "        https://www.spigotmc.org/resources/72679/", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + "        https://discord.io/spigotdevtec", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
	}
	
	public static class VersionChecker {
		public static enum Version {
			OLD, NEW, SAME, UKNOWN;
		}
		
		public static Version getVersion(String currentVersion, String version) {
			if(currentVersion==null || version==null || currentVersion.replaceAll("[^0-9.]+", "").trim().isEmpty()||version.replaceAll("[^0-9.]+", "").trim().isEmpty())return Version.UKNOWN;
			Version is = Version.UKNOWN;
			int d = 0;
	    	String[] s = currentVersion.replaceAll("[^0-9.]+", "").split("\\.");
	    	for(String f : version.replaceAll("[^0-9.]+", "").split("\\.")) {
	    		int id = StringUtils.getInt(f), bi = StringUtils.getInt(s[d++]);
	    		if(id == bi) {
	    			is=Version.SAME;
	    			continue;
	    		}
	    		is=id > bi?Version.NEW:Version.OLD;
	    		break;
	    	}
	    	return is;
		}
	}

	public class UpdateChecker {
	    private URL checkURL;
	    
	    public UpdateChecker reconnect() {
	    	try {
				checkURL=new URL("https://api.spigotmc.org/legacy/update.php?resource=71147");
			} catch (Exception e) {}
	        return this;
	    }

	    //0 == SAME VERSION
	    //1 == NEW VERSION
	    //2 == BETA VERSION
	    public VersionChecker.Version checkForUpdates() {
	    	if(checkURL==null)
	    		reconnect();
	    	String[] readerr = null;
	    	try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(checkURL.openConnection().getInputStream()));
				ArrayList<String> s = new ArrayList<>();
				String read;
				while((read=reader.readLine()) != null)
					s.add(read);
				readerr=s.toArray(new String[s.size()]);
			} catch (Exception e) {
			}
	    	if(readerr==null)return VersionChecker.Version.UKNOWN;
	        return VersionChecker.getVersion(getDescription().getVersion(), readerr[0]);
	    }
	}
	
	@Override
	public void onDisable() {
		unhook();
		CommandsManager.unload();
		org.bukkit.event.HandlerList.unregisterAll(this);
		for (Player p : TheAPI.getOnlinePlayers()) {
			p.setDisplayName(null);
			p.setCustomName(null);
		}
		TabList.removeTab();
		Tasks.unload();
		Portal.unload();
		stop();
		DisplayManager.unload();
		for (String w : mw.getStringList("Worlds"))
			if (Bukkit.getWorld(w) != null) {
				Bukkit.getLogger().info("Saving world '" + w + "'");
				Bukkit.getWorld(w).save();
			}
		if(reg instanceof ThePlaceholder)((ThePlaceholder)reg).unregister();
	}

	private static boolean setupVault() {
		RegisteredServiceProvider<Chat> economyProvider = Bukkit.getServicesManager()
				.getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (economyProvider != null) {
			vault = economyProvider.getProvider();
		}

		return (vault != null);
	}


	public static boolean setupCustomEco() {
		econ = new Eco();
		Bukkit.getServicesManager().register(net.milkbowl.vault.economy.Economy.class, (net.milkbowl.vault.economy.Economy)econ, Loader.getInstance, ServicePriority.Normal);
		Loader.EconomyLog("Vault hooked into plugin Economy");
		return true;
	}

	public static void unhook() {
		if (econ != null) {
			Bukkit.getServicesManager().unregister(net.milkbowl.vault.economy.Economy.class, econ);
			Loader.EconomyLog("Vault unhooked from plugin Economy");
		}
	}

	public static void EconomyLog(String s) {
		if (config.getBoolean("Options.Economy.Log"))
			getInstance.getLogger().info("[EconomyLog] " + s);
	}

	private static boolean setupEco() {
		try {
			RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = Bukkit.getServicesManager()
					.getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (economyProvider != null) {
				if (config.getBoolean("Options.Economy.CanUseOtherEconomy")) {
					EconomyLog("Found economy '" + economyProvider.getProvider().getName()
							+ "', using setting and skipping plugin economy.");
					return true;
				} else {
					EconomyLog("Found economy '" + economyProvider.getProvider().getName()
							+ "', skipping plugin economy.");
					return true;
				}
			}
			if (econ == null) {
				if (!config.getBoolean("Options.Economy.DisablePluginEconomy")) {
					EconomyLog("Plugin not found any economy, loading plugin economy.");
					return setupCustomEco();
				}
			}
		} catch (Exception e) {
			EconomyLog("Error when hooking economy.");
		}
		return false;
	}

	private static boolean setupPermisions() {
		RegisteredServiceProvider<Permission> economyProvider = Bukkit.getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (economyProvider != null) {
			perms = economyProvider.getProvider();
		}
		return (perms != null);
	}
	
	public static void reload() {
		loading = System.currentTimeMillis();
		TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
		if(aad==1) {
			DisplayManager.unload();
			getInstance.stop();
			Configs.load(true);
			DisplayManager.load();
			getInstance.starts();
		}else {
			DisplayManager.load();
			getInstance.starts();
			if (PluginManagerAPI.getPlugin("Vault") != null) {
				setupVault();
				setupPermisions();
				if (vault == null) {
					TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
					TheAPI.msg(setting.prefix + " &eINFO: &7Missing Permissions plugin for Groups (TabList and ChatFormat).", TheAPI.getConsole());
					TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
				}
			} else {
				TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
				TheAPI.msg(setting.prefix + " &eINFO: &7Missing Vault plugin for Economy.", TheAPI.getConsole());
				TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			}
		}
		rules.clear();
		Converter.convert();
		ItemGUI clear=new ItemGUI(ItemCreatorAPI.create(XMaterial.LAVA_BUCKET.getMaterial(), 1, Loader.getTranslation("Trash.Clear")+"")) {
				public void onClick(Player s, HolderGUI g, me.devtec.theapi.guiapi.GUI.ClickType c) {
					for (int i = 0; i < 45; ++i)
					g.remove(i);
				}
			};
			GUI sa = new GUI(""+Loader.getTranslation("Trash.Name"), 54);
			sa.setInsertable(true);
			MultiWorldsGUI.smallInv(sa);
			sa.setItem(49, clear);
			Trash.s=sa;
		for(String s : config.getKeys("Rules")) {
			int flags = 0;
			for(String d : config.getStringList("Rules."+s+".RegexFlags")) {
				if(flags==0) {
					try {
					flags=(int)Ref.getNulled(java.util.regex.Pattern.class,d);
					}catch(Exception err) {}
				}
				try {
				flags|=(int)Ref.getNulled(java.util.regex.Pattern.class,d);
				}catch(Exception err) {}
			}
			rules.add(new Rule(s, config.getString("Rules."+s+".Text"), config.getString("Rules."+s+".Type"), config.getBoolean("Rules."+s+".Replacement.Use"), config.getString("Rules."+s+".Replacement.Text"),flags));
		}
		for (Player p : TheAPI.getOnlinePlayers()) {
			SPlayer s = API.getSPlayer(p);
			if (s.hasTempFlyEnabled())
				s.enableTempFly();
			else if (s.hasFlyEnabled())
				s.enableFly();
			if (EconomyAPI.getEconomy() != null && !EconomyAPI.hasAccount(p))
				EconomyAPI.createAccount(p);
		}
		Portal.reload();
		MultiWorldsUtils.gamemodeWorldCheck();
		TheAPI.msg(setting.prefix + " &7"+(aad == 0 ? "L" : "Rel")+"oading kits:", TheAPI.getConsole());
		for (String s : Loader.kit.getKeys("Kits")) {
			TheAPI.msg(setting.prefix + "   &e"+s+"&7:", TheAPI.getConsole());
			Kit kit = Kit.load(s);
			TheAPI.msg(setting.prefix + "     &7Cooldown: &e" + StringUtils.setTimeToString(kit.getDelay()), TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "     &7Cost: &e$" + API.setMoneyFormat(kit.getCost(), false), TheAPI.getConsole());
		}
		TabList.reload();
		Tasks.reload();
		CommandsManager.load();
		TheAPI.msg(setting.prefix + " &7"+(aad == 0 ? "L" : "Rel")+"oading of SCR took "+(System.currentTimeMillis()-loading)+"ms", TheAPI.getConsole());
		aad=1;
		TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
	}

	public Map<String, Location> moving = new HashMap<>();
	public void starts() {
		time = StringUtils.timeFromString(Loader.config.getString("Options.AFK.TimeToAFK"));
		rkick = StringUtils.timeFromString(Loader.config.getString("Options.AFK.TimeToKick"));
		task=new Tasker() {
			public void run() {
				for(Player p : TheAPI.getOnlinePlayers()) {
					Location before = moving.put(p.getName(), p.getLocation());
					if(before!=null)
					if(Math.abs(before.getBlockX()-p.getLocation().getBlockX()) > 0 ||
							Math.abs(before.getBlockY()-p.getLocation().getBlockY()) > 0 ||
							Math.abs(before.getBlockZ()-p.getLocation().getBlockZ()) > 0) {
						save(p);
					}
					SPlayer s = API.getSPlayer(p);
					if (setting.afk_auto) {;
						if (getTime(s) <= 0) {
							if (!s.bc) {
								s.bc = true;
								for(Player canSee : API.getPlayersThatCanSee(p))
									Loader.sendMessages(canSee, p, "AFK.Start");
								NMSAPI.postToMainThread(() -> {
									for(String ds : Loader.config.getStringList("Options.AFK.Action.onStartAFK"))
										TheAPI.sudoConsole(TabList.replace(ds,p,true));
								});
							}
							if (setting.afk_kick) {
								if (s.kick >= rkick) {
									if (!p.hasPermission(getPerm("AFK", "Other", "Bypass"))) {
										NMSAPI.postToMainThread(() -> {
											for(String ds : Loader.config.getStringList("Options.AFK.Action.onKickAFK"))
												TheAPI.sudoConsole(TabList.replace(ds,p,true));
										});
									}
								} else
									++s.kick;
							}
						}else
							++s.afk;
					}
				}
			}
		}.runRepeating(0, 20);
	}

	public void setAFK(SPlayer s) {
		Player player = s.getPlayer();
		moving.put(s.getName(), player.getLocation());
		s.afk=0;
		s.kick = 0;
		s.bc = true;
		s.manual = true;
		for(Player canSee : API.getPlayersThatCanSee(s.getPlayer())) {
			Loader.sendMessages(canSee, player, "AFK.Start");
		}
	}
	public void setAFK(SPlayer s, String reason) {
		Player player = s.getPlayer();
		save(player);
		s.bc = true;
		s.manual = true;
		for(Player canSee : API.getPlayersThatCanSee(player))
			Loader.sendMessages(canSee, player, "AFK.Start_WithReason", Placeholder.c().add("%reason%", reason));
	}
	
	public long getTime(SPlayer s) {
		return time - s.afk;
	}

	public void save(Player d) {
		moving.put(d.getName(), d.getLocation());
		SPlayer s = API.getSPlayer(d);
		if(isAFK(s) || isManualAfk(s)) {
			for(Player canSee : API.getPlayersThatCanSee(d))
				Loader.sendMessages(canSee, d, "AFK.End");
			NMSAPI.postToMainThread(() -> {
				for(String ds : Loader.config.getStringList("Options.AFK.Action.onStopAFK"))
					TheAPI.sudoConsole(TabList.replace(ds,d,true));
			});
		}
		s.afk=0;
		s.kick = 0;
		s.manual = false;
		s.bc = false;
	}

	public boolean isManualAfk(SPlayer s) {
		return s.manual;
	}

	public boolean isAFK(SPlayer s) {
		return getTime(s) <= 0;
	}

	private void stop() {
		Scheduler.cancelTask(task);
	}
	
	private void EventC(org.bukkit.event.Listener l) {
		Bukkit.getPluginManager().registerEvents(l, this);
	}

	private void EventsRegister() {
		EventC(new DisableItems());
		EventC(new SecurityListenerV4());
		EventC(new OnPlayerJoin());
		EventC(new SecurityListenerCooldowns());
		EventC(new ChatFormat());
		EventC(new RewardsListenerChat());
		EventC(new LoginEvent());
		EventC(new DeathEvent());
		EventC(new AFkPlayerEvents());
		EventC(new WorldChange());
		EventC(new CreatePortal());
		EventC(new EntitySpawn());
		EventC(new Signs());
		EventC(new FarmingSystem());
		EventC(new MirrorEvents());
		EventC(new ItemUse());
	}
	
	public static void notOnline(CommandSender s, String player) {
		sendMessages(s, "Missing.Player.Offline", Placeholder.c().add("%player%", player).add("%playername%", player));
	}
	
	public static void notExist(CommandSender s, String player) {
		sendMessages(s, "Missing.Player.NotExist", Placeholder.c().add("%player%", player).add("%playername%", player));
	}

	public static boolean has(CommandSender s, String cmd, String section) {
		if(!cmds.exists(section+"."+cmd+".Permission")) {
			Bukkit.getLogger().severe("[BUG] Missing configuration path [Commands.yml]!");
			Bukkit.getLogger().severe("[BUG] Report this to the DevTec discord:");
			Bukkit.getLogger().severe("[BUG] Missing path: "+section+"."+cmd);
			return false;
		}
		return cmds.getString(section+"."+cmd+".Permission").equals("")?true:s.hasPermission(cmds.getString(section+"."+cmd+".Permission"));
	}

	public static void noPerms(CommandSender s, String cmd, String section) {
		sendMessages(s, "NoPerms", Placeholder.c().add("%permission%", cmds.getString(section+"."+cmd+".Permission")));
	}

	public static void noPerms(CommandSender s, String cmd, String section, String sub) {
		sendMessages(s, "NoPerms", Placeholder.c().add("%permission%", cmds.getString(section+"."+cmd+".SubPermission."+sub)));
	}

	public static boolean has(CommandSender s, String cmd, String section, String subPerm) {
		return cmds.exists(section+"."+cmd+".SubPermission."+subPerm)?cmds.getString(section+"."+cmd+".SubPermission."+subPerm).equals("")?true:s.hasPermission(cmds.getString(section+"."+cmd+".SubPermission."+subPerm)):true;
	}
	public static boolean has(Player s, String cmd, String section, String subPerm) {
		return cmds.exists(section+"."+cmd+".SubPermission."+subPerm)?cmds.getString(section+"."+cmd+".SubPermission."+subPerm).equals("")?true:s.hasPermission(cmds.getString(section+"."+cmd+".SubPermission."+subPerm)):true;
	}

	public static Kit getKit(String kitName) {
		return Kit.load(kitName);
	}

	public static boolean hasKits(CommandSender p, String name) {
		return kit.exists("Kits."+name+".permission")?p.hasPermission(kit.getString("Kits."+name+".permission")):true;
	}
	public static String getPerm(String cmd, String section) {
		return cmds.getString(section+"."+cmd+".Permission");
	}
	public static String getPerm(String cmd, String section, String sub) {
		return cmds.getString(section+"."+cmd+".SubPermission."+sub);
	}
	public static String getElse(String string, boolean value) {
		String sd = plac.getString(string+"."+(value?"yes":"no"));
		return sd==null?value+"":sd;
	}
}
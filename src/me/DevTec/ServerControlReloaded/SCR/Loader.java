package me.DevTec.ServerControlReloaded.SCR;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.DevTec.ServerControlReloaded.Commands.BanSystem.Accounts;
//github.com/TheDevTec/ServerControlReloaded
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Ban;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.BanIP;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.DelJail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Immune;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Jail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Kick;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Mute;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.SetJail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.TempBan;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.TempBanIP;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.TempJail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.TempMute;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.UnBan;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.UnBanIP;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.UnJail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.UnMute;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Warn;
import me.DevTec.ServerControlReloaded.Commands.Economy.Balance;
import me.DevTec.ServerControlReloaded.Commands.Economy.Eco;
import me.DevTec.ServerControlReloaded.Commands.Economy.EcoTop;
import me.DevTec.ServerControlReloaded.Commands.Economy.MultiEconomy;
import me.DevTec.ServerControlReloaded.Commands.Economy.Pay;
import me.DevTec.ServerControlReloaded.Commands.Enchantment.EnchantTable;
import me.DevTec.ServerControlReloaded.Commands.Enchantment.EnchantTableRemove;
import me.DevTec.ServerControlReloaded.Commands.Enchantment.EnchantTableRemoveAll;
import me.DevTec.ServerControlReloaded.Commands.GameMode.Gamemode;
import me.DevTec.ServerControlReloaded.Commands.GameMode.GamemodeA;
import me.DevTec.ServerControlReloaded.Commands.GameMode.GamemodeC;
import me.DevTec.ServerControlReloaded.Commands.GameMode.GamemodeS;
import me.DevTec.ServerControlReloaded.Commands.GameMode.GamemodeSP;
import me.DevTec.ServerControlReloaded.Commands.Info.Chunks;
import me.DevTec.ServerControlReloaded.Commands.Info.ListCmd;
import me.DevTec.ServerControlReloaded.Commands.Info.Maintenance;
import me.DevTec.ServerControlReloaded.Commands.Info.RAM;
import me.DevTec.ServerControlReloaded.Commands.Info.SCR;
import me.DevTec.ServerControlReloaded.Commands.Info.Seen;
import me.DevTec.ServerControlReloaded.Commands.Info.Staff;
import me.DevTec.ServerControlReloaded.Commands.Info.TPS;
import me.DevTec.ServerControlReloaded.Commands.Info.WhoIs;
import me.DevTec.ServerControlReloaded.Commands.Inventory.ClearConfirmToggle;
import me.DevTec.ServerControlReloaded.Commands.Inventory.ClearInv;
import me.DevTec.ServerControlReloaded.Commands.Inventory.CloseInventory;
import me.DevTec.ServerControlReloaded.Commands.Inventory.Craft;
import me.DevTec.ServerControlReloaded.Commands.Inventory.EnderChest;
import me.DevTec.ServerControlReloaded.Commands.Inventory.EnderSee;
import me.DevTec.ServerControlReloaded.Commands.Inventory.Invsee;
import me.DevTec.ServerControlReloaded.Commands.Kill.Kill;
import me.DevTec.ServerControlReloaded.Commands.Kill.KillAll;
import me.DevTec.ServerControlReloaded.Commands.Kill.Suicide;
import me.DevTec.ServerControlReloaded.Commands.Message.Broadcast;
import me.DevTec.ServerControlReloaded.Commands.Message.ClearChat;
import me.DevTec.ServerControlReloaded.Commands.Message.Helpop;
import me.DevTec.ServerControlReloaded.Commands.Message.Mail;
import me.DevTec.ServerControlReloaded.Commands.Message.PrivateMessage;
import me.DevTec.ServerControlReloaded.Commands.Message.ReplyPrivateMes;
import me.DevTec.ServerControlReloaded.Commands.Message.SocialSpy;
import me.DevTec.ServerControlReloaded.Commands.Message.Sudo;
import me.DevTec.ServerControlReloaded.Commands.Nickname.Nick;
import me.DevTec.ServerControlReloaded.Commands.Nickname.NickReset;
import me.DevTec.ServerControlReloaded.Commands.Other.AFK;
import me.DevTec.ServerControlReloaded.Commands.Other.ActionBar;
import me.DevTec.ServerControlReloaded.Commands.Other.BossBar;
import me.DevTec.ServerControlReloaded.Commands.Other.Butcher;
import me.DevTec.ServerControlReloaded.Commands.Other.ChatLock;
import me.DevTec.ServerControlReloaded.Commands.Other.ColorsCmd;
import me.DevTec.ServerControlReloaded.Commands.Other.Exp;
import me.DevTec.ServerControlReloaded.Commands.Other.Feed;
import me.DevTec.ServerControlReloaded.Commands.Other.Fly;
import me.DevTec.ServerControlReloaded.Commands.Other.Give;
import me.DevTec.ServerControlReloaded.Commands.Other.God;
import me.DevTec.ServerControlReloaded.Commands.Other.Hat;
import me.DevTec.ServerControlReloaded.Commands.Other.Heal;
import me.DevTec.ServerControlReloaded.Commands.Other.Kits;
import me.DevTec.ServerControlReloaded.Commands.Other.MultiWorlds;
import me.DevTec.ServerControlReloaded.Commands.Other.Repair;
import me.DevTec.ServerControlReloaded.Commands.Other.RulesCmd;
import me.DevTec.ServerControlReloaded.Commands.Other.Scoreboard;
import me.DevTec.ServerControlReloaded.Commands.Other.Skin;
import me.DevTec.ServerControlReloaded.Commands.Other.Skull;
import me.DevTec.ServerControlReloaded.Commands.Other.Spawner;
import me.DevTec.ServerControlReloaded.Commands.Other.TempFly;
import me.DevTec.ServerControlReloaded.Commands.Other.Thor;
import me.DevTec.ServerControlReloaded.Commands.Other.Trash;
import me.DevTec.ServerControlReloaded.Commands.Other.Uuid;
import me.DevTec.ServerControlReloaded.Commands.Other.Vanish;
import me.DevTec.ServerControlReloaded.Commands.Other.tablist.Tab;
import me.DevTec.ServerControlReloaded.Commands.Server.Reload;
import me.DevTec.ServerControlReloaded.Commands.Server.Restart;
import me.DevTec.ServerControlReloaded.Commands.Server.Stop;
import me.DevTec.ServerControlReloaded.Commands.Speed.FlySpeed;
import me.DevTec.ServerControlReloaded.Commands.Speed.WalkSpeed;
import me.DevTec.ServerControlReloaded.Commands.Time.Day;
import me.DevTec.ServerControlReloaded.Commands.Time.Night;
import me.DevTec.ServerControlReloaded.Commands.Time.PDay;
import me.DevTec.ServerControlReloaded.Commands.Time.PNight;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tp;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpa;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.TpaBlock;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpaall;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpaccept;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpadeny;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpahere;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpall;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpcancel;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tphere;
import me.DevTec.ServerControlReloaded.Commands.Warps.Back;
import me.DevTec.ServerControlReloaded.Commands.Warps.DelHome;
import me.DevTec.ServerControlReloaded.Commands.Warps.DelWarp;
import me.DevTec.ServerControlReloaded.Commands.Warps.Home;
import me.DevTec.ServerControlReloaded.Commands.Warps.HomeOther;
import me.DevTec.ServerControlReloaded.Commands.Warps.Homes;
import me.DevTec.ServerControlReloaded.Commands.Warps.SetHome;
import me.DevTec.ServerControlReloaded.Commands.Warps.SetSpawn;
import me.DevTec.ServerControlReloaded.Commands.Warps.SetWarp;
import me.DevTec.ServerControlReloaded.Commands.Warps.Spawn;
import me.DevTec.ServerControlReloaded.Commands.Warps.Warp;
import me.DevTec.ServerControlReloaded.Commands.Weather.PRain;
import me.DevTec.ServerControlReloaded.Commands.Weather.PSun;
import me.DevTec.ServerControlReloaded.Commands.Weather.Rain;
import me.DevTec.ServerControlReloaded.Commands.Weather.Sun;
import me.DevTec.ServerControlReloaded.Commands.Weather.Thunder;
import me.DevTec.ServerControlReloaded.Events.AFkPlayerEvents;
import me.DevTec.ServerControlReloaded.Events.ChatFormat;
import me.DevTec.ServerControlReloaded.Events.CreatePortal;
import me.DevTec.ServerControlReloaded.Events.DeathEvent;
import me.DevTec.ServerControlReloaded.Events.DisableItems;
import me.DevTec.ServerControlReloaded.Events.EntitySpawn;
import me.DevTec.ServerControlReloaded.Events.FarmingSystem;
import me.DevTec.ServerControlReloaded.Events.LoginEvent;
import me.DevTec.ServerControlReloaded.Events.OnPlayerJoin;
import me.DevTec.ServerControlReloaded.Events.RewardsListenerChat;
import me.DevTec.ServerControlReloaded.Events.SecurityListenerCooldowns;
import me.DevTec.ServerControlReloaded.Events.SecurityListenerV4;
import me.DevTec.ServerControlReloaded.Events.Signs;
import me.DevTec.ServerControlReloaded.Events.WorldChange;
import me.DevTec.ServerControlReloaded.Utils.Colors;
import me.DevTec.ServerControlReloaded.Utils.Configs;
import me.DevTec.ServerControlReloaded.Utils.Converter;
import me.DevTec.ServerControlReloaded.Utils.DisplayManager;
import me.DevTec.ServerControlReloaded.Utils.Kit;
import me.DevTec.ServerControlReloaded.Utils.Metrics;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsGUI;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsUtils;
import me.DevTec.ServerControlReloaded.Utils.Rule;
import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.DevTec.ServerControlReloaded.Utils.Tasks;
import me.DevTec.ServerControlReloaded.Utils.VaultHook;
import me.DevTec.ServerControlReloaded.Utils.XMaterial;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.listener.EventHandler;
import me.devtec.theapi.utils.listener.Listener;
import me.devtec.theapi.utils.listener.events.PlayerVanishEvent;
import me.devtec.theapi.utils.reflections.Ref;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Loader extends JavaPlugin implements Listener {
	public static Config config, sb, tab, mw, kit, trans, events, cmds, anim, ac, bb;
	public static List<Rule> rules = new ArrayList<>();
	private int task;
	private long time, rkick;
	private static List<PluginCommand> registered = new ArrayList<>();
	public static String[] colorsText, rulesText;
	public static Economy econ;
	public static Loader getInstance;
	public static Config english;
	private static UpdateChecker updater;
	public static Chat vault = null;
	public static Permission perms = null;
	private static int aad = 0;
	
	public static class Placeholder {
		private final HashMap<String, String> set = new HashMap<>();
		public Placeholder add(String placeholder, String replace) {
			set.put(placeholder, replace);
			return this;
		}
		
		public static Placeholder c() {
			return new Placeholder();
		}

		public Placeholder replace(String placeholder, String replace) {
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
			string=string.replace("%player%", sender.getName())
					.replace("%playername%", sender.getName())
					.replace("%customname%", sender.getName());
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
						return english.getString(path);
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
	
	public static void sendMessages(CommandSender to, String path) {
		sendMessages(to, path, null);
	}
	
	public static void sendBroadcasts(CommandSender whoIsSelected, String path) {
		sendBroadcasts(whoIsSelected, path, null);
	}
	
	public static void sendMessages(CommandSender to, String path, Placeholder placeholders) {
		Object o = getTranslation(path);
		if(o==null)return;
		if(o instanceof Collection) {
			for(Object d : (Collection<?>)o)
				TheAPI.msg(placeholder(to, d+"", placeholders), to);
		}else
			if(!(o+"").isEmpty())
		TheAPI.msg(placeholder(to, o+"", placeholders), to);
	}
	
	@SuppressWarnings("unchecked")
	public static void sendBroadcasts(CommandSender whoIsSelected, String path, Placeholder placeholders) {
		Object o = getTranslation(path);
		if(o==null)return;
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
		if(o instanceof List) {
			for(String s : (List<String>)o)
				TheAPI.broadcast(placeholder(whoIsSelected, s, placeholders), perms);
		}else
		TheAPI.bc(placeholder(whoIsSelected, o.toString(), placeholders), perms);
	}
	
	public static enum Item {
		PREFIX,
		SUFFIX,
		GROUP
	}
	public static String get(Player p, Item type) {
		switch(type) {
		case GROUP:
			try {
				if (PluginManagerAPI.getPlugin("Vault") != null)
					if (vault != null) {
						if (Loader.config.exists("Chat-Groups." + Loader.vault.getPrimaryGroup(p)))
							return vault.getPrimaryGroup(p);
					}
				return "default";
			} catch (Exception e) {
				return "default";
			}
		case PREFIX:
			if (PluginManagerAPI.getPlugin("Vault") != null && vault != null) {
				if (getGroup(p) != null && vault.getGroupPrefix(p.getWorld().getName(), getGroup(p)) != null)
					return vault.getGroupPrefix(p.getWorld().getName(), getGroup(p));
				return "";
			}
			return "";
		case SUFFIX:
			if (PluginManagerAPI.getPlugin("Vault") != null && vault != null) {
				if (getGroup(p) != null && vault.getGroupSuffix(p.getWorld().getName(), getGroup(p)) != null)
					return vault.getGroupSuffix(p.getWorld().getName(), getGroup(p));
				return "";
			}
			return "";
		}
		return null;
	}
	
	public static void setupChatFormat(Player p) {
		if(p==null)return;
		if (config.exists("Chat-Groups." + get(p, Item.GROUP) + ".Name")) {
			String g = PlaceholderAPI.setPlaceholders(p,config.getString("Chat-Groups." + get(p, Item.GROUP) + ".Name"));
			g = ChatFormat.r(p, g, null);
			p.setDisplayName(Colors.colorize(g, false, p));
		} else
			p.setDisplayName(get(p, Item.PREFIX) + p.getName() + get(p, Item.SUFFIX));
		if(TheAPI.getUser(p).exist("DisplayName"))
			p.setCustomName(TheAPI.colorize(PlaceholderAPI.setPlaceholders(p, TheAPI.getUser(p).getString("DisplayName"))));
	}

	public static String getGroup(Player p) {
		try {
			if (PluginManagerAPI.getPlugin("Vault") != null) {
				if (Loader.vault != null && Loader.vault.getPrimaryGroup(p) != null)
					return Loader.vault.getPrimaryGroup(p);
				return "";
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	public static String isAfk(Player p) {
		return API.getSPlayer(p).isAFK() ? tab.getString("AFK.true") : tab.getString("AFK.false");
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
		if (tab.getBoolean("Colored-Ping") == true)
			return getColoredPing(who);
		return String.valueOf(TheAPI.getPlayerPing(who));
	}
	
	@Override
	public void onLoad() {
		getInstance = this;
		Configs.load(false);
		if (PluginManagerAPI.getPlugin("Vault") != null) {
			setupEco();
		} else {
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &eINFO: &7Missing Vault plugin for Economy.", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
		}
	}
	
	private static long loading;

	@Override
	public void onEnable() {
		updater = new UpdateChecker();
		switch(updater.checkForUpdates()) {
		case -1:
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &eUpdate checker: &7Unable to connect to spigot, check internet connection.", TheAPI.getConsole());
			updater=null; //close updater
			break;
		case 1:
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &eUpdate checker: &7Found new version of SCR.", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "        https://www.spigotmc.org/resources/71147/", TheAPI.getConsole());
			break;
		case 2:
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &eUpdate checker: &7You are using the BETA version of SCR, report bugs to our Discord.", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "        https://discord.io/spigotdevtec", TheAPI.getConsole());
			break;
		}
		if(updater!=null)
		new Tasker() {
			public void run() {
				switch(updater.checkForUpdates()) {
					case -1:
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &eUpdate checker: &7Unable to connect to spigot, check internet connection.", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						updater=null; //close updater
						cancel(); //destroy task
						break;
					case 1:
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &eUpdate checker: &7Found new version of SCR.", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + "        https://www.spigotmc.org/resources/71147/", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						break;
					case 2:
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &eUpdate checker: &7You are using the BETA version of SCR, report bugs to our Discord.", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + "        https://discord.io/spigotdevtec", TheAPI.getConsole());
						TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
						break;
				}
			}
		}.runRepeating(144000, 144000);
		reload();
		new Tasker() {
			public void run() {
				for(Player p : TheAPI.getOnlinePlayers()) {
					SPlayer d = API.getSPlayer(p);
					Location from = d.l;
					if(from==null)from=p.getLocation();
					Location to = p.getLocation();
					if (Math.abs(from.getBlockX() - to.getBlockX()) > 0 || Math.abs(from.getBlockZ() - to.getBlockZ()) > 0 || Math.abs(from.getBlockY() - to.getBlockY()) > 0) {
						if (d.isAFK() && !d.hasVanish())
							sendBroadcasts(p, "AFK.End");
						save(d);
					}
					d.l=to;
				}
			}
		}.runRepeating(0, 10);
		TheAPI.msg(setting.prefix + " &eINFO: &7Newest versions of &eTheAPI &7can be found on Spigot or Discord:", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + "        https://www.spigotmc.org/resources/72679/", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + "        https://discord.io/spigotdevtec", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
		EventsRegister();
	}
	
	public int isNewer(String a, String b) {
    	int is = 0, d = 0;
    	String[] s = a.split("\\.");
    	for(String f : b.split("\\.")) {
    		int id = StringUtils.getInt(f),
    			bi = StringUtils.getInt(s[d++]);
    		if(id > bi) {
    			is=1;
    			break;
    		}
    		if(id < bi) {
    			is=2;
    			break;
    		}
    	}
    	return is;
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
	    public int checkForUpdates() {
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
	    	if(readerr==null)return -1;
	        return Loader.getInstance.isNewer(getDescription().getVersion(), readerr[0]);
	    }
	}
	
	@Override
	public void onDisable() {
		org.bukkit.event.HandlerList.unregisterAll(this);
		for (Player p : TheAPI.getOnlinePlayers()) {
			p.setFlying(false);
			p.setAllowFlight(false);
			p.setDisplayName(null);
			p.setCustomName(null);
		}
		TabList.removeTab();
		Tasks.unload();
		DisplayManager.unload();
		for (String w : mw.getStringList("Worlds"))
			if (Bukkit.getWorld(w) != null) {
				Bukkit.getLogger().info("Saving world '" + w + "'");
				Bukkit.getWorld(w).save();
			}
	}

	private static boolean setupVault() {
		RegisteredServiceProvider<Chat> economyProvider = Bukkit.getServicesManager()
				.getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (economyProvider != null) {
			vault = economyProvider.getProvider();
		}

		return (vault != null);
	}

	private static boolean setupCustomEco() {
		new VaultHook().hook();
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();

		}
		return econ != null;
	}

	public static void EconomyLog(String s) {
		if (config.getBoolean("Options.Economy.Log"))
			Bukkit.getLogger().info("[EconomyLog] " + s);
	}

	private static boolean setupEco() {
		try {
			RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager()
					.getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (economyProvider != null) {
				if (config.getBoolean("Options.Economy.CanUseOtherEconomy")) {
					EconomyLog("Found economy '" + economyProvider.getProvider().getName()
							+ "', using setting and loading plugin economy.");
					setupCustomEco();
				} else {
					EconomyLog("Found economy '" + economyProvider.getProvider().getName()
							+ "', skipping plugin economy.");
					econ = economyProvider.getProvider();
				}
			}
			if (econ == null) {
				EconomyLog("Plugin not found any economy, loading plugin economy.");
				if (!config.getBoolean("Options.Economy.DisablePluginEconomy"))
					setupCustomEco();
			}
		} catch (Exception e) {
			EconomyLog("Error when hooking economy.");
		}
		return (econ != null);
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
			DisplayManager.load();
			getInstance.stop();
			getInstance.starts();
			Configs.load(true);
		}else {
			DisplayManager.load();
			getInstance.starts();
			if(config.getBoolean("Options.Metrics") && TheAPI.isNewerThan(7))
			new Metrics();
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
		MultiWorldsUtils.LoadWorlds();
		ItemGUI clear=new ItemGUI(ItemCreatorAPI.create(XMaterial.LAVA_BUCKET.getMaterial(), 1, "&6Clear")) {
				public void onClick(Player s, GUI g, ClickType c) {
					for (int i = 0; i < 45; ++i)
					g.remove(i);
				}
			};
			GUI sa = new GUI(""+Loader.getTranslation("Trash"), 54);
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
			else if (has(p, "Fly", "Other") && s.hasFlyEnabled())
				s.enableFly();
			setupChatFormat(p);
			if (EconomyAPI.getEconomy() != null)
				if(!EconomyAPI.hasAccount(p))
				EconomyAPI.createAccount(p);
		}
		MultiWorldsUtils.EnableWorldCheck();
		TheAPI.msg(setting.prefix + " &7"+(aad == 0 ? "L" : "Rel")+"oading kits:", TheAPI.getConsole());
		for (String s : Loader.kit.getKeys("Kits")) {
			TheAPI.msg(setting.prefix + "   &e"+s+"&7:", TheAPI.getConsole());
			Kit kit = Kit.load(s);
			TheAPI.msg(setting.prefix + "     &7Cooldown: &e" + StringUtils.setTimeToString(kit.getDelay()), TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "     &7Cost: &e$" + API.setMoneyFormat(kit.getCost(), false), TheAPI.getConsole());
		}
		TabList.reload();
		Tasks.reload();
		for(PluginCommand reg : registered) {
			TheAPI.unregisterCommand(reg);
		}
		registered.clear();
		CommandsRegister();
		TheAPI.msg(setting.prefix + " &7"+(aad == 0 ? "L" : "Rel")+"oading of SCR took "+(System.currentTimeMillis()-loading)+"ms", TheAPI.getConsole());
		aad=1;
		TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
	}
	
	private static void CmdC(String section, String command, CommandExecutor cs) {
		if(cmds.getBoolean(section+"."+command+".Enabled")) {
			PluginCommand c = TheAPI.createCommand(cmds.getString(section+"."+command+".Name"), getInstance);
			List<String> aliases = new ArrayList<>();
			if(cmds.exists(section+"."+command+".Aliases")) {
			if(cmds.get(section+"."+command+".Aliases") instanceof List)
				aliases=cmds.getStringList(section+"."+command+".Aliases");
			else aliases.add(cmds.getString(section+"."+command+".Aliases"));
			}
			c.setAliases(aliases);
			c.setExecutor(cs);
			c.setPermission(cmds.getString(section+"."+command+".Permission"));
			TheAPI.registerCommand(c);
			registered.add(c);
		}
	}

	public void starts() {
		time = StringUtils.timeFromString(Loader.config.getString("Options.AFK.TimeToAFK"));
		rkick = StringUtils.timeFromString(Loader.config.getString("Options.AFK.TimeToKick"));
		task=new Tasker() {
			@Override
			public void run() {
				for(SPlayer s : API.getSPlayers()) {
				boolean is = getTime(s) <= 0;
				if (setting.afk_auto) {
					if (is) {
						if (!s.bc && !s.mp) {
							s.bc = true;
							s.mp = true;
							if (!s.hasVanish())
								Loader.sendBroadcasts(s.getPlayer(), "AFK.Start");
						}
						if (setting.afk_kick && is) {
							if (s.kick >= rkick) {
								if (!s.hasPermission("scr.afk.bypass"))
									if(s.getPlayer()!=null && s.getPlayer().isOnline())
										new Tasker() {
											public void run() {
												s.getPlayer().kickPlayer(TheAPI.colorize(Loader.config.getString("Options.AFK.KickMessage")));
											}
										}.runTaskSync();
								cancel();
								return;
							} else
								++s.kick;
						}
					}
					++s.afk;
				}
				}
			}
		}.runRepeating(0, 20);
	}

	public void setAFK(SPlayer s) {
		save(s);
		s.mp = true;
		s.manual = true;
			if (!s.hasVanish())
				Loader.sendBroadcasts(s.getPlayer(), "AFK.Start");
	}
	public void setAFK(SPlayer s, String reason) {
		save(s);
		s.mp = true;
		s.manual = true;
			if (!s.hasVanish())
				Loader.sendBroadcasts(s.getPlayer(), "AFK.Start_WithReason", Placeholder.c().add("%reason%", reason));
				//Loader.sendBroadcasts(s.getPlayer(), "AFK.Start");
	}

	public long getTime(SPlayer s) {
		return time - s.afk;
	}

	public void save(SPlayer s) {
		s.afk = 0;
		s.kick = 0;
		s.manual = false;
		s.mp = false;
		s.bc = false;
	}

	public boolean isManualAfk(SPlayer s) {
		return s.manual;
	}

	public boolean isAfk(SPlayer s) {
		return getTime(s) <= 0;
	}

	private void stop() {
		Scheduler.cancelTask(task);
	}

	private static void CommandsRegister() {
		//Server
		CmdC("Server", "Stop" , new Stop());
		CmdC("Server", "Reload", new Reload());
		CmdC("Server", "Restart", new Restart());
		
		//Kill
		CmdC("Kill", "Kill",new Kill());
		CmdC("Kill", "KillAll",new KillAll());
		CmdC("Kill", "Suicide",new Suicide());
		
		//Info
		CmdC("Info", "Memory",new RAM());
		CmdC("Info", "Chunks",new Chunks());
		CmdC("Info", "SCR",new SCR());
		CmdC("Info","Seen", new Seen());
		CmdC("Info","ChatFormat", new me.DevTec.ServerControlReloaded.Commands.Info.ChatFormat());
		CmdC("Info","List", new ListCmd());// /list == error
		CmdC("Info","Staff", new Staff());
		CmdC("Info", "TPS",new TPS());
		CmdC("Info","WhoIs", new WhoIs()); 
		CmdC("Info", "Maintenance",new Maintenance());
		
		//Speed
		CmdC("Speed", "FlySpeed",new FlySpeed());
		CmdC("Speed", "WalkSpeed",new WalkSpeed());
		
		//Warps
		CmdC("Warps", "SetSpawn",new SetSpawn());
		CmdC("Warps", "Spawn",new Spawn());
		CmdC("Warps", "SetWarp",new SetWarp());
		CmdC("Warps", "DelWarp",new DelWarp());
		CmdC("Warps", "Warp",new Warp());
		CmdC("Warps", "Home",new Home());
		CmdC("Warps", "HomeOther", new HomeOther());
		CmdC("Warps", "SetHome",new SetHome());
		CmdC("Warps", "DelHome",new DelHome());
		CmdC("Warps", "Homes",new Homes());
		CmdC("Warps", "Back",new Back());
		
		//Economy
		CmdC("Economy", "BalanceTop",new EcoTop());
		CmdC("Economy", "Balance",new Balance());
		CmdC("Economy", "Economy",new Eco());
		CmdC("Economy", "Pay",new Pay());
		CmdC("Economy", "MultiEconomy", new MultiEconomy());
		
		//Weather
		CmdC("Weather", "Sun",new Sun());
		CmdC("Weather", "Thunder",new Thunder());
		CmdC("Weather", "Rain",new Rain());
		CmdC("Weather", "PlayerSun",new PSun());
		CmdC("Weather", "PlayerRain",new PRain());
		
		//Time
		CmdC("Time", "Day",new Day());
		CmdC("Time", "Night",new Night());
		CmdC("Time", "PDay",new PDay());
		CmdC("Time", "PNight",new PNight());
		
		//Message
		CmdC("Message","SocialSpy", new SocialSpy());
		CmdC("Message","Mail", new Mail());
		CmdC("Message","Sudo", new Sudo());
		CmdC("Message","Broadcast", new Broadcast());
		CmdC("Message", "PrivateMessage", new PrivateMessage());
		CmdC("Message", "ClearChat",new ClearChat());
		CmdC("Message","Helpop", new Helpop());
		CmdC("Message","Reply", new ReplyPrivateMes());
		
		//Gamemode
		CmdC("GameMode", "GameMode",new Gamemode());
		CmdC("GameMode", "GameModeSurvival",new GamemodeS());
		CmdC("GameMode", "GameModeCreative",new GamemodeC());
		CmdC("GameMode", "GameModeAdventure",new GamemodeA());
		if(TheAPI.isNewerThan(7))
			CmdC("GameMode", "GameModeSpectator",new GamemodeSP());
			
		//BanSystem	
		CmdC("BanSystem", "Kick", new Kick());
		CmdC("BanSystem", "Ban", new Ban());
		CmdC("BanSystem", "Immune", new Immune());
		CmdC("BanSystem", "TempBan", new TempBan());
		CmdC("BanSystem", "Jail",new Jail());
		CmdC("BanSystem", "TempBanIP",new TempBanIP());
		CmdC("BanSystem", "UnJail",new UnJail());
		CmdC("BanSystem", "SetJail",new SetJail());
		CmdC("BanSystem", "DelJail",new DelJail());
		CmdC("BanSystem", "TempJail", new TempJail());
		CmdC("BanSystem", "BanIP", new BanIP());
		CmdC("BanSystem", "UnBanIP", new UnBanIP());
		CmdC("BanSystem", "UnBan", new UnBan());
		CmdC("BanSystem", "TempMute", new TempMute());
		CmdC("BanSystem", "Mute", new Mute());
		CmdC("BanSystem", "UnMute", new UnMute());
		CmdC("BanSystem", "Warn", new Warn());
		CmdC("BanSystem", "Accounts", new Accounts());
		
		//Inventory
		CmdC("Inventory", "EnderChest", new EnderChest());
		CmdC("Inventory", "Endersee", new EnderSee());
		CmdC("Inventory", "CloseInventory", new CloseInventory());
		CmdC("Inventory", "ClearInventory",new ClearInv());
		CmdC("Inventory", "ClearConfirmToggle", new ClearConfirmToggle());
		CmdC("Inventory", "Inventory", new Invsee());
		CmdC("Inventory", "Workbench", new Craft());
		
		//Enchantment
		CmdC("Enchantment", "Enchant", new EnchantTable());
		CmdC("Enchantment", "EnchantRemove", new EnchantTableRemove());
		CmdC("Enchantment", "EnchantRemoveAll", new EnchantTableRemoveAll());
		
		//TpSystem
		CmdC("TpSystem", "Tp", new Tp());
		CmdC("TpSystem", "TpHere", new Tphere());
		CmdC("TpSystem", "TpCancel", new Tpcancel());
		CmdC("TpSystem", "Tpa", new Tpa());
		CmdC("TpSystem", "TpaHere", new Tpahere());
		CmdC("TpSystem", "TpToggle", new TpaBlock());
		CmdC("TpSystem", "TpaAll", new Tpaall());
		CmdC("TpSystem", "TpAll", new Tpall());
		CmdC("TpSystem", "TpAccept", new Tpaccept());
		CmdC("TpSystem", "TpDeny", new Tpadeny());
		
		//Other
		CmdC("Other", "ChatLock",new ChatLock());
		CmdC("Other", "Repair", new Repair());
		CmdC("Other", "Feed", new Feed());
		CmdC("Other", "Item", new me.DevTec.ServerControlReloaded.Commands.Other.Item());
		CmdC("Other", "TempFly", new TempFly());
		CmdC("Other", "ScoreBoard", new Scoreboard());
		CmdC("Other", "ActionBar", new ActionBar());
		CmdC("Other", "BossBar", new BossBar());
		CmdC("Other", "Trash", new Trash());
		CmdC("Other", "Thor", new Thor());
		CmdC("Other", "Give",new Give());
		CmdC("Other", "Kits",new Kits());
		CmdC("Other", "Skull",new Skull());
		CmdC("Other", "God",new God());
		CmdC("Other", "Heal", new Heal());
		CmdC("Other", "Fly",new Fly());
		CmdC("Other", "Vanish",new Vanish());
		CmdC("Other", "Butcher",new Butcher());
		CmdC("Other", "AFK",new AFK());
		CmdC("Other", "MultiWorlds",new MultiWorlds());
		CmdC("Other", "TabList",new Tab());
		CmdC("Other", "Hat",new Hat());
		CmdC("Other", "Skin",new Skin());
		CmdC("Other", "Experiences", new Exp());
		CmdC("Other", "Spawner", new Spawner());
		CmdC("Other", "Uuid", new Uuid());
		//Utility
		CmdC("Other", "Colors", new ColorsCmd());
		CmdC("Other", "Rules", new RulesCmd());
		//Nickname
		CmdC("Nickname", "Nickname", new Nick());
		CmdC("Nickname", "NicknameReset", new NickReset());
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
		TheAPI.register(new Listener() {
			@EventHandler
			public void onVanish(PlayerVanishEvent e) {
				if(setting.tab && setting.tab_vanish)
					LoginEvent.moveInTab(e.getPlayer());
			}
		});
	}
	
	public static void notOnline(CommandSender s, String player) {
		sendMessages(s, "Missing.Player.Offline", Placeholder.c().add("%player%", player).add("%playername%", player));
	}
	
	public static void notExist(CommandSender s, String player) {
		sendMessages(s, "Missing.Player.NotExist", Placeholder.c().add("%player%", player).add("%playername%", player));
	}

	public static boolean has(CommandSender s, String cmd, String section) {
		if(!cmds.exists(section+"."+cmd+".Permission")) {
			Bukkit.getLogger().severe("[BUG] Missing configuration path!");
			Bukkit.getLogger().severe("[BUG] Report this to the DevTec discord:");
			Bukkit.getLogger().severe("[BUG] Missing path: "+section+"."+cmd);
			return false;
		}
		return s.hasPermission(cmds.getString(section+"."+cmd+".Permission"));
	}

	public static void noPerms(CommandSender s, String cmd, String section) {
		sendMessages(s, "NoPerms", Placeholder.c().add("%permission%", cmds.getString(section+"."+cmd+".Permission")));
	}

	public static void noPerms(CommandSender s, String cmd, String section, String sub) {
		sendMessages(s, "NoPerms", Placeholder.c().add("%permission%", cmds.getString(section+"."+cmd+".SubPermission."+sub)));
	}

	public static boolean has(CommandSender s, String cmd, String section, String subPerm) {
		return cmds.exists(section+"."+cmd+".SubPermission."+subPerm)?s.hasPermission(cmds.getString(section+"."+cmd+".SubPermission."+subPerm)):true;
	}
	public static boolean has(Player s, String cmd, String section, String subPerm) {
		return cmds.exists(section+"."+cmd+".SubPermission."+subPerm)?s.hasPermission(cmds.getString(section+"."+cmd+".SubPermission."+subPerm)):true;
	}

	public static Kit getKit(String kitName) {
		return Kit.load(kitName);
	}

	public static boolean hasKits(CommandSender p, String name) {
		return kit.exists("Kits."+name+".permission")?p.hasPermission(kit.getString("Kits."+name+".permission")):true;
	}
}
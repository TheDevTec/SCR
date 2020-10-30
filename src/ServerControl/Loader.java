package ServerControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import Events.AFKPlus;
import Events.AFkPlayerEvents;
import Events.ChatFormat;
import Events.CreatePortal;
import Events.DeathEvent;
import Events.DisableItems;
import Events.EntitySpawn;
import Events.FarmingSystem;
import Events.LoginEvent;
import Events.OnPlayerJoin;
import Events.RewardsListenerChat;
import Events.SecurityListenerAntiAD;
import Events.SecurityListenerCooldowns;
import Events.SecurityListenerV3;
import Events.Signs;
import Events.WorldChange;
import Utils.Colors;
import Utils.Configs;
import Utils.Converter;
import Utils.Kit;
import Utils.Metrics;
import Utils.MultiWorldsUtils;
import Utils.ScoreboardStats;
import Utils.TabList;
import Utils.Tasks;
import Utils.VaultHook;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.PluginManagerAPI;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.PlaceholderAPI.PlaceholderAPI;
import me.DevTec.TheAPI.Scheduler.Scheduler;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.NMS.NMSAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Loader extends JavaPlugin implements Listener {
	public static Config config, sb, tab, mw, kit, trans, events, cmds;
	
	public static Economy econ;
	public static Loader getInstance;
	
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
		if(o instanceof List<?>) {
			for(Object d : (List<?>)o)
			TheAPI.msg(d+"", s);
		}else
			TheAPI.msg(o+"", s);
	}
	
	public static String placeholder(CommandSender sender, String string, Placeholder placeholders) {
		if(setting.prefix!=null)
			string=string.replace("%prefix%", setting.prefix);
		if(placeholders!=null)
		for(Entry<String, String> placeholder : placeholders.set.entrySet())
			string=string.replace(placeholder.getKey(), placeholder.getValue());
		if(sender!=null) {
		if(sender instanceof Player)
			string=TabList.replace(string, (Player)sender);
		else
			string=string.replace("%player%", sender.getName())
					.replace("%playername%", sender.getName())
					.replace("%customname%", sender.getName());
		string=string.replace("%op%", ""+sender.isOp());
		}
		return PlaceholderAPI.setPlaceholders(sender instanceof Player ? (Player)sender : null, string);
	}
	
	private static Config english;
	
	public static Object getTranslation(String path) {
		if(!trans.exists(path)) {
			if(english.exists(path)) {
				if(english.get(path) instanceof List) {
					return english.getStringList(path);
				}else
					if(!english.getString(path).trim().isEmpty())
						return english.getString(path);
			}
		}else {
		if(trans.get(path) instanceof List) {
			return trans.getStringList(path);
		}else
			if(!trans.getString(path).trim().isEmpty())
				return trans.getString(path);
		}
		return null;
	}
	
	public static void sendMessages(CommandSender to, String path) {
		sendMessages(to, path, null);
	}
	
	public static void sendBroadcasts(CommandSender whoIsSelected, String path) {
		sendBroadcasts(whoIsSelected, path, null);
	}
	
	@SuppressWarnings("unchecked")
	public static void sendMessages(CommandSender to, String path, Placeholder placeholders) {
		Object o = getTranslation(path);
		if(o==null)return;
		if(o instanceof List) {
			for(String s : (List<String>)o)
				TheAPI.msg(placeholder(to, s, placeholders), to);
		}else
		TheAPI.msg(placeholder(to, o.toString(), placeholders), to);
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
						if (Loader.config.getString("Chat-Groups." + Loader.vault.getPrimaryGroup(p)) != null)
							return vault.getPrimaryGroup(p);
					}
				return "DefaultFormat";
			} catch (Exception e) {
				return "DefaultFormat";
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
		if (config.exists("Chat-Groups." + get(p, Item.GROUP) + ".Name")) {
			String g = TheAPI.colorize(PlaceholderAPI.setPlaceholders(p,
					config.getString("Chat-Groups." + get(p, Item.GROUP) + ".Name")));
			g = ChatFormat.r(p, g, null, false);
			API.setDisplayName(p, Colors.colorize(g, false, p));
		} else
			API.setDisplayName(p, get(p, Item.PREFIX) + p.getName() + get(p, Item.SUFFIX));
		if(TheAPI.getUser(p).exist("DisplayName"))
		NMSAPI.getNMSPlayerAPI(p).setCustomName(TheAPI.colorize(PlaceholderAPI.setPlaceholders(p, TheAPI.getUser(p).getString("DisplayName"))));
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
		return API.getSPlayer(p).isAFK() ? tab.getString("AFK.IsAFK") : tab.getString("AFK.IsNotAFK");
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
		Configs.load();
		english = new Config("ServerControlReloaded/Translations/translation-en.yml");
		setting.load();
	}
	private static long loading;

	@Override
	public void onEnable() {
		reload();
		TheAPI.msg(setting.prefix + " &eINFO: &7Newest versions of &eTheAPI &7can be found on Spigot:", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + "        https://www.spigotmc.org/resources/theapi.72679/", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
		EventsRegister();
		CommmandsRegister();
	}

	private static Metrics metrics;

	@Override
	public void onDisable() {
		for (Player p : TheAPI.getOnlinePlayers()) {
			p.setFlying(false);
			p.setAllowFlight(false);
		}
		TabList.removeTab();
		ScoreboardStats.removeScoreboard();
		for (String w : mw.getStringList("Worlds"))
			if (Bukkit.getWorld(w) != null) {
				Bukkit.getLogger().info("Saving world '" + w + "'");
				Bukkit.getWorld(w).save();
				Bukkit.getLogger().info("World '" + w + "' saved");
			}
	}

	public static Chat vault = null;

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

	public static Permission perms = null;

	private static boolean setupPermisions() {
		RegisteredServiceProvider<Permission> economyProvider = Bukkit.getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (economyProvider != null) {
			perms = economyProvider.getProvider();
		}
		return (perms != null);
	}
	
	private static int aad = 0;
	public static void reload() {
		loading = System.currentTimeMillis();
		if(aad==0) {
		MultiWorldsUtils.LoadWorlds();
		if (PluginManagerAPI.getPlugin("Vault") != null) {
			setupEco();
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
		TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
		if(aad==1) {
			if(metrics!=null) {
				Bukkit.getServicesManager().unregister(metrics);
				metrics.getTimer().cancel();
			}
			Tasks.reload();
			getInstance.stop();
		}
		TheAPI.msg(setting.prefix + " &7"+(aad == 0 ? "L" : "Rel")+"oading configs..", TheAPI.getConsole());
		Configs.load();
		TheAPI.msg(setting.prefix + " &7Configs "+(aad == 0 ? "l" : "rel")+"oaded.", TheAPI.getConsole());
		setting.load();
		Converter.convert();
		for (World wa : Bukkit.getWorlds())
			MultiWorldsUtils.DefaultSet(wa, Loader.mw.getString("WorldsSettings." + wa.getName() + ".Generator"));
		getInstance.starts();
		for (Player p : TheAPI.getOnlinePlayers()) {
			SPlayer s = API.getSPlayer(p);
			if (s.hasTempFlyEnabled())
				s.enableTempFly();
			else if (s.hasPermission("servercontrol.fly") && s.hasFlyEnabled())
				s.enableFly();
			setupChatFormat(p);
			if (EconomyAPI.getEconomy() != null)
				if(!EconomyAPI.hasAccount(p))
				EconomyAPI.createAccount(p);
		}
		MultiWorldsUtils.EnableWorldCheck();
		try {
			metrics=new Metrics();
		} catch (Exception er) {
			//unsuported
		}
		getInstance.kits.clear();
		TheAPI.msg(setting.prefix + " &7Loading kits:", TheAPI.getConsole());
		for (String s : Loader.kit.getKeys("Kits")) {
			TheAPI.msg(setting.prefix + "   &e"+s+"&7:", TheAPI.getConsole());
			Kit kit = Kit.load(s);
			TheAPI.msg(setting.prefix + "     &7Cooldown: &e" + StringUtils.setTimeToString(kit.getDelay()), TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "     &7Cost: &e$" + API.setMoneyFormat(kit.getCost(), false), TheAPI.getConsole());
		}
		TheAPI.msg(setting.prefix + " &7"+(aad == 0 ? "L" : "Rel")+"oading of SCR took "+(System.currentTimeMillis()-loading)+"ms", TheAPI.getConsole());
		aad=1;
		TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
	}

	private void CmdC(String section, String command, CommandExecutor p) {
		if(cmds.getBoolean(section+"."+command+".Enabled")) {
			PluginCommand c = TheAPI.createCommand(cmds.getString(section+"."+command+".Name"), this);
			List<String> aliases = new ArrayList<>();
			if(cmds.exists(section+"."+command+".Aliases")) {
			if(cmds.get(section+"."+command+".Aliases") instanceof List)
				aliases=cmds.getStringList(section+"."+command+".Aliases");
			else aliases.add(cmds.getString(section+"."+command+".Aliases"));
			}
			c.setAliases(aliases);
			c.setExecutor(p);
			c.setPermission(cmds.getString(section+"."+command+".Permission"));
			TheAPI.registerCommand(c);
		}
	}
	
	private static int task;
	private static long time, rkick;

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
								if (!s.hasPermission("servercontrol.afk.bypass"))
									if(s.getPlayer()!=null && s.getPlayer().isOnline())
									s.getPlayer().kickPlayer(TheAPI.colorize(Loader.config.getString("Options.AFK.KickMessage")));
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

	private void CommmandsRegister() {
		//Server
		CmdC("Server", "Stop" , new Commands.Server.Stop());
		CmdC("Server", "Reload", new Commands.Server.Reload());
		CmdC("Server", "Restart", new Commands.Server.Restart());
		
		//Kill
		CmdC("Kill", "Kill",new Commands.Kill.Kill());
		CmdC("Kill", "KillAll",new Commands.Kill.KillAll());
		CmdC("Kill", "Suicide",new Commands.Kill.Suicide());
		
		//Info
		CmdC("Info", "Memory",new Commands.Info.RAM());
		CmdC("Info", "Chunks",new Commands.Info.Chunks());
		CmdC("Info", "SCR",new Commands.Info.ServerControl());
		CmdC("Info","Seen", new Commands.Info.Seen());
		CmdC("Info","ChatFormat", new Commands.Info.ChatFormat());
		CmdC("Info","List", new Commands.Info.ListCmd());
		CmdC("Info","Staff", new Commands.Info.Staff());
		CmdC("Info", "TPS",new Commands.Info.TPS());
		CmdC("Info","WhoIs", new Commands.Info.WhoIs()); 
		CmdC("Info", "Maintenance",new Commands.Info.Maintenance());
		
		//Speed
		CmdC("Speed", "FlySpeed",new Commands.Speed.FlySpeed());
		CmdC("Speed", "WalkSpeed",new Commands.Speed.WalkSpeed());
		
		//Warps
		CmdC("Warps", "SetSpawn",new Commands.Warps.SetSpawn());
		CmdC("Warps", "Spawn",new Commands.Warps.Spawn());
		CmdC("Warps", "SetWarp",new Commands.Warps.SetWarp());
		CmdC("Warps", "DelWarp",new Commands.Warps.DelWarp());
		CmdC("Warps", "Warp",new Commands.Warps.Warp());
		CmdC("Warps", "Home",new Commands.Warps.Home());
		CmdC("Warps","homeother", new Commands.Warps.HomeOther());
		CmdC("Warps", "SetHome",new Commands.Warps.SetHome());
		CmdC("Warps", "DelHome",new Commands.Warps.DelHome());
		CmdC("Warps", "Homes",new Commands.Warps.Homes());
		CmdC("Warps", "Back",new Commands.Warps.Back());
		
		//Economy
		CmdC("Economy", "BalanceTop",new Commands.Economy.EcoTop());
		CmdC("Economy", "Balance",new Commands.Economy.Balance());
		CmdC("Economy", "Economy",new Commands.Economy.Eco());
		CmdC("Economy", "Pay",new Commands.Economy.Pay());
		CmdC("Economy","multieconomy", new Commands.Economy.MultiEconomy());
		
		//Weather
		CmdC("Weather", "Sun",new Commands.Weather.Sun());
		CmdC("Weather", "Thunder",new Commands.Weather.Thunder());
		CmdC("Weather", "Rain",new Commands.Weather.Rain());
		CmdC("Weather", "PSun",new Commands.Weather.PSun());
		CmdC("Weather", "PRain",new Commands.Weather.PRain());
		
		//Time
		CmdC("Time", "Day",new Commands.Time.Day());
		CmdC("Time", "Night",new Commands.Time.Night());
		CmdC("Time", "PDay",new Commands.Time.PDay());
		CmdC("Time", "PNight",new Commands.Time.PNight());
		
		//Message
		CmdC("Message","mail", new Commands.Message.Mail());
		CmdC("Message","sudo", new Commands.Message.Sudo());
		CmdC("Message","broadcast", new Commands.Message.Broadcast());
		CmdC("Message", "PrivateMessage", new Commands.Message.PrivateMessage());
		CmdC("Message", "ClearChat",new Commands.Message.ClearChat());
		CmdC("Message","helpop", new Commands.Message.Helpop());
		CmdC("Message","reply", new Commands.Message.ReplyPrivateMes());
		
		//Gamemode
		CmdC("Gamemode", "Gamemode",new Commands.Gamemode.Gamemode());
		CmdC("Gamemode", "GamemodeS",new Commands.Gamemode.GamemodeS());
		CmdC("Gamemode", "GamemodeC",new Commands.Gamemode.GamemodeC());
		CmdC("Gamemode", "GamemodeA",new Commands.Gamemode.GamemodeA());
		if(TheAPI.isNewerThan(7))
		CmdC("Gamemode", "GamemodeSP",new Commands.Gamemode.GamemodeSP());
			
		//BanSystem	
		CmdC("BanSystem","Kick", new Commands.BanSystem.Kick());
		CmdC("BanSystem","Ban", new Commands.BanSystem.Ban());
		CmdC("BanSystem","Immune", new Commands.BanSystem.Immune());
		CmdC("BanSystem","TempBan", new Commands.BanSystem.TempBan());
		CmdC("BanSystem", "Jail",new Commands.BanSystem.Jail());
		CmdC("BanSystem", "TempBanIP",new Commands.BanSystem.TempBanIP());
		CmdC("BanSystem", "UnJail",new Commands.BanSystem.UnJail());
		CmdC("BanSystem", "SetJail",new Commands.BanSystem.SetJail());
		CmdC("BanSystem", "DelJail",new Commands.BanSystem.DelJail());
		CmdC("BanSystem","tempjail", new Commands.BanSystem.TempJail());
		CmdC("BanSystem","BanIP", new Commands.BanSystem.BanIP());
		CmdC("BanSystem","UnBan-IP", new Commands.BanSystem.UnBanIP());
		CmdC("BanSystem","UnBan", new Commands.BanSystem.UnBan());
		CmdC("BanSystem","tempmute", new Commands.BanSystem.TempMute());
		CmdC("BanSystem","mute", new Commands.BanSystem.Mute());
		CmdC("BanSystem","unmute", new Commands.BanSystem.UnMute());
		CmdC("BanSystem","warn", new Commands.BanSystem.Warn());
		
		//Inventory
		CmdC("Inventory","enderchest", new Commands.Inventory.EnderChest());
		CmdC("Inventory","endersee", new Commands.Inventory.EnderSee());
		CmdC("Inventory","closeinv", new Commands.Inventory.CloseInventory());
		CmdC("Inventory", "ClearInv",new Commands.Inventory.ClearInv());
		CmdC("Inventory","ClearConfirmToggle", new Commands.Inventory.ClearConfirmToggle());
		CmdC("Inventory","Invsee", new Commands.Inventory.Invsee());
		
		//Enchantment
		CmdC("Enchantment","Enchant", new Commands.Enchantment.EnchantTable());
		CmdC("Enchantment","EnchantRemove", new Commands.Enchantment.EnchantTableRemove());
		CmdC("Enchantment","EnchantRemoveAll", new Commands.Enchantment.EnchantTableRemoveAll());
		
		//TpSystem
		CmdC("TpSystem","tp", new Commands.TpSystem.Tp());
		CmdC("TpSystem","tphere", new Commands.TpSystem.Tphere());
		CmdC("TpSystem","tpcancel", new Commands.TpSystem.Tpcancel());
		CmdC("TpSystem","tpa", new Commands.TpSystem.Tpa());
		CmdC("TpSystem","tpahere", new Commands.TpSystem.Tpahere());
		CmdC("TpSystem","tpblock", new Commands.TpSystem.TpaBlock());
		CmdC("TpSystem","tpaall", new Commands.TpSystem.Tpaall());
		CmdC("TpSystem","tpall", new Commands.TpSystem.Tpall());
		CmdC("TpSystem","tpaccept", new Commands.TpSystem.Tpaccept());
		CmdC("TpSystem","tpadeny", new Commands.TpSystem.Tpadeny());
		
		//Other
		CmdC("Other", "ChatLock",new Commands.Other.ChatLock());
		CmdC("Other","Repair", new Commands.Other.Repair());
		CmdC("Other","Feed", new Commands.Other.Feed());
		CmdC("Other","item", new Commands.Other.Item());
		CmdC("Other","tempfly", new Commands.Other.TempFly());
		CmdC("Other","board", new Commands.Other.ScoreboardStats());
		CmdC("Other","Trash", new Commands.Other.Trash());
		CmdC("Other","thor", new Commands.Other.Thor());
		CmdC("Other", "Give",new Commands.Other.Give());
		CmdC("Other", "Kits",new Commands.Other.KitCmd());
		CmdC("Other","craft", new Commands.Other.Craft());
		CmdC("Other", "Skull",new Commands.Other.Skull());
		CmdC("Other", "God",new Commands.Other.God());
		CmdC("Other", "Heal", new Commands.Other.Heal());
		CmdC("Other", "Fly",new Commands.Other.Fly());
		CmdC("Other", "Vanish",new Commands.Other.Vanish());
		CmdC("Other", "Butcher",new Commands.Other.Butcher());
		CmdC("Other", "AFK",new Commands.Other.AFK());
		CmdC("Other", "MultiWorlds",new Commands.Other.MultiWorlds());
		CmdC("Other", "Tablist",new Commands.Other.Tab());
		CmdC("Other", "Hat",new Commands.Other.Hat());
		CmdC("Other","exp", new Commands.Other.Exp());
		CmdC("Other","spawner", new Commands.Other.Spawner());
		
		//Nickname
		CmdC("Nickname","nick", new Commands.Nickname.Nick());
		CmdC("Nickname","nickreset", new Commands.Nickname.NickReset());
	}

	private void EventC(Listener l) {
		Bukkit.getPluginManager().registerEvents(l, this);
	}

	private void EventsRegister() {
		EventC(new DisableItems());
		EventC(new SecurityListenerAntiAD());
		EventC(new OnPlayerJoin());
		EventC(new SecurityListenerCooldowns());
		EventC(new ChatFormat());
		EventC(new RewardsListenerChat());
		EventC(new LoginEvent());
		EventC(new SecurityListenerV3());
		EventC(new DeathEvent());
		EventC(new AFkPlayerEvents());
		EventC(new WorldChange());
		EventC(new CreatePortal());
		EventC(new EntitySpawn());
		EventC(new Signs());
		EventC(new FarmingSystem());
		if (PluginManagerAPI.getPlugin("AFKPlus")!=null)
			EventC(new AFKPlus());
	}
	
	public static void notOnline(CommandSender s, String player) {
		sendMessages(s, "Missing.Player.Offline", Placeholder.c().add("%player%", player).add("%playername%", player));
	}
	
	public static void notExist(CommandSender s, String player) {
		sendMessages(s, "Missing.Player.NotExist", Placeholder.c().add("%player%", player).add("%playername%", player));
	}

	public static boolean has(CommandSender s, String cmd, String section) {
		return cmds.exists(section+"."+cmd+".Permission")?s.hasPermission(cmds.getString(section+"."+cmd+".Permission")):true;
	}

	public static void noPerms(CommandSender s, String cmd, String section) {
		sendMessages(s, "NoPerms", Placeholder.c().add("%permission%", cmds.getString(section+"."+cmd+".Permission")));
	}

	public static void noPerms(CommandSender s, String cmd, String section, String sub) {
		sendMessages(s, "NoPerms", Placeholder.c().add("%permission%", cmds.getString(section+"."+cmd+".SubPermissions."+sub)));
	}

	public static boolean has(CommandSender s, String cmd, String section, String subPerm) {
		return cmds.exists(section+"."+cmd+".SubPermissions."+subPerm)?s.hasPermission(cmds.getString(section+"."+cmd+".SubPermissions."+subPerm)):true;
	}

	public HashMap<String, Kit> kits = new HashMap<>();
	public static Kit getKit(String kitName) {
		return getInstance.kits.getOrDefault(kitName.toLowerCase(), null);
	}

	public static boolean hasKits(CommandSender p, String name) {
		return kit.exists("Kit."+name+".Permission")?p.hasPermission(kit.getString("Kit."+name+".Permission")):true;
	}
}
package ServerControl;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
import Utils.AFK;
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
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.NMS.NMSAPI;
import me.DevTec.TheAPI.Utils.TheAPIUtils.LoaderClass;
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
				if (API.existVaultPlugin())
					if (vault != null) {
						if (Loader.config.getString("Chat-Groups." + Loader.vault.getPrimaryGroup(p)) != null)
							return vault.getPrimaryGroup(p);
					}
				return "DefaultFormat";
			} catch (Exception e) {
				return "DefaultFormat";
			}
		case PREFIX:
			if (API.existVaultPlugin() && vault != null) {
				if (getGroup(p) != null && vault.getGroupPrefix(p.getWorld().getName(), getGroup(p)) != null)
					return vault.getGroupPrefix(p.getWorld().getName(), getGroup(p));
				return "";
			}
			return "";
		case SUFFIX:
			if (API.existVaultPlugin() && vault != null) {
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
			if (API.existVaultPlugin()) {
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
	private static boolean disabling;
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
		if(disabling) {
			TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "&4ERROR: &7You are using old version of TheAPI", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "&4ERROR: &7Please update TheAPI to newest version!", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "&4ERROR: &7Links:", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "&4ERROR: &7 Discord: &ehttps://discord.io/spigotdevtec", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "&4ERROR: &7 Github: &ehttps://github.com/TheDevTec/TheAPI", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "&4ERROR: &7 Spigot: &ehttps://www.spigotmc.org/resources/theapi.72679/", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		reload();
		TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + "&eINFO: &7Newest versions of &eTheAPI &7can be found on Spigot:", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + "       https://www.spigotmc.org/resources/theapi.72679/", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
		Tasks();
	}

	private static Metrics metrics;
	public void Tasks() {
		EventsRegister();
		CommmandsRegister();
	}

	@Override
	public void onDisable() {
		if(disabling)return;
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
		int count = 0;
		for(String s : LoaderClass.plugin.getDescription().getVersion().split("\\."))
			count+=Integer.valueOf(s);
		if(count < 9) {
			disabling = true;
			return;
		}
		MultiWorldsUtils.LoadWorlds();
		if (API.existVaultPlugin()) {
			setupEco();
			setupVault();
			setupPermisions();
			if (vault == null) {
				TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
				TheAPI.msg(setting.prefix + "&eINFO: &7Missing Permissions plugin for Groups (TabList and ChatFormat).", TheAPI.getConsole());
				TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
			}
		} else {
			TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "&eINFO: &7Missing Vault plugin for Economy.", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
		}
		}
		TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
		TheAPI.msg(setting.prefix + "&7"+(aad == 0 ? "L" : "Rel")+"oading of ServerControlReloaded:", TheAPI.getConsole());
		if(aad==1) {
			if(metrics!=null) {
				Bukkit.getServicesManager().unregister(metrics);
				metrics.getTimer().cancel();
			}
			Tasks.reload();
			AFK.stop();
		}
		for (World wa : Bukkit.getWorlds())
			MultiWorldsUtils.DefaultSet(wa, Loader.mw.getString("WorldsSettings." + wa.getName() + ".Generator"));
		AFK.start();
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
		TheAPI.msg(setting.prefix + "&7"+(aad == 0 ? "L" : "Rel")+"oading configs..", TheAPI.getConsole());
		Configs.load();
		TheAPI.msg(setting.prefix + "&7Loading kits:", TheAPI.getConsole());
		for (String s : Loader.kit.getKeys("Kits")) {
			TheAPI.msg(setting.prefix + "  &e"+s+"&7:", TheAPI.getConsole());
			Kit.load(s);
			TheAPI.msg(setting.prefix + "    &7Cooldown: " + StringUtils.setTimeToString(getInstance.kits.get(s.toLowerCase()).getDelay()), TheAPI.getConsole());
			TheAPI.msg(setting.prefix + "    &7Cost: &e$" + API.setMoneyFormat(getInstance.kits.get(s.toLowerCase()).getCost(), false), TheAPI.getConsole());
		}
		setting.load();
		Converter.convert();
		TheAPI.msg(setting.prefix + "&7ServerControlReloaded "+(aad == 0 ? "l" : "rel")+"oading took "+(System.currentTimeMillis()-loading)+"ms", TheAPI.getConsole());
		aad=1;
		TheAPI.msg(setting.prefix + "&8*********************************************", TheAPI.getConsole());
	}

	private void CmdC(String s, CommandExecutor p) {
		getCommand(s).setExecutor(p);
	}

	private void CommmandsRegister() {
		CmdC("stop", new Commands.Server.Stop());
		CmdC("reload", new Commands.Server.Reload());
		CmdC("restart", new Commands.Server.Restart());
		CmdC("give", new Commands.Other.Give());
		CmdC("Kill", new Commands.Kill.Kill());
		CmdC("KillAll", new Commands.Kill.KillAll());
		CmdC("Butcher", new Commands.Other.Butcher());
		CmdC("jail", new Commands.BanSystem.Jail());
		CmdC("TempBanIP", new Commands.BanSystem.TempBanIP());
		CmdC("unjail", new Commands.BanSystem.UnJail());
		CmdC("setjail", new Commands.BanSystem.SetJail());
		CmdC("deljail", new Commands.BanSystem.DelJail());
		CmdC("Mem", new Commands.Info.RAM());
		CmdC("Chunks", new Commands.Info.Chunks());
		CmdC("ClearChat", new Commands.Message.ClearChat());
		CmdC("SCR", new Commands.Info.ServerControl());
		CmdC("Maintenance", new Commands.Info.Maintenance());
		CmdC("Clear", new Commands.Inventory.ClearInv());
		CmdC("God", new Commands.Other.God());
		CmdC("Heal", new Commands.Other.Heal());
		CmdC("Fly", new Commands.Other.Fly());
		CmdC("FlySpeed", new Commands.Speed.FlySpeed());
		CmdC("WalkSpeed", new Commands.Speed.WalkSpeed());
		CmdC("TPS", new Commands.Info.TPS());
		CmdC("AFK", new Commands.Other.AFK());
		CmdC("MultiWorlds", new Commands.Other.MultiWorlds());
		CmdC("TabList", new Commands.Other.Tab());
		CmdC("suicide", new Commands.Kill.Suicide());
		CmdC("SetSpawn", new Commands.Warps.SetSpawn());
		CmdC("Spawn", new Commands.Warps.Spawn());
		CmdC("SetWarp", new Commands.Warps.SetWarp());
		CmdC("DelWarp", new Commands.Warps.DelWarp());
		CmdC("Warps", new Commands.Warps.Warp());
		CmdC("Skull", new Commands.Other.Skull());
		CmdC("BalanceTop", new Commands.Economy.EcoTop());
		CmdC("Money", new Commands.Economy.Eco());
		CmdC("Pay", new Commands.Economy.Pay());
		CmdC("Home", new Commands.Warps.Home());
		CmdC("SetHome", new Commands.Warps.SetHome());
		CmdC("DelHome", new Commands.Warps.DelHome());
		CmdC("Homes", new Commands.Warps.Homes());
		CmdC("Back", new Commands.Warps.Back());
		CmdC("Return", new Commands.Warps.Back());
		CmdC("Vanish", new Commands.Other.Vanish());
		CmdC("Sun", new Commands.Weather.Sun());
		CmdC("Thunder", new Commands.Weather.Thunder());
		CmdC("Strorm", new Commands.Weather.Thunder());
		CmdC("Rain", new Commands.Weather.Rain());
		CmdC("Day", new Commands.Time.Day());
		CmdC("Night", new Commands.Time.Night());
		CmdC("Kit", new Commands.Other.KitCmd());
		CmdC("ChatLock", new Commands.Other.ChatLock());
		CmdC("GameMode", new Commands.Gamemode.Gamemode());
		CmdC("GMS", new Commands.Gamemode.GamemodeS());
		CmdC("GMC", new Commands.Gamemode.GamemodeC());
		CmdC("GMSP", new Commands.Gamemode.GamemodeSP());
		CmdC("hat", new Commands.Other.Hat());
		if(TheAPI.isNewerThan(7))
		CmdC("GMA", new Commands.Gamemode.GamemodeA());
		CmdC("pm", new Commands.Message.PrivateMessage());
		CmdC("helpop", new Commands.Message.Helpop());
		CmdC("reply", new Commands.Message.ReplyPrivateMes());
		CmdC("ClearConfirmToggle", new Commands.Inventory.ClearConfirmToggle());
		CmdC("Kick", new Commands.BanSystem.Kick());
		CmdC("Ban", new Commands.BanSystem.Ban());
		CmdC("Immune", new Commands.BanSystem.Immune());
		CmdC("TempBan", new Commands.BanSystem.TempBan());
		CmdC("ChatFormat", new Commands.Info.ChatFormat());
		CmdC("BanIP", new Commands.BanSystem.BanIP());
		CmdC("UnBan-IP", new Commands.BanSystem.UnBanIP());
		CmdC("UnBan", new Commands.BanSystem.UnBan());
		CmdC("tempmute", new Commands.BanSystem.TempMute());
		CmdC("mute", new Commands.BanSystem.Mute());
		CmdC("unmute", new Commands.BanSystem.UnMute());
		CmdC("warn", new Commands.BanSystem.Warn());
		CmdC("craft", new Commands.Other.Craft());
		CmdC("enderchest", new Commands.Inventory.EnderChest());
		CmdC("endersee", new Commands.Inventory.EnderSee());
		CmdC("Seen", new Commands.Info.Seen());
		CmdC("List", new Commands.Info.ListCmd());
		CmdC("Staff", new Commands.Info.Staff());
		CmdC("Trash", new Commands.Other.Trash());
		CmdC("Invsee", new Commands.Inventory.Invsee());
		CmdC("Enchant", new Commands.Enchantment.EnchantTable());
		CmdC("EnchantRemove", new Commands.Enchantment.EnchantTableRemove());
		CmdC("EnchantRemoveAll", new Commands.Enchantment.EnchantTableRemoveAll());
		CmdC("broadcast", new Commands.Message.Broadcast());
		CmdC("multieconomy", new Commands.Economy.MultiEconomy());
		CmdC("tp", new Commands.Tpa.Tp());
		CmdC("tphere", new Commands.Tpa.Tphere());
		CmdC("tpa", new Commands.Tpa.Tpa());
		CmdC("tpahere", new Commands.Tpa.Tpahere());
		CmdC("tpblock", new Commands.Tpa.TpaBlock());
		CmdC("tpaall", new Commands.Tpa.Tpaall());
		CmdC("tpall", new Commands.Tpa.Tpall());
		CmdC("tpaccept", new Commands.Tpa.Tpaccept());
		CmdC("tpadeny", new Commands.Tpa.Tpadeny());
		CmdC("Repair", new Commands.Other.Repair());
		CmdC("Feed", new Commands.Other.Feed());
		CmdC("item", new Commands.Other.Item());
		CmdC("board", new Commands.Other.ScoreboardStats());
		CmdC("thor", new Commands.Other.Thor());
		CmdC("spawner", new Commands.Other.Spawner());
		CmdC("sudo", new Commands.Message.Sudo());
		CmdC("exp", new Commands.Other.Exp());
		CmdC("nick", new Commands.Nickname.Nick());
		CmdC("nickreset", new Commands.Nickname.NickReset());
		CmdC("WhoIs", new Commands.Info.WhoIs());
		CmdC("closeinv", new Commands.Inventory.CloseInventory());
		CmdC("homeother", new Commands.Warps.HomeOther());
		CmdC("tempfly", new Commands.Other.TempFly());
		CmdC("tpcancel", new Commands.Tpa.Tpcancel());
		CmdC("mail", new Commands.Message.Mail());

		CmdC("balance", new Commands.Economy.Balance());
		CmdC("tempjail", new Commands.BanSystem.TempJail());

		CmdC("psun", new Commands.Weather.PSun());
		CmdC("prain", new Commands.Weather.PRain());
		CmdC("pday", new Commands.Time.PDay());
		CmdC("pnight", new Commands.Time.PNight());
	}

	private void EventC(Listener l) {
		getServer().getPluginManager().registerEvents(l, this);
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
		EventC(new Signs());
		EventC(new FarmingSystem());
		EventC(new EntitySpawn());
		try {
			if (PluginManagerAPI.isEnabledPlugin("AFKPlus"))
				EventC(new AFKPlus());
		} catch (Exception e) {
		}
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
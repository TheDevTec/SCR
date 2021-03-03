package me.DevTec.ServerControlReloaded.SCR;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.DevTec.ServerControlReloaded.Commands.CommandsManager;
import me.DevTec.ServerControlReloaded.Commands.Other.Trash;
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
import me.devtec.theapi.utils.listener.Listener;
import me.devtec.theapi.utils.reflections.Ref;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Loader extends JavaPlugin implements Listener {
	public static Config config, plac, sb, tab, mw, kit, trans, events, cmds, anim, ac, bb;
	public static List<Rule> rules = new ArrayList<>();
	private int task;
	private long time, rkick;
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
	
	public static void sendMessages(CommandSender to, String path, Placeholder placeholders) {
		Object o = getTranslation(path);
		if(!existsTranslation(path)) {
			Bukkit.getLogger().severe("[BUG] Missing configuration path [Translations]!");
			Bukkit.getLogger().severe("[BUG] Report this to the DevTec discord:");
			Bukkit.getLogger().severe("[BUG] Missing path: "+path);
			return;
		}
		if(o==null)return;
		if(o instanceof Collection) {
			for(Object d : (Collection<?>)o)
				TheAPI.msg(placeholder(to, d+"", placeholders), to);
		}else
			if(!(o+"").isEmpty())
		TheAPI.msg(placeholder(to, o+"", placeholders), to);
	}
	
	public static void sendMessages(CommandSender to, CommandSender replace, String path, Placeholder placeholders) {
		Object o = getTranslation(path);
		if(!existsTranslation(path)) {
			Bukkit.getLogger().severe("[BUG] Missing configuration path [Translations]!");
			Bukkit.getLogger().severe("[BUG] Report this to the DevTec discord:");
			Bukkit.getLogger().severe("[BUG] Missing path: "+path);
			return;
		}
		if(o==null)return;
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
	public static String getChatFormat(Player p, Item type) {
		switch(type) {
		case GROUP:
			String group = API.getGroup(p);
			if (Loader.config.exists("Chat-Groups." + group))
				return group;
			return "default";
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
		CommandsManager.unload();
		org.bukkit.event.HandlerList.unregisterAll(this);
		for (Player p : TheAPI.getOnlinePlayers()) {
			p.setFlying(false);
			p.setAllowFlight(false);
			p.setDisplayName(null);
			p.setCustomName(null);
		}
		TabList.removeTab();
		Tasks.unload();
		stop();
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
				public void onClick(Player s, GUI g, me.devtec.theapi.guiapi.GUI.ClickType c) {
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
								for(Player canSee : API.getPlayers(p))
									Loader.sendMessages(canSee, p, "AFK.Start");
							}
							if (setting.afk_kick) {
								if (s.kick >= rkick) {
									if (!p.hasPermission(getPerm("AFK", "Other", "Bypass"))) {
										Ref.invoke(Ref.playerCon(p), "disconnect", TheAPI.colorize(Loader.config.getString("Options.AFK.KickMessage")));
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
		for(Player canSee : API.getPlayers(s.getPlayer()))
			Loader.sendMessages(canSee, player, "AFK.Start");
	}
	public void setAFK(SPlayer s, String reason) {
		Player player = s.getPlayer();
		save(player);
		s.bc = true;
		s.manual = true;
		for(Player canSee : API.getPlayers(player))
			Loader.sendMessages(canSee, player, "AFK.Start_WithReason", Placeholder.c().add("%reason%", reason));
	}
	
	public long getTime(SPlayer s) {
		return time - s.afk;
	}

	public void save(Player d) {
		moving.put(d.getName(), d.getLocation());
		SPlayer s = API.getSPlayer(d);
		if(isAFK(s) || isManualAfk(s)) {
			for(Player canSee : API.getPlayers(d))
				Loader.sendMessages(canSee, d, "AFK.End");
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
package ServerControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import com.earth2me.essentials.Essentials;

import Commands.BanSystem.BanSystem;
import Commands.Tpa.RequestMap;
import me.Straiker123.TheAPI;
import Utils.AFK;
import Utils.Configs;
import Utils.Metrics;
import Utils.MultiWorldsUtils;
import Utils.ScoreboardStats;
import Utils.TabList;
import Utils.VaultHook;
import Utils.gui;
import net.lapismc.afkplus.playerdata.AFKPlusPlayer;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
 
public class Loader extends JavaPlugin implements Listener {
	public static HashMap<String, Integer> tempfly = new HashMap<String, Integer>();
    public static FileConfiguration TranslationsFile;
    public static FileConfiguration chatLogFile;
    public static FileConfiguration mw;
    public static FileConfiguration ban;
    public static FileConfiguration scFile;
    public static FileConfiguration config;
    public static FileConfiguration me;
    public static FileConfiguration tab;
    public static FileConfiguration kit;
	public static Economy econ;
    public static Loader getInstance;
    
    public String getPrefix(Player p) {
    	if(API.existVaultPlugin()) {
    	if(vault!=null&&getGroup(p)!=null)return vault.getGroupPrefix(p.getWorld(), getGroup(p));
    	}
    	return "";
    }
    
    public static String FormatgetGroup(Player p) {
    	try {
    		if(API.existVaultPlugin())
		if(vault!=null) {
			if(Loader.config.getString("Chat-Groups."+Loader.vault.getPrimaryGroup(p))!=null)
			return vault.getPrimaryGroup(p);
		}
		return "DefaultFormat";
    	}catch(Exception e) {
    		return "DefaultFormat";
    	}
	}
    public static void setupChatFormat(Player p) {
    	if(config.getString("Chat-Groups."+FormatgetGroup(p)+".Name")!=null) {
			String g = TheAPI.colorize(TheAPI.getPlaceholderAPI().setPlaceholders(p,config.getString("Chat-Groups."+FormatgetGroup(p)+".Name")));
			g=g.replace("%", "%%");
			g=Events.ChatFormat.r(p,g, null,false);
			g=g.replace("%%", "%");
			API.setDisplayName(p, g);
		}else
	 		API.setDisplayName(p,Loader.getInstance.getPrefix(p)+p.getName()+Loader.getInstance.getSuffix(p));
    }
    public static List<Plugin> addons = new ArrayList<Plugin>();
    public String getSuffix(Player p) {
    	if(API.existVaultPlugin()) {
    	if(vault!=null) {
    		if(getGroup(p)!=null)return vault.getGroupSuffix(p.getWorld(), getGroup(p));
    	}}
    	return "";
    }
    public static String PlayerNotEx(String s) {
    	return Loader.s("Prefix")+Loader.s("PlayerNotExists").replace("%player%", s).replace("%playername%", s);
    }
    public static String PlayerNotOnline(String s) {
    	return Loader.s("Prefix")+Loader.s("PlayerNotOnline").replace("%player%", s).replace("%playername%", s);
    }
	@SuppressWarnings("deprecation")
	public String getGroup(Player p) {
		try {
		if(API.existVaultPlugin()) {
			if(Loader.vault!=null && Loader.vault.getPrimaryGroup(p)!=null)
			return Loader.vault.getPrimaryGroup(p.getWorld().getName(),p.getName());
			return "";
		}
		}catch(Exception e) {
			return "";
		}
		return "";
	}
	public static void Help(CommandSender s, String cmd, String help) {
	    s.sendMessage(TheAPI.colorize(config.getString("HelpFormat")
	    		.replace("%prefix%", Loader.s("Prefix"))
	    		.replace("%command%", cmd).replace("%space%", " - ")
	    		.replace("%help%", Loader.s("Help."+help))));
		}
	public static void startConvertMoney() {
		if(config.getBoolean("MultiEconomy.Enabled")&&!config.getBoolean("MultiEconomy.Converted")) {
		int amount = 0;
		Loader.getInstance.EconomyLog("Converting economies..");
		config.set("MultiEconomy.Converted", true);
		if(me.getString("Players")!=null) {
		for(String s:me.getConfigurationSection("Players").getKeys(false)) {
			if(me.getString("Players."+s+".Money")!=null) {
				amount=amount+1;
			double money =me.getDouble("Players."+s+".Money");
			me.set("Players."+s+".Money.default",money);
			}}}
		 Configs.chatme.save();
		 Configs.config.save();
			Loader.getInstance.EconomyLog("Converted "+amount+" economies.");
		}}
public String isAfk(Player p) {
	if(API.getPlugin("AFKPlus")) {
		AFKPlusPlayer afkplus = (AFKPlusPlayer)Bukkit.getServer().getPluginManager().getPlugin("AFKPlus");
	if(afkplus.isAFK()||AFK.isAFK(p))return tab.getString("AFK.IsAFK");
	return tab.getString("AFK.IsNotAFK");
}
	if(API.getPlugin("Essentials")) {
		Essentials ess = (com.earth2me.essentials.Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		if (ess.getUser(p).isAfk()||AFK.isAFK(p))return tab.getString("AFK.IsAFK");
		return tab.getString("AFK.IsNotAFK");
	}
	if(!API.getPlugin("Essentials")&&!API.getPlugin("AFKPlus")) {
		if(AFK.isAFK(p))return tab.getString("AFK.IsAFK");
	return tab.getString("AFK.IsNotAFK");
	}
	return tab.getString("AFK.IsNotAFK");
}
private String getColoredPing(Player p) {
	int s = TheAPI.getPlayerPing(p);
	if(s >= 500)return TheAPI.colorize("&c"+s);
	if(s >= 200 && s < 500)return TheAPI.colorize("&e"+s);
	if(s >= 0 && s < 200)return TheAPI.colorize("&a"+s);
	return TheAPI.colorize("&4"+s);
}
public String pingPlayer(Player who) {
	if(tab.getBoolean("Colored-Ping")==true)return getColoredPing(who);
	return String.valueOf(TheAPI.getPlayerPing(who));
}
@Override
public void onLoad() {
	Configs.LoadConfigs();
	if(ver() == null) {
		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&4ERROR: You are using an unsupported version of the plugin for this server."));
		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&4ERROR: Disabling plugin..."));
		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
		Bukkit.getPluginManager().disablePlugin(this);
		}
	regClasses();
    getInstance = this;
	if(API.existVaultPlugin()) {
	setupEco();
    }else {
    	TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
    	TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&6INFO: Missing Vault plugin for Economy."));
    	TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
    }
	}
private void regClasses() {
	try {
		new API();
		new MultiWorldsUtils();
		new BanSystem();
		new RequestMap();
		new Utils.AFK();
		new gui();
		new ScoreboardStats();
	
	}catch(NoClassDefFoundError | Exception e) {
		Bukkit.getLogger().severe(TheAPI.colorize(Loader.s("Prefix")+"&cError when loading classes"));
	}
}
long loading;
@Override
public void onEnable() {
	loading=System.currentTimeMillis()/100;
	Configs.LoadConfigs();
	
		if(API.existVaultPlugin()) {
    	setupVault();
    	setupPermisions();
		}
        try {
		if(config.getBoolean("TimeZone-Enabled")) {
			TimeZone.setDefault(TimeZone.getTimeZone(config.getString("TimeZone")));
		}}catch(Exception e) {
			TheAPI.getConsole().sendMessage(TheAPI.colorize("&6Invalid time zone: &c"+config.getString("TimeZone")));
			TheAPI.getConsole().sendMessage(TheAPI.colorize("&6List of available time zones:"));
			TheAPI.getConsole().sendMessage(TheAPI.colorize(" &6https://greenwichmeantime.com/time-zone/"));
	        }
		if(econ!=null&& API.existVaultPlugin()) {
	    	startConvertMoney();
		}
        if(vault==null && API.existVaultPlugin()) {
        	TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
        	TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&6INFO: Missing Permissions plugin for Groups (TabList and ChatFormat)."));
    		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
        }
			if(API.getPlugin("PlaceholderAPI")) {
				TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
				TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&aINFO: Hooked PlaceholderAPI plugin."));
				TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
			}
	    MultiWorldsUtils.LoadWorlds();
		APIChecker();
		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&6INFO: Newest versions of TheAPI can be found on Spigot:"));
		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&6       https://www.spigotmc.org/resources/theapi.72679/"));
		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
    	Tasks();
    }

public void afk(Player p, boolean afk) {
		      if(afk) {
		 TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("AFK.NoLongerAFK").replace("%player%", p.getName())
				   .replace("%playername%", p.getDisplayName()));
		      } else {
		 TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("AFK.IsAFK").replace("%player%", p.getName())
			       .replace("%playername%", p.getDisplayName()));
}}
public void Tasks() {
    EventsRegister();
    CommmandsRegister();
     for(World wa: Bukkit.getWorlds()) {
		MultiWorldsUtils.DefaultSet(wa);
	}
	if(mw.getInt("SavingTask.Delay") <= 600) {
		mw.set("SavingTask.Delay", 3600);
		 Configs.mw.save();
	}
	for(Player p:Bukkit.getOnlinePlayers()) {
		SPlayer s = new SPlayer(p);
		if(s.hasPermission("servercontrol.fly"))
		s.enableFly();
		setupChatFormat(p);
 		if(econ != null)
 		if(Loader.econ.createPlayerAccount(p))
 			EconomyLog("Creating economy account for player "+p.getName());
	}
	Utils.Tasks.load();
  	MultiWorldsUtils.EnableWorldCheck();
	new Metrics(this);
	msg(Loader.s("Prefix")+"&aPlugin loaded in "+(loading - System.currentTimeMillis()/100)*-1+"ms",Bukkit.getConsoleSender());
	loading=0;
}
@Override
public void onDisable() {
	for(Player p : Bukkit.getOnlinePlayers()) {
		p.setFlying(false);
		p.setAllowFlight(false);
	}
	TabList.removeTab();
	ScoreboardStats.removeScoreboard();
	for(Team t:Bukkit.getScoreboardManager().getMainScoreboard().getTeams())t.unregister();
	if(mw.getString("Worlds")!=null)
	 for(String w: mw.getStringList("Worlds")) {
			if(Bukkit.getWorld(w) != null && !mw.getBoolean("WorldsSettings."+w+".AutoSave")) {
				Loader.info("Saving world '"+w+"'");
				Bukkit.getWorld(w).save();
				Loader.info("World '"+w+"' saved");
			}}
	Configs.LoadConfigs();
}
public static void msg(String message, CommandSender s){
s.sendMessage(TheAPI.colorize(message));
}
public boolean version(String version){
	return Bukkit.getBukkitVersion().contains(version);
}
public static void warn(String s) {
	Bukkit.getLogger().warning(TheAPI.colorize(s));
}
public static void info(String s) {
	Bukkit.getLogger().info(TheAPI.colorize(s));
}
public static Chat vault = null;
private boolean setupVault(){
    RegisteredServiceProvider<Chat> economyProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
    if (economyProvider != null) {
    	vault = economyProvider.getProvider();
    }

    return (vault != null);
}
private boolean setupCustomEco(){
	new VaultHook().hook();
	RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
    if (economyProvider != null) {
    	econ = economyProvider.getProvider();
    	
    }
	return econ !=null;
	
}
public void EconomyLog(String s) {
	info("[Economy] "+s);
}
private boolean setupEco(){
	try {
    RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
    if (economyProvider != null) {
    	if(config.getBoolean("Economy.UseOnlyPluginEco")==true) {
    		EconomyLog("Found economy '"+economyProvider.getProvider().getName()+"', using setting and loading plugin economy.");
        	setupCustomEco();
    	}else {
    		EconomyLog("Found economy '"+economyProvider.getProvider().getName()+"', skipping plugin economy.");
    	econ = economyProvider.getProvider();
    	}
    }
    if(econ ==null) {
    	EconomyLog("Plugin not found any economy, loading plugin economy.");
    	setupCustomEco();
    }
	}catch(Exception e) {
    	EconomyLog("Error when hooking economy.");
	}
    return (econ != null);
}
public static Permission perms = null;
private boolean setupPermisions(){
    RegisteredServiceProvider<Permission> economyProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
    if (economyProvider != null) {
    	perms = economyProvider.getProvider();
    }
    return (perms != null);
}
  public static String s(String s) {
		if(TranslationsFile.getString(s)!=null)
			return TranslationsFile.getString(s);
			else {
		warn("String '"+s+"' isn't exist in Tranlations.yml, please report this bug on https://github.com/Straiker123/ServerControlReloaded");
		return "&4ERROR, Missing path, See console for more informations";}
	}
public String ver() {
	String v = null;
if(version("1.8.8")) {
	v = "1.8.8";
}
if(version("1.9")) {
	v = "1.9+";
}
if(version("1.10")) {
	v = "1.10+";
}
if(version("1.11")) {
	v = "1.11+";
}
if(version("1.12")) {
	v = "1.12+";
}
if(version("1.13")) {
	v = "1.13+";
}
if(version("1.14")) {
	v = "1.14+";
}
if(version("1.15")) {
	v = "1.15+";
}
if(version("1.16")) {
	v = "1.16+";
}
return v;
}
public void APIChecker() {
	TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
	TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&2INFO: You are using version for servers version "+ver()));
	if(Loader.kit.getString("Kits")!=null) {
	TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&2INFO: Loading kits.."));
	for(String s:Loader.kit.getConfigurationSection("Kits").getKeys(false)) {
		TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&2Kits: Name: "+s+", Cooldown: "+TheAPI.getTimeConventorAPI().setTimeToString(TheAPI.getTimeConventorAPI().getTimeFromString(Loader.kit.getString("Kits."+s+".Cooldown")))+", Price: $"+API.setMoneyFormat(kit.getDouble("Kits."+s+".Price"),false)));
	}}
	TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&8*********************************************"));
    SoundsChecker();
	}

private void CmdC(String s, CommandExecutor p) {
	getCommand(s).setExecutor(p);
}
private void CommmandsRegister() {
	CmdC("addons", new Commands.Addons());
	CmdC("give", new Commands.Give());
	CmdC("Kill", new Commands.Kill());
	CmdC("KillAll", new Commands.KillAll());
	CmdC("Butcher", new Commands.Butcher());
	CmdC("jail", new Commands.BanSystem.Jail());
	CmdC("TempBanIP", new Commands.BanSystem.TempBanIP());
	CmdC("unjail", new Commands.BanSystem.UnJail()); 
	CmdC("setjail", new Commands.BanSystem.SetJail());
	CmdC("deljail", new Commands.BanSystem.DelJail());
	CmdC("unwarn", new Commands.BanSystem.UnWarn());
	CmdC("Mem", new Commands.RAM());
	CmdC("Chunks", new Commands.Chunks());
	CmdC("ClearChat", new Commands.ClearChat());
	CmdC("SCR", new Commands.ServerControl());
	CmdC("Chat", new Commands.Chat());
	CmdC("WordAdd", new Commands.WordAdd());
	CmdC("WordDel", new Commands.WordDel());
	CmdC("Maintenance", new Commands.Maintenance());
	CmdC("Clear", new Commands.ClearInv());
	CmdC("God", new Commands.God());
	CmdC("Heal", new Commands.Heal());
	CmdC("Fly", new Commands.Fly());
	CmdC("FlySpeed", new Commands.FlySpeed());
	CmdC("WalkSpeed", new Commands.WalkSpeed());
	CmdC("TPS", new Commands.TPS());
	CmdC("AFK", new Commands.AFK());
	CmdC("MultiWorlds", new Commands.Worlds());
	CmdC("TabList", new Commands.Tab());
	CmdC("suicide", new Commands.Suicide());
	CmdC("SetSpawn", new Commands.SetSpawn());
	CmdC("Spawn", new Commands.Spawn());
	CmdC("SetWarp", new Commands.SetWarp());
	CmdC("DelWarp", new Commands.DelWarp());
	CmdC("Warps", new Commands.Warp());
	CmdC("Skull", new Commands.Skull());
	CmdC("BalanceTop", new Commands.Economy.EcoTop());
	CmdC("Money", new Commands.Economy.Eco());
	CmdC("Pay", new Commands.Economy.Pay());
	CmdC("Home", new Commands.Home());
	CmdC("SetHome", new Commands.SetHome());
	CmdC("DelHome", new Commands.DelHome());
	CmdC("Homes", new Commands.Homes());
	CmdC("Back", new Commands.Back());
	CmdC("Return", new Commands.Back());
	CmdC("Vanish", new Commands.Vanish());
	CmdC("Sun", new Commands.Sun());
	CmdC("Thunder", new Commands.Thunder());
	CmdC("Strorm", new Commands.Thunder());
	CmdC("Rain", new Commands.Rain());
	CmdC("Day", new Commands.Day());
	CmdC("Night", new Commands.Night());
	CmdC("Kit", new Commands.Kit());
	CmdC("ChatLock", new Commands.ChatLock());
	CmdC("GameMode", new Commands.Gamemode());
	CmdC("GMS", new Commands.GamemodeS());
	CmdC("GMC", new Commands.GamemodeC());
	CmdC("GMSP", new Commands.GamemodeSP());
	CmdC("hat", new Commands.Hat());
	CmdC("GMA", new Commands.GamemodeA());
	CmdC("pm", new Commands.PrivateMessage());
	CmdC("helpop", new Commands.Helpop());
	CmdC("reply", new Commands.ReplyPrivateMes());
	CmdC("ClearConfirmToggle", new Commands.ClearConfirmToggle());
	CmdC("Kick", new Commands.BanSystem.Kick());
	CmdC("Ban", new Commands.BanSystem.Ban());
	CmdC("Immune", new Commands.BanSystem.Immune());
	CmdC("TempBan", new Commands.BanSystem.TempBan());
	CmdC("ChatFormat", new Commands.ChatFormat());
	CmdC("BanIP", new Commands.BanSystem.BanIP());
	CmdC("UnBan-IP", new Commands.BanSystem.UnBanIP());
	CmdC("UnBan", new Commands.BanSystem.UnBan());
	CmdC("tempmute", new Commands.BanSystem.TempMute());
	CmdC("mute", new Commands.BanSystem.Mute());
	CmdC("unmute", new Commands.BanSystem.UnMute());
	CmdC("warn", new Commands.BanSystem.Warn());
	CmdC("craft", new Commands.Craft());
	CmdC("enderchest", new Commands.EnderChest());
	CmdC("endersee", new Commands.EnderSee());
	CmdC("Seen", new Commands.Seen());
	CmdC("List", new Commands.List());
	CmdC("Staff", new Commands.Staff());
	CmdC("Trash", new Commands.Trash());
	CmdC("Invsee", new Commands.Invsee());
	CmdC("Enchant", new Commands.EnchantTable());
	CmdC("EnchantRemove", new Commands.EnchantTableRemove());
	CmdC("EnchantRemoveAll", new Commands.EnchantTableRemoveAll());
	CmdC("broadcast", new Commands.Broadcast());
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
	CmdC("Repair", new Commands.Repair());
	CmdC("Feed", new Commands.Feed());
	CmdC("item", new Commands.Item());
	CmdC("board", new Commands.ScoreboardStats());
	CmdC("thor", new Commands.Thor());
	CmdC("spawner", new Commands.Spawner());
	CmdC("sudo", new Commands.Sudo());
	CmdC("exp", new Commands.Exp());
	CmdC("nick", new Commands.Nick());
	CmdC("nickreset", new Commands.NickReset());
	CmdC("WhoIs", new Commands.WhoIs());
	CmdC("closeinv", new Commands.CloseInventory());
	CmdC("closeinv", new Commands.CloseInventory());
	CmdC("closeinventory", new Commands.CloseInventory());
	CmdC("closeopeninv", new Commands.CloseInventory());
	CmdC("closeopeninventory", new Commands.CloseInventory());
	CmdC("homeother", new Commands.HomeOther());
	
	CmdC("tempfly", new Commands.TempFly());
}
private void EventC(Listener l) {
	getServer().getPluginManager().registerEvents(l, this);
}
private void EventsRegister() {
	EventC(new Events.DisableItems());
	EventC(new Events.SecurityListenerAntiAD());
	EventC(new Events.OnPlayerJoin());
	EventC(new Events.OnPlayerLeave());
	EventC(new Events.SecurityListenerCooldowns());
	EventC(new Events.ChatFormat());
	EventC(new Events.RewardsListenerChat());
	EventC(new Events.LoginEvent());
	EventC(new Events.NewSecurityListener());
	EventC(new Events.DeathEvent());
	EventC(new Events.AFkPlayerEvents());
	EventC(new Events.WorldChange());
	EventC(new Events.CreatePortal());
	EventC(new Events.Signs());
	EventC(new Events.foodLevel());
	EventC(new Events.EntitySpawn());
}
public void SoundsChecker() {
	if(!config.getString("Sounds").equalsIgnoreCase("1")&&!config.getString("Sounds").equalsIgnoreCase("2")
			&&!config.getString("Sounds").equalsIgnoreCase("3")&&!config.getString("Sounds").equalsIgnoreCase("4")
			&&!config.getString("Sounds").equalsIgnoreCase("5")&&!config.getString("Sounds").equalsIgnoreCase("false")){
		msg("",TheAPI.getConsole());
		msg("",TheAPI.getConsole());
		msg(Loader.s("Prefix")+Loader.s("SoundErrorMessage"),TheAPI.getConsole());
		msg("",TheAPI.getConsole());
		msg("",TheAPI.getConsole());
		}}
}
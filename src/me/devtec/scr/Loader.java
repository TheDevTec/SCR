package me.devtec.scr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import me.devtec.scr.commands.CommandsManager;
import me.devtec.scr.modules.ActionBar;
import me.devtec.scr.modules.BossBar;
import me.devtec.scr.modules.Module;
import me.devtec.scr.modules.SEconomy;
import me.devtec.scr.modules.Scoreboard;
import me.devtec.scr.modules.Tablist;
import me.devtec.scr.modules.events.Listeners;
import me.devtec.scr.punishment.SPunishmentAPI;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.VersionChecker;
import me.devtec.theapi.utils.theapiutils.LoaderClass;
import net.milkbowl.vault.permission.Permission;

public class Loader extends JavaPlugin {
	public static Config config, defaultTranslation, translation;
	public static List<String> positive = new ArrayList<>(), negative = new ArrayList<>();
	public static List<Module> modules = new ArrayList<>();
	public static Loader plugin;
	public static Permission perms;
	public static SEconomy economy;
	
	public void onLoad() {
		//Latest TheAPI only.
		if(VersionChecker.getVersion(LoaderClass.plugin.getDescription().getVersion(), "8.2")==VersionChecker.Version.NEW) {
			TheAPI.msg("&8*********************************************", TheAPI.getConsole());
			TheAPI.msg("&4SECURITY: &cYou are running on outdated version of plugin TheAPI", TheAPI.getConsole());
			TheAPI.msg("&4SECURITY: &cPlease update plugin TheAPI to latest version.", TheAPI.getConsole());
			TheAPI.msg("      &6https://www.spigotmc.org/resources/72679/", TheAPI.getConsole());
			TheAPI.msg("&8*********************************************", TheAPI.getConsole());
			setNaggable(true);
			return;
		}
		plugin=this;
		ConfigManager.load();
		if(Bukkit.getPluginManager().getPlugin("Vault")!=null) {
			economy = new SEconomy();
			Bukkit.getServicesManager().register(net.milkbowl.vault.economy.Economy.class, economy, this, ServicePriority.Normal);
			vaultHooking();
			
		}
		TheAPI.setPunishmentAPI(new SPunishmentAPI());
	}
	
	public void onEnable() {
		if(isNaggable())
			return;
		if(config.getBoolean("modules.commands"))
			modules.add(new CommandsManager().load());
		if(config.getBoolean("modules.listeners"))
			modules.add(new Listeners().load());
		
		//LOADING OF MODULES
		if(config.getBoolean("modules.tablist"))
			modules.add(new Tablist().load());
		if(config.getBoolean("modules.scoreboard"))
			modules.add(new Scoreboard().load());
		if(config.getBoolean("modules.bossbar"))
			modules.add(new BossBar().load());
		if(config.getBoolean("modules.actionbar"))
			modules.add(new ActionBar().load());
		
	}
	
	public void vaultHooking() {
		TheAPI.msg("&4SCR&7: &8********************", TheAPI.getConsole());
		TheAPI.msg("&4SCR&7: &eAction: &fLooking for Vault Permission plugin..", TheAPI.getConsole());
		TheAPI.msg("&4SCR&7: &8********************", TheAPI.getConsole());
		new Tasker() {
			@Override
			public void run() {
				if (getVaultPerms()) {
					TheAPI.msg("&4SCR&7: &8********************", TheAPI.getConsole());
					TheAPI.msg("&4SCR&7: &eFound Vault Permission plugin ("+perms.getName()+")", TheAPI.getConsole());
					TheAPI.msg("&4SCR&7: &8********************", TheAPI.getConsole());
					cancel();
				}
			}
		}.runTimer(0, 20, 15);
	}

	private boolean getVaultPerms() {
		try {
			RegisteredServiceProvider<Permission> provider = Bukkit.getServicesManager().getRegistration(Permission.class);
			if (provider != null)
				perms = provider.getProvider();
			return perms != null;
		} catch (Exception e) {
			return false;
		}
	}

	public void onDisable() {
		if(isNaggable())
			return;
		modules.forEach(module -> module.unload());
		modules.clear();
		perms=null;
		if (economy != null) {
			Bukkit.getServicesManager().unregister(net.milkbowl.vault.economy.Economy.class, economy);
			economy=null;
		}
		ConfigManager.unload();
	}
	
	public static boolean isPositive(String value) {
		return positive.contains(value.toLowerCase());
	}
	
	public static boolean isNegative(String value) {
		return negative.contains(value.toLowerCase());
	}
	
	public static boolean isArmor(Material item) {
		String name = item.name();
		return name.endsWith("_HELMET")||name.endsWith("_BOOTS")||
		name.endsWith("_LEGGINGS")||name.endsWith("_CHESTPLATE")||name.equals("ELYTRA");
	}
	
	public static boolean isTool(Material item) {
		String name = item.name();
		return name.endsWith("_PICKAXE")||name.endsWith("_AXE")||name.endsWith("_SPADE")||name.endsWith("_SHOVEL")
				||name.endsWith("_HOE")||name.endsWith("_ON_A_STICK")||name.endsWith("_SWORD")||name.equals("BOW")
		||name.equals("SHEARS")||name.equals("FLINT_AND_STEEL")||name.equals("TRIDENT")
		||name.equals("CROSSBOW")||name.equals("SHIELD")||name.equals("FISHING_ROD");
	}
	
	public static void send(CommandSender sender, String transPath, PlaceholderBuilder builder) {
		Object trans = translation.get(transPath);
		JsonUtils.msgRaw(trans==null?defaultTranslation.get(transPath):trans, trans==null?defaultTranslation.isJson(transPath):translation.isJson(transPath), builder, sender);
	}

	public static List<Player> onlinePlayers(CommandSender sender){
		List<Player> players = TheAPI.getOnlinePlayers();
		if(sender instanceof Player) {
			Iterator<Player> iter = players.iterator();
			while(iter.hasNext()) {
				Player player = iter.next();
				if(sender==player)continue;
				if(!((Player)sender).canSee(player))
					iter.remove();
			}
		}
		return players;
	}
	
	public static List<String> onlinePlayerNames(CommandSender sender){
		List<Player> players = onlinePlayers(sender);
		List<String> playerNames = new ArrayList<>(players.size());
		for(Player player : players)playerNames.add(player.getName());
		return playerNames;
	}

	//TPA & TPAHERE
	public static long getRequestTime() {
		return 15;
	}
	
	private static final Pattern moneyPattern = Pattern.compile("([+-]*[0-9]+.*[0-9]*[E]*[0-9]*)([kmbt]|qu[ia]|se[px]|non|oct|dec|und|duo|tre|sed|nov)", Pattern.CASE_INSENSITIVE);
    public static double moneyFromString(String s) {
		double has = 0;
		Matcher m = moneyPattern.matcher(s);
		while(m.find())
			has+=StringUtils.getDouble(m.group(1))*getMultiply(m.group(2));
		if(has==0)has=StringUtils.getDouble(s);
		return has;
	}
    
    private static double getMultiply(String name) {
    	switch(name) {
    	case "k":
    		return 1000;
    	case "m":
    		return 1000000;
    	case "b":
    		return 1.0E9;
    	case "t":
    		return 1.0E12;
    	case "qua":
    		return 1.0E15;
    	case "qui":
    		return 1.0E18;
    	case "sex": //No, it's not "sex"...
    		return 1.0E21;
    	case "sep":
    		return 1.0E24;
    	case "oct":
    		return 1.0E27;
    	case "non":
    		return 1.0E30;
    	case "dec":
    		return 1.0E33;
    	case "und":
    		return 1.0E36;
    	case "duo":
    		return 1.0E39;
    	case "tre":
    		return 1.0E42;
    	case "QUA":
    		return 1.0E45;
    	case "QUI":
    		return 1.0E48;
    	case "SED":
    		return 1.0E51;
    	case "SEP":
    		return 1.0E54;
    	case "OCT":
    		return 1.0E57;
    	case "NOV":
    		return 1.0E60;
    	}
    	return 1;
    }
}

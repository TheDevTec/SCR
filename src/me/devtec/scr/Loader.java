package me.devtec.scr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.devtec.scr.modules.ActionBar;
import me.devtec.scr.modules.BossBar;
import me.devtec.scr.modules.Scoreboard;
import me.devtec.scr.modules.Tablist;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;

public class Loader extends JavaPlugin {
	public static Config config, defaultTranslation, translation;
	public static List<String> positive = new ArrayList<>(), negative = new ArrayList<>();
	public static Loader plugin;
	public static Permission perms;
	
	public void onLoad() {
		plugin=this;
		ConfigManager.load();
		if(Bukkit.getPluginManager().getPlugin("Vault")!=null)
			vaultHooking();
	}
	
	public void onEnable() {
		//CommandsManager.load();
		Tablist.load(ConfigManager.tablist.getStringList("settings.disabledWorlds"), (long)StringUtils.calculate(ConfigManager.tablist.getString("settings.reflesh.header-footer"))
				, (long)StringUtils.calculate(ConfigManager.tablist.getString("settings.reflesh.tablist-name"))
				, (long)StringUtils.calculate(ConfigManager.tablist.getString("settings.reflesh.nametag"))
				, (long)StringUtils.calculate(ConfigManager.tablist.getString("settings.reflesh.yellow-number")));

	}
	public void vaultHooking() {
		TheAPI.msg("&5The&dAPI&7: &8********************", TheAPI.getConsole());
		TheAPI.msg("&5The&dAPI&7: &eAction: &fLooking for Vault Permission plugin..", TheAPI.getConsole());
		TheAPI.msg("&5The&dAPI&7: &8********************", TheAPI.getConsole());
		new Tasker() {
			@Override
			public void run() {
				if (getVaultPerms()) {
					TheAPI.msg("&5The&dAPI&7: &8********************", TheAPI.getConsole());
					TheAPI.msg("&5The&dAPI&7: &eFound Vault Permission plugin", TheAPI.getConsole());
					TheAPI.msg("&5The&dAPI&7: &8********************", TheAPI.getConsole());
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
		ConfigManager.unload();
		Tablist.unload();
		Scoreboard.unload();
		BossBar.unload();
		ActionBar.unload();
		//CommandsManager.unload();
	}
	
	public static boolean isPositive(String value) {
		//TODO config
		return value.equalsIgnoreCase("on")||value.equalsIgnoreCase("true")||value.equalsIgnoreCase("yes")||value.equalsIgnoreCase("allow");
	}
	
	public static boolean isNegative(String value) {
		//TODO config
		return value.equalsIgnoreCase("off")||value.equalsIgnoreCase("false")||value.equalsIgnoreCase("no")||value.equalsIgnoreCase("disallow");
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
		JsonUtils.msgRaw(trans==null?defaultTranslation.get(transPath):trans, builder, sender);
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

	public static long getRequestTime() {
		return 0;
	}
}

package me.devtec.scr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.devtec.scr.commands.CommandsManager;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.configapi.Config;

public class Loader extends JavaPlugin {
	public static Config config; //main config
	protected static Config translation;
	
	public void onLoad() {
		ConfigManager.load();
	}
	
	public void onEnable() {
		CommandsManager.load();
	}

	public void onDisable() {
		ConfigManager.unload();
		CommandsManager.unload();
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
		JsonUtils.msgRaw(translation.get(transPath), builder, sender);
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

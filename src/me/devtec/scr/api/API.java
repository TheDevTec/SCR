package me.devtec.scr.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.theapi.bukkit.BukkitLoader;

public class API {

	// Getting users
	private static final Map<String, User> usercache = new HashMap<>();

	public static User getUser(String playername) {
		if (!usercache.containsKey(playername))
			usercache.put(playername, new User(playername));
		return usercache.get(playername);
	}

	public static User getUser(Player player) {
		if (!usercache.containsKey(player.getName()))
			usercache.put(player.getName(), new User(player));
		return usercache.get(player.getName());
	}

	public static User getUser(CommandSender commandsender) {
		if (!(commandsender instanceof Player))
			return new User("console");
		if (!usercache.containsKey(commandsender.getName()))
			usercache.put(commandsender.getName(), new User(commandsender));
		return usercache.get(commandsender.getName());
	}

	public static void removeUser(String player) {
		if (usercache.containsKey(player))
			usercache.remove(player);
	}

	public static String getPlayerName(Player player) {
		if (player == null)
			return null;
		return getUser(player).getName();
	}

	public static String getPlayerName(CommandSender player) {
		if (player == null)
			return null;
		return getUser(player).getName();
	}

	public static String getPlayerName(String player) {
		if (player == null)
			return null;
		return getUser(player).getName();
	}

	public static String getRealName(String player) {
		if (player == null)
			return null;
		return getUser(player).getRealName();
	}

	public static Player getPlayer(String name) {
		if (name == null)
			return null;
		return Bukkit.getPlayer(getUser(name).getRealName());
	}
	
	//ONLINE PLAYERS
	public static List<CommandSender> getOnlinePlayers(boolean console) {
		List<CommandSender> list = new ArrayList<>();
		list.addAll(BukkitLoader.getOnlinePlayers());
		if(console)
			list.add(Bukkit.getConsoleSender());
		return list;
	}
	//VANISH
	public static boolean isVanished(Player p) {
		return false;
	}
}

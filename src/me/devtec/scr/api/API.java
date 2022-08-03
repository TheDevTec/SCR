package me.devtec.scr.api;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

	public static String getPlayerName(Player player) {
		return getUser(player).getName();
	}

	public static String getPlayerName(CommandSender player) {
		return getUser(player).getName();
	}

	public static String getPlayerName(String player) {
		return getUser(player).getName();
	}

	public static String getRealName(String player) {
		return getUser(player).getRealName();
	}

	public static Player getPlayer(String name) {
		return Bukkit.getPlayer(getUser(name).getRealName());
	}
}

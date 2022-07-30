package me.devtec.scr.api;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.utils.sUser;

public class API {

	// Getting users
	private static final Map<String, sUser> usercache = new HashMap<>();

	public static sUser getUser(String playername) {
		if (!usercache.containsKey(playername))
			usercache.put(playername, new sUser(playername));
		return usercache.get(playername);
	}

	public static sUser getUser(Player player) {
		if (!usercache.containsKey(player.getName()))
			usercache.put(player.getName(), new sUser(player));
		return usercache.get(player.getName());
	}

	public static sUser getUser(CommandSender commandsender) {
		if (!(commandsender instanceof Player))
			return new sUser("console");
		if (!usercache.containsKey(commandsender.getName()))
			usercache.put(commandsender.getName(), new sUser(commandsender));
		return usercache.get(commandsender.getName());
	}

}

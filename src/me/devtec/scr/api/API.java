package me.devtec.scr.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		else if(!playername.equalsIgnoreCase("console")) {
			User u = usercache.get(playername);
			if(u.player==null ) {
				usercache.remove(playername, new User(playername));
				usercache.put(playername, new User(playername));
			}
			return u;
		}
		return usercache.get(playername);
	}

	public static User getUser(Player player) {
		if (!usercache.containsKey(player.getName()))
			usercache.put(player.getName(), new User(player));
		else {
			User u = usercache.get(player.getName());
			if(u.player==null) {
				usercache.remove(player.getName(), new User(player));
				usercache.put(player.getName(), new User(player));
			}
			return u;
		}
		return usercache.get(player.getName());
	}

	public static User getUser(CommandSender commandsender) {
		if (!(commandsender instanceof Player))
			return new User("console");
		if (!usercache.containsKey(commandsender.getName()))
			usercache.put(commandsender.getName(), new User(commandsender));
		else {
			User u = usercache.get(commandsender.getName());
			if(u.player==null) {
				usercache.remove(commandsender.getName(), new User(commandsender));
				usercache.put(commandsender.getName(), new User(commandsender));
			}
		}
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
		return getUser(player).getName() != null ? getUser(player).getName() : player;
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

	/*
	 * ONLINE PLAYERS
	 */

	public static List<CommandSender> getOnlinePlayers(boolean console) {
		List<CommandSender> list = new ArrayList<>(BukkitLoader.getOnlinePlayers());
		if (console)
			list.add(Bukkit.getConsoleSender());
		return list;
	}

	// které sender vidí
	public static List<Player> getOnlinePlayersFor(CommandSender sender) {
		List<Player> list = new ArrayList<>();
		// list.addAll(BukkitLoader.getOnlinePlayers());
		for (Player target : BukkitLoader.getOnlinePlayers())
			if (canSee(sender, target) && !list.contains(target))
				list.add(target);
		return list;
	}

	public static List<CommandSender> getOnlinePlayersThatcanSee(CommandSender target, boolean console) {
		List<CommandSender> list = getOnlinePlayers(console);
		Iterator<CommandSender> iterator = list.iterator();
		while (iterator.hasNext()) {
			CommandSender online = iterator.next();
			if (!canSee(online, target))
				iterator.remove();
		}
		return list;
	}

	// kteří mají permisi
	public static List<CommandSender> getOnlinePlayersWith(String permission) {
		List<CommandSender> list = getOnlinePlayers(true);
		Iterator<CommandSender> iterator = list.iterator();
		while (iterator.hasNext()) {
			CommandSender online = iterator.next();
			if (!online.hasPermission(permission))
				iterator.remove();
		}
		return list;
	}

	// kteří nemají permisi
	public static List<CommandSender> getOnlinePlayersWithout(String permission) {
		List<CommandSender> list = new ArrayList<>();
		Iterator<CommandSender> iterator = list.iterator();
		while (iterator.hasNext()) {
			CommandSender online = iterator.next();
			if (online.hasPermission(permission))
				iterator.remove();
		}
		return list;
	}

	// VANISH
	public static boolean isVanished(CommandSender p) {
		// TODO
		return false;
	}

	public static boolean canSee(CommandSender sender, CommandSender target) { // if sender can see target
		return sender == null || target == null || !(sender instanceof Player) || !(target instanceof Player) || ((Player) sender).canSee((Player) target);
	}
}

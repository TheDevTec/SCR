package me.devtec.scr.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.theapi.bukkit.BukkitLoader;

public class API {

	// Getting users
	private static final Map<UUID, User> userCache = new HashMap<>();

	public static User getUser(String name) {
		UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
		User cached = userCache.get(uuid);
		if (cached != null)
			return cached;
		return new User(uuid); // Not caching
	}

	public static User getUser(UUID uuid) {
		User user = userCache.get(uuid);
		if (user == null)
			userCache.put(uuid, user = new User(uuid));
		return user;
	}

	public static User getUser(Player player) {
		User user = userCache.get(player.getUniqueId());
		if (user == null)
			userCache.put(player.getUniqueId(), user = new User(player));
		return user;
	}

	public static void removeUser(UUID uuid) {
		userCache.remove(uuid);
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

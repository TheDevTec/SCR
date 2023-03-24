package me.devtec.scr.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.listeners.commands.PluginEnable;
import me.devtec.scr.utils.Utils;
import me.devtec.shared.commands.manager.PermissionChecker;
import me.devtec.shared.utility.StringUtils;
import me.devtec.shared.utility.TimeUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public interface ScrCommand {

	public static final PermissionChecker<CommandSender> PERMS_CHECKER = (sender, perm, tablist) -> {
		if (!(sender instanceof Player))
			return true;
		return API.getUser((Player) sender).hasPerm(perm, !tablist);
	};
	public static final PermissionChecker<Player> PLAYER_PERMS_CHECKER = (sender, perm, tablist) -> API.getUser(sender).hasPerm(perm, !tablist);

	public default void msg(CommandSender sender, String path) {
		MessageUtils.message(sender, path, null);
	}

	public default void msg(CommandSender sender, String path, Placeholders placeholders) {
		MessageUtils.message(sender, path, placeholders);
	}

	public default void msgSec(CommandSender sender, String path) {
		MessageUtils.message(sender, configSection() + "." + path, null);
	}

	public default void msgSec(CommandSender sender, String path, Placeholders placeholders) {
		MessageUtils.message(sender, configSection() + "." + path, placeholders);
	}

	public default void offlinePlayer(CommandSender sender, String player) {
		MessageUtils.message(sender, "offlinePlayer", Placeholders.c().add("player", player));
	}

	public default Collection<? extends Player> playerSelectors(CommandSender sender, String selector) {
		char lowerCase = selector.charAt(0) == '*' ? '*' : selector.charAt(0) == '@' && selector.length() == 2 ? Character.toLowerCase(selector.charAt(1)) : 0;
		if (lowerCase != 0)
			switch (lowerCase) {
			case 'a':
			case 'e':
			case '*':
				return BukkitLoader.getOnlinePlayers();
			case 'r':
				return Arrays.asList(StringUtils.randomFromCollection(BukkitLoader.getOnlinePlayers()));
			case 's':
			case 'p':
				Location pos = null;
				if (sender instanceof Player)
					pos = ((Player) sender).getLocation();
				else if (sender instanceof BlockCommandSender)
					pos = ((BlockCommandSender) sender).getBlock().getLocation();
				else
					pos = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
				double distance = -1;
				Player nearestPlayer = null;
				for (Player sameWorld : pos.getWorld().getPlayers())
					if (distance == -1 || distance < sameWorld.getLocation().distance(pos)) {
						distance = sameWorld.getLocation().distance(pos);
						nearestPlayer = sameWorld;
					}
				return BukkitLoader.getOnlinePlayers().isEmpty() ? Collections.emptyList() : Arrays.asList(nearestPlayer == null ? BukkitLoader.getOnlinePlayers().iterator().next() : nearestPlayer);
			}
		Player target = Bukkit.getPlayer(selector);
		return target == null ? Collections.emptyList() : Arrays.asList(target);
	}

	public default void help(CommandSender sender, String arg) {
		MessageUtils.msgConfig(sender, configSection() + ".help." + arg, Loader.commands, null);
	}

	// Do not overide this - onLoad
	@SuppressWarnings("unchecked")
	public default void initFirst(List<String> cmds) {
		List<String> loadAfter = Loader.commands.getStringList(configSection() + ".loadAfter");
		if (!loadAfter.isEmpty()) {
			List<String> registered = new ArrayList<>();
			for (String pluginName : loadAfter)
				if (Bukkit.getPluginManager().getPlugin(pluginName) != null && !Bukkit.getPluginManager().getPlugin(pluginName).isEnabled())
					registered.add(pluginName);
			if (!registered.isEmpty()) {
				PluginEnable.init();
				PluginEnable.waiting.put(this, new List[] { registered, cmds });
				return;
			}
		}
		String firstUp = Character.toUpperCase(configSection().charAt(0)) + configSection().substring(1);
		Loader.plugin.getLogger().info("[" + firstUp + "] Registering command.");
		init(cmds);
	}

	// Permission
	public default String permission(String path) {
		if (path.equalsIgnoreCase("cd-bypass") && !Loader.commands.exists(configSection() + ".permission." + path))
			return null;
		if (Loader.commands.exists(configSection() + ".permission." + path))
			return Loader.commands.getString(configSection() + ".permission." + path);
		Loader.plugin.getLogger().warning("[" + configSection() + "] Missing permission in commands.yml: (" + configSection() + ".permission." + path + ")");
		return "scr.missingpermission." + configSection() + "." + path;
	}

	public default boolean hasPermission(CommandSender s, String path) {
		return permission(path) != null ? s instanceof Player ? API.getUser((Player) s).hasPerm(permission(path), false) : true : false;
	}

	public default boolean inCooldown(CommandSender sender) {
		if (!(sender instanceof Player))
			return false;
		User user = API.getUser((Player) sender);
		if (!Utils.cooldownExpired(user, configSection())) { // Cooldown check
			// if cooldownExpired == true -> no cooldown
			MessageUtils.message(sender, "cooldowns.commands",
					Placeholders.c().replace("time", TimeUtils.timeToString(Utils.expires(user, configSection()))).replace("time_sec", Utils.expires(user, configSection())));
			return true; // switching booleans, beacause there is isCommandInCooldown? true ==> is
							// cooldown
		}
		return false;
	}

	public void init(List<String> cmds);
	// public void init(int cd,List<String> cmds);

	public default void disabling() {
	}

	public String configSection();

}

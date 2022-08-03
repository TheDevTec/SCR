package me.devtec.scr.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.commands.structures.CommandStructure.CooldownDetection;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public interface ScrCommand {

	public class CooldownHolder {
		public CooldownHolder(ScrCommand cmd, int cooldownTime) {
			this.cmd = cmd;
			this.cooldownTime = cooldownTime;
		}

		public ScrCommand cmd;
		public Map<UUID, Long> cooldownMap = new ConcurrentHashMap<>();
		public int cooldownTime;
	}

	public static final Map<CommandStructure<?>, CooldownHolder> cooldownMap = new ConcurrentHashMap<>();

	public static final PermissionChecker<CommandSender> PERMS_CHECKER = (sender, perm, tablist) -> {
		if (tablist)
			return API.getUser(sender).isAutorized(perm);
		return API.getUser(sender).checkPerm(perm); // also noperm message if needed
	};
	public static final PermissionChecker<Player> PLAYER_PERMS_CHECKER = (sender, perm, tablist) -> {
		if (tablist)
			return API.getUser(sender).isAutorized(perm);
		return API.getUser(sender).checkPerm(perm); // also noperm message if needed
	};
	public static final CooldownDetection<CommandSender> COOLDOWN = (sender, structure, args) -> {
		CooldownHolder holder = cooldownMap.get(structure.first());
		if (!(sender instanceof Player) || sender.hasPermission("scr.bypass.commands") || holder.cmd.hasPermission(sender, "cd-bypass"))
			return false;
		long cd = holder.cooldownMap.getOrDefault(((Player) sender).getUniqueId(), 0L);
		int expireIn = (int) (cd - System.currentTimeMillis() / 1000 + holder.cooldownTime);
		if (expireIn <= 0) {
			holder.cooldownMap.put(((Player) sender).getUniqueId(), System.currentTimeMillis() / 1000);
			return false;
		}
		MessageUtils.message(sender, "cooldowns.commands", Placeholders.c().add("time", StringUtils.timeToString(expireIn)).add("expire", StringUtils.timeToString(expireIn)));
		return true;
	};

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
		char lowerCase = selector.equals("*") ? '*' : Character.toLowerCase(selector.charAt(1));
		if (lowerCase == '*' || selector.charAt(0) == '@')
			switch (lowerCase) {
			case 'a':
			case 'e':
			case '*':
				return BukkitLoader.getOnlinePlayers();
			case 'r':
				return Arrays.asList(StringUtils.getRandomFromCollection(BukkitLoader.getOnlinePlayers()));
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
				return Arrays.asList(nearestPlayer == null ? BukkitLoader.getOnlinePlayers().iterator().next() : nearestPlayer);
			}
		return Arrays.asList(API.getPlayer(selector));
	}

	public default void help(CommandSender sender, String arg) {
		MessageUtils.msgConfig(sender, configSection() + ".help." + arg, Loader.commands, null);
		/*
		 * Object val = Loader.commands.get(configSection() + ".help." + arg); if (val
		 * instanceof Collection) for (String list :
		 * Loader.commands.getStringList(configSection() + ".help." + arg)) msg(sender,
		 * list, null); else MessageUtils.msgConfig(sender,
		 * configSection()+".help."+arg, Loader.commands, null); //msg(sender,
		 * Loader.commands.getString(configSection() + ".help." + arg), null);
		 */
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
		// init(Loader.commands.getInt(configSection() + ".cooldown"), cmds);
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

	public default Boolean hasPermission(CommandSender s, String path) {
		return permission(path) != null ? API.getUser(s).isAutorized(permission(path)) : false;
	}

	public default boolean inCooldown(CommandSender sender) {
		User user = API.getUser(sender);
		if (!Utils.cooldownExpired(user, configSection())) { // Cooldown check
			// if cooldownExpired == true -> no cooldown
			MessageUtils.message(user.player, "cooldowns.commands",
					Placeholders.c().replace("time", StringUtils.timeToString(Utils.expires(user, configSection()))).replace("time_sec", Utils.expires(user, configSection())));
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

package me.devtec.scr.utils;

import org.bukkit.permissions.PermissionAttachmentInfo;

import me.devtec.scr.Loader;
import me.devtec.scr.api.User;
import me.devtec.shared.utility.StringUtils;

public class Utils {

	// COMMAND COOLDOWNS
	public static boolean cooldownExpired(User user, String command) { // Check if cooldown on command expired
		if(user == null) return true;
		if (user.isAutorized("scr.bypass.cooldowns"))
			return true;
		// Permission cooldown: scr.bypass.cooldowns.<command>.<time>
		String permcooldown = "0";
		for (PermissionAttachmentInfo permission : user.player.getEffectivePermissions())
			if (permission.getPermission().toLowerCase().startsWith("scr.bypass.cooldowns." + command.toLowerCase()))
				permcooldown = "" + StringUtils.timeFromString(permission.getPermission().toLowerCase().replace("scr.bypass.cooldowns." + command.toLowerCase() + ".", ""));
		if (!permcooldown.equalsIgnoreCase("0")) {
			if (!user.cooldownExpired("cooldowns.permcommands." + command, permcooldown))
				return false;
			user.newCooldown("cooldowns.permcommands." + command);
			return true;
		}
		// SCR Command cooldown
		if (Loader.commands.exists(command + ".cooldown")) {
			if (!user.cooldownExpired("cooldowns.commands." + command, Loader.commands.getString(command + ".cooldown"))) {
				if (Loader.commands.exists(command + ".permission.cd-bypass") && user.isAutorized(Loader.commands.getString(command + ".permission.cd-bypass")))
					return true;
				return false;
			}
			user.newCooldown("cooldowns.commands." + command);
			return true;
		}
		// Other commands cooldown in config.yml
		if (Loader.config.exists("commandCooldowns." + command)) {
			if (!user.cooldownExpired("cooldowns.othercommands." + command, Loader.config.getString("commandCooldowns." + command)))
				return false;
			user.newCooldown("cooldowns.othercommands." + command);
		}
		return true;
	}

	public static long expires(User user, String command) { // How long before it expires (return seconds)
		if(user == null) return 0;
		// Permission cooldown: SCR.Other.Cooldowns.<command>.<time>
		String permcooldown = "0";
		for (PermissionAttachmentInfo permission : user.player.getEffectivePermissions())
			if (permission.getPermission().toLowerCase().startsWith("scr.bypass.cooldowns." + command.toLowerCase()))
				permcooldown = "" + StringUtils.timeFromString(permission.getPermission().toLowerCase().replace("scr.bypass.cooldowns." + command.toLowerCase() + ".", ""));
		if (!permcooldown.equalsIgnoreCase("0"))
			return user.expires("cooldowns.permcommands." + command, permcooldown);
		// SCR Command cooldown
		if (Loader.commands.exists(command + ".cooldown"))
			return user.expires("cooldowns.commands." + command, Loader.commands.getString(command + ".cooldown"));
		// Other commands cooldown in config.yml
		if (Loader.config.exists("commandCooldowns." + command))
			return user.expires("cooldowns.othercommands." + command, Loader.config.getString("commandCooldowns." + command));
		return 0;
	}
}

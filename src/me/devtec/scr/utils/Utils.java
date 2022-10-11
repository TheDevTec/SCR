package me.devtec.scr.utils;

import org.bukkit.permissions.PermissionAttachmentInfo;

import me.devtec.scr.Loader;
import me.devtec.scr.api.User;
import me.devtec.shared.utility.StringUtils;

public class Utils {

	// COMMAND COOLDOWNS
	public static boolean cooldownExpired(User user, String command) { // Check if cooldown on command expired
		if (user == null || user.hasPerm("scr.bypass.cooldowns", false))
			return true;
		// Permission cooldown: scr.bypass.cooldowns.<command>.<time>
		String permcooldown = "0";
		for (PermissionAttachmentInfo permission : user.getPlayer().getEffectivePermissions())
			if (permission.getPermission().toLowerCase().startsWith("scr.bypass.cooldowns." + command.toLowerCase()))
				permcooldown = "" + StringUtils.timeFromString(permission.getPermission().toLowerCase().replace("scr.bypass.cooldowns." + command.toLowerCase() + ".", ""));
		if (!permcooldown.equalsIgnoreCase("0")) {
			if (!user.cooldownExpired("permcommands." + command, permcooldown))
				return false;
			user.cooldownMake("permcommands." + command);
			return true;
		}
		// SCR Command cooldown
		if (Loader.commands.exists(command + ".cooldown")) {
			if (!user.cooldownExpired("commands." + command, Loader.commands.getString(command + ".cooldown"))) {
				if (Loader.commands.exists(command + ".permission.cd-bypass") && user.hasPerm(Loader.commands.getString(command + ".permission.cd-bypass"), false))
					return true;
				return false;
			}
			user.cooldownMake("commands." + command);
			return true;
		}
		// Other commands cooldown in config.yml
		if (Loader.config.exists("commandCooldowns." + command)) {
			if (!user.cooldownExpired("othercommands." + command, Loader.config.getString("commandCooldowns." + command)))
				return false;
			user.cooldownMake("othercommands." + command);
		}
		return true;
	}

	public static long expires(User user, String command) { // How long before it expires (return seconds)
		if (user == null)
			return 0;
		// Permission cooldown: SCR.Other.Cooldowns.<command>.<time>
		String permcooldown = "0";
		for (PermissionAttachmentInfo permission : user.getPlayer().getEffectivePermissions())
			if (permission.getPermission().toLowerCase().startsWith("scr.bypass.cooldowns." + command.toLowerCase()))
				permcooldown = "" + StringUtils.timeFromString(permission.getPermission().toLowerCase().replace("scr.bypass.cooldowns." + command.toLowerCase() + ".", ""));
		if (!permcooldown.equalsIgnoreCase("0"))
			return user.cooldownExpire("permcommands." + command, permcooldown);
		// SCR Command cooldown
		if (Loader.commands.exists(command + ".cooldown"))
			return user.cooldownExpire("commands." + command, Loader.commands.getString(command + ".cooldown"));
		// Other commands cooldown in config.yml
		if (Loader.config.exists("commandCooldowns." + command))
			return user.cooldownExpire("othercommands." + command, Loader.config.getString("commandCooldowns." + command));
		return 0;
	}
}

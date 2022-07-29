package me.devtec.scr.commands;

import org.bukkit.permissions.PermissionAttachmentInfo;

import me.devtec.scr.Loader;
import me.devtec.scr.api.User;

public class CommandsManager {

	
	//COOLDOWNS
	public static boolean cooldownExpired(User user, String command) { //Check if cooldown on command expired
		if(user.isAutorized("SCR.Other.Cooldowns.IgnoresAll"))
			return true;
		//Permission cooldown: SCR.Other.Cooldowns.<command>.<time>
		String permcooldown = "0";
		for(PermissionAttachmentInfo permission : user.player.getEffectivePermissions()) {
			if(permission.getPermission().toLowerCase().startsWith("scr.other.cooldowns."+command.toLowerCase()))
				permcooldown = permission.getPermission().toLowerCase().replace("scr.other.cooldowns.", "");
		}
		if(!permcooldown.equalsIgnoreCase("0")) {
			if(!user.cooldownExpired("cooldowns.permcommands."+command, permcooldown))
				return false;
			user.newCooldown("cooldowns.permcommands."+command);
			return true;
		}
		//SCR Command cooldown
		if(Loader.commands.exists(command+".cooldown")) {
			if(!user.cooldownExpired("cooldowns.commands."+command,
					Loader.commands.getString(command+".cooldown")))
				return false;
			user.newCooldown("cooldowns.commands."+command);
			return true;
		}
		//Other commands cooldown in config.yml
		if(Loader.config.exists("CommandCooldowns."+command)) {
			if(!user.cooldownExpired("cooldowns.othercommands."+command,
					Loader.config.getString("CommandCooldowns."+command)))
				return false;
			user.newCooldown("cooldowns.othercommands."+command);
			return true;
		}
		return true;
	}
	
	public static long expires(User user, String command) { //How long before it expires (return seconds)
		//Permission cooldown: SCR.Other.Cooldowns.<command>.<time>
		String permcooldown = "0";
		for(PermissionAttachmentInfo permission : user.player.getEffectivePermissions()) {
			if(permission.getPermission().toLowerCase().startsWith("scr.other.cooldowns."+command.toLowerCase()))
				permcooldown = permission.getPermission().toLowerCase().replace("scr.other.cooldowns.", "");
		}
		if(!permcooldown.equalsIgnoreCase("0")) {
			return user.expires("cooldowns.permcommands."+command, permcooldown);
		}
		//SCR Command cooldown
		if(Loader.commands.exists(command+".cooldown")) {
			return user.expires("cooldowns.commands."+command,
					Loader.commands.getString(command+".cooldown"));
		}
		//Other commands cooldown in config.yml
		if(Loader.config.exists("CommandCooldowns."+command)) {
			return user.expires("cooldowns.othercommands."+command,
					Loader.config.getString("CommandCooldowns."+command));
		}
		return 0;
	}
	
	
}

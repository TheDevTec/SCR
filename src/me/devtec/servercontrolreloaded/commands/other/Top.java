package me.devtec.servercontrolreloaded.commands.other;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;

public class Top implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "Top", "Other")) {
			if(s instanceof Player==false)return true;
			if(!CommandsManager.canUse("Other.Top", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Top", s))));
				return true;
			}
			Location loc = ((Player)s).getLocation();
			loc.setY(((Player)s).getWorld().getHighestBlockYAt(((Player)s).getLocation())+1);
			API.teleport((Player)s, loc);
			return true;
		}
		Loader.noPerms(s, "Top", "Other");
		return true;
	}

}

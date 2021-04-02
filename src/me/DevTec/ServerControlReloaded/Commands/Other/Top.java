package me.DevTec.ServerControlReloaded.Commands.Other;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Top implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "Other", "Top")) {
			if(s instanceof Player==false)return true;
			Location loc = ((Player)s).getLocation();
			loc.setY(((Player)s).getWorld().getHighestBlockYAt(((Player)s).getLocation()));
			((Player)s).teleport(loc);
			return true;
		}
		Loader.noPerms(s, "Other", "Top");
		return true;
	}

}

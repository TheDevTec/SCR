package me.devtec.servercontrolreloaded.commands.time;


import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Night implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "Night", "Time")) {
					((Player) s).getLocation().getWorld().setTime(13000);
					Loader.sendMessages(s, "Time.Night", Placeholder.c().add("%world%", ((Player) s).getLocation().getWorld().getName()));
					return true;
				}
				Loader.noPerms(s, "Night", "Time");
				return true;
			}
			Loader.Help(s, "/Night <world>", "Time");
			return true;
		}
		if (args.length == 1) {
			if (Loader.has(s, "Night", "Time")) {
				if (Bukkit.getWorld(args[0]) != null) {
					Bukkit.getWorld(args[0]).setTime(12000);
					Loader.sendMessages(s, "Time.Night", Placeholder.c().add("%world%", args[0]));
					return true;
				}
				Loader.sendMessages(s, "Missing.World", Placeholder.c().add("%world%", (args[0])));
				return true;
			}
			Loader.noPerms(s, "Night", "Time");
			return true;
		}
		return false;
	}

	public List<String> worlds() {
		List<String> list = new ArrayList<>();
		for (World p2 : Bukkit.getWorlds()) {
			list.add(p2.getName());
		}
		return list;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (Loader.has(s, "Night", "Time") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], worlds());
		return Arrays.asList();
	}
}
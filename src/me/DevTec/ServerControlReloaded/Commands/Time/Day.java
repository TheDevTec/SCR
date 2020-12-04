package me.DevTec.ServerControlReloaded.Commands.Time;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.Utils.DataKeeper.Collections.UnsortedList;

public class Day implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "Day", "Time")) {
					((Player) s).getLocation().getWorld().setTime(1000);
					Loader.sendMessages(s, "Time.Day", Placeholder.c().add("%world%", ((Player) s).getLocation().getWorld().getName()));
					return true;
				}
				Loader.noPerms(s, "Day", "Time");
				return true;
			}
			Loader.Help(s, "/Day <world>", "Time");
			return true;
		}
		if (args.length == 1) {
			if (Loader.has(s, "Day", "Time")) {
				if (Bukkit.getWorld(args[0]) != null) {
					Bukkit.getWorld(args[0]).setTime(0);
					Loader.sendMessages(s, "Time.Day", Placeholder.c().add("%world%", (args[0])));
					return true;
				}
				Loader.sendMessages(s, "Missing.World", Placeholder.c().add("%world%", (args[0])));
				return true;
			}
			Loader.noPerms(s, "Day", "Time");
			return true;
		}
		return false;
	}

	public List<String> worlds() {
		List<String> list = new UnsortedList<String>();
		for (World p2 : Bukkit.getWorlds()) {
			list.add(p2.getName());
		}
		return list;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new UnsortedList<>();
		if (cmd.getName().equalsIgnoreCase("day") && args.length == 1) {
			if (Loader.has(s, "Day", "Time")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], worlds(), new UnsortedList<>()));
			}
		}
		return c;
	}
}

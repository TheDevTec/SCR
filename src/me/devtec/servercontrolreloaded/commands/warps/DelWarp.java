package me.devtec.servercontrolreloaded.commands.warps;


import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;

public class DelWarp implements CommandExecutor, TabCompleter {
	public String warp(String ss) {
		for (String s : Loader.config.getKeys("Warps")) {
			if (s.equalsIgnoreCase(ss)) {
				return s;
			}
		}
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "DelWarp", "Warps")) {
			if (args.length == 0) {
				Loader.Help(s, "DelWarp", "Warps");
				return true;
			}
			if (warp(args[0])!=null) {
				String name = warp(args[0]);
				Loader.config.remove("Warps." + name);
				Loader.config.save();
				Loader.sendMessages(s, "Warp.Deleted", Placeholder.c()
						.add("%warp%", name));
				return true;
			}
			Loader.sendMessages(s, "Warp.NotExist", Placeholder.c()
					.add("%warp%", args[0]));
			return true;
		}
		Loader.noPerms(s, "DelWarp", "Warps");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (Loader.has(s, "DelWarp", "Warps") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], Loader.config.getKeys("Warps"));
		return Arrays.asList();
	}
}
package me.devtec.servercontrolreloaded.commands.warps;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;

public class SetWarp implements CommandExecutor, TabCompleter {
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
		if (Loader.has(s, "SetWarp", "Warps")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Loader.Help(s, "SetWarp", "Warps");
					return true;
				}
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					if (warp(args[0])==null) {
						Player p = (Player) s;
						Location local = p.getLocation();
						Loader.config.set("Warps." + args[0], StringUtils.getLocationAsString(local));
						Loader.config.save();
						Loader.sendMessages(s, "Warp.Created.Normal", Placeholder.c()
								.add("%warp%", args[0]));
						return true;
					}
					Loader.sendMessages(s, "Warp.Exists", Placeholder.c().add("%warp%", warp(args[0])));
					return true;
				}
				return true;
			}
			if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("yes")) {
				if (s instanceof Player) {
					if (warp(args[0])==null) {
						Player p = (Player) s;
						Location local = p.getLocation();
						Loader.config.set("Warps." + args[0] + ".NeedPermission", true);
						Loader.config.set("Warps." + args[0], StringUtils.getLocationAsString(local));
						Loader.config.save();
						Loader.sendMessages(s, "Warp.Created.WithPerms", Placeholder.c()
								.add("%warp%", args[0])
								.add("%permission%", "ServerControl.Warp." + args[0]));
						return true;
					}
					Loader.sendMessages(s, "Warp.Exist", Placeholder.c()
						.add("%warp%", warp(args[0])));
					return true;
				}
				return true;
			}
		}
		Loader.noPerms(s, "SetWarp", "Warps");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "SetWarp", "Warps")) {
			if (args.length == 1)
				return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
			if (args.length == 2)
				return StringUtils.copyPartialMatches(args[1], Arrays.asList("yes"));
		}
		return Arrays.asList();
	}
}
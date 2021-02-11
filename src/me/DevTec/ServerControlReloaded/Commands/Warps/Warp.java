package me.DevTec.ServerControlReloaded.Commands.Warps;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Warp implements CommandExecutor, TabCompleter {

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
		if (Loader.has(s, "Warp", "Warps")) {
			if (Loader.config.exists("Warps")) {
				if (args.length == 0) {
					Loader.sendMessages(s, "Warp.List", Placeholder.c()
							.add("%warps%", StringUtils.join(warpss(s), ", ")));
					return true;
				}
				if (args.length == 1) {
					if (s instanceof Player) {
						if (warp(args[0]) != null) {
							Location loc = StringUtils.getLocationFromString(Loader.config.getString("Warps." + warp(args[0])));
							if (loc == null) {
								Loader.sendMessages(s, "Warp.WrongLocation", Placeholder.c()
										.add("%warp%", warp(args[0])));
								return true;
							}
							if (Loader.config.getBoolean("Warps." + warp(args[0]) + ".NeedPermission")) {
								if (s.hasPermission(Loader.cmds.getString("Warps.Warp.SubPermission.PerWarp").replace("%warp%", warp(args[0])))) {
									API.setBack((Player) s);
									if (setting.tp_safe)
										API.safeTeleport((Player)s, loc);
									else
										((Player) s).teleport(loc);
									Loader.sendMessages(s, "Warp.Teleport.You", Placeholder.c()
											.add("%warp%", warp(args[0])));
									return true;
								}
								Loader.sendMessages(s, "NoPerms", Placeholder.c()
										.add("%permission%", Loader.cmds.getString("Warps.Warp.SubPermission.PerWarp")
										.replace("%warp%", warp(args[0]))));
								return true;
							}
							API.setBack((Player) s);
							if (setting.tp_safe)
								API.safeTeleport((Player)s, loc);
							else
								((Player) s).teleport(loc);
							Loader.sendMessages(s, "Warp.Teleport.You", Placeholder.c()
									.add("%warp%", warp(args[0])));
							return true;
						}
						Loader.sendMessages(s, "Warp.NotExist", Placeholder.c()
								.add("%warp%", args[0]));
						return true;
					}
					return true;
				}
				if (args.length == 2 && Loader.has(s, "Warp", "Warps","Other")) {
					Player p = TheAPI.getPlayer(args[1]);
					if (p == null) {
						Loader.notOnline(s, args[1]);
						return true;
					}
					if (warp(args[0]) != null) {
						Location loc = StringUtils.getLocationFromString(Loader.config.getString("Warps." + warp(args[0])));
						if (loc == null) {
							Loader.sendMessages(s, "Warp.WrongLocation", Placeholder.c()
									.add("%warp%", warp(args[0])));
							return true;
						}
						if (Loader.config.getBoolean("Warps." + warp(args[0]) + ".NeedPermission")) {
							if (s.hasPermission(Loader.getPerm("Warp", "Warps", "PerWarp").replace("%warp%", warp(args[0])))) {
								API.setBack((Player) s);
								if (setting.tp_safe)
									API.safeTeleport((Player)s, loc);
								else
									((Player) s).teleport(loc);
								Loader.sendMessages(s, "Warp.Teleport.You", Placeholder.c()
										.add("%warp%", warp(args[0])));
								return true;
							}
							Loader.sendMessages(s, "NoPerms", Placeholder.c()
									.add("%permission%", Loader.getPerm("Warp", "Warps", "PerWarp").replace("%warp%", warp(args[0]))));
							return true;
						}
						API.setBack(p);
						if (setting.tp_safe)
							API.safeTeleport(p, loc);
						else
							p.teleport(loc);
						Loader.sendMessages(p, "Warp.Teleport.You", Placeholder.c()
								.add("%warp%", warp(args[0])));
						Loader.sendMessages(s, "Warp.Teleport.Other.Sender", Placeholder.c()
								.add("%warp%", warp(args[0]))
								.add("%player%", p.getName())
								.add("%playername%", p.getDisplayName()));
						return true;
					}
					Loader.sendMessages(s, "Warp.NotExist", Placeholder.c()
							.add("%warp%", args[0]));
					return true;
				}
			}
			Loader.sendMessages(s, "Warp.Empty");
			return true;
		}
		Loader.noPerms(s, "Warp", "Warps");
		return true;
	}

	public String player(CommandSender s) {
		if (TheAPI.getPlayer(s.getName()) != null)
			return TheAPI.getPlayer(s.getName()).getDisplayName();
		return s.getName();
	}

	public List<String> warpss(CommandSender s) {
		List<String> w = new ArrayList<>();
		for (String st : Loader.config.getKeys("Warps")) {
			boolean needperm = Loader.config.getBoolean("Warps." + st + ".NeedPermission");
			if (!needperm || needperm && s.hasPermission(Loader.getPerm("Warp", "Warps", "PerWarp").replace("%warp%", st)))
				w.add(st);
		}
		return w;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Warp", "Warps")) {
			if (args.length == 1)
				return StringUtils.copyPartialMatches(args[0], warpss(s));
			if (args.length == 2 && Loader.has(s, "Warp", "Warps","Other"))
				return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
		}
		return Arrays.asList();
	}
}
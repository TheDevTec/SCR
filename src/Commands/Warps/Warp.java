package Commands.Warps;

import java.util.ArrayList; 
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Warp implements CommandExecutor, TabCompleter {

	public String warp(String[] args) {
		if (Loader.config.getString("Warps") != null)
			for (String s : Loader.config.getKeys("Warps")) {
				if (s.toLowerCase().equalsIgnoreCase(args[0])) {
					return s;
				}
			}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Warp", "Warps")) {
			if (Loader.config.getString("Warps") != null) {
				if (args.length == 0) {
					Loader.sendMessages(s, "Warp.List", Placeholder.c()
							.add("%warps%", StringUtils.join(warpss(s), ", ")));
					return true;
				}
				if (args.length == 1) {
					if (s instanceof Player) {
						if (warp(args) != null) {
							Location loc = StringUtils.getLocationFromString(Loader.config.getString("Warps." + warp(args)));
							if (loc == null) {
								Loader.sendMessages(s, "Warp.WrongLocation", Placeholder.c()
										.add("%warp%", warp(args)));
								return true;
							}
							boolean needperm = Loader.config.getBoolean("Warps." + warp(args) + ".NeedPermission");
							if (needperm == true) {
								if (s.hasPermission(Loader.cmds.getString("Warps.Warp.SubPermissions.PerWarp")+"."+warp(args))) {
									API.setBack((Player) s);
									if (setting.tp_safe)
										API.safeTeleport((Player)s, loc);
									else
										((Player) s).teleport(loc);
									Loader.sendMessages(s, "Warp.Teleport.You", Placeholder.c()
											.add("%warp%", warp(args)));
									return true;
								}
								Loader.sendMessages(s, "NoPerms", Placeholder.c().add("%permission%", Loader.cmds.getString("Warps.Warp.SubPermissions.PerWarp")+"."+warp(args)));
								return true;
							}
							API.setBack((Player) s);
							if (setting.tp_safe)
								API.safeTeleport((Player)s, loc);
							else
								((Player) s).teleport(loc);
							Loader.sendMessages(s, "Warp.Teleport.You", Placeholder.c()
									.add("%warp%", warp(args)));
							return true;
						}
						Loader.sendMessages(s, "Warp.NotExist", Placeholder.c()
								.add("%warp%", args[0]));
						return true;
					}
					return true;
				}
				if (args.length == 2) {
					Player p = TheAPI.getPlayer(args[1]);
					if (p == null) {
						Loader.notOnline(s, args[1]);
						return true;
					} else {
						if (warp(args) != null) {
							Location loc = StringUtils.getLocationFromString(Loader.config.getString("Warps." + warp(args)));
							if (loc == null) {
								Loader.sendMessages(s, "Warp.WrongLocation", Placeholder.c()
										.add("%warp%", warp(args)));
								return true;
							}
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport(p, loc);
							else
								p.teleport(loc);
							Loader.sendMessages(p, "Warp.Teleport.You", Placeholder.c()
									.add("%warp%", warp(args)));
							Loader.sendMessages(s, "Warp.Teleport.Other.Sender", Placeholder.c()
									.add("%warp%", warp(args))
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
		if (Loader.config.getString("Warps") != null)
			for (String st : Loader.config.getKeys("Warps")) {
				boolean needperm = Loader.config.getBoolean("Warps." + st + ".NeedPermission");
				String needperm2 = Loader.config.getString("Warps." + st + ".NeedPermission");
				if (s.hasPermission("ServerControl.Warp." + st) && needperm == true) {
					w.add(st);
				}
				if (needperm == false || needperm2 == null) {
					w.add(st);
				}
			}
		return w;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length == 1) {
			if (Loader.has(s, "Warp", "Warps")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], warpss(s), new ArrayList<>()));
			}
		}
		return c;
	}
}
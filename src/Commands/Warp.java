package Commands;

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
import Utils.setting;
import me.Straiker123.TheAPI;

public class Warp implements CommandExecutor, TabCompleter {

	public String warp(String[] args) {
		if (Loader.config.getString("Warps") != null)
			for (String s : Loader.config.getConfigurationSection("Warps").getKeys(false)) {
				if (s.toLowerCase().equalsIgnoreCase(args[0])) {
					return s;
				}
			}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (API.hasPerm(s, "ServerControl.Warp")) {
			if (Loader.config.getString("Warps") != null) {
				if (args.length == 0) {
					Loader.msg(Loader.s("Warp.List").replace("%warps%", TheAPI.getStringUtils().join(warpss(s), ", "))
							.replace("%player%", s.getName()).replace("%prefix%", Loader.s("Prefix")), s);
					return true;
				}
				if (args.length == 1) {
					if (s instanceof Player) {
						if (warp(args) != null) {
							Location loc = TheAPI.getStringUtils()
									.getLocationFromString(Loader.config.getString("Warps." + warp(args)));
							if (loc == null) {
								Loader.msg(Loader.s("Warp.CantGetLocation").replace("%warp%", warp(args))
										.replace("%world%", "-").replace("%player%", s.getName())
										.replace("%playername%", ((Player) s).getDisplayName())
										.replace("%prefix%", Loader.s("Prefix")), s);
								return true;
							}
							boolean needperm = Loader.config.getBoolean("Warps." + warp(args) + ".NeedPermission");
							if (needperm == true) {
								if (API.hasPerm(s, "ServerControl.Warp." + warp(args))) {
									API.setBack((Player) s);
									if (setting.tp_safe)
										TheAPI.getPlayerAPI((Player) s).safeTeleport(loc);
									else
										TheAPI.getPlayerAPI((Player) s).teleport(loc);
									Loader.msg(Loader.s("Warp.Warping").replace("%warp%", warp(args))
											.replace("%world%", loc.getWorld().getName())
											.replace("%player%", s.getName())
											.replace("%playername%", ((Player) s).getDisplayName())
											.replace("%prefix%", Loader.s("Prefix")), s);
									return true;
								}
								return true;
							}
							API.setBack((Player) s);
							if (setting.tp_safe)
								TheAPI.getPlayerAPI((Player) s).safeTeleport(loc);
							else
								TheAPI.getPlayerAPI((Player) s).teleport(loc);
							Loader.msg(Loader.s("Warp.Warping").replace("%warp%", warp(args))
									.replace("%world%", loc.getWorld().getName()).replace("%player%", s.getName())
									.replace("%playername%", ((Player) s).getDisplayName())
									.replace("%prefix%", Loader.s("Prefix")), s);
							return true;
						}
						Loader.msg(Loader.s("Warp.NotExists").replace("%warp%", args[0])
								.replace("%player%", s.getName()).replace("%playername%", ((Player) s).getDisplayName())
								.replace("%prefix%", Loader.s("Prefix")), s);
						return true;
					} else {
						Loader.msg(Loader.s("ConsoleErrorMessage"), s);
						return true;
					}
				}
				if (args.length == 2) {
					Player p = TheAPI.getPlayer(args[1]);
					if (p == null) {
						Loader.msg(Loader.PlayerNotOnline(args[1]), s);
						return true;
					} else {
						if (warp(args) != null) {
							Location loc = TheAPI.getStringUtils()
									.getLocationFromString(Loader.config.getString("Warps." + warp(args)));
							if (loc == null) {
								Loader.msg(Loader.s("Warp.CantGetLocation").replace("%warp%", warp(args))
										.replace("%world%", "-").replace("%player%", s.getName())
										.replace("%playername%", ((Player) s).getDisplayName())
										.replace("%prefix%", Loader.s("Prefix")), s);
								return true;
							}
							API.setBack(p);
							if (setting.tp_safe)
								TheAPI.getPlayerAPI(p).safeTeleport(loc);
							else
								TheAPI.getPlayerAPI(p).teleport(loc);
							Loader.msg(Loader.s("Warp.Warping").replace("%warp%", warp(args))
									.replace("%world%", loc.getWorld().getName()).replace("%player%", p.getName())
									.replace("%playername%", p.getDisplayName())
									.replace("%prefix%", Loader.s("Prefix")), p);
							Loader.msg(Loader.s("Warp.PlayerWarped").replace("%warp%", warp(args))
									.replace("%world%", loc.getWorld().getName()).replace("%player%", p.getName())
									.replace("%playername%", p.getDisplayName())
									.replace("%prefix%", Loader.s("Prefix")), s);
							return true;
						}
						Loader.msg(
								Loader.s("Warp.NotExists").replace("%warp%", args[0]).replace("%player%", s.getName())
										.replace("%playername%", player(s)).replace("%prefix%", Loader.s("Prefix")),
								s);
						return true;
					}
				}
				Loader.msg(Loader.s("Warp.NoWarps").replace("%prefix%", Loader.s("Prefix")), s);
				return true;
			}
		}
		Loader.msg(Loader.s("Warp.NoWarps").replace("%prefix%", Loader.s("Prefix")), s);
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
			for (String st : Loader.config.getConfigurationSection("Warps").getKeys(false)) {
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
			if (s.hasPermission("ServerControl.Warp")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], warpss(s), new ArrayList<>()));
			}
		}
		return c;
	}
}
package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI;

public class Tp implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Tp")) {
			if (args.length == 0) {
				Loader.Help(s, "/Tp <player|player x y z>", "TpaSystem.Tp");
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					Player target = TheAPI.getPlayer(args[0]);
					if (target == null) {
						if (TheAPI.getStringUtils().isInt(args[0])) {
							Loader.Help(s, "/Tp <x> <y> <z>", "TpaSystem.Tp");
							return true;
						} else {
							TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
							return true;
						}
					} else {
						if (!TheAPI.getUser(target).getBoolean("TpBlock." + s.getName())
								&& !TheAPI.getUser(target).getBoolean("TpBlock-Global")) {
							TheAPI.msg(Loader.s("Prefix")
									+ Loader.s("TpaSystem.Teleported").replace("%player%", target.getName())
											.replace("%playername%", target.getDisplayName()),
									s);
							API.setBack(((Player) s));
							if (setting.tp_safe)
								TheAPI.getPlayerAPI((Player) s).safeTeleport(target.getLocation());
							else ((Player) s).teleport(target);
							return true;
						} else {
							if (s.hasPermission("ServerControl.Tp.Blocked")) {
								TheAPI.msg(Loader.s("Prefix")
										+ Loader.s("TpaSystem.Teleported").replace("%player%", target.getName())
												.replace("%playername%", target.getDisplayName()),
										s);
								API.setBack(((Player) s));
								((Player) s).teleport(target);
								return true;
							} else {
								TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpBlocked")
										.replace("%playername%", target.getDisplayName())
										.replace("%player%", target.getName()), s);
								return true;
							}

						}

					}
				}
				if (TheAPI.getPlayer(args[0]) == null) {
					if (TheAPI.getStringUtils().isInt(args[0])) {
						Loader.Help(s, "/Tp <player> <x> <y> <z>", "TpaSystem.Tp");
						return true;
					} else if (s.hasPermission("ServerControl.Tp.Location"))
						Loader.Help(s, "/Tp <player player|player x y z>", "TpaSystem.Tp");
					else
						Loader.Help(s, "/Tp <player> <player>", "TpaSystem.Tp");
					return true;
				} else {
					if (s.hasPermission("ServerControl.Tp.Location"))
						Loader.Help(s, "/Tp <player player|player x y z>", "TpaSystem.Tp");
					else
						Loader.Help(s, "/Tp <player> <player>", "TpaSystem.Tp");
					return true;
				}
			}
			if (args.length == 2) {
				if (API.hasPerm(s, "ServerControl.Tp.Other")) {
					Player p0 = TheAPI.getPlayer(args[0]);
					Player p1 = TheAPI.getPlayer(args[1]);
					if (p1 == null) {
						if (s instanceof Player) {
							if (p0 != null && TheAPI.getStringUtils().isInt(args[1])) {
								if (s.hasPermission("ServerControl.Tp.Location"))
									Loader.Help(s, "/Tp <player player|player x y z>", "TpaSystem.Tp");
								else
									Loader.Help(s, "/Tp <player> <player>", "TpaSystem.Tp");
								return true;
							} else if (p0 == null && TheAPI.getStringUtils().isInt(args[1])) {
								if (s.hasPermission("ServerControl.Tp.Location"))
									Loader.Help(s, "/Tp <player player|player x y z>", "TpaSystem.Tp");
								else
									Loader.Help(s, "/Tp <player> <player>", "TpaSystem.Tp");
								return true;
	
							} else {
								TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
								return true;
							}
						}
						if (s.hasPermission("ServerControl.Tp.Location"))
							Loader.Help(s, "/Tp <player player|player x y z>", "TpaSystem.Tp");
						else
							Loader.Help(s, "/Tp <player> <player>", "TpaSystem.Tp");
						return true;
					} else {
						String player = args[0];
						if (p0 != null)
							player = p0.getName();
						String playername = args[0];
						if (p0 != null)
							playername = p0.getDisplayName();
						String player1 = args[1];
						if (p1 != null)
							player1 = p1.getName();
						String playername1 = args[1];
						if (p1 != null)
							playername1 = p1.getDisplayName();
						TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpPlayerToPlayer")
								.replace("%firstplayer%", player).replace("%firstplayername%", playername)
								.replace("%lastplayer%", player1).replace("%lastplayername%", playername1), s);
						API.setBack(p0);
						if (setting.tp_safe)
						TheAPI.getPlayerAPI(p0).safeTeleport(p1.getLocation());
						else
							p0.teleport(p1.getLocation());
						return true;
	
					}
				}
			}
			if (args.length == 3) {
				if (API.hasPerm(s, "ServerControl.Tp.Location")) {
				
					Player p = TheAPI.getPlayer(args[0]);
					if (p == null) {
						if (TheAPI.getStringUtils().isInt(args[0]) && TheAPI.getStringUtils().isInt(args[1])
								&& TheAPI.getStringUtils().isInt(args[2])) {
							if (s instanceof Player) {
								TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpLocation")
										.replace("%playername%", ((Player) s).getDisplayName())
										.replace("%player%", ((Player) s).getName())
										.replace("%world%", ((Player) s).getWorld().getName()).replace("%x%", args[0])
										.replace("%y%", args[1]).replace("%z%", args[2]), s);
								Location loc = new Location(((Player) s).getWorld(),
										TheAPI.getStringUtils().getDouble(args[0]),
										TheAPI.getStringUtils().getDouble(args[1]),
										TheAPI.getStringUtils().getDouble(args[2]));

								API.setBack(((Player) s));
								if (setting.tp_safe)
									TheAPI.getPlayerAPI((Player) s).safeTeleport(loc);
								else ((Player) s).teleport(loc);
								return true;
							} else {
								if (s.hasPermission("ServerControl.Tp.Location"))
									Loader.Help(s, "/Tp <player player|player x y z>", "TpaSystem.Tp");
								else
									Loader.Help(s, "/Tp <player> <player>", "TpaSystem.Tp");
								return true;
							}
						} else if (s.hasPermission("ServerControl.Tp.Location"))
							Loader.Help(s, "/Tp <player player|player x y z>", "TpaSystem.Tp");
						else
							Loader.Help(s, "/Tp <player> <player>", "TpaSystem.Tp");
						return true;
					} else if (s.hasPermission("ServerControl.Tp.Location"))
						Loader.Help(s, "/Tp <player player|player x y z>", "TpaSystem.Tp");
					else
						Loader.Help(s, "/Tp <player> <player>", "TpaSystem.Tp");
					return true;
				}
			}
			if (args.length == 4) {
				if (API.hasPerm(s, "ServerControl.Tp.Location")) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						if (TheAPI.getStringUtils().isInt(args[1]) && TheAPI.getStringUtils().isInt(args[2])
								&& TheAPI.getStringUtils().isInt(args[3])) {
							TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpLocationPlayer")
									.replace("%world%", p.getWorld().getName())
									.replace("%playername%", p.getDisplayName()).replace("%player%", p.getName())
									.replace("%x%", args[1]).replace("%y%", args[2]).replace("%z%", args[3]), s);
							API.setBack(p);
							if (setting.tp_safe)
								TheAPI.getPlayerAPI(p)
								.safeTeleport(new Location(p.getWorld(), TheAPI.getStringUtils().getDouble(args[1]),
										TheAPI.getStringUtils().getDouble(args[2]),
										TheAPI.getStringUtils().getDouble(args[3])));
							else p.teleport(new Location(p.getWorld(), TheAPI.getStringUtils().getDouble(args[1]),
									TheAPI.getStringUtils().getDouble(args[2]),
									TheAPI.getStringUtils().getDouble(args[3])));
							return true;
						}
					} else {
						if (s.hasPermission("ServerControl.Tp.Location"))
							Loader.Help(s, "/Tp <player player|player x y z>", "TpaSystem.Tp");
						else
							Loader.Help(s, "/Tp <player> <player>", "TpaSystem.Tp");
						return true;
					}
				}
			}
			return true;

		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length == 1 || args.length == 2)
			return null;
		return c;
	}

}

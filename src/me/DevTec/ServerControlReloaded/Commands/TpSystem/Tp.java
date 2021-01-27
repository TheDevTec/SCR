package me.DevTec.ServerControlReloaded.Commands.TpSystem;

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

public class Tp implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Tp","TpSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "Tp", "TpSystem");
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					Player target = TheAPI.getPlayer(args[0]);
					if (target == null) {
						if (StringUtils.isInt(args[0])) {
							Loader.Help(s, "Tp", "TpSystem");
							return true;
						} else {
							Loader.notOnline(s,args[0]);
							return true;
						}
					} else {
						if (Loader.has(s, "Tp", "TpSystem", "Blocked") || !Loader.has(s, "Tp", "TpSystem", "Blocked") && !RequestMap.isBlocking(target.getName(), s.getName())) {
							Loader.sendMessages(s, "TpSystem.Tp.Player.YouToPlayer", Placeholder.c().replace("%player%", target.getName()));
							API.setBack(((Player) s));
							if (setting.tp_safe)
								API.safeTeleport((Player) s,target.getLocation());
							else ((Player) s).teleport(target);
							return true;
						}
						Loader.sendMessages(s, "TpSystem.Block.IsBlocked.Teleport", Placeholder.c().replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()));
						return true;
					}
				}
				Loader.Help(s, "Tp", "TpSystem");
				return true;
			}
			if (args.length == 2) {
				if (Loader.has(s, "Tp","TpSystem","Other")) {
					Player p0 = TheAPI.getPlayer(args[0]);
					if(p0==null) {
						Loader.notOnline(s,args[0]);
						return true;
					}
					Player p1 = TheAPI.getPlayer(args[1]);
					if (p1 == null) {
						if (s instanceof Player) {
							if (StringUtils.isInt(args[1])) {
								Loader.Help(s, "Tp", "TpSystem");
								return true;
							} else {
								Loader.notOnline(s,args[1]);
								return true;
							}
						}
						Loader.Help(s, "Tp", "TpSystem");
						return true;
					} else {
						String player = p0.getName();
						String playername = p0.getDisplayName();
						String player1 = p1.getName();
						String playername1 = p1.getDisplayName();
						Loader.sendMessages(s, "TpSystem.Tp.Player.PlayerToNextPlayer", Placeholder.c()
								.replace("%player%", player)
								.replace("%playername%", playername)
								.replace("%next-player%", player1)
								.replace("%next-playername%", playername1));
						API.setBack(p0);
						if (setting.tp_safe)
							API.safeTeleport(p0,p1.getLocation());
						else
							p0.teleport(p1.getLocation());
						return true;
					}
				}
				Loader.noPerms(s, "Tp", "TpSystem","Other");
				return true;
			}
			if (args.length == 3) {
				if (Loader.has(s, "Tp","TpSystem","Location")) {
					Player p = (Player)s;
					if (StringUtils.isInt(args[0]) && StringUtils.isInt(args[1])
							&& StringUtils.isInt(args[2])) {
						if (s instanceof Player) {
							p=(Player)s;
						Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
								.replace("%x%", String.format("%2.02f", StringUtils.getDouble(args[0])).replaceFirst("\\.00", ""))
								.replace("%y%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
								.replace("%z%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
								.replace("%yaw%", "0").replace("%pitch%", "0"));
						API.setBack(p);
						if (setting.tp_safe)
							API.safeTeleport(p,new Location(p.getWorld(), StringUtils.getDouble(args[0]), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), 0, 0));
						else p.teleport(new Location(p.getWorld(), StringUtils.getDouble(args[0]), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), 0, 0));
						return true;
						}}else{Loader.Help(s, "Tp", "TpSystem");return true;}
						Loader.Help(s, "Tp", "TpSystem");
						return true;
				}
				Loader.noPerms(s, "Tp", "TpSystem","Location");
				return true;
			}
			if (args.length == 4) {
				if(Loader.has(s, "Tp", "TpSystem","Location")&&StringUtils.isDouble(args[0])&&StringUtils.isDouble(args[1])&&StringUtils.isDouble(args[2])&&StringUtils.isFloat(args[3])) {
					Player p = (Player)s;
					Loader.sendMessages(s, "TpSystem.Tp.Location.YouToLocation", Placeholder.c().add("%player%", s.getName())
							.add("%x%", String.format("%2.02f", StringUtils.getDouble(args[0])).replaceFirst("\\.00", ""))
							.add("%y%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
							.add("%z%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
							.add("%yaw%", String.format("%2.02f", StringUtils.getFloat(args[3])).replaceFirst("\\.00", ""))
							.add("%pitch%", "0"));
					
					API.setBack((Player) s);
					if(setting.tp_safe)						
						API.safeTeleport((Player)s, new Location (p.getWorld(), StringUtils.getDouble(args[0]), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), (float)StringUtils.getDouble(args[3]), 0)); 
					else p.teleport(new Location (p.getWorld(), StringUtils.getDouble(args[0]), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), (float)StringUtils.getDouble(args[3]), 0)); return true;
				}
				if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						if (StringUtils.isDouble(args[1]) && StringUtils.isDouble(args[2])
								&& StringUtils.isDouble(args[3])) {
							Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%x%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
									.replace("%y%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
									.replace("%z%", String.format("%2.02f", StringUtils.getDouble(args[3])).replaceFirst("\\.00", ""))
									.add("%yaw%", "0")
									.add("%pitch%", "0"));
							Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
									.replace("%x%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
									.replace("%y%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
									.replace("%z%", String.format("%2.02f", StringUtils.getDouble(args[3])).replaceFirst("\\.00", ""))
									.add("%yaw%", "0")
									.add("%pitch%", "0"));
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport(p,new Location(p.getWorld(), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), StringUtils.getDouble(args[3]), 0, 0));
							else p.teleport(new Location(p.getWorld(), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), StringUtils.getDouble(args[3]), 0, 0));
							return true;
						}
					}else {
						Loader.Help(s, "Tp", "TpSystem");
						return true;
					}
					Loader.Help(s, "Tp", "TpSystem");
					return true;
				}				
				Loader.noPerms(s, "Tp", "TpSystem","LocationOther");
				return true;				
			}
			if (args.length == 5) {
				if(Loader.has(s, "Tp", "TpSystem","Location")&&StringUtils.isDouble(args[0])&&StringUtils.isDouble(args[1])&&StringUtils.isDouble(args[2])&&StringUtils.isFloat(args[3])) {
					Player p = (Player)s;
					Loader.sendMessages(s, "TpSystem.Tp.Location.YouToLocation", Placeholder.c().add("%player%", s.getName())
							.add("%x%", String.format("%2.02f", StringUtils.getDouble(args[0])).replaceFirst("\\.00", ""))
							.add("%y%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
							.add("%z%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
							.add("%yaw%", String.format("%2.02f", StringUtils.getFloat(args[3])).replaceFirst("\\.00", ""))
							.add("%pitch%", String.format("%2.02f", StringUtils.getFloat(args[4])).replaceFirst("\\.00", "")));
					
					API.setBack((Player) s);
					if(setting.tp_safe)						
						API.safeTeleport((Player)s, new Location (p.getWorld(), StringUtils.getDouble(args[0]), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), (float)StringUtils.getDouble(args[3]), (float)StringUtils.getDouble(args[4]))); 
					else p.teleport(new Location (p.getWorld(), StringUtils.getDouble(args[0]), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), (float)StringUtils.getDouble(args[3]), (float)StringUtils.getDouble(args[4]))); return true;
				}
				if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						if (StringUtils.isDouble(args[1]) && StringUtils.isDouble(args[2])
								&& StringUtils.isDouble(args[3])) {
							Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%x%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
									.replace("%y%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
									.replace("%z%", String.format("%2.02f", StringUtils.getDouble(args[3])).replaceFirst("\\.00", ""))
									.add("%yaw%", String.format("%2.02f", StringUtils.getFloat(args[4])).replaceFirst("\\.00", ""))
									.add("%pitch%", "0"));
							Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
									.replace("%x%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
									.replace("%y%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
									.replace("%z%", String.format("%2.02f", StringUtils.getDouble(args[3])).replaceFirst("\\.00", ""))
									.add("%yaw%", String.format("%2.02f", StringUtils.getFloat(args[4])).replaceFirst("\\.00", ""))
									.add("%pitch%", "0"));
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport(p,new Location(p.getWorld(), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), StringUtils.getDouble(args[3]), (float)StringUtils.getDouble(args[4]), 0));
							else p.teleport(new Location(p.getWorld(), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), StringUtils.getDouble(args[3]), (float)StringUtils.getDouble(args[4]), 0));
							return true;
						}
					}else {
						Loader.Help(s, "Tp", "TpSystem");
						return true;
					}
					Loader.Help(s, "Tp", "TpSystem");
					return true;
				}				
				Loader.noPerms(s, "Tp", "TpSystem","LocationOther");
				return true;				
			}
			if(Loader.has(s, "Tp", "TpSystem","Location")&&StringUtils.isDouble(args[0])&&StringUtils.isDouble(args[1])&&StringUtils.isDouble(args[2])&&StringUtils.isFloat(args[3])) {
				Player p = (Player)s;
				Loader.sendMessages(s, "TpSystem.Tp.Location.YouToLocation", Placeholder.c().add("%player%", s.getName())
						.add("%x%", String.format("%2.02f", StringUtils.getDouble(args[0])).replaceFirst("\\.00", ""))
						.add("%y%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
						.add("%z%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
						.add("%yaw%", String.format("%2.02f", StringUtils.getFloat(args[3])).replaceFirst("\\.00", ""))
						.add("%pitch%", String.format("%2.02f", StringUtils.getFloat(args[4])).replaceFirst("\\.00", "")));
				
				API.setBack((Player) s);
				if(setting.tp_safe)						
					API.safeTeleport((Player)s, new Location (p.getWorld(), StringUtils.getDouble(args[0]), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), (float)StringUtils.getDouble(args[3]), (float)StringUtils.getDouble(args[4]))); 
				else p.teleport(new Location (p.getWorld(), StringUtils.getDouble(args[0]), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), (float)StringUtils.getDouble(args[3]), (float)StringUtils.getDouble(args[4]))); return true;
			}
			if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null) {
					if (StringUtils.isDouble(args[1]) && StringUtils.isDouble(args[2])
							&& StringUtils.isDouble(args[3])) {
						Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName())
								.replace("%x%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
								.replace("%y%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
								.replace("%z%", String.format("%2.02f", StringUtils.getDouble(args[3])).replaceFirst("\\.00", ""))
								.add("%yaw%", String.format("%2.02f", StringUtils.getFloat(args[4])).replaceFirst("\\.00", ""))
								.add("%pitch%", String.format("%2.02f", StringUtils.getFloat(args[5])).replaceFirst("\\.00", "")));
						Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
								.replace("%x%", String.format("%2.02f", StringUtils.getDouble(args[1])).replaceFirst("\\.00", ""))
								.replace("%y%", String.format("%2.02f", StringUtils.getDouble(args[2])).replaceFirst("\\.00", ""))
								.replace("%z%", String.format("%2.02f", StringUtils.getDouble(args[3])).replaceFirst("\\.00", ""))
								.add("%yaw%", String.format("%2.02f", StringUtils.getFloat(args[4])).replaceFirst("\\.00", ""))
								.add("%pitch%", String.format("%2.02f", StringUtils.getFloat(args[5])).replaceFirst("\\.00", "")));
						API.setBack(p);
						if (setting.tp_safe)
							API.safeTeleport(p,new Location(p.getWorld(), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), StringUtils.getDouble(args[3]), (float)StringUtils.getDouble(args[4]), (float)StringUtils.getDouble(args[5])));
						else p.teleport(new Location(p.getWorld(), StringUtils.getDouble(args[1]), StringUtils.getDouble(args[2]), StringUtils.getDouble(args[3]), (float)StringUtils.getDouble(args[4]), (float)StringUtils.getDouble(args[5])));
						return true;
					}
				}else {
					Loader.Help(s, "Tp", "TpSystem");
					return true;
				}
				Loader.Help(s, "Tp", "TpSystem");
				return true;
			}				
			Loader.noPerms(s, "Tp", "TpSystem","LocationOther");
			return true;
		}
		Loader.noPerms(s, "Tp", "TpSystem");
		return true;
	}
}

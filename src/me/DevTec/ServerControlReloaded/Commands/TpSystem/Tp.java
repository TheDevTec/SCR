package me.DevTec.ServerControlReloaded.Commands.TpSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;

public class Tp implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "Tp","TpSystem")) {
		if(args.length==1) {
			List<String> c = new ArrayList<>();
			if(Loader.has(s, "Tp","TpSystem","Location")) {
			if(s instanceof ConsoleCommandSender == false) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				c.add(StringUtils.fixedFormatDouble(d.getX())+" "+StringUtils.fixedFormatDouble(d.getY())+" "+StringUtils.fixedFormatDouble(d.getZ()));
				c.add(StringUtils.fixedFormatDouble(d.getX())+" "+StringUtils.fixedFormatDouble(d.getY()));
				c.add(StringUtils.fixedFormatDouble(d.getX()));
				c.add("~ ~ ~");
				c.add("~ ~");
				c.add("~");
			}else {
				c.add("X Y Z");
				c.add("X Y");
				c.add("X");
			}}
			if(Loader.has(s, "Tp","TpSystem","Other"))
			c.addAll(API.getPlayerNames(s));
			return StringUtils.copyPartialMatches(args[0], c);
		}
		if(args.length==2) {
			List<String> c = new ArrayList<>();
			if(s instanceof ConsoleCommandSender == false) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					if(Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther")) {
					c.add(StringUtils.fixedFormatDouble(d.getX())+" "+StringUtils.fixedFormatDouble(d.getY())+" "+StringUtils.fixedFormatDouble(d.getZ()));
					c.add(StringUtils.fixedFormatDouble(d.getX())+" "+StringUtils.fixedFormatDouble(d.getY()));
					c.add(StringUtils.fixedFormatDouble(d.getX()));
					c.add("~ ~ ~");
					c.add("~ ~");
					c.add("~");
					}
					if(Loader.has(s, "Tp","TpSystem","Other"))
					c.addAll(API.getPlayerNames(s));
				}else {
					if(Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther")) {
					c.add(StringUtils.fixedFormatDouble(d.getY())+" "+StringUtils.fixedFormatDouble(d.getZ()));
					c.add(StringUtils.fixedFormatDouble(d.getY()));
					c.add("~ ~");
					c.add("~");
					}
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.addAll(API.getPlayerNames(s));
					c.add("X Y Z");
					c.add("X Y");
					c.add("X");
				}else {
					c.add("Y Z");
					c.add("Y");
				}
			}
			return StringUtils.copyPartialMatches(args[1], c);
		}
		if(args.length==3 && (Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther"))) {
			List<String> c = new ArrayList<>();
			if(s instanceof ConsoleCommandSender == false) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add(StringUtils.fixedFormatDouble(d.getY())+" "+StringUtils.fixedFormatDouble(d.getZ()));
					c.add(StringUtils.fixedFormatDouble(d.getY()));
					c.add("~ ~");
					c.add("~");
				}else {
					c.add(StringUtils.fixedFormatDouble(d.getZ()));
					c.add("~");
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add("Y Z");
					c.add("Y");
				}else {
					c.add("Z");
				}
			}
			return StringUtils.copyPartialMatches(args[2], c);
		}
		if(args.length==4 && (Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther"))) {
			List<String> c = new ArrayList<>();
			if(s instanceof ConsoleCommandSender == false) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add(StringUtils.fixedFormatDouble(d.getZ()));
					c.add("~");
				}else {
					c.add(StringUtils.fixedFormatDouble(d.getYaw())+" "+StringUtils.fixedFormatDouble(d.getPitch()));
					c.add(StringUtils.fixedFormatDouble(d.getYaw()));
					c.add("~ ~");
					c.add("~");
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add("Z");
				}else {
					c.add("YAW PITCH");
					c.add("YAW");
				}
			}
			return StringUtils.copyPartialMatches(args[3], c);
		}
		if(args.length==5 && (Loader.has(s, "Tp","TpSystem","Location")||Loader.has(s, "Tp","TpSystem","LocationOther"))) {
			List<String> c = new ArrayList<>();
			if(s instanceof ConsoleCommandSender == false) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add(StringUtils.fixedFormatDouble(d.getYaw())+" "+StringUtils.fixedFormatDouble(d.getPitch()));
					c.add(StringUtils.fixedFormatDouble(d.getYaw()));
					c.add("~ ~");
					c.add("~");
				}else {
					c.add(StringUtils.fixedFormatDouble(d.getPitch()));
					c.add("~");
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add("YAW PITCH");
					c.add("YAW");
				}else {
					c.add("PITCH");
				}
			}
			return StringUtils.copyPartialMatches(args[4], c);
		}
		if(args.length==6 && Loader.has(s, "Tp","TpSystem","LocationOther")) {
			List<String> c = new ArrayList<>();
			if(s instanceof ConsoleCommandSender == false) {
				Location d = s instanceof BlockCommandSender ? ((BlockCommandSender)s).getBlock().getLocation() : ((Player)s).getLocation();
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add(StringUtils.fixedFormatDouble(d.getPitch()));
					c.add("~");
				}
			}else {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					c.add("PITCH");
				}
			}
			return StringUtils.copyPartialMatches(args[5], c);
		}
		}
		return Arrays.asList();
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
					if (target == null || !API.getPlayers(s).contains(target)) {
						Loader.Help(s, "Tp", "TpSystem");
						return true;
					} else {
						if (Loader.has(s, "TpToggle", "TpSystem", "Bypass") || !Loader.has(s, "TpToggle", "TpSystem", "Bypass") && !RequestMap.isBlocking(s.getName(), target.getName())) {
							Loader.sendMessages(s, "TpSystem.Tp.Player.YouToPlayer", Placeholder.c().replace("%player%", target.getName()));
							API.setBack(((Player) s));
							if (setting.tp_safe)
								API.safeTeleport((Player) s,new Position(target.getLocation()));
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
					if(p0==null || !API.getPlayers(s).contains(p0)) {
						Loader.notOnline(s,args[0]);
						return true;
					}
					Player p1 = TheAPI.getPlayer(args[1]);
					if (p1 == null || !API.getPlayers(s).contains(p1)) {
						if (s instanceof Player) {
							Loader.Help(s, "Tp", "TpSystem");
							return true;
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
							API.safeTeleport(p0,new Position(p1.getLocation()));
						else
							p0.teleport(p1.getLocation());
						return true;
					}
				}
				Loader.noPerms(s, "Tp", "TpSystem","Other");
				return true;
			}
			if (args.length == 3) {
				if (Loader.has(s, "Tp","TpSystem","Location") && s instanceof Player) {
					Player p = (Player)s;
						if (s instanceof Player) {
							p=(Player)s;
							double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+"")).doubleValue()
							, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getY()+"")).doubleValue(), 
							z=StringUtils.calculate(args[2].replace("~", p.getLocation().getZ()+"")).doubleValue();
						Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
								.replace("%x%", StringUtils.fixedFormatDouble(x))
								.replace("%y%", StringUtils.fixedFormatDouble(y))
								.replace("%z%", StringUtils.fixedFormatDouble(z))
								.replace("%yaw%", "0").replace("%pitch%", "0"));
						API.setBack(p);
						if (setting.tp_safe)
							API.safeTeleport(p,new Position(p.getWorld(), x, y, z, 0, 0));
						else p.teleport(new Location(p.getWorld(), x, y, z, 0, 0));
						return true;
						}
						Loader.Help(s, "Tp", "TpSystem");
						return true;
				}
				Loader.noPerms(s, "Tp", "TpSystem","Location");
				return true;
			}
			if (args.length == 4) {
				if(Loader.has(s, "Tp", "TpSystem","Location") && s instanceof Player) {
					Player p = (Player)s;
					double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+"")).doubleValue()
					, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getY()+"")).doubleValue(), 
					z=StringUtils.calculate(args[2].replace("~", p.getLocation().getZ()+"")).doubleValue();
					float yaw = StringUtils.calculate(args[3].replace("~", p.getLocation().getYaw()+"")).floatValue();
					Loader.sendMessages(s, "TpSystem.Tp.Location.YouToLocation", Placeholder.c().add("%player%", s.getName())
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
							.replace("%pitch%", "0"));
					
					API.setBack((Player) s);
					if(setting.tp_safe)						
						API.safeTeleport((Player)s, new Position(p.getWorld(), x, y, z, yaw, 0)); 
					else p.teleport(new Location(p.getWorld(), x, y, z, yaw, 0)); return true;
				}
				if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null && API.getPlayers(s).contains(p)) {
						double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+"")).doubleValue()
								, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getY()+"")).doubleValue(), 
								z=StringUtils.calculate(args[2].replace("~", p.getLocation().getZ()+"")).doubleValue();
							Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%x%", StringUtils.fixedFormatDouble(x))
									.replace("%y%", StringUtils.fixedFormatDouble(y))
									.replace("%z%", StringUtils.fixedFormatDouble(z))
									.add("%yaw%", "0")
									.add("%pitch%", "0"));
							Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
									.replace("%x%", StringUtils.fixedFormatDouble(x))
									.replace("%y%", StringUtils.fixedFormatDouble(y))
									.replace("%z%", StringUtils.fixedFormatDouble(z))
									.add("%yaw%", "0")
									.add("%pitch%", "0"));
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport(p,new Position(p.getWorld(), x, y, z, 0, 0));
							else p.teleport(new Location(p.getWorld(), x, y, z, 0, 0));
							return true;
					}
					Loader.Help(s, "Tp", "TpSystem");
					return true;
				}				
				Loader.noPerms(s, "Tp", "TpSystem","LocationOther");
				return true;				
			}
			if (args.length == 5) {
				if(Loader.has(s, "Tp", "TpSystem","Location") && s instanceof Player) {
					Player p = (Player)s;
					double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+"")).doubleValue()
					, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getZ()+"")).doubleValue(), 
					z=StringUtils.calculate(args[2].replace("~", p.getLocation().getY()+"")).doubleValue();
					float yaw = StringUtils.calculate(args[3].replace("~", p.getLocation().getYaw()+"")).floatValue(),
							pitch = StringUtils.calculate(args[4].replace("~", p.getLocation().getPitch()+"")).floatValue();
					Loader.sendMessages(s, "TpSystem.Tp.Location.YouToLocation", Placeholder.c().add("%player%", s.getName())
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
							.replace("%pitch%", StringUtils.fixedFormatDouble(pitch)));
					
					API.setBack((Player) s);
					if(setting.tp_safe)						
						API.safeTeleport((Player)s, new Position(p.getWorld(), x, y, z, yaw, pitch)); 
					else p.teleport(new Location(p.getWorld(), x, y, z, yaw, pitch)); return true;
				}
				if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null && API.getPlayers(s).contains(p)) {
						double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+"")).doubleValue()
							, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getY()+"")).doubleValue(), 
							z=StringUtils.calculate(args[2].replace("~", p.getLocation().getZ()+"")).doubleValue();
							float yaw = StringUtils.calculate(args[3].replace("~", p.getLocation().getYaw()+"")).floatValue();
							Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%x%", StringUtils.fixedFormatDouble(x))
									.replace("%y%", StringUtils.fixedFormatDouble(y))
									.replace("%z%", StringUtils.fixedFormatDouble(z))
									.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
									.replace("%pitch%", "0"));
							Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
									.replace("%x%", StringUtils.fixedFormatDouble(x))
									.replace("%y%", StringUtils.fixedFormatDouble(y))
									.replace("%z%", StringUtils.fixedFormatDouble(z))
									.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
									.replace("%pitch%", "0"));
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport(p,new Position(p.getWorld(), x, y, z, yaw, 0));
							else p.teleport(new Location(p.getWorld(), x, y, z, yaw, 0));
							return true;
					}
					Loader.Help(s, "Tp", "TpSystem");
					return true;
				}				
				Loader.noPerms(s, "Tp", "TpSystem","LocationOther");
				return true;				
			}
			
			if (Loader.has(s, "Tp","TpSystem","LocationOther")) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null && API.getPlayers(s).contains(p)) {
					double x=StringUtils.calculate(args[0].replace("~", p.getLocation().getX()+"")).doubleValue()
					, y=StringUtils.calculate(args[1].replace("~", p.getLocation().getY()+"")).doubleValue(), 
					z=StringUtils.calculate(args[2].replace("~", p.getLocation().getZ()+"")).doubleValue();
					float yaw = StringUtils.calculate(args[3].replace("~", p.getLocation().getYaw()+"")).floatValue(),
							pitch = StringUtils.calculate(args[4].replace("~", p.getLocation().getPitch()+"")).floatValue();
					Loader.sendMessages(s, "TpSystem.Tp.Location.PlayerToLocation", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName())
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
							.replace("%pitch%", StringUtils.fixedFormatDouble(pitch)));
					Loader.sendMessages(p, "TpSystem.Tp.Location.YouToLocation", Placeholder.c()
							.replace("%x%", StringUtils.fixedFormatDouble(x))
							.replace("%y%", StringUtils.fixedFormatDouble(y))
							.replace("%z%", StringUtils.fixedFormatDouble(z))
							.replace("%yaw%", StringUtils.fixedFormatDouble(yaw))
							.replace("%pitch%", StringUtils.fixedFormatDouble(pitch)));
					API.setBack(p);
					if (setting.tp_safe)
						API.safeTeleport(p,new Position(p.getWorld(), x, y, z, yaw, pitch));
					else p.teleport(new Location(p.getWorld(), x, y, z, yaw, pitch));
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

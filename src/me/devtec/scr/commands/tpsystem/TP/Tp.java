package me.devtec.scr.commands.tpsystem.TP;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.tpsystem.TpSystem;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;
import me.devtec.shared.utility.StringUtils.FormatType;

public class Tp implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if(hasPermission(s, "other"))
				help(s, "other");
			else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		.fallback((s, structure, args) -> { // /tp [player]
					offlinePlayer(s, args[0]);
		})
		.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // tp [player]
			for(Player p : playerSelectors(s, args[0])) {
				TpSystem.teleport((Player)s, p);
				msgSec(s, "tp", Placeholders.c().addPlayer("player", p).addPlayer("target", p));
			}
		})
			.argument("-s", (s, structure, args) -> { // tp [player] -s
				for(Player p : playerSelectors(s, args[0]))
					TpSystem.teleport((Player)s, p);
			})
		.parent() // tp [player]
		
			.fallback((s, structure, args) -> { // /tp [player] [target]
				offlinePlayer(s, args[0]);
			})
			.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // tp [player] [target]
				for(Player who : playerSelectors(s, args[0]))
					for(Player to : playerSelectors(s, args[1])) {
						TpSystem.teleport(who, to);
						msgSec(s, "tptarget", Placeholders.c().addPlayer("player", who).addPlayer("target", to));
					}
			})
			.permission(permission("other"))
				.argument("-s", (s, structure, args) -> { // tp [player]  [target] -s
					for(Player who : playerSelectors(s, args[0]))
						for(Player to : playerSelectors(s, args[1])) {
							TpSystem.teleport(who, to);
						}
				})
				.parent() // tp [player] [target]
			.parent() // tp [player]
			
			/*
			 * tp [player] [location]
			 */
			
			.fallback((s, structure, args) -> {
				msg(s, "missing.number", Placeholders.c().replace("number", args[1]));
			})
			.selector(Selector.NUMBER, (s, structure, args) -> { // tp [player] [X]
				for(Player p : playerSelectors(s, args[0])) {
					Location loc = p.getLocation();
					loc.setX(StringUtils.getDouble(args[1]));
					TpSystem.teleport(p, loc);
					msgSec(p, "tploc", Placeholders.c()
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					msgSec(s, "tploctarget", Placeholders.c().addPlayer("player", p)
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
				}
			}, (s, structure, args) -> {
				List<String> l = new ArrayList<>();
				for(Player p : playerSelectors(s, args[0])) {
					l.add(""+p.getLocation().getBlockX());
				}
				return l;
			})
			.permission(permission("location"))
			.fallback((s, structure, args) -> {
				msg(s, "missing.number", Placeholders.c().replace("number", args[2]));
			})
			.selector(Selector.NUMBER, (s, structure, args) -> { // tp [player] [X] [Y]
				for(Player p : playerSelectors(s, args[0])) {
					Location loc = p.getLocation();
					loc.setX(StringUtils.getDouble(args[1]));
					loc.setY(StringUtils.getDouble(args[2]));
					TpSystem.teleport(p, loc);
					msgSec(p, "tploc", Placeholders.c()
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					msgSec(s, "tploctarget", Placeholders.c().addPlayer("player", p)
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					}
			}, (s, structure, args) -> {
				List<String> l = new ArrayList<>();
				for(Player p : playerSelectors(s, args[0])) {
					l.add(""+p.getLocation().getBlockY());
				}
				return l;
			})
			.fallback((s, structure, args) -> {
				msg(s, "missing.number", Placeholders.c().replace("number", args[3]));
			})
			.selector(Selector.NUMBER, (s, structure, args) -> { // tp [player] [X] [Y] [Z]
				for(Player p : playerSelectors(s, args[0])) {
					Location loc = p.getLocation();
					loc.setX(StringUtils.getDouble(args[1]));
					loc.setY(StringUtils.getDouble(args[2]));
					loc.setZ(StringUtils.getDouble(args[3]));
					TpSystem.teleport(p, loc);
					msgSec(p, "tploc", Placeholders.c()
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					msgSec(s, "tploctarget", Placeholders.c().addPlayer("player", p)
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					}
			}, (s, structure, args) -> {
				List<String> l = new ArrayList<>();
				for(Player p : playerSelectors(s, args[0])) {
					l.add(""+p.getLocation().getBlockZ());
				}
				return l;
			})
			.fallback((s, structure, args) -> {
				msg(s, "missing.world", Placeholders.c().replace("world", args[4]));
			})
			.selector(Selector.WORLD, (s, structure, args) -> { // tp [player] [X] [Y] [Z] [WORLD]
				for(Player p : playerSelectors(s, args[0])) {
					Location loc = p.getLocation();
					loc.setX(StringUtils.getDouble(args[1]));
					loc.setY(StringUtils.getDouble(args[2]));
					loc.setZ(StringUtils.getDouble(args[3]));
					loc.setWorld(Bukkit.getWorld(args[4]));
					TpSystem.teleport(p, loc);
					msgSec(p, "tploc", Placeholders.c()
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					msgSec(s, "tploctarget", Placeholders.c().addPlayer("player", p)
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					}
			})
			.fallback((s, structure, args) -> {
				msg(s, "missing.number", Placeholders.c().replace("number", args[5]));
			})
			.selector(Selector.NUMBER, (s, structure, args) -> { // tp [player] [X] [Y] [Z] [WORLD] [YAW]
				for(Player p : playerSelectors(s, args[0])) {
					Location loc = p.getLocation();
					loc.setX(StringUtils.getDouble(args[1]));
					loc.setY(StringUtils.getDouble(args[2]));
					loc.setZ(StringUtils.getDouble(args[3]));
					loc.setWorld(Bukkit.getWorld(args[4]));
					loc.setYaw(StringUtils.getFloat(args[5]));
					TpSystem.teleport(p, loc);
					msgSec(p, "tploc", Placeholders.c()
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					msgSec(s, "tploctarget", Placeholders.c().addPlayer("player", p)
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					}
			}, (s, structure, args) -> {
				List<String> l = new ArrayList<>();
				for(Player p : playerSelectors(s, args[0])) {
					l.add(""+p.getLocation().getYaw());
				}
				return l;
			})
			.fallback((s, structure, args) -> {
				msg(s, "missing.number", Placeholders.c().replace("number", args[6]));
			})
			.selector(Selector.NUMBER, (s, structure, args) -> { // tp [player] [X] [Y] [Z] [WORLD] [YAW] [PITCH]
				for(Player p : playerSelectors(s, args[0])) {
					Location loc = p.getLocation();
					loc.setX(StringUtils.getDouble(args[1]));
					loc.setY(StringUtils.getDouble(args[2]));
					loc.setZ(StringUtils.getDouble(args[3]));
					loc.setWorld(Bukkit.getWorld(args[4]));
					loc.setYaw(StringUtils.getFloat(args[5]));
					loc.setPitch(StringUtils.getFloat(args[6]));
					TpSystem.teleport(p, loc);
					msgSec(p, "tploc", Placeholders.c()
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					msgSec(s, "tploctarget", Placeholders.c().addPlayer("player", p)
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					}
			}, (s, structure, args) -> {
				List<String> l = new ArrayList<>();
				for(Player p : playerSelectors(s, args[0])) {
					l.add(""+p.getLocation().getPitch());
				}
				return l;
			})
			.parent() // tp [player] [X] [Y] [Z] [WORLD] [YAW]
			.parent() // tp [player] [X] [Y] [Z] [WORLD]
			.parent() // tp [player] [X] [Y] [Z]
			.fallback((s, structure, args) -> {
				msg(s, "missing.number", Placeholders.c().replace("number", args[4]));
			})
			.selector(Selector.NUMBER, (s, structure, args) -> { // tp [player] [X] [Y] [Z] [YAW]
				for(Player p : playerSelectors(s, args[0])) {
					Location loc = p.getLocation();
					loc.setX(StringUtils.getDouble(args[1]));
					loc.setY(StringUtils.getDouble(args[2]));
					loc.setZ(StringUtils.getDouble(args[3]));
					loc.setYaw(StringUtils.getFloat(args[4]));
					TpSystem.teleport(p, loc);
					msgSec(p, "tploc", Placeholders.c()
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					msgSec(s, "tploctarget", Placeholders.c().addPlayer("player", p)
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					}
			}, (s, structure, args) -> {
				List<String> l = new ArrayList<>();
				for(Player p : playerSelectors(s, args[0])) {
					l.add(""+p.getLocation().getYaw());
				}
				return l;
			})
			.fallback((s, structure, args) -> {
				msg(s, "missing.number", Placeholders.c().replace("number", args[5]));
			})
			.selector(Selector.NUMBER, (s, structure, args) -> { // tp [player] [X] [Y] [Z] [YAW] [PITCH]
				for(Player p : playerSelectors(s, args[0])) {
					Location loc = p.getLocation();
					loc.setX(StringUtils.getDouble(args[1]));
					loc.setY(StringUtils.getDouble(args[2]));
					loc.setZ(StringUtils.getDouble(args[3]));
					loc.setYaw(StringUtils.getFloat(args[4]));
					loc.setPitch(StringUtils.getFloat(args[5]));
					TpSystem.teleport(p, loc);
					msgSec(p, "tploc", Placeholders.c()
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					msgSec(s, "tploctarget", Placeholders.c().addPlayer("player", p)
							.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
							.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
							.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
							.add("world", loc.getWorld().getName())
							.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
							.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
					}
			}, (s, structure, args) -> {
				List<String> l = new ArrayList<>();
				for(Player p : playerSelectors(s, args[0])) {
					l.add(""+p.getLocation().getPitch());
				}
				return l;
			})
			
		.first() // tp <-
		
		/*
		 * LOCATION
		 */
		
		.fallback((s, structure, args) -> {
			msg(s, "missing.number", Placeholders.c().replace("number", args[2]));
		})
		.selector(Selector.NUMBER, (s, structure, args) -> { // tp [X]
			Location loc = ( (Player)s).getLocation();
			loc.setX(StringUtils.getDouble(args[0]));
			TpSystem.teleport((Player)s, loc);
			msgSec(s, "tploc", Placeholders.c()
					.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
					.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
					.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
					.add("world", loc.getWorld().getName())
					.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
					.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
		}, (s, structure, args) -> {
			List<String> l = new ArrayList<>();
			l.add(""+((Player)s).getLocation().getBlockX());
			return l;
		})
		.permission(permission("location"))
		.selector(Selector.NUMBER, (s, structure, args) -> { // tp [X] [Y]
			Location loc = ( (Player)s).getLocation();
			loc.setX(StringUtils.getDouble(args[0]));
			loc.setY(StringUtils.getDouble(args[1]));
			TpSystem.teleport((Player)s, loc);
			msgSec(s, "tploc", Placeholders.c()
					.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
					.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
					.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
					.add("world", loc.getWorld().getName())
					.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
					.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
		}, (s, structure, args) -> {
			List<String> l = new ArrayList<>();
			l.add(""+((Player)s).getLocation().getBlockY());
			return l;
		})
		.selector(Selector.NUMBER, (s, structure, args) -> { // tp [X] [Y] [Z]
			Location loc = ( (Player)s).getLocation();
			loc.setX(StringUtils.getDouble(args[0]));
			loc.setY(StringUtils.getDouble(args[1]));
			loc.setZ(StringUtils.getDouble(args[2]));
			TpSystem.teleport((Player)s, loc);
			msgSec(s, "tploc", Placeholders.c()
					.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
					.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
					.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
					.add("world", loc.getWorld().getName())
					.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
					.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
		}, (s, structure, args) -> {
			List<String> l = new ArrayList<>();
			l.add(""+((Player)s).getLocation().getBlockZ());
			return l;
		})
		.fallback((s, structure, args) -> {
			msg(s, "missing.world", Placeholders.c().replace("world", args[2]));
		})
		.selector(Selector.WORLD, (s, structure, args) -> { // tp [X] [Y] [Z] [WORLD]
			Location loc = ( (Player)s).getLocation();
			loc.setX(StringUtils.getDouble(args[0]));
			loc.setY(StringUtils.getDouble(args[1]));
			loc.setZ(StringUtils.getDouble(args[2]));
			loc.setWorld(Bukkit.getWorld(args[3]));
			TpSystem.teleport((Player)s, loc);
			msgSec(s, "tploc", Placeholders.c()
					.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
					.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
					.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
					.add("world", loc.getWorld().getName())
					.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
					.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
		})
		.fallback((s, structure, args) -> {
			msg(s, "missing.number", Placeholders.c().replace("number", args[2]));
		})
		.selector(Selector.NUMBER, (s, structure, args) -> { // tp [X] [Y] [Z] [WORLD] [YAW]
			Location loc = ( (Player)s).getLocation();
			loc.setX(StringUtils.getDouble(args[0]));
			loc.setY(StringUtils.getDouble(args[1]));
			loc.setZ(StringUtils.getDouble(args[2]));
			loc.setWorld(Bukkit.getWorld(args[3]));
			loc.setYaw(StringUtils.getFloat(args[4]));
			TpSystem.teleport((Player)s, loc);
			msgSec(s, "tploc", Placeholders.c()
					.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
					.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
					.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
					.add("world", loc.getWorld().getName())
					.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
					.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
		}, (s, structure, args) -> {
			List<String> l = new ArrayList<>();
			l.add(""+((Player)s).getLocation().getYaw());
			return l;
		})
		.selector(Selector.NUMBER, (s, structure, args) -> { // tp [X] [Y] [Z] [WORLD] [YAW] [PITCH]
			Location loc = ( (Player)s).getLocation();
			loc.setX(StringUtils.getDouble(args[0]));
			loc.setY(StringUtils.getDouble(args[1]));
			loc.setZ(StringUtils.getDouble(args[2]));
			loc.setWorld(Bukkit.getWorld(args[3]));
			loc.setYaw(StringUtils.getFloat(args[4]));
			loc.setPitch(StringUtils.getFloat(args[5]));
			TpSystem.teleport((Player)s, loc);
			msgSec(s, "tploc", Placeholders.c()
					.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
					.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
					.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
					.add("world", loc.getWorld().getName())
					.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
					.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
		}, (s, structure, args) -> {
			List<String> l = new ArrayList<>();
			l.add(""+((Player)s).getLocation().getPitch());
			return l;
		})
		.parent() // tp [X] [Y] [Z] [WORLD] [YAW]
		.parent() // tp [X] [Y] [Z] [WORLD]
		.parent() // tp [X] [Y] [Z]
		.selector(Selector.NUMBER, (s, structure, args) -> { // tp [X] [Y] [Z] [YAW]
			Location loc = ( (Player)s).getLocation();
			loc.setX(StringUtils.getDouble(args[0]));
			loc.setY(StringUtils.getDouble(args[1]));
			loc.setZ(StringUtils.getDouble(args[2]));
			loc.setYaw(StringUtils.getFloat(args[3]));
			TpSystem.teleport((Player)s, loc);
			msgSec(s, "tploc", Placeholders.c()
					.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
					.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
					.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
					.add("world", loc.getWorld().getName())
					.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
					.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
		}, (s, structure, args) -> {
			List<String> l = new ArrayList<>();
			l.add(""+((Player)s).getLocation().getYaw());
			return l;
		})
		.selector(Selector.NUMBER, (s, structure, args) -> { // tp [X] [Y] [Z] [YAW] [PITCH]
			Location loc = ( (Player)s).getLocation();
			loc.setX(StringUtils.getDouble(args[0]));
			loc.setY(StringUtils.getDouble(args[1]));
			loc.setZ(StringUtils.getDouble(args[2]));
			loc.setYaw(StringUtils.getFloat(args[3]));
			loc.setPitch(StringUtils.getFloat(args[4]));
			TpSystem.teleport((Player)s, loc);
			msgSec(s, "tploc", Placeholders.c()
					.add("x", StringUtils.formatDouble(FormatType.BASIC, loc.getX()) )
					.add("y", StringUtils.formatDouble(FormatType.BASIC, loc.getY()) )
					.add("z", StringUtils.formatDouble(FormatType.BASIC, loc.getZ()) )
					.add("world", loc.getWorld().getName())
					.add("yaw", StringUtils.formatDouble(FormatType.BASIC, loc.getYaw()) )
					.add("pitch", StringUtils.formatDouble(FormatType.BASIC, loc.getPitch()) ));
		}, (s, structure, args) -> {
			List<String> l = new ArrayList<>();
			l.add(""+((Player)s).getLocation().getPitch());
			return l;
		})
		
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tp";
	}
}

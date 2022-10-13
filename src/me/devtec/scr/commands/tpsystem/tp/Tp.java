package me.devtec.scr.commands.tpsystem.tp;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
			if (hasPermission(s, "other"))
				help(s, "other");
			else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // tp player (self -> other)
					if (s instanceof Player)
						for (Player p : playerSelectors(s, args[0])) {
							TpSystem.teleport((Player) s, p);
							msgSec(s, "self-target", Placeholders.c().addPlayer("player", s).addPlayer("target", p));
						}
					else
						help(s, "other");
				}).selector(Selector.PLAYER, (s, structure, args) -> { // tp player player (other -> target)
					Player target = Bukkit.getPlayer(args[1]);
					for (Player p : playerSelectors(s, args[0])) {
						TpSystem.teleport(p, target);
						msgSec(s, "other-target.sender", Placeholders.c().addPlayer("player", s).addPlayer("target", p).addPlayer("target2", target));
						msgSec(p, "other-target.receiver", Placeholders.c().addPlayer("player", p).addPlayer("target", s).addPlayer("target2", target));
					}
				}).parent().selector(Selector.WORLD, (s, structure, args) -> { // tp player world (other -> world)
					World destination = Bukkit.getWorld(args[1]);
					for (Player p : playerSelectors(s, args[0])) {
						TpSystem.teleport(p, destination.getSpawnLocation());
						msgSec(s, "other-world.sender", Placeholders.c().addPlayer("player", s).addPlayer("target", p).add("world", destination.getName()));
						msgSec(p, "other-world.receiver", Placeholders.c().addPlayer("player", p).addPlayer("target", s).add("world", destination.getName()));
					}
				}).callableArgument((sender, structure, args) -> {
					if (args[2].equals("~") || StringUtils.isNumber(args[2]) || args[2].matches("[~0-9.+-]+"))
						return Arrays.asList(args[2]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getX())) : Arrays.asList("{x}"))
				.callableArgument((sender, structure, args) -> {
					if (args[3].equals("~") || StringUtils.isNumber(args[3]) || args[3].matches("[~0-9.+-]+"))
						return Arrays.asList(args[3]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getY())) : Arrays.asList("{y}"))
				.callableArgument((sender, structure, args) -> { // tp player world x y z
					if (args[4].equals("~") || StringUtils.isNumber(args[4]) || args[4].matches("[~0-9.+-]+"))
						return Arrays.asList(args[4]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					Location destination = new Location(Bukkit.getWorld(args[1]), StringUtils.calculate(args[2].replace("~", ((Player) s).getLocation().getX() + "+0")),
							StringUtils.calculate(args[3].replace("~", ((Player) s).getLocation().getY() + "+0")),
							StringUtils.calculate(args[4].replace("~", ((Player) s).getLocation().getZ() + "+0")), ((Player) s).getLocation().getYaw(), ((Player) s).getLocation().getPitch());
					for (Player p : playerSelectors(s, args[0])) {
						TpSystem.teleport(p, destination);
						msgSec(s, "other-world-loc.sender",
								Placeholders.c().addPlayer("player", s).addPlayer("target", p).add("world", destination.getWorld().getName())
										.add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX())).add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY()))
										.add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ())).add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw()))
										.add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
						msgSec(p, "other-world-loc.receiver",
								Placeholders.c().addPlayer("player", p).addPlayer("target", s).add("world", destination.getWorld().getName())
										.add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX())).add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY()))
										.add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ())).add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw()))
										.add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
					}
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getZ())) : Arrays.asList("{z}"))
				.callableArgument((sender, structure, args) -> { // yaw
					if (args[5].equals("~") || StringUtils.isNumber(args[5]) || args[5].matches("[~0-9.+-]+"))
						return Arrays.asList(args[5]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getYaw())) : Arrays.asList("{yaw}"))
				.callableArgument((sender, structure, args) -> { // pitch
					if (args[6].equals("~") || StringUtils.isNumber(args[6]) || args[6].matches("[~0-9.+-]+"))
						return Arrays.asList(args[6]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					Location destination = new Location(Bukkit.getWorld(args[1]), StringUtils.calculate(args[2].replace("~", ((Player) s).getLocation().getX() + "+0")),
							StringUtils.calculate(args[3].replace("~", ((Player) s).getLocation().getY() + "+0")),
							StringUtils.calculate(args[4].replace("~", ((Player) s).getLocation().getZ() + "+0")),
							(float) StringUtils.calculate(args[5].replace("~", ((Player) s).getLocation().getYaw() + "+0")),
							(float) StringUtils.calculate(args[6].replace("~", ((Player) s).getLocation().getPitch() + "+0")));
					for (Player p : playerSelectors(s, args[0])) {
						TpSystem.teleport(p, destination);
						msgSec(s, "other-world-loc.sender",
								Placeholders.c().addPlayer("player", s).addPlayer("target", p).add("world", destination.getWorld().getName())
										.add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX())).add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY()))
										.add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ())).add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw()))
										.add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
						msgSec(p, "other-world-loc.receiver",
								Placeholders.c().addPlayer("player", p).addPlayer("target", s).add("world", destination.getWorld().getName())
										.add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX())).add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY()))
										.add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ())).add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw()))
										.add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
					}
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getPitch())) : Arrays.asList("{pitch}"))
				.parent(6).callableArgument((sender, structure, args) -> {
					if (args[1].equals("~") || StringUtils.isNumber(args[1]) || args[1].matches("[~0-9.+-]+"))
						return Arrays.asList(args[1]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getX())) : Arrays.asList("{x}"))
				.callableArgument((sender, structure, args) -> {
					if (args[2].equals("~") || StringUtils.isNumber(args[2]) || args[2].matches("[~0-9.+-]+"))
						return Arrays.asList(args[2]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getY())) : Arrays.asList("{y}"))
				.callableArgument((sender, structure, args) -> { // tp player x y z
					if (args[3].equals("~") || StringUtils.isNumber(args[3]) || args[3].matches("[~0-9.+-]+"))
						return Arrays.asList(args[3]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					Location destination = new Location(((Player) s).getWorld(), StringUtils.calculate(args[1].replace("~", ((Player) s).getLocation().getX() + "+0")),
							StringUtils.calculate(args[2].replace("~", ((Player) s).getLocation().getY() + "+0")),
							StringUtils.calculate(args[3].replace("~", ((Player) s).getLocation().getZ() + "+0")), ((Player) s).getLocation().getYaw(), ((Player) s).getLocation().getPitch());
					for (Player p : playerSelectors(s, args[0])) {
						TpSystem.teleport(p, destination);
						msgSec(s, "other-world-loc.sender",
								Placeholders.c().addPlayer("player", s).addPlayer("target", p).add("world", destination.getWorld().getName())
										.add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX())).add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY()))
										.add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ())).add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw()))
										.add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
						msgSec(p, "other-world-loc.receiver",
								Placeholders.c().addPlayer("player", p).addPlayer("target", s).add("world", destination.getWorld().getName())
										.add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX())).add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY()))
										.add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ())).add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw()))
										.add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
					}
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getZ())) : Arrays.asList("{z}"))
				.callableArgument((sender, structure, args) -> { // yaw
					if (args[4].equals("~") || StringUtils.isNumber(args[4]) || args[4].matches("[~0-9.+-]+"))
						return Arrays.asList(args[4]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getYaw())) : Arrays.asList("{yaw}"))
				.callableArgument((sender, structure, args) -> { // pitch
					if (args[5].equals("~") || StringUtils.isNumber(args[5]) || args[5].matches("[~0-9.+-]+"))
						return Arrays.asList(args[5]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					Location destination = new Location(((Player) s).getWorld(), StringUtils.calculate(args[1].replace("~", ((Player) s).getLocation().getX() + "+0")),
							StringUtils.calculate(args[2].replace("~", ((Player) s).getLocation().getY() + "+0")),
							StringUtils.calculate(args[3].replace("~", ((Player) s).getLocation().getZ() + "+0")),
							(float) StringUtils.calculate(args[4].replace("~", ((Player) s).getLocation().getYaw() + "+0")),
							(float) StringUtils.calculate(args[5].replace("~", ((Player) s).getLocation().getPitch() + "+0")));
					for (Player p : playerSelectors(s, args[0])) {
						TpSystem.teleport(p, destination);
						msgSec(s, "other-world-loc.sender",
								Placeholders.c().addPlayer("player", s).addPlayer("target", p).add("world", destination.getWorld().getName())
										.add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX())).add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY()))
										.add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ())).add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw()))
										.add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
						msgSec(p, "other-world-loc.receiver",
								Placeholders.c().addPlayer("player", p).addPlayer("target", s).add("world", destination.getWorld().getName())
										.add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX())).add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY()))
										.add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ())).add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw()))
										.add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
					}
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getPitch())) : Arrays.asList("{pitch}"))
				.first().callableArgument((sender, structure, args) -> {
					if (args[0].equals("~") || StringUtils.isNumber(args[0]) || args[0].matches("[~0-9.+-]+"))
						return Arrays.asList(args[0]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getX())) : Arrays.asList("{x}"))
				.callableArgument((sender, structure, args) -> {
					if (args[1].equals("~") || StringUtils.isNumber(args[1]) || args[1].matches("[~0-9.+-]+"))
						return Arrays.asList(args[1]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getY())) : Arrays.asList("{y}"))
				.callableArgument((sender, structure, args) -> { // tp player x y z
					if (args[2].equals("~") || StringUtils.isNumber(args[2]) || args[2].matches("[~0-9.+-]+"))
						return Arrays.asList(args[2]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					Player p = (Player) s;
					Location destination = new Location(p.getWorld(), StringUtils.calculate(args[0].replace("~", p.getLocation().getX() + "+0")),
							StringUtils.calculate(args[1].replace("~", p.getLocation().getY() + "+0")), StringUtils.calculate(args[2].replace("~", p.getLocation().getZ() + "+0")),
							p.getLocation().getYaw(), p.getLocation().getPitch());
					TpSystem.teleport(p, destination);
					msgSec(s, "self-world-loc",
							Placeholders.c().addPlayer("player", s).add("world", destination.getWorld().getName()).add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX()))
									.add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY())).add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ()))
									.add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw())).add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getZ())) : Arrays.asList("{z}"))
				.callableArgument((sender, structure, args) -> { // yaw
					if (args[3].equals("~") || StringUtils.isNumber(args[3]) || args[3].matches("[~0-9.+-]+"))
						return Arrays.asList(args[3]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getYaw())) : Arrays.asList("{yaw}"))
				.callableArgument((sender, structure, args) -> { // pitch
					if (args[4].equals("~") || StringUtils.isNumber(args[4]) || args[4].matches("[~0-9.+-]+"))
						return Arrays.asList(args[4]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					Player p = (Player) s;
					Location destination = new Location(p.getWorld(), StringUtils.calculate(args[0].replace("~", p.getLocation().getX() + "+0")),
							StringUtils.calculate(args[1].replace("~", p.getLocation().getY() + "+0")), StringUtils.calculate(args[2].replace("~", p.getLocation().getZ() + "+0")),
							(float) StringUtils.calculate(args[3].replace("~", p.getLocation().getYaw() + "+0")),
							(float) StringUtils.calculate(args[4].replace("~", p.getLocation().getPitch() + "+0")));
					TpSystem.teleport(p, destination);
					msgSec(s, "self-world-loc",
							Placeholders.c().addPlayer("player", s).add("world", destination.getWorld().getName()).add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX()))
									.add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY())).add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ()))
									.add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw())).add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getPitch())) : Arrays.asList("{pitch}"))
				.first().selector(Selector.WORLD, (s, structure, args) -> { // tp world
					World destination = Bukkit.getWorld(args[0]);
					Player p = (Player) s;
					TpSystem.teleport(p, destination.getSpawnLocation());
					msgSec(s, "self-world", Placeholders.c().addPlayer("player", s).add("world", destination.getName()));
				}).callableArgument((sender, structure, args) -> {
					if (args[1].equals("~") || StringUtils.isNumber(args[1]) || args[1].matches("[~0-9.+-]+"))
						return Arrays.asList(args[1]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getX())) : Arrays.asList("{x}"))
				.callableArgument((sender, structure, args) -> {
					if (args[2].equals("~") || StringUtils.isNumber(args[2]) || args[2].matches("[~0-9.+-]+"))
						return Arrays.asList(args[2]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getY())) : Arrays.asList("{y}"))
				.callableArgument((sender, structure, args) -> { // tp world x y z
					if (args[3].equals("~") || StringUtils.isNumber(args[3]) || args[3].matches("[~0-9.+-]+"))
						return Arrays.asList(args[3]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					Player p = (Player) s;
					Location destination = new Location(Bukkit.getWorld(args[0]), StringUtils.calculate(args[1].replace("~", p.getLocation().getX() + "+0")),
							StringUtils.calculate(args[2].replace("~", p.getLocation().getY() + "+0")), StringUtils.calculate(args[3].replace("~", p.getLocation().getZ() + "+0")),
							p.getLocation().getYaw(), p.getLocation().getPitch());
					TpSystem.teleport(p, destination);
					msgSec(s, "self-world-loc",
							Placeholders.c().addPlayer("player", s).add("world", destination.getWorld().getName()).add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX()))
									.add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY())).add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ()))
									.add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw())).add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getZ())) : Arrays.asList("{z}"))
				.callableArgument((sender, structure, args) -> { // yaw
					if (args[4].equals("~") || StringUtils.isNumber(args[4]) || args[4].matches("[~0-9.+-]+"))
						return Arrays.asList(args[4]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					help(s, "usage");
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getYaw())) : Arrays.asList("{yaw}"))
				.callableArgument((sender, structure, args) -> { // pitch
					if (args[5].equals("~") || StringUtils.isNumber(args[5]) || args[5].matches("[~0-9.+-]+"))
						return Arrays.asList(args[5]);
					return Arrays.asList("~");
				}, (s, structure, args) -> {
					Player p = (Player) s;
					Location destination = new Location(Bukkit.getWorld(args[0]), StringUtils.calculate(args[1].replace("~", p.getLocation().getX() + "+0")),
							StringUtils.calculate(args[2].replace("~", p.getLocation().getY() + "+0")), StringUtils.calculate(args[3].replace("~", p.getLocation().getZ() + "+0")),
							(float) StringUtils.calculate(args[4].replace("~", p.getLocation().getYaw() + "+0")),
							(float) StringUtils.calculate(args[5].replace("~", p.getLocation().getPitch() + "+0")));
					TpSystem.teleport(p, destination);
					msgSec(s, "self-world-loc",
							Placeholders.c().addPlayer("player", s).add("world", destination.getWorld().getName()).add("x", StringUtils.formatDouble(FormatType.BASIC, destination.getX()))
									.add("y", StringUtils.formatDouble(FormatType.BASIC, destination.getY())).add("z", StringUtils.formatDouble(FormatType.BASIC, destination.getZ()))
									.add("yaw", StringUtils.formatDouble(FormatType.BASIC, destination.getYaw())).add("pitch", StringUtils.formatDouble(FormatType.BASIC, destination.getPitch())));
				}, (s, structure, args) -> s instanceof Player ? Arrays.asList("~", StringUtils.formatDouble(FormatType.BASIC, ((Player) s).getLocation().getPitch())) : Arrays.asList("{pitch}"))
				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tp";
	}
}

package me.devtec.scr.commands.time;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Day implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			World world = ((Player) s).getWorld();
			apply(world);
			msg(s, "time.day", Placeholders.c().add("world", world.getName()));
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
		.fallback((s, structure, args) -> {
			help(s, "usage");
		})
			// day -s
			.argument("-s", (s, structure, args) -> {
				if (!(s instanceof Player)) {
					help(s, "usage");
					return;
				}
				World world = ((Player) s).getWorld();
				apply(world);
			})
		.first() // da
				// day [world]
			.selector(Selector.WORLD, (s, structure, args) -> {
				World world = Bukkit.getWorld(args[0]);
				apply(world);
				msg(s, "day.sun", Placeholders.c().add("world", world.getName()));
			}).permission(permission("world"))
				// day [world] -s
				.argument("-s", (s, structure, args) -> {
					World world = Bukkit.getWorld(args[0]);
					apply(world);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure();
	}

	public void apply(World world) {
		/*
		 * day - 1000
		 * night - 13000
		 * midnight - 18000
		 */
		world.setTime(1000);
	}

	@Override
	public String configSection() {
		return "day";
	}

}

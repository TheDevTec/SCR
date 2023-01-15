package me.devtec.scr.commands.tpsystem;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.listeners.additional.TpBackListener;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Back implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		Loader.registerListener(new TpBackListener());
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (s instanceof Player) {
				TpSystem.teleportBack((Player) s);
				msgSec(s, "teleported", null);
			} else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.selector(Selector.PLAYER, (s, structure, args) -> {
					for (Player p : playerSelectors(s, args[0])) {
						TpSystem.teleportBack(p);
						msgSec(s, "sender", Placeholders.c().addPlayer("target", p));
						msgSec(p, "teleported", null);
					}
				}).permission(permission("other"))

				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "back";
	}
}
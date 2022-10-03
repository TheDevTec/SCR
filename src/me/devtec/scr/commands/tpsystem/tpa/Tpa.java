package me.devtec.scr.commands.tpsystem.tpa;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.tpsystem.TpSystem;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Tpa implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		.selector(Selector.PLAYER, (s, structure, args) -> {
			for(Player p : playerSelectors(s, args[0])) {
				TpSystem.askTpa( (Player) s, p);
				msgSec(s, "sender", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
				msgSec(s, "receiver", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
			}
		})
		
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tpa";
	}
}
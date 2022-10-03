package me.devtec.scr.commands.tpsystem.tpa;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.tpsystem.TpSystem;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class TpaHere implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		.fallback((s, structure, args) -> { // /tpahere [player]
			offlinePlayer(s, args[0]);
		})
		.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // /tpahere [player]
			for(Player p : playerSelectors(s, args[0])) {
				TpSystem.askTpaHere(p, (Player)s);
				msgSec(s, "sender", Placeholders.c().addPlayer("player", s).addPlayer("target", p));
				msgSec(s, "receiver", Placeholders.c().addPlayer("player", s).addPlayer("target", p));
			}
		})
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tpahere";
	}
}
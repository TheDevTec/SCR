package me.devtec.scr.commands.tpsystem.TP;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.tpsystem.TpSystem;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class TpHere implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		.fallback((s, structure, args) -> { // /tphere [player]
			offlinePlayer(s, args[0]);
		})
		.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // /tphere [player]
			for(Player p : playerSelectors(s, args[0])) {
				msgSec(s, "sender", Placeholders.c().addPlayer("player", s).addPlayer("target", p));
				msgSec(s, "target", Placeholders.c().addPlayer("player", s).addPlayer("target", p));
				TpSystem.teleport(p, (Player)s);
			}
		})
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tphere";
	}
}
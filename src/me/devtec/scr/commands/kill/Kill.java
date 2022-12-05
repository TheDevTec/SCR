package me.devtec.scr.commands.kill;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Kill implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (s instanceof Player) {
				Player player = (Player) s;
				player.setHealth(0);
				msgSec(s, "self", Placeholders.c().addPlayer("player", player));
			} else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
		}).argument("-s", (s, structure, args) -> { // cmd -s
			if (s instanceof Player) {
				Player player = (Player) s;
				player.setHealth(0);
			} else
				help(s, "usage");
		}).first().selector(Selector.ENTITY_SELECTOR, (sender, structure, args) -> { // cmd [entity_selector]
			for (Player s : playerSelectors(sender, args[0])) {
				msgSec(sender, "other.sender", Placeholders.c().addPlayer("player", sender).addPlayer("target", s));
				msgSec(s, "other.receiver", Placeholders.c().addPlayer("target", s).addPlayer("player", sender));
				s.setHealth(0);
			}
		}).permission(permission("other")).argument("-s", (sender, structure, args) -> { // cmd [entity_selector] -s
			for (Player s : playerSelectors(sender, args[0]))
				s.setHealth(0);
		}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "kill";
	}

}

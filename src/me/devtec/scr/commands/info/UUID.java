package me.devtec.scr.commands.info;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class UUID implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // uuid
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			msgSec(s, "self", Placeholders.c().add("uuid", ((Player) s).getUniqueId().toString()));
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
		}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // uuid [player]
			for (Player p : playerSelectors(s, args[0]))
				msgSec(s, "target", Placeholders.c().add("uuid", p.getUniqueId().toString()).addPlayer("target", p));
		}).permission(permission("other")).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "uuid";
	}

}

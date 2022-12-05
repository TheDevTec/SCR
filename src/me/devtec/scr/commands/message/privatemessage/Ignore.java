package me.devtec.scr.commands.message.privatemessage;

import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Ignore implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).selector(Selector.PLAYER, (s, structure, args) -> { // ignore [player]
			MessageManager.ignore(s, args[0]);
		}).first().argument(null, (s, structure, args) -> { // ignore [offlinePlayer]
			MessageManager.ignore(s, args[0]);
		}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "ignore";
	}

}

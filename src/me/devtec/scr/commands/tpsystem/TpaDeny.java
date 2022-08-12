package me.devtec.scr.commands.tpsystem;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;

public class TpaDeny implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if(TpSystem.askTpa.containsKey( ((Player)s )) || TpSystem.askTpaHere.containsKey( ((Player)s )) ) {
				TpSystem.deny((Player)s);
			}else {
				msgSec(s, "norequest");
			}
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tpadeny";
	}
}

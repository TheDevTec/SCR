package me.devtec.scr.commands.server_managment;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.utils.ChatUtils;
import me.devtec.shared.commands.structures.CommandStructure;

public class ChatLock implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if(ChatUtils.isChatLocked())
				msgSec(s, "disabled");
			else
				msgSec(s, "enabled");
			ChatUtils.lockChat();
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "chatlock";
	}

}
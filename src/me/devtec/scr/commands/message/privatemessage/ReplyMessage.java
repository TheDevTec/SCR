package me.devtec.scr.commands.message.privatemessage;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;

public class ReplyMessage implements ScrCommand {


	@Override
	public void init(List<String> cmds) {
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
			.argument(null, (s, structure, args) -> { // /reply [message]
				MessageManager.message(s, null, StringUtils.buildString(args));
			})
			
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "reply";
	}

}

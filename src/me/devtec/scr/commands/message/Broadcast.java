package me.devtec.scr.commands.message;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;

public class Broadcast implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
			.argument(null, (s, structure, args) -> { // /broadcast [message]
				String msg = StringUtils.buildString(args);
				MessageUtils.message(s, "broadcast", Placeholders.c().add("message", msg), true, 
						API.getOnlinePlayers(true).toArray(new CommandSender[0]));
			})
			
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "broadcast";
	}

}

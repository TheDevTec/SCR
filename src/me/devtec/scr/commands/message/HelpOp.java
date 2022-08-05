package me.devtec.scr.commands.message;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.message.privatemessage.MessageManager;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;

public class HelpOp implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if( s instanceof Player) {
				if(hasPermission(s, "see")) //just a safety feature, players are sometimes dumb :D 
					MessageManager.acChatLock(s);
				else
					help(s, "usage");
			}
			else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
			.argument(null, (s, structure, args) -> { // /helpop [message]
				MessageManager.sendhelpop(s, StringUtils.buildString(args));
			})
			
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "helpop";
	}

}

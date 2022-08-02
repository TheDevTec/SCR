package me.devtec.scr.commands.fun.nickname;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class NickReset implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		// /nick [nickname]
		// /nick [nickname] [player]
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			User user = API.getUser(s);
			if(!user.isConsole()) {
				user.resetNickname();
				msg(s, "nickname.reset.target", Placeholders.c().addPlayer("player", s) );
			} else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
			.argument("-s", (s, structure, args) -> {  // /cmd -s
				User user = API.getUser(s);
				if(!user.isConsole()) {
					user.resetNickname();
				} else
					help(s, "usage");
				
			})
			.parent() // cmd <-
			.fallback((s, structure, args) -> { // /cmd [player]
				offlinePlayer(s, args[0]);
				})
			.selector(Selector.PLAYER, (s, structure, args) -> { // /cmd [player]
				User u = API.getUser(args[0]);
				u.resetNickname();
				msg(s, "nickname.reset.sender", Placeholders.c().addPlayer("target", u.player));
				msg(s, "nickname.reset.target", Placeholders.c().addPlayer("player", s) );
				
			}).permission(permission("other"))
				.argument("-s", (s, structure, args) -> {  // /cmd [player] -s
					User u = API.getUser(args[0]);
					u.resetNickname();
					
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));	
		
	}

	@Override
	public String configSection() {
		return "nicknamereset";
	}


}

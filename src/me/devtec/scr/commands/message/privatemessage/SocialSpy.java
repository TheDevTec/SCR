package me.devtec.scr.commands.message.privatemessage;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.dataholder.Config;

public class SocialSpy implements ScrCommand {


	@Override
	public void init(List<String> cmds) {
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			MessageManager.socialSpy(s, s, true);
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
			.argument("-s", (s, structure, args) -> { // cmd -s
				MessageManager.socialSpy(s, s, false);
			})
		.parent() // cmd
			.selector(Selector.BOOLEAN, (s, structure, args) -> { // cmd {boolean}
				Config c = me.devtec.shared.API.getUser(s.getName());
				if(c.getBoolean("privateMessage.socialspy") != Boolean.parseBoolean(args[0]))
					MessageManager.socialSpy(s, s, true);
			})
				.argument("-s", (s, structure, args) -> { // cmd {boolean} -s
					Config c = me.devtec.shared.API.getUser(s.getName());
					if(c.getBoolean("privateMessage.socialspy") != Boolean.parseBoolean(args[0]))
						MessageManager.socialSpy(s, s, true);
				})
			.parent() // cmd {boolean}
		.parent() // cmd
			.selector(Selector.PLAYER, (s, structure, args) -> { // cmd [player]
				for(Player p : playerSelectors(s, args[0]))
					MessageManager.socialSpy(s, p, true);
			}).permission(permission("other"))
				.argument("-s", (s, structure, args) -> { // cmd [player] -s
					
					for(Player p : playerSelectors(s, args[0]))
						MessageManager.socialSpy(s, p, false);
				})
			.parent() // cmd [player]
				.selector(Selector.BOOLEAN, (s, structure, args) -> { // cmd [player] {boolean}
					for(Player p : playerSelectors(s, args[0])) {
						Config c = me.devtec.shared.API.getUser(s.getName());
						if(c.getBoolean("privateMessage.socialspy") != Boolean.parseBoolean(args[0]))
							MessageManager.socialSpy(s, p, true);
					}
				})
					.argument("-s", (s, structure, args) -> { // cmd [player]  {boolean} -s
						for(Player p : playerSelectors(s, args[0])) {
							Config c = me.devtec.shared.API.getUser(s.getName());
							if(c.getBoolean("privateMessage.socialspy") != Boolean.parseBoolean(args[0]))
								MessageManager.socialSpy(s, p, false);
						}
					})
		
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "socialSpy";
	}

}

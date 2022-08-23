package me.devtec.scr.commands.info;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User.SeenType;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;

public class Seen implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if(s instanceof Player) {
				long time = API.getUser(s).getSeen(SeenType.ONLINE);
				msgSec(s, "online", Placeholders.c()
						.add("time", StringUtils.timeToString(time)).addPlayer("player", s));
			} else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // seen
		
			.fallback((s, structure, args) -> { // /seen [player]
				if( me.devtec.shared.API.getUser(args[0]).exists("lastLeave") ) {
					long time = API.getUser(args[0]).getSeen(SeenType.OFFLINE);
					Loader.plugin.getLogger().info("Time: "+time+ " ; "+
							me.devtec.shared.API.getUser(args[0]).getLong("lastLeave") );
					msgSec(s, "offline", Placeholders.c()
							.add("time", StringUtils.timeToString(time)).addOffline("player", args[0]));
				}else {
					msg(s, "missing.playerDontExist", Placeholders.c().addOffline("player", args[0]));
				}
			}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // seen [player]
				long time = 0;
				for(Player p : playerSelectors(s, args[0])) {
					 time = API.getUser(p).getSeen(SeenType.ONLINE);
					msgSec(s, "online", Placeholders.c()
							.add("time", StringUtils.timeToString(time)).addPlayer("player", p));
				}
			}).permission(permission("other")) // seen [player]
				
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "seen";
	}

}

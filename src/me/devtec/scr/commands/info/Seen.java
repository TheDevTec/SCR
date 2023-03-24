package me.devtec.scr.commands.info;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.TimeUtils;

public class Seen implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (s instanceof Player) {
				long time = API.getUser((Player) s).seen();
				msgSec(s, "online", Placeholders.c().add("time", TimeUtils.timeToString(time)).addPlayer("player", s));
			} else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // seen
				.fallback((s, structure, args) -> { // /seen [player]
					Config user = me.devtec.shared.API.getUser(args[0]);
					if (user.exists("lastLeave")) {
						long time = API.getUser(args[0]).seen();
						msgSec(s, "offline", Placeholders.c().add("time", TimeUtils.timeToString(time)).addOffline("player", args[0]));
					} else
						msg(s, "missing.playerDontExist", Placeholders.c().addOffline("player", args[0]));
				}).selector(Selector.PLAYER, (s, structure, args) -> { // seen [player]
					Player p = Bukkit.getPlayer(args[0]);
					long time = API.getUser(p).seen();
					msgSec(s, "online", Placeholders.c().add("time", TimeUtils.timeToString(time)).addPlayer("player", p));

				}).permission(permission("other")) // seen [player]

				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "seen";
	}

}

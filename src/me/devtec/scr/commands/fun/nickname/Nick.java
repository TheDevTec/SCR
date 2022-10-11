package me.devtec.scr.commands.fun.nickname;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Nick implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		// /nick [nickname]
		// /nick [nickname] [player]
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (s instanceof Player) {
				help(s, "usage");
				if (hasPermission(s, "other"))
					help(s, "admin_usage");
			} else
				help(s, "admin_usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).argument("list", (s, structure, args) -> { // /cmd [list]
			// nicknames:
			// <nickname>: real_player_name
			msg(s, "nickname.list.header");
			for (String nickname : Loader.data.getKeys("nicknames"))
				msg(s, "nickname.list.format", Placeholders.c().replace("name", Loader.data.getString("nicknames." + nickname)).replace("nickname", nickname));
		}).priority(1).permission(permission("list")).parent() // cmd <-
				.argument(null, (s, structure, args) -> {
					if (s instanceof Player) {
						User user = API.getUser((Player) s);
						msg(s, "nickname.change.target", Placeholders.c().addPlayer("player", user.getPlayer()).replace("nickname", args[0]));
						user.setNickname(args[0]);
					} else
						help(s, "admin_usage");
				}).permission(permission("cmd")).argument("-s", (s, structure, args) -> { // /cmd [nickname] -s
					if (s instanceof Player) {
						User user = API.getUser((Player) s);
						user.setNickname(args[0]);
					} else
						help(s, "admin_usage");

				}).parent() // cmd [nickname]
				.fallback((s, structure, args) -> { // /cmd [nickname] [player]
					offlinePlayer(s, args[0]);
				}).selector(Selector.PLAYER, (s, structure, args) -> { // /cmd [nickname] [player]
					User u = API.getUser(args[1]);

					msg(s, "nickname.change.sender", Placeholders.c().addPlayer("target", u.getPlayer())/* .replace("target", u.getName()) */.replace("nickname", args[0]));
					msg(s, "nickname.change.target", Placeholders.c().addPlayer("player", s)/* .replace("target", API.getPlayerName(s)) */.replace("nickname", args[0]));
					u.setNickname(args[0]);

				}).permission(permission("other")).argument("-s", (s, structure, args) -> { // /cmd [nickname] [player] -s
					User u = API.getUser(args[1]);

					u.setNickname(args[0]);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));

	}

	@Override
	public String configSection() {
		return "nickname";
	}

}

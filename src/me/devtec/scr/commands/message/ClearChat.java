package me.devtec.scr.commands.message;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class ClearChat implements ScrCommand {
	private static final String CHAT_MESSAGE = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n"
			+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n" + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n"
			+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n" + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n"
			+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n" + "\n\n\n\n\n\n\n\n\n\n";

	@Override
	public void init(List<String> cmds) {

		// clearchat [player] [-s
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			clearChat(null, permission("bypass"));
			MessageUtils.message(s, "clearchat.cleared", Placeholders.c().addPlayer("player", s), true, API.getOnlinePlayers(true).toArray(new CommandSender[0]));
		}).permission(permission("cmd")).fallback((s, structure, args) -> { // clearchat [player]
			offlinePlayer(s, args[0]);
		}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // clearchat [player]
			for (Player p : playerSelectors(s, args[0])) {
				if (p.getName().equalsIgnoreCase(s.getName())) // s is p
					continue;
				if (hasPermission(p, "bypass") && !p.getName().equalsIgnoreCase(s.getName()))
					msgSec(s, "bypass", Placeholders.c().addPlayer("target", p));
				else {
					clearChat(p, permission("bypass"));
					msgSec(s, "specific", Placeholders.c().addPlayer("target", p));
					msgSec(p, "cleared", Placeholders.c().addPlayer("player", s));
				}
			}
		}).argument("-s", (s, structure, args) -> { // clearchat [player] -s
			for (Player p : playerSelectors(s, args[0])) {
				if (p.getName().equalsIgnoreCase(s.getName())) // s is p
					continue;
				if (hasPermission(p, "bypass") && !p.getName().equalsIgnoreCase(s.getName()))
					msgSec(s, "bypass", Placeholders.c().addPlayer("target", p));
				else
					clearChat(p, permission("bypass"));
			}
		})

				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "clearchat";
	}

	public static void clearChat(Player target, String bypass_perm) {
		if (target == null)
			for (CommandSender p : API.getOnlinePlayersWithout(bypass_perm))
				p.sendMessage(CHAT_MESSAGE);
		else
			target.sendMessage(CHAT_MESSAGE);
	}

}

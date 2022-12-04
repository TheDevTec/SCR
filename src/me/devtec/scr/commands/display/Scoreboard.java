package me.devtec.scr.commands.display;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;

public class Scoreboard implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		if (Loader.plugin.scoreboard == null)
			return;
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (s instanceof Player) {
				Player player = (Player) s;
				if (Loader.plugin.scoreboard.players.containsKey(player.getUniqueId())) {
					msgSec(s, "disabled", Placeholders.c().addPlayer("player", player));
					Loader.plugin.scoreboard.hidden.add(player.getUniqueId());
					Loader.plugin.scoreboard.fullyUnregister(player);
				} else {
					msgSec(s, "enabled", Placeholders.c().addPlayer("player", player));
					Loader.plugin.scoreboard.hidden.remove(player.getUniqueId());
				}
			} else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
		}).argument("-s", (s, structure, args) -> { // cmd
			if (s instanceof Player) {
				Player player = (Player) s;
				if (Loader.plugin.scoreboard.players.containsKey(player.getUniqueId())) {
					Loader.plugin.scoreboard.hidden.add(player.getUniqueId());
					Loader.plugin.scoreboard.fullyUnregister(player);
				} else
					Loader.plugin.scoreboard.hidden.remove(player.getUniqueId());
			} else
				help(s, "usage");
		}).first().selector(Selector.BOOLEAN, (s, structure, args) -> { // cmd [boolean]
			if (s instanceof Player) {
				Player player = (Player) s;
				if (StringUtils.getBoolean(args[0])) {
					msgSec(s, "enabled", Placeholders.c().addPlayer("player", player));
					Loader.plugin.scoreboard.hidden.remove(player.getUniqueId());
				} else {
					msgSec(s, "disabled", Placeholders.c().addPlayer("player", player));
					if (!Loader.plugin.scoreboard.hidden.contains(player.getUniqueId())) {
						Loader.plugin.scoreboard.hidden.add(player.getUniqueId());
						Loader.plugin.scoreboard.fullyUnregister(player);
					}
				}
			} else
				help(s, "usage");
		}).argument("-s", (s, structure, args) -> { // cmd [boolean] -s
			if (s instanceof Player) {
				Player player = (Player) s;
				if (StringUtils.getBoolean(args[0]))
					Loader.plugin.scoreboard.hidden.remove(player.getUniqueId());
				else if (!Loader.plugin.scoreboard.hidden.contains(player.getUniqueId())) {
					Loader.plugin.scoreboard.hidden.add(player.getUniqueId());
					Loader.plugin.scoreboard.fullyUnregister(player);
				}
			} else
				help(s, "usage");
		}).first().selector(Selector.ENTITY_SELECTOR, (sender, structure, args) -> { // cmd [entity_selector]
			for (Player s : playerSelectors(sender, args[0]))
				if (Loader.plugin.scoreboard.players.containsKey(s.getUniqueId())) {
					if (sender.equals(s))
						msgSec(s, "disabled", Placeholders.c().addPlayer("player", s));
					else {
						msgSec(s, "other.disabled.sender", Placeholders.c().addPlayer("player", sender).addPlayer("target", s));
						msgSec(s, "other.disabled.receiver", Placeholders.c().addPlayer("target", s).addPlayer("player", sender));
					}
					Loader.plugin.scoreboard.hidden.add(s.getUniqueId());
					Loader.plugin.scoreboard.fullyUnregister(s);
				} else {
					if (sender.equals(s))
						msgSec(s, "enabled", Placeholders.c().addPlayer("player", s));
					else {
						msgSec(s, "other.enabled.sender", Placeholders.c().addPlayer("player", sender).addPlayer("target", s));
						msgSec(s, "other.enabled.receiver", Placeholders.c().addPlayer("target", s).addPlayer("player", sender));
					}
					Loader.plugin.scoreboard.hidden.remove(s.getUniqueId());
				}
		}).permission(permission("other")).argument("-s", (sender, structure, args) -> { // cmd [entity_selector] -s
			for (Player s : playerSelectors(sender, args[0]))
				if (Loader.plugin.scoreboard.players.containsKey(s.getUniqueId())) {
					Loader.plugin.scoreboard.hidden.add(s.getUniqueId());
					Loader.plugin.scoreboard.fullyUnregister(s);
				} else
					Loader.plugin.scoreboard.hidden.remove(s.getUniqueId());
		}).parent().selector(Selector.BOOLEAN, (sender, structure, args) -> { // cmd [entity_selector] [boolean]
			boolean status = StringUtils.getBoolean(args[1]);
			for (Player s : playerSelectors(sender, args[0]))
				if (status) {
					if (sender.equals(s))
						msgSec(s, "enabled", Placeholders.c().addPlayer("player", s));
					else {
						msgSec(s, "other.enabled.sender", Placeholders.c().addPlayer("player", sender).addPlayer("target", s));
						msgSec(s, "other.enabled.receiver", Placeholders.c().addPlayer("target", s).addPlayer("player", sender));
					}
					Loader.plugin.scoreboard.hidden.remove(s.getUniqueId());
				} else {
					if (sender.equals(s))
						msgSec(s, "disabled", Placeholders.c().addPlayer("player", s));
					else {
						msgSec(s, "other.disabled.sender", Placeholders.c().addPlayer("player", sender).addPlayer("target", s));
						msgSec(s, "other.disabled.receiver", Placeholders.c().addPlayer("target", s).addPlayer("player", sender));
					}
					if (!Loader.plugin.scoreboard.hidden.contains(s.getUniqueId())) {
						Loader.plugin.scoreboard.hidden.add(s.getUniqueId());
						Loader.plugin.scoreboard.fullyUnregister(s);
					}
				}
		}).argument("-s", (sender, structure, args) -> { // cmd [entity_selector] [boolean] -s
			boolean status = StringUtils.getBoolean(args[1]);
			for (Player s : playerSelectors(sender, args[0]))
				if (status)
					Loader.plugin.scoreboard.hidden.remove(s.getUniqueId());
				else if (!Loader.plugin.scoreboard.hidden.contains(s.getUniqueId())) {
					Loader.plugin.scoreboard.hidden.add(s.getUniqueId());
					Loader.plugin.scoreboard.fullyUnregister(s);
				}
		}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "scoreboard";
	}

}

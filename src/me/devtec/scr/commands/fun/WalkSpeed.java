package me.devtec.scr.commands.fun;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.ParseUtils;

public class WalkSpeed implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (s instanceof Player)
				if (hasPermission(s, "other"))
					help(s, "other");
				else
					help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.fallback((s, structure, args) -> { // fspeed [level]
					msg(s, "missing.number", Placeholders.c().replace("number", args[0]));
				}).selector(Selector.NUMBER, (s, structure, args) -> { // fspeed [level]

					double modifier = ParseUtils.getDouble(args[0]);
					if (modifier > 10.0)
						modifier = 10.0;
					if (modifier < -10.0)
						modifier = -10.0;
					((Player) s).setWalkSpeed((float) modifier / 10);
					msgSec(s, "you", Placeholders.c().add("speed", modifier));

				}, (s, structure, args) -> {
					List<String> list = new ArrayList<>();
					for (double i = -10; i <= 10; i++)
						list.add("" + i);
					return list;
				}).argument("-s", (s, structure, args) -> { // fspeed [level] -s

					double modifier = ParseUtils.getDouble(args[0]);
					if (modifier > 10.0)
						modifier = 10.0;
					if (modifier < -10.0)
						modifier = -10.0;
					((Player) s).setWalkSpeed((float) modifier / 10);
				}).parent() // fspeed [level]
				.parent() // fspeed
				.fallback((s, structure, args) -> { // fspeed [level]
					offlinePlayer(s, args[0]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // fspeed [player]
					help(s, "other");
				}).permission(permission("other")).fallback((s, structure, args) -> { // fspeed [player] [level]
					msg(s, "missing.number", Placeholders.c().replace("number", args[0]));
				}).selector(Selector.NUMBER, (s, structure, args) -> { // fspeed [player] [level]
					for (Player p : playerSelectors(s, args[0])) {
						double modifier = ParseUtils.getDouble(args[1]);
						if (modifier > 10.0)
							modifier = 10.0;
						if (modifier < -10.0)
							modifier = -10.0;
						p.setWalkSpeed((float) modifier / 10);
						msgSec(s, "other.sender", Placeholders.c().add("speed", modifier).addPlayer("target", p).addPlayer("player", s));
						msgSec(p, "other.receiver", Placeholders.c().add("speed", modifier).addPlayer("target", p).addPlayer("player", s));
					}
				}, (s, structure, args) -> {
					List<String> list = new ArrayList<>();
					for (double i = -10; i <= 10; i++)
						list.add("" + i);
					return list;
				}).argument("-s", (s, structure, args) -> { // fspeed [player] [level] -s
					for (Player p : playerSelectors(s, args[0])) {
						double modifier = ParseUtils.getDouble(args[1]);
						if (modifier > 10.0)
							modifier = 10.0;
						if (modifier < -10.0)
							modifier = -10.0;
						p.setWalkSpeed((float) modifier / 10);
					}
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "walkspeed";
	}
}

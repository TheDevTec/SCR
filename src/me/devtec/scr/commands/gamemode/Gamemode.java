package me.devtec.scr.commands.gamemode;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.Ref;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Gamemode implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		// gamemode [gamemode] {player}
		CommandStructure<CommandSender> str = CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)) // cd
				.permission(permission("cmd")) // perm
				// CREATIVE
				.argument("creative", (s, structure, args) -> { // cmd creative
					if (!(s instanceof Player)) {
						help(s, "usage");
						return;
					}
					Player p = (Player) s;
					p.setGameMode(GameMode.CREATIVE);
					msg(s, "gamemodes.target." + p.getGameMode().name().toLowerCase());
				}).permission(permission("creative")).argument("-s", (s, structure, args) -> { // cmd creative -s
					if (!(s instanceof Player)) {
						help(s, "usage");
						return;
					}
					Player p = (Player) s;
					p.setGameMode(GameMode.CREATIVE);
				}).parent() // cmd gamemode
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd creative [target]
					for (Player p : playerSelectors(s, args[1])) {
						p.setGameMode(GameMode.CREATIVE);
						msg(s, "gamemodes.target." + p.getGameMode().name().toLowerCase());
						msg(p, "gamemodes.sender." + p.getGameMode().name().toLowerCase(), Placeholders.c().addPlayer("target", p));
					}
				}).permission(permission("other")).argument("-s", (s, structure, args) -> { // cmd creative [target] -s
					for (Player p : playerSelectors(s, args[1]))
						p.setGameMode(GameMode.CREATIVE);
				})

				.first() // cmd
				// SURVIVAL
				.argument("survival", (s, structure, args) -> { // cmd survival
					if (!(s instanceof Player)) {
						help(s, "usage");
						return;
					}
					Player p = (Player) s;
					p.setGameMode(GameMode.SURVIVAL);
					msg(s, "gamemodes.target." + p.getGameMode().name().toLowerCase());
				}).permission(permission("survival")).argument("-s", (s, structure, args) -> { // cmd survival -s
					if (!(s instanceof Player)) {
						help(s, "usage");
						return;
					}
					Player p = (Player) s;
					p.setGameMode(GameMode.SURVIVAL);
				}).parent() // cmd survival
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd survival [target]
					for (Player p : playerSelectors(s, args[1])) {
						p.setGameMode(GameMode.SURVIVAL);
						msg(s, "gamemodes.target." + p.getGameMode().name().toLowerCase());
						msg(p, "gamemodes.sender." + p.getGameMode().name().toLowerCase(), Placeholders.c().addPlayer("target", p));
					}
				}).permission(permission("other")).argument("-s", (s, structure, args) -> { // cmd survival [target] -s
					for (Player p : playerSelectors(s, args[1]))
						p.setGameMode(GameMode.SURVIVAL);
				}).first() // cmd
				// SPECTATOR
				.argument("adventure", (s, structure, args) -> { // cmd adventure
					if (!(s instanceof Player)) {
						help(s, "usage");
						return;
					}
					Player p = (Player) s;
					p.setGameMode(GameMode.ADVENTURE);
					msg(s, "gamemodes.target." + p.getGameMode().name().toLowerCase());
				}).permission(permission("adventure")) // perm
				.argument("-s", (s, structure, args) -> {// cmd adventure -s
					if (!(s instanceof Player)) {
						help(s, "usage");
						return;
					}
					Player p = (Player) s;
					p.setGameMode(GameMode.ADVENTURE);
				}).parent() // cmd adventure
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd adventure [target]
					for (Player p : playerSelectors(s, args[1])) {
						p.setGameMode(GameMode.ADVENTURE);
						msg(s, "gamemodes.target." + p.getGameMode().name().toLowerCase());
						msg(p, "gamemodes.sender." + p.getGameMode().name().toLowerCase(), Placeholders.c().addPlayer("target", p));
					}
				}).permission(permission("other")) // perm
				.argument("-s", (s, structure, args) -> { // cmd adventure [target] -s
					for (Player p : playerSelectors(s, args[1]))
						p.setGameMode(GameMode.ADVENTURE);
				}).first();

		if (Ref.isNewerThan(7))
			str.argument("spectator", (s, structure, args) -> { // cmd spectator
				if (!(s instanceof Player)) {
					help(s, "usage");
					return;
				}
				Player p = (Player) s;
				p.setGameMode(GameMode.SPECTATOR);
				msg(s, "gamemodes.target." + p.getGameMode().name().toLowerCase());
			}).permission(permission("spectator")) // perm
					.argument("-s", (s, structure, args) -> { // cmd spectator
						if (!(s instanceof Player)) {
							help(s, "usage");
							return;
						}
						Player p = (Player) s;
						p.setGameMode(GameMode.SPECTATOR);
					}).parent() // cmd survival
					.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd spectator [target]
						for (Player p : playerSelectors(s, args[1])) {
							p.setGameMode(GameMode.SPECTATOR);
							msg(s, "gamemodes.target." + p.getGameMode().name().toLowerCase());
							msg(p, "gamemodes.sender." + p.getGameMode().name().toLowerCase(), Placeholders.c().addPlayer("target", p));
						}
					}).permission(permission("other")) // perm
					.argument("-s", (s, structure, args) -> { // cmd spectator [target] -s
						for (Player p : playerSelectors(s, args[1]))
							p.setGameMode(GameMode.SPECTATOR);
					});

		str.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "gamemode";
	}

}
package me.devtec.scr.commands.gamemode;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class GamemodeC implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		// /gmc {Player} -s
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (s instanceof Player) {
				Player p = (Player) s;

				p.setGameMode(GameMode.CREATIVE);
				msg(s, "gamemodes.target." + p.getGameMode().name().toLowerCase());
			} else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).argument("-s", (s, structure, args) -> { // cmd -s
			if (s instanceof Player) {
				Player p = (Player) s;

				p.setGameMode(GameMode.CREATIVE);
			} else
				help(s, "usage");
		}).parent() // /cmd
				.fallback((s, structure, args) -> {
					offlinePlayer(s, args[0]);
				}).selector(Selector.PLAYER, (s, structure, args) -> { // cmd {player}
					Player p = API.getPlayer(args[0]);

					p.setGameMode(GameMode.CREATIVE);
					msg(s, "gamemodes.sender." + p.getGameMode().name().toLowerCase(), Placeholders.c().addPlayer("target", p));
					msg(p, "gamemodes.target." + p.getGameMode().name().toLowerCase());

				}).argument("-s", (s, structure, args) -> { // cmd {player} -s
					Player p = API.getPlayer(args[0]);

					p.setGameMode(GameMode.CREATIVE);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));

	}

	@Override
	public String configSection() {
		return "gamemodeCreative";
	}

}
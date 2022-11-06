package me.devtec.scr.commands.info;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;

public class Staff implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			List<Player> staff = new ArrayList<>();
			List<String> staff_groups = Loader.config.getStringList("staff");

			for (Player player : API.getOnlinePlayersFor(s))
				if (staff_groups.contains(Loader.getVaultGroup(player)))
					staff.add(player);
			if (!staff.isEmpty()) {
				msgSec(s, "header", Placeholders.c().add("staff_online", staff.size()));
				for (Player p : staff)
					msgSec(s, "format", Placeholders.c().add("staff_online", staff.size()).addPlayer("player", p));
				msgSec(s, "footer", Placeholders.c().add("staff_online", staff.size()));
			} else
				msgSec(s, "noone");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // ping
				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "staff";
	}
}
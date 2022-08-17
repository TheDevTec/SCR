package me.devtec.scr.commands.info;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.functions.Tablist;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.commands.structures.CommandStructure;

public class ListCmd implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {

			List<String> staff_groups = Loader.config.getStringList("staff");

			List<Player> staff = new ArrayList<>();
			List<Player> players = new ArrayList<>();

			for (Player player : API.getOnlinePlayersFor(s))
				if (staff_groups.contains(Tablist.getVaultGroup(player)))
					staff.add(player);
				else
					players.add(player);
			if (players.isEmpty() && staff.isEmpty())
				return;
			StringBuilder staff_b = new StringBuilder();
			for (Player staff_player : staff) {
				if (staff_b.length() != 0)
					staff_b.append(Loader.translations.getString(configSection() + ".format.split"));
				staff_b.append(PlaceholderAPISupport.replace(
						Loader.translations.getString(configSection() + ".format.staff"), staff_player, true, null));
			}
			StringBuilder player_b = new StringBuilder();
			for (Player player_p : players) {
				if (player_b.length() != 0)
					player_b.append(Loader.translations.getString(configSection() + ".format.split"));
				player_b.append(PlaceholderAPISupport.replace(
						Loader.translations.getString(configSection() + ".format.player"), player_p, true, null));
			}
			msgSec(s, "message", Placeholders.c().add("staff", staff_b.length() == 0 ? "-" : staff_b).add("players", player_b.length() == 0 ? "-" : player_b)
					.add("online", staff.size() + players.size()).add("online_max", Bukkit.getMaxPlayers()));
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // ping
				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "list";
	}
}

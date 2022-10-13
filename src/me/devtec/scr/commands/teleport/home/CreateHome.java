package me.devtec.scr.commands.teleport.home;

import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.theapi.bukkit.game.Position;
import me.devtec.theapi.bukkit.xseries.XMaterial;

public class CreateHome implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (HomeManager.homesOf(s.getUniqueId()).size() + 1 > HomeManager.getLimit(s)) {
				msgSec(s, "limit", Placeholders.c().add("limit", HomeManager.getLimit(s)));
				return;
			}
			HomeManager.create(s.getUniqueId(), "home", new Position(s), XMaterial.OAK_DOOR);
			msgSec(s, "created", Placeholders.c().add("home", "home"));
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).argument(null, (s, structure, args) -> { // cmd [any string]
			HomeHolder home = HomeManager.find(s.getUniqueId(), args[0]);
			if (home != null) {
				msgSec(s, "already_exists", Placeholders.c().add("home", home.name()));
				return;
			}
			if (HomeManager.homesOf(s.getUniqueId()).size() + 1 > HomeManager.getLimit(s)) {
				msgSec(s, "limit", Placeholders.c().add("limit", HomeManager.getLimit(s)));
				return;
			}
			HomeManager.create(s.getUniqueId(), args[0], new Position(s), XMaterial.OAK_DOOR);
			msgSec(s, "created", Placeholders.c().add("home", args[0]));
		}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure();
	}

	@Override
	public String configSection() {
		return "sethome";
	}

}

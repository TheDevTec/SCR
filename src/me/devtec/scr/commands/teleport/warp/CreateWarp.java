package me.devtec.scr.commands.teleport.warp;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.theapi.bukkit.game.Position;

public class CreateWarp implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")).argument(null, (s, structure, args) -> { // cmd [any string]
			WarpHolder warp = WarpManager.find(args[0]);
			if (warp != null) {
				msgSec(s, "already_exists", Placeholders.c().add("warp", warp.name()));
				return;
			}
			WarpManager.create(s.getUniqueId(), args[0], new Position(s), Material.ENDER_PEARL, null, 0);
			msgSec(s, "created", Placeholders.c().add("warp", args[0]));
		}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure();
	}

	@Override
	public String configSection() {
		return "setwarp";
	}

}

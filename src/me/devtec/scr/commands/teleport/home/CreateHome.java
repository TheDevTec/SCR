package me.devtec.scr.commands.teleport.home;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.theapi.bukkit.game.Position;

public class CreateHome implements ScrCommand {

	@Override
	public void init(int cd, List<String> cmds) {
		cooldownMap.put(CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, "usage");
		}).permission(permission("cmd")).argument(null, (s, structure, args) -> { // cmd [any string]
			HomeHolder home = HomeManager.find(s.getUniqueId(), args[0]);
			if (home != null) {
				msgSec(s, "already_exists", Placeholders.c().add("home", home.name()));
				return;
			}
			HomeManager.create(s.getUniqueId(), args[0], new Position(s), Material.ENDER_PEARL);
			msgSec(s, "created", Placeholders.c().add("home", args[0]));
		}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure(), new CooldownHolder(this, cd));
	}

	@Override
	public String configSection() {
		return "sethome";
	}

}

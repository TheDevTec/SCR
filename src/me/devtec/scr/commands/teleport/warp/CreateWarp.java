package me.devtec.scr.commands.teleport.warp;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.theapi.bukkit.game.Position;

public class CreateWarp implements ScrCommand {
	
	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, 0);
		}).permission("scr."+configSection())
			.argument(null, (s, structure, args) -> { // cmd [any string]
				WarpHolder warp = WarpManager.find(args[0]);
				if(warp != null) {
					msgConfig(s, configSection()+".already_exists", warp.name());
					return;
				}
				WarpManager.create(s.getUniqueId(), args[0], new Position(s), Material.ENDER_PEARL, null, 0);
				msgConfig(s, configSection()+".created", args[0]);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "setwarp";
	}
	
}

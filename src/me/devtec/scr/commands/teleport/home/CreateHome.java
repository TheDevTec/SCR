package me.devtec.scr.commands.teleport.home;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.theapi.bukkit.game.Position;

public class CreateHome implements ScrCommand {
	
	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, 0);
		}).permission("scr."+configSection())
			.argument(null, (s, structure, args) -> { // cmd [any string]
				HomeHolder holder = HomeManager.find(s.getUniqueId(), args[0]);
				if(holder!=null) {
					msgConfig(s, configSection()+".already_exists", holder.name());
					return;
				}
				HomeManager.create(s.getUniqueId(), args[0], new Position(s), Material.ENDER_PEARL);
				msgConfig(s, configSection()+".created", args[0]);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "sethome";
	}
	
}

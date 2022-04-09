package me.devtec.scr.commands.teleport.spawn;

import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.theapi.bukkit.game.Position;

public class SetSpawn implements ScrCommand {
	
	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			Spawn.spawn=new Position(s);
			msgConfig(s, configSection(), Spawn.spawn.getWorldName(), Spawn.spawn.getBlockX(), Spawn.spawn.getBlockY(), Spawn.spawn.getBlockZ());
		}).permission("scr."+configSection()).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "setspawn";
	}
	
}

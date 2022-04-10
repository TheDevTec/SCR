package me.devtec.scr.commands.teleport.home;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;

public class DeleteHome implements ScrCommand {
	
	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, 0);
		}).permission("scr."+configSection()).fallback((s, structure, args) -> {
			msgConfig(s, configSection()+".not_exists", args[0]);
			})
			.callableArgument((s, structure, args) -> {return new ArrayList<>(HomeManager.homesOf(s.getUniqueId()));}, (s, structure, args) -> { // cmd [warp]
				HomeHolder warp = HomeManager.find(s.getUniqueId(), args[0]);
				HomeManager.delete(s.getUniqueId(), warp.name());
				msgConfig(s, configSection()+".deleted", warp.name());
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "delhome";
	}
	
}

package me.devtec.scr.commands.teleport.warp;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;

public class DeleteWarp implements ScrCommand {
	
	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, 0);
		}).permission("scr."+configSection()).fallback((s, structure, args) -> {
			msgConfig(s, configSection()+".not_exists", args[0]);
			})
			.callableArgument((s) -> {return WarpManager.availableWarpNames(s);}, (s, structure, args) -> { // cmd [warp]
				WarpHolder warp = WarpManager.find(args[0]);
				WarpManager.delete(warp.name());
				msgConfig(s, configSection()+".deleted", warp.name());
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "delwarp";
	}
	
}

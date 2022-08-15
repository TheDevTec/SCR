package me.devtec.scr.commands.server_managment;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;

public class Chunks  implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { //chunks
			int chunks = 0;
			for(World world : Bukkit.getWorlds()) {
				chunks = chunks + world.getLoadedChunks().length;
				msgSec(s, "load", Placeholders.c().add("world", world.getName())
						.add("chunks", world.getLoadedChunks().length));
			}
			msgSec(s, "totalLoad", Placeholders.c().add("chunks", chunks));
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "chunks";
	}
}

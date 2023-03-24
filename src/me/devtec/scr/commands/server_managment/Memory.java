package me.devtec.scr.commands.server_managment;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.MemoryAPI;

public class Memory implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (Loader.config.getBoolean("options.ram-percentage"))
				msg(s, "memory",
						Placeholders.c().add("ram_free", "" + MemoryAPI.getFreeMemory(true)).add("ram_max", "" + MemoryAPI.getMaxMemory()).add("ram_used", "" + MemoryAPI.getUsedMemory(true)));
			else
				msg(s, "memory",
						Placeholders.c().add("ram_free", "" + MemoryAPI.getFreeMemory(false)).add("ram_max", "" + MemoryAPI.getMaxMemory()).add("ram_used", "" + MemoryAPI.getUsedMemory(false)));

		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "memory";
	}
}

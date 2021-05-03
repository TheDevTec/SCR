package me.devtec.servercontrolreloaded.commands.info;


import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.MultiWorldsUtils;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class Chunks implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Chunks", "Info")) {
			if (args.length == 0) {
				int chunks = 0;
				for (World w : Bukkit.getWorlds()) {
					chunks = chunks + w.getLoadedChunks().length;
					Loader.sendMessages(s, "Chunks.Load", Placeholder.c()
							.add("%world%", w.getName())
							.add("%chunks%", String.valueOf(w.getLoadedChunks().length)));
				}
				Loader.sendMessages(s, "Chunks.TotalLoad", Placeholder.c()
						.add("%chunks%", String.valueOf(chunks)));
				return true;
			}
			MultiWorldsUtils.unloadWorlds(s);
			return true;
		}
		Loader.noPerms(s, "Chunks", "Info");
		return true;
	}

	private static final List<String> Unload = Arrays.asList("Unload");

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (args.length==1 && Loader.has(s, "Chunks", "Info"))
			return StringUtils.copyPartialMatches(args[0], Unload);
		return Arrays.asList();
	}
}
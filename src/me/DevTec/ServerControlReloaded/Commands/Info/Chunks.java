package me.DevTec.ServerControlReloaded.Commands.Info;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.Collections.UnsortedList;

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
		List<String> c = new UnsortedList<>();
		if (cmd.getName().equalsIgnoreCase("chunks") && args.length == 1) {
			if (Loader.has(s, "Chunks", "Info")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Unload, new UnsortedList<>()));
			}
		}
		return c;
	}
}
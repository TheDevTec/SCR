package Commands.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.MultiWorldsUtils;

public class Chunks implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (Loader.has(s, "Chunks", "Info")) {
			if (cmd.getName().equalsIgnoreCase("Chunks")) {
				if (args.length == 0) {
					int chunks = 0;
					for (World w : Bukkit.getWorlds()) {
						chunks = chunks + w.getLoadedChunks().length;
						Loader.sendMessages(s, "Chunk.Load", Placeholder.c()
								.add("%world%", w.getName())
								.add("%chunks%", String.valueOf(w.getLoadedChunks().length)));
					}
					Loader.sendMessages(s, "Chunk.TotalLoad", Placeholder.c()
							.add("%chunks%", String.valueOf(chunks)));
					return true;
				}
				if (args[0].equalsIgnoreCase("Unload")) {
					MultiWorldsUtils.unloadWorlds(s);
					return true;
				}
			}
		}
		Loader.noPerms(s, "Chunks", "Info");
		return true;
	}

	private static final List<String> Unload = Arrays.asList("Unload");

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (cmd.getName().equalsIgnoreCase("chunks") && args.length == 1) {
			if (Loader.has(s, "Chunks", "Info")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Unload, new ArrayList<>()));
			}
		}
		return c;
	}
}
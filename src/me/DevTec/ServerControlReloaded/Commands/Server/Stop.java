package me.DevTec.ServerControlReloaded.Commands.Server;


import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import me.DevTec.ServerControlReloaded.Commands.Server.BigTask.TaskType;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.Collections.UnsortedList;

public class Stop implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Stop", "Server")) {
			if (args.length == 0) {
				BigTask.start(TaskType.STOP, Loader.config.getLong("Options.WarningSystem.Stop.PauseTime"));
				return true;
			}
			if (args[0].equalsIgnoreCase("cancel")) {
				BigTask.cancel();
				return true;
			}
			if (args[0].equalsIgnoreCase("now")) {
				BigTask.start(TaskType.STOP, 0);
				return true;
			}
			if (BigTask.r == -1)
				BigTask.start(TaskType.STOP, StringUtils.getTimeFromString(args[0]));
			return true;
		}
		Loader.noPerms(s, "Stop", "Server");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new UnsortedList<>();
		if (s.hasPermission("ServerControl.Stop") && args.length == 1)
			c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("15s", "30s", "now", "cancel"),
					new UnsortedList<>()));
		return c;
	}

}

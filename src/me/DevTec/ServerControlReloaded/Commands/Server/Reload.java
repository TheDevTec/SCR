package me.DevTec.ServerControlReloaded.Commands.Server;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import me.DevTec.ServerControlReloaded.Commands.Server.BigTask.TaskType;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.utils.StringUtils;

public class Reload implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Reload", "Server")) {
			if (args.length == 0) {
				BigTask.start(TaskType.RELOAD, Loader.config.getLong("Options.WarningSystem.Reload.PauseTime"));
				return true;
			}
			if (args[0].equalsIgnoreCase("cancel")) {
				BigTask.cancel();
				return true;
			}
			if (args[0].equalsIgnoreCase("now")) {
				BigTask.start(TaskType.RELOAD, 0);
				return true;
			}
			if (BigTask.r == -1)
				BigTask.start(TaskType.RELOAD, StringUtils.getTimeFromString(args[0]));
			return true;
		}
		Loader.noPerms(s, "Reload", "Server");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (s.hasPermission("ServerControl.Reload") && args.length == 1)
			c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("15s", "30s", "now", "cancel"),
					new ArrayList<>()));
		return c;
	}
}

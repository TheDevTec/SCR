package me.devtec.servercontrolreloaded.commands.server;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.servercontrolreloaded.commands.CommandHolder;
import me.devtec.servercontrolreloaded.commands.server.BigTask.TaskType;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.utils.StringUtils;

public class Reload extends CommandHolder {

	public Reload(String section, String name) {
		super(section, name);
	}

	@Override
	public List<String> tabCompleter(CommandSender s, String[] args) {
		if(args.length==1)
			return StringUtils.copyPartialMatches(args[0], Arrays.asList("15s", "30s", "cancel"));
		return Collections.emptyList();
	}

	@Override
	public void command(CommandSender s, String[] args) {
		if (args.length == 0) {
			apply(StringUtils.getTimeFromString(Loader.config.getString("Options.WarningSystem.Reload.PauseTime")));
			return;
		}
		if (args[0].equalsIgnoreCase("cancel")) {
			BigTask.cancel(true);
			return;
		}
		apply(StringUtils.getTimeFromString(args[0]));
	}
	
	public static void apply(long time) {
		if(time<=0)
			BigTask.end();
		else
		if (BigTask.r == -1) {
			BigTask.start(TaskType.RELOAD, time);
		}
	}
}
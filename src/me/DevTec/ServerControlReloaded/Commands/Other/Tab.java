package me.DevTec.ServerControlReloaded.Commands.Other;


import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.theapi.utils.datakeeper.collections.UnsortedList;

public class Tab implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String awd, String[] args) {
		//must be rewrited
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new UnsortedList<>();
		//must be rewrited
		return c;
	}
}

package me.DevTec.ServerControlReloaded.Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.devtec.theapi.TheAPI;

public class RulesCmd implements CommandExecutor {


	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		for(String read : Loader.rulesText)
			TheAPI.msg(TabList.replace(read, s instanceof Player?(Player)s:null, false), s);
		return true;
	}
}

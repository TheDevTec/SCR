package me.DevTec.ServerControlReloaded.Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.Utils.MultiWorldsGUI;

public class MultiWorlds implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				MultiWorldsGUI.openInv((Player) s);
				return true;
			}
			return true;
		}
		//must rewrite code
		return true;
	}
}

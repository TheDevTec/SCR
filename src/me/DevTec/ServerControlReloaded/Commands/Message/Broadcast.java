package me.DevTec.ServerControlReloaded.Commands.Message;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Broadcast implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Broadcast", "Message")) {
			if (args.length == 0) {
				Loader.Help(s, "Broadcast", "Message");
				return true;
			}
			String msg = TheAPI.buildString(args);
			TheAPI.broadcastMessage(Loader.config.getString("Format.Broadcast").replace("%sender%", s.getName())
					.replace("%message%", msg));
			return true;
		}
		Loader.noPerms(s, "Broadcast", "Message");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
	}
}

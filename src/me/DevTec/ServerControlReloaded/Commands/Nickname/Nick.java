package me.DevTec.ServerControlReloaded.Commands.Nickname;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Nick implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (s instanceof Player) {
			if (Loader.has(s, "Nickname", "Nickname")) {
				if (args.length == 0) {
					Loader.Help(s, "Nickname", "Nickname");
					return true;
				}
				String msg = TheAPI.buildString(args);
				TheAPI.getUser(s.getName()).setAndSave("DisplayName", msg);
				if(TheAPI.getPlayerOrNull(s.getName())!=null)
					TheAPI.getPlayerOrNull(s.getName()).setCustomName(TheAPI.colorize(msg));
				Loader.sendMessages(s, "Nickname.Change", Placeholder.c()
						.add("%nickname%", msg)
						.add("%nick%", msg));
				return true;
			}
			Loader.noPerms(s, "Nickname", "Nickname");
			return true;
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Nickname", "Nickname"))
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

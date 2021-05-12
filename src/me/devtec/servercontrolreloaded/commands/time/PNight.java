package me.devtec.servercontrolreloaded.commands.time;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PNight implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "PlayerNight", "Time")) {
					((Player) s).setPlayerTime(13000, false);
					Loader.sendMessages(s, "Time.PlayerNight", Placeholder.c().add("%world%", ((Player) s).getWorld().getName())
							.add("%player%", s.getName()));
					return true;
				}
				Loader.noPerms(s, "PlayerNight", "Time");
				return true;
			}
			Loader.Help(s, "PlayerNight", "Time");
			return true;
		}
		if (args.length == 1) {
			if (Loader.has(s, "PlayerNight", "Time", "Other")) {
				if (TheAPI.getPlayer(args[0]) != null) {
					TheAPI.getPlayer(args[0]).setPlayerTime(13000, false);
					Loader.sendMessages(s, "Time.PlayerNight", Placeholder.c().add("%world%", TheAPI.getPlayer(args[0]).getWorld().getName())
							.add("%player%", args[0]));
					return true;
				}
				Loader.notOnline(s, args[0]);
				return true;
			}
			Loader.noPerms(s, "PlayerNight", "Time", "Other");
			return true;
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "PlayerNight", "Time", "Other") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

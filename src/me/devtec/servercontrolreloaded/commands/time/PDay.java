package me.devtec.servercontrolreloaded.commands.time;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class PDay implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "PlayerDay", "Time")) {
			if(!CommandsManager.canUse("Time.PlayerDay", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Time.PlayerDay", s))));
				return true;
			}
			if (args.length == 0) {
				if (s instanceof Player) {
					((Player) s).setPlayerTime(1000, false);
					Loader.sendMessages(s, "Time.PlayerDay", Placeholder.c().add("%world%", ((Player) s).getWorld().getName())
							.add("%player%", s.getName()));
					return true;
				}
				Loader.Help(s, "PlayerDay", "Time");
				return true;
			}
			if (Loader.has(s, "PlayerDay", "Time", "Other")) {
				if (TheAPI.getPlayer(args[0]) != null) {
					TheAPI.getPlayer(args[0]).setPlayerTime(1000, false);
					Loader.sendMessages(s, "Time.PlayerDay", Placeholder.c().add("%world%", TheAPI.getPlayer(args[0]).getWorld().getName())
							.add("%player%", args[0]));
					return true;
				}
				Loader.notOnline(s, args[0]);
				return true;
			}
			Loader.noPerms(s, "PlayerDay", "Time", "Other");
			return true;
		}
		Loader.noPerms(s, "PlayerDay", "Time");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "PlayerDay", "Time", "Other") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}
}

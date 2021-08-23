package me.devtec.servercontrolreloaded.commands.other;

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
import me.devtec.servercontrolreloaded.utils.Repeat;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Feed implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Feed", "Other") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Feed", "Other")) {
		if(!CommandsManager.canUse("Other.Feed", s)) {
			Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Feed", s))));
			return true;
		}
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					p.setFoodLevel(20);
					Loader.sendMessages(s, "Feed.You");
					return true;
				}
				Loader.Help(s, "Feed", "Other");
				return true;
			}
			if (args[0].equals("*")) {
				Repeat.a(s, "feed *");
				return true;
			}
			Player p = TheAPI.getPlayer(args[0]);
			if(p==null) {
				Loader.notOnline(s, args[0]);
				return true;
			}
			if (p == s) {
				if (Loader.has(s, "Feed", "Other")) {
				p.setFoodLevel(20);
				Loader.sendMessages(s, "Feed.You");
				return true;
				}
				Loader.noPerms(s, "Feed", "Other");
				return true;
			}
			if (Loader.has(s, "Feed", "Other","Other")) {
				p.setFoodLevel(20);
				Loader.sendMessages(s, "Feed.Other.Sender", Placeholder.c().replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName()));
				Loader.sendMessages(p, "Feed.Other.Receiver", Placeholder.c().replace("%player%", s.getName())
						.replace("%playername%", s.getName()));
				return true;
			}
			Loader.noPerms(s, "Feed", "Other", "Other");
			return true;
		}
		Loader.noPerms(s, "Feed", "Other");
		return true;
	}

}
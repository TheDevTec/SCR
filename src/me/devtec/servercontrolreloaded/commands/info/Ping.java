package me.devtec.servercontrolreloaded.commands.info;


import me.devtec.servercontrolreloaded.commands.CommandsManager;
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
import java.util.Collections;
import java.util.List;

public class Ping implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Ping", "Info")) {
			if(!CommandsManager.canUse("Info.Ping", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Info.Ping", s))));
				return true;
			}
			if(args.length==0) {
				if(s instanceof Player)
				Loader.sendMessages(s, "Ping.You", Placeholder.c().add("%ping%", ""+TheAPI.getPlayerPing((Player)s)));
				else Loader.Help(s, "Ping", "Info");
				return true;
			}
			if(Loader.has(s, "Ping", "Info", "Other")) {
				Player p = TheAPI.getPlayer(args[0]);
				if(p==null) {
					Loader.notOnline(s, args[0]);
					return true;
				}
				Loader.sendMessages(s, "Ping.Other", Placeholder.c().add("%player%", p.getName()).add("%ping%", ""+TheAPI.getPlayerPing(p))
						.add("%playername%", p.getDisplayName()).add("%customname%", p.getCustomName()));
				return true;
			}
			if(s instanceof Player)
			Loader.sendMessages(s, "Ping.You", Placeholder.c().add("%ping%", ""+TheAPI.getPlayerPing((Player)s)));
			return true;
		}
		Loader.noPerms(s, "Ping", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Ping", "Info", "Other") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}
}

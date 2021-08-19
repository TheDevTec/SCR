package me.devtec.servercontrolreloaded.commands.other;

import java.util.Arrays;
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
import me.devtec.servercontrolreloaded.utils.SPlayer;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class AFK implements CommandExecutor, TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "AFK", "Other"))
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "AFK", "Other")) {
			if(!CommandsManager.canUse("Other.AFK", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.AFK", s))));
				return true;
			}
			if (args.length == 0) {
				if (s instanceof Player) {
					SPlayer p = API.getSPlayer((Player) s);
                    p.setAFK(!p.isAFK());
					return true;
				}
				Loader.Help(s, "AFK", "Other");
				return true;
			}
			if (Loader.has(s, "AFK", "Other", "Other")) {
				if (args[0].equals("*")) {
					Repeat.a(s, "AFK *");
					return true;
				}
				Player player = TheAPI.getPlayer(args[0]);
				if(player!=null) {
					SPlayer p = API.getSPlayer(player);
					if (p.isAFK()) {
						Loader.sendMessages(s, "AFK.Command.Other.End", new Placeholder().add("%target%", player.getName()).add("%player%", player.getName()));
						Loader.sendMessages(player, "AFK.Command.End");
						p.setAFK(false);
					} else {
						Loader.sendMessages(s, "AFK.Command.Other.Start", new Placeholder().add("%target%", player.getName()).add("%player%", player.getName()));
						Loader.sendMessages(player, "AFK.Command.Start");
						p.setAFK(true);
					}
					return true;
				}
			}
			if(Loader.has(s, "AFK", "Other")) {
				if (Loader.has(s, "AFK", "Other", "Reason")) {
					if (s instanceof Player) {
						SPlayer p = API.getSPlayer((Player) s);
						if (p.isAFK()) {
							p.setAFK(false);
						} else {
							p.setAFK(true, StringUtils.buildString(args));
						}
						return true;
					}
					Loader.Help(s, "AFK", "Other");
					return true;
				}
				Loader.noPerms(s, "AFK", "Other", "Reason");
				return true;
				
			}
			Loader.noPerms(s, "AFK", "Other");
			return true;
		}
		Loader.noPerms(s, "AFK", "Other");
		return true;
	}
}

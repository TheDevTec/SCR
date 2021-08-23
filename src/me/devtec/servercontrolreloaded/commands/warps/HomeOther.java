package me.devtec.servercontrolreloaded.commands.warps;


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
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class HomeOther implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			if (Loader.has(s, "HomeOther", "Warps")) {
				if(!CommandsManager.canUse("Warps.HomeOther", s)) {
					Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Warps.HomeOther", s))));
					return true;
				}
				if (args.length <= 1) {
					Loader.Help(s, "HomeOther", "Warps");
					return true;
				}
				if (args.length == 2) {
					User d = TheAPI.getUser(args[0]);
					if (d.exist("Homes." + args[1])) {
						Position loc = Position.fromString(d.getString("Homes." + args[1]));
						if (loc != null) {
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport((Player)s,false,loc);
							else
								API.teleport((Player)s, loc);
							Loader.sendMessages(s, "Home.Other.Teleporting", Placeholder.c()
									.add("%home%", args[1])
									.add("%player%", p.getName())
									.add("%playername%", p.getDisplayName()));
							return true;
						}
					}
					Loader.sendMessages(s, "Home.Other.NotExist", Placeholder.c()
							.add("%home%", args[1])
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					return true;
				}
				if (Loader.has(s, "HomeOther", "Warps", "Other")) {
					Player pl = TheAPI.getPlayer(args[2]);
					if (pl == null) {
						Loader.notOnline(s, args[2]);
						return true;
					}
					User d = TheAPI.getUser(args[0]);
					if (d.exist("Homes." + args[1])) {
						Position loc = Position.fromString(d.getString("Homes." + args[1]));
						if (loc != null) {
							API.setBack(pl);
							if (setting.tp_safe)
								API.safeTeleport(pl,false,loc);
							else
								API.teleport(pl, loc);
							Loader.sendMessages(s, "Home.Other.Teleporting", Placeholder.c()
									.add("%home%", args[1])
									.add("%player%", p.getName())
									.add("%playername%", p.getDisplayName()));
							return true;
						}
					}
					Loader.sendMessages(s, "Home.Other.NotExist", Placeholder.c()
							.add("%home%", args[1])
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					return true;
				}
				Loader.noPerms(s, "HomeOther", "Warps","Other");
				return true;
			}
			Loader.noPerms(s, "HomeOther", "Warps");
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (Loader.has(s, "HomeOther", "Warps")) {
			if(args.length == 1)
				return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
			if(args.length==2)
				return StringUtils.copyPartialMatches(args[1], TheAPI.getUser(args[0]).getKeys("Homes"));
			if(args.length == 3 && Loader.has(s, "HomeOther", "Warps", "Other"))
				return StringUtils.copyPartialMatches(args[2], API.getPlayerNames(s));
		}
		return Collections.emptyList();
	}
}

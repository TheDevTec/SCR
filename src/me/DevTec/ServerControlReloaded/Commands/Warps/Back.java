package me.DevTec.ServerControlReloaded.Commands.Warps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class Back implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Back", "Warps")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					API.TeleportBack((Player) s);
					return true;
				}
				Loader.Help(s, "Back", "Warps");
				return true;
			}
			Player p = TheAPI.getPlayer(args[0]);
			if (p == null) {
				Loader.notOnline(s, args[0]);
				return true;
			}
			if (p == s) {
				API.TeleportBack(p);
				return true;
			}
			if (Loader.has(s, "Back", "Warps", "Other")) {
				Loader.sendMessages(s, "Back.Teleport.Other.Sender", Placeholder.c()
						.add("%player%", p.getName())
						.add("%playername%", p.getDisplayName()));
				Loader.sendMessages(p, "Back.Teleport.Other.Receiver", Placeholder.c()
						.add("%player%", s.getName())
						.add("%playername%", s.getName()));
				API.TeleportBack(p);
				return true;
			}
			Loader.noPerms(s, "Back", "Warps", "Other");
			return true;
		}
		Loader.noPerms(s, "Back", "Warps");
		return true;
	}
}
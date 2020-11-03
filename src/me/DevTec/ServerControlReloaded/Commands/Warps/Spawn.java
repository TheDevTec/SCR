package me.DevTec.ServerControlReloaded.Commands.Warps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.API.TeleportLocation;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class Spawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Spawn", "Warps")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					API.setBack((Player) s);
					API.teleportPlayer((Player) s, TeleportLocation.SPAWN);
					Loader.sendMessages(s, "Spawn.Teleport.You");
					return true;
				} else {
					Loader.Help(s, "Spawn", "Warps");
					return true;
				}
			}
			Player p = TheAPI.getPlayer(args[0]);
			if (p == null) {
				Loader.notOnline(s, args[0]);
				return true;
			}
			if (p == s) {
				API.setBack(p);
				API.teleportPlayer(p, TeleportLocation.SPAWN);
				Loader.sendMessages(p, "Spawn.Teleport.You");
				return true;
			}
			if (Loader.has(s, "Spawn", "Warps", "Other")) {
				API.setBack(p);
				API.teleportPlayer(p, TeleportLocation.SPAWN);
				Loader.sendMessages(p, "Spawn.Teleport.Other.Receiver");
				Loader.sendMessages(s, "Spawn.Teleport.Other.Sender", Placeholder.c()
						.add("%player%", p.getName())
						.add("%playername%", p.getDisplayName()));
				return true;
			}
			Loader.noPerms(s, "Spawn", "Warps", "Other");
			return true;
		}
		Loader.noPerms(s, "Spawn", "Warps");
		return true;
	}
}

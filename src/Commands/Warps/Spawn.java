package Commands.Warps;

import org.bukkit.command.Command; 
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader.Placeholder;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class Spawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Spawn", "Warps")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					API.setBack((Player) s);
					API.teleportPlayer((Player) s, TeleportLocation.SPAWN);
					Loader.sendMessages(s, "Spawn.Teleported.You");
					return true;
				} else {
					Loader.Help(s, "/Spawn <player>", "Warps");
					return true;
				}
			}
			Player p = TheAPI.getPlayer(args[0]);
			if (p == null) {
				Loader.sendMessages(s, "Missing.Player.NotExist", Placeholder.c()
						.add("%player%", args[0])
						.add("%playername%", args[0]));
				return true;
			}
			if (p == s) {
				API.setBack(p);
				API.teleportPlayer(p, TeleportLocation.SPAWN);
				Loader.sendMessages(p, "Spawn.Teleported.You");
				return true;
			}
			if (Loader.has(s, "Spawn", "Warps", "Other")) {
				API.setBack(p);
				API.teleportPlayer(p, TeleportLocation.SPAWN);
				Loader.sendMessages(p, "Spawn.Other.Sender");
				Loader.sendMessages(s, "Spawn.Teleported.You", Placeholder.c()
						.add("%player%", p.getName())
						.add("%playername%", p.getDisplayName()));
				return true;
			}
			return true;
		}
		return true;
	}
}

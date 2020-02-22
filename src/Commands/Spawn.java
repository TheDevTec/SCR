package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;

public class Spawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(API.hasPerm(s, "ServerControl.Spawn")) {
		if(args.length==0) {
		if(s instanceof Player) {
			API.setBack((Player)s);
		API.teleportPlayer((Player)s, TeleportLocation.SPAWN);
		Loader.msg(Loader.s("Spawn.TeleportedToSpawn")
					.replace("%world%", ((Player)s).getWorld().getName())
					.replace("%player%", s.getName())
					.replace("%playername%", ((Player)s).getDisplayName())
					, s);
			return true;
		}else {
			Loader.Help(s, "/Spawn <player>", "Spawn");
			return true;
		}}
			Player p = Bukkit.getPlayer(args[0]);
			if(p==null) {
				Loader.msg(Loader.PlayerNotEx(args[0]), s);
				return true;
			}
			if(p==s) {
					API.setBack(p);
					API.teleportPlayer(p, TeleportLocation.SPAWN);
					Loader.msg(Loader.s("Spawn.TeleportedToSpawn")
						.replace("%world%", p.getWorld().getName())
						.replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName())
						, p);
			return true;
			}
				if(API.hasPerm(s, "ServerControl.Spawn.Other")) {
							API.setBack(p);
							API.teleportPlayer(p, TeleportLocation.SPAWN);
							Loader.msg(Loader.s("Spawn.TeleportedToSpawn")
									.replace("%world%", p.getWorld().getName())
									.replace("%player%", p.getName())
									.replace("%playername%", p.getDisplayName())
									, p);
							Loader.msg(Loader.s("Spawn.PlayerTeleportedToSpawn")
									.replace("%world%", p.getWorld().getName())
									.replace("%player%", p.getName())
									.replace("%playername%", p.getDisplayName())
									, s);
			return true;
			}return true;
	}return true;
	}
}

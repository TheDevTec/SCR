package me.DevTec.ServerControlReloaded.Commands.Time;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class PDay implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "PlayerDay", "Time")) {
					((Player) s).setPlayerTime(1000, true);
					Loader.sendMessages(s, "Time.PDay", Placeholder.c().add("%world%", ((Player) s).getLocation().getWorld().getName())
							.add("%player%", s.getName()));
					return true;
				}
				Loader.noPerms(s, "PlayerDay", "Time");
				return true;
			}
			Loader.Help(s, "/Day <world>", "Time");
			return true;
		}
		if (args.length == 1) {
			if (Loader.has(s, "PlayerDay", "Time", "Other")) {
				if (TheAPI.getPlayer(args[0]) != null) {
					TheAPI.getPlayer(args[0]).setPlayerTime(1000, true);
					Loader.sendMessages(s, "Time.PDay", Placeholder.c().add("%world%", ((Player) s).getLocation().getWorld().getName())
							.add("%player%", args[0]));
					return true;
				}
				Loader.notOnline(s, args[0]);
				return true;
			}
			Loader.noPerms(s, "PlayerDay", "Time", "Other");
			return true;
		}
		return false;
	}
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

}

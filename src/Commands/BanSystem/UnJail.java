package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class UnJail implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.unJail")) {
			if (args.length == 0) {
				Loader.Help(s, "/unJail <player>", "BanSystem.unJail");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.existsUser(args[0])) {
					TheAPI.getUser(args[0]).set("Jail", null);
					TheAPI.getUser(args[0]).setAndSave("TempJail", null);
					if (TheAPI.getPlayer(args[0]) != null)
						API.teleportPlayer(TheAPI.getPlayer(args[0]), TeleportLocation.SPAWN);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.unJailed").replace("%playername%", args[0])
							.replace("%player%", args[0]), s);
					Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.UnJail").replace("%playername%", args[0])
							.replace("%operator%", s.getName())
							));
					TheAPI.sendMessage(Loader.s("BanSystem.UnJail").replace("%playername%", args[0])
							.replace("%operator%", s.getName()), s);
					return true;
				}
				if (TheAPI.existsUser(args[0]))
					TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.PlayerHasNotBan").replace("%player%", args[0])
							.replace("%playername%", args[0]), s);
				else
					TheAPI.msg(Loader.PlayerNotEx(args[0]), s);
				return true;
			}
		}
		return true;
	}
}

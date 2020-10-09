package Commands.Warps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
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
				Loader.Help(s, "/Back <player>", "Warps");
				return true;
			}
			Player p = TheAPI.getPlayer(args[0]);
			if (p == null) {
				Loader.sendMessages(s, "Missing.Player.NotExist", Placeholder.c()
						.add("%player%", args[0])
						.add("%playername%", args[0]));
				return true;
			}
			if (p == s) {
				API.TeleportBack(p);
				return true;
			}
			if (p != s) {
				if (Loader.has(s, "Back", "Warps", "Other")) {
					Loader.sendMessages(s, "Back.Teleport.Other.Sender", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					API.TeleportBack(p);
					return true;
				}
				return true;
			}
		}

		return true;
	}
}
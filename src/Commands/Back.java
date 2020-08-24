package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class Back implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Back")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					API.TeleportBack((Player) s);
					return true;
				}
				Loader.Help(s, "/Back <player>", "Back");
				return true;
			}
			Player p = TheAPI.getPlayer(args[0]);
			if (p == null) {
				TheAPI.msg(Loader.PlayerNotEx(args[0]), s);
				return true;
			}
			if (p == s) {
				API.TeleportBack(p);
				return true;
			}
			if (p != s) {//tak co? XD MY»
				if (API.hasPerm(s, "ServerControl.Back.Other")) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Back.PlayerTeleported").replace("%player%", p.getName())
							.replace("%playername%", p.getDisplayName()), s);
					API.TeleportBack(p);
					return true;
				}
				return true;
			}
		}

		return true;
	}
}
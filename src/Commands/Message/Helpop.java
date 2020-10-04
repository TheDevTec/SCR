package Commands.Message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class Helpop implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Helpop")) {
			if (args.length == 0) {
				Loader.Help(s, "/Helpop <message>", "Helpop");
				return true;
			}
			if (args.length >= 1) {
				TheAPI.broadcast(
						API.replacePlayerName(Loader.config.getString("Format.Helpop"), s.getName())
								.replace("%sender%", s.getName()).replace("%message%", TheAPI.buildString(args)),
						"ServerControl.Helpop.Receive");
				if (!s.hasPermission("ServerControl.Helpop.Receive")) {
					TheAPI.msg(
							API.replacePlayerName(Loader.config.getString("Format.Helpop"), s.getName())
									.replace("%sender%", s.getName()).replace("%message%", TheAPI.buildString(args)),
							s);
					return true;
				}
				return true;
			}
		}
		return true;
	}
}

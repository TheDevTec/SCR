package Commands.Message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class Helpop implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Helpop", "Message")) {
			if (args.length == 0) {
				Loader.Help(s, "/Helpop <message>", "Message");
				return true;
			}
			if (args.length >= 1) {
					Loader.sendBroadcasts(s, Loader.config.getString("Format.HelpOp"), Placeholder.c()
						.add("%sender%", s.getName())
						.add("%message%", TheAPI.buildString(args)), "SCR.Helpop.Receive"); 
				if (!s.hasPermission("ServerControl.Helpop.Receive")) {
					Loader.sendMessages(s, Loader.config.getString("Format.HelpOp"), Placeholder.c()
							.add("%sender%", s.getName())
							.add("%message%", TheAPI.buildString(args)));
					return true;
				}
				return true;
			}
		}
		return true;
	}
}

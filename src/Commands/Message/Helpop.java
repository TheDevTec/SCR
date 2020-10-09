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
				Loader.Help(s, "Helpop", "Message");
				return true;
			}
			Loader.sendBroadcasts(s, Loader.config.getString("Format.HelpOp"), Placeholder.c()
				.add("%sender%", s.getName())
				.add("%message%", TheAPI.buildString(args)), Loader.cmds.exists("Message.Helpop.SubPermissions.Receive")?Loader.cmds.getString("Message.Helpop.SubPermissions.Receive"):"SCR.Command.Helpop.Receive"); 
			if (!Loader.has(s, "Helpop", "Message", "Receive")) {
				Loader.sendMessages(s, Loader.config.getString("Format.HelpOp"), Placeholder.c()
					.add("%sender%", s.getName())
					.add("%message%", TheAPI.buildString(args)));
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "Helpop", "Message");
		return true;
	}
}

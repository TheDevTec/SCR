package Commands.Message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class Broadcast implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Broadcast", "Message")) {
			if (args.length == 0) {
				Loader.Help(s, "Broadcast", "Message");
				return true;
			}
			String msg = TheAPI.buildString(args);
			TheAPI.broadcastMessage(Loader.config.getString("Format.Broadcast").replace("%sender%", s.getName())
					.replace("%message%", msg));
			return true;
		}
		Loader.noPerms(s, "Broadcast", "Message");
		return true;
	}

}

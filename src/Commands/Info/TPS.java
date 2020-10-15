package Commands.Info;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class TPS implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (Loader.has(s, "TPS", "Info")) {
			Loader.sendMessages(s, "TPS", Placeholder.c()
					.add("%tps%", TheAPI.getServerTPS() + ""));
			return true;

		}
		Loader.noPerms(s, "TPS", "Info");
		return true;
	}

}

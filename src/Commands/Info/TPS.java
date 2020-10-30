package Commands.Info;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.TheAPI.TPSType;

public class TPS implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (Loader.has(s, "TPS", "Info")) {
			if (args.length == 0) {
			Loader.sendMessages(s, "TPS", Placeholder.c()
					.add("%tps%", TheAPI.getServerTPS() + "")
					.add("%tps-from-1%", TheAPI.getServerTPS(TPSType.ONE_MINUTE) + "")
					.add("%tps-from-5%", TheAPI.getServerTPS(TPSType.FIVE_MINUTES) + "")
					.add("%tps-from-15%", TheAPI.getServerTPS(TPSType.FIFTEEN_MINUTES) + ""));
				return true;
			}
		}
		Loader.noPerms(s, "TPS", "Info");
		return true;
	}

}

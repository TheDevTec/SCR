package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;

public class DelJail implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.delJail")) {
			if (args.length == 0) {
				TheAPI.msg("/delJail <jail>", s);
				return true;
			}
			if (args.length == 1) {
				if (!PunishmentAPI.getjails().contains(args[0])) {
					Loader.sendMessages(s, "Jail.NotExist", Placeholder.c().add("%jail%", args[0]));
					return true;
				}
				PunishmentAPI.deljail(args[0]);
				Loader.sendMessages(s, "Jail.Delete", Placeholder.c().add("%jail%", args[0]));
				return true;
			}
		}
		return true;
	}

}
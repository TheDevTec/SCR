package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;

public class DelJail implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.delJail")) {
			if (args.length == 0) {
				Loader.Help(s, "/delJail <name>", "BanSystem.delJail");
				return true;
			}
			if (args.length == 1) {
				if (!PunishmentAPI.getjails().contains(args[0])) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.JailNotExist").replace("%jail%", args[0]), s);
					return true;
				}
				PunishmentAPI.deljail(args[0]);
				TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.DeletedJail").replace("%jail%", args[0]), s);
				return true;
			}
		}
		return true;
	}

}
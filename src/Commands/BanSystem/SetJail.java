package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;

public class SetJail implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.setJail")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					Loader.Help(s, "/setJail <name>", "BanSystem.setJail");
					return true;
				}
				if (args.length == 1) {
					if (PunishmentAPI.getjails().contains(args[0])) {
						TheAPI.msg(
								Loader.s("Prefix") + Loader.s("BanSystem.JailAlreadyExist").replace("%jail%", args[0]),
								s);
						return true;
					}
					Player p = (Player) s;
					PunishmentAPI.setjail(p.getLocation(), args[0]);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.CreatedJail").replace("%jail%", args[0]), s);
					return true;
				}
			}
			TheAPI.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}

}

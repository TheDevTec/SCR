package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class TpaBlock implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.TpBlock")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					if (!TheAPI.getUser(s.getName()).getBoolean("TpBlock-Global")) {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpaBlock.Blocked-Global")
								.replace("%player%", "All").replace("%playername%", "All"), s);
						TheAPI.getUser(s.getName()).setAndSave("TpBlock-Global", true);
						return true;
					} else {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpaBlock.UnBlocked-Global")
								.replace("%player%", "All").replace("%playername%", "All"), s);
						TheAPI.getUser(s.getName()).setAndSave("TpBlock-Global", null);
						return true;
					}
				}
				if (args.length == 1) {
					if (TheAPI.existsUser(args[0])) {
						if (s.getName() != args[0]) {
							if (!TheAPI.getUser(s.getName()).getBoolean("TpBlock." + args[0])) {
								TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpaBlock.Blocked")
										.replace("%player%", args[0]).replace("%playername%", args[0]), s);
								TheAPI.getUser(s.getName()).setAndSave("TpBlock." + args[0], true);
								return true;
							} else {
								TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpaBlock.UnBlocked")
										.replace("%player%", args[0]).replace("%playername%", args[0]), s);
								TheAPI.getUser(s.getName()).setAndSave("TpBlock." + args[0], null);
								return true;
							}
						}
						TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.CantBlockSelf")
								.replace("%playername%", ((Player) s).getDisplayName())
								.replace("%player%", s.getName()), s);
						return true;
					}
					TheAPI.msg(Loader.PlayerNotEx(args[0]), s);
					return true;
				}
				return true;
			}
			TheAPI.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;

		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length == 1)
			return null;
		return c;
	}
}

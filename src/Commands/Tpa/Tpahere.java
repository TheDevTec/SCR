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
import me.Straiker123.TheAPI;

public class Tpahere implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Tpahere")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					Loader.Help(s, "/Tpahere <player>", "TpaSystem.Tpahere");
					return true;
				}
				if (args.length == 1) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p == null) {
						Loader.msg(Loader.PlayerNotOnline(args[0]), s);
						return true;
					} else {
						if (p != s) {

							if (!TheAPI.getUser(p).getBoolean("TpBlock." + s.getName())
									&& !TheAPI.getUser(p).getBoolean("TpBlock-Global")) {
								if (!RequestMap.containsRequest(p.getName(), s.getName())) {
									Loader.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpahereSender")
											.replace("%playername%", p.getDisplayName())
											.replace("%player%", p.getName()), s);
									Loader.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpahereTarget")
											.replace("%playername%", ((Player) s).getDisplayName())
											.replace("%player%", s.getName()), p);
									RequestMap.addRequest(s.getName(), p.getName(), RequestMap.Type.TPAHERE);
									return true;
								} else {
									Loader.msg(Loader.s("Prefix") + Loader.s("TpaSystem.AlreadyHaveRequest"), s);
									return true;
								}

							}
							Loader.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpaBlocked")
									.replace("%playername%", p.getDisplayName()).replace("%player%", p.getName()), s);
							return true;
						}

						Loader.msg(
								Loader.s("Prefix") + Loader.s("TpaSystem.CantSendRequestToSelf")
										.replace("%playername%", p.getDisplayName()).replace("%player%", p.getName()),
								s);
						return true;
					}
				}
				return true;
			}
			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
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
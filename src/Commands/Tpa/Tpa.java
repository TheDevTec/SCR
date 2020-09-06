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

public class Tpa implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender f, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(f, "ServerControl.Tpa")) {
			if (f instanceof Player) {
				Player s = (Player) f;
				if (args.length == 0) {
					Loader.Help(s, "/Tpa <player>", "TpaSystem.Tpa");
					return true;
				}
				if (args.length == 1) {
					Player d = TheAPI.getPlayer(args[0]);
					if (d == null) {
						TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
						return true;
					} else {
						String p = d.getName();
						if (s != d) {
							if (!TheAPI.getUser(d).getBoolean("TpBlock." + s.getName())
									&& !TheAPI.getUser(d).getBoolean("TpBlock-Global")) {

								if (!RequestMap.containsRequest(p, s.getName())) {
									TheAPI.msg(
											Loader.s("Prefix") + Loader.s("TpaSystem.TpaSender")
													.replace("%playername%", d.getDisplayName()).replace("%player%", p),
											s);
									TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpaTarget")
											.replace("%playername%", s.getDisplayName())
											.replace("%player%", s.getName()), d);
									RequestMap.addRequest(s.getName(), p, RequestMap.Type.TPA);
									return true;
								} else {
									TheAPI.msg(Loader.s("Prefix")
											+ API.replacePlayerName(Loader.s("TpaSystem.AlreadyHaveRequest").replace("%playername%", d.getDisplayName())
													.replace("%player%", d.getName()), p), s);
									return true;
								}

							}
							TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.TpaBlocked")
									.replace("%playername%", d.getDisplayName()).replace("%player%", p), s);
							return true;
						}
						TheAPI.msg(Loader.s("Prefix") + Loader.s("TpaSystem.CantSendRequestToSelf")
								.replace("%playername%", d.getDisplayName()).replace("%player%", p), s);
						return true;
					}
				}
				return true;
			}
			TheAPI.msg(Loader.s("ConsoleErrorMessage"), f);
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

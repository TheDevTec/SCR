package Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class Homes implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				if (API.hasPerm(s, "ServerControl.Home")) {
					User d = TheAPI.getUser(p);
					if (d.exist("Homes")) {
						List<String> ne = new ArrayList<>(d.getKeys("Homes"));
						if (!ne.isEmpty()) {
							TheAPI.msg(Loader.s("Prefix") + Loader.s("Homes.List").replace("%player%", p.getName())
									.replace("%playername%", p.getDisplayName())
									.replace("%list%", StringUtils.join(ne, ", ")), s);
							return true;
						}
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Homes.ListEmpty").replace("%player%", p.getName())
								.replace("%playername%", p.getDisplayName()), s);
						return true;
					}
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Homes.ListEmpty").replace("%player%", p.getName())
							.replace("%playername%", p.getDisplayName()), s);
					return true;
				}
				return true;
			}
			TheAPI.msg(Loader.s("Prefix") + Loader.s("ConsoleErrorMessage"), s);
			return true;
		}

		if (args.length == 1) {
			if (API.hasPerm(s, "ServerControl.Home.Other")) {
				User d = TheAPI.getUser(args[0]);
				if (d.exist("Homes")) {
					List<String> ne = new ArrayList<String>(d.getKeys("Homes"));
					if (!ne.isEmpty()) {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Homes.ListOther").replace("%target%", args[0])
								.replace("%playername%", args[0])
								.replace("%list%", StringUtils.join(ne, ", ")), s);
						return true;
					}
					TheAPI.msg(Loader.s("Prefix")
							+ Loader.s("Homes.ListEmpty").replace("%target%", args[0]).replace("%playername%", args[0]),
							s);
					return true;
				}
				TheAPI.msg(Loader.s("Prefix")
						+ Loader.s("Homes.ListEmpty").replace("%target%", args[0]).replace("%playername%", args[0]), s);
				return true;
			}
			return true;
		}
		return true;
	}
}

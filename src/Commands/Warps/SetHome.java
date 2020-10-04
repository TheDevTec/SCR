package Commands.Warps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class SetHome implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if (s instanceof Player) {
			Player p = (Player) s;
			if (API.hasPerm(s, "ServerControl.SetHome")) {
				if (Loader.vault == null) {
					if (args.length == 0) {
						TheAPI.getUser(s.getName()).setAndSave("Homes.home",
								new Position(p.getLocation()).toString());
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Homes.Created").replace("%home%", "home"), s);
						return true;
					}
						User d = TheAPI.getUser(s.getName());
						d.setAndSave("Homes." + args[0], new Position(p.getLocation()).toString());
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Homes.Created").replace("%player%", p.getName())
								.replace("%playername%", p.getDisplayName()).replace("%home%", args[0]), s);
						return true;
				} else {
					if (args.length == 0) {
						TheAPI.getUser(s.getName()).setAndSave("Homes.home",
								new Position(p.getLocation()).toString());
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Homes.Created").replace("%player%", p.getName())
								.replace("%playername%", p.getDisplayName()).replace("%home%", "home"), s);
						return true;
					}
						User d = TheAPI.getUser(s.getName());
						if (Loader.config.exists("Homes." + Loader.vault.getPrimaryGroup(p))) {
							if (d.getKeys("Homes").size() >= Loader.config.getInt("Homes." + Loader.vault.getPrimaryGroup(p))) {
								TheAPI.msg(
										Loader.s("Prefix")
												+ Loader.s("Homes.LimitReached").replace("%limit%",
														String.valueOf(Loader.config
																.getInt("Homes." + Loader.vault.getPrimaryGroup(p)))),
										s);
								return true;
							}
							d.setAndSave("Homes." + args[0],
									new Position(p.getLocation()).toString());
							TheAPI.msg(Loader.s("Prefix") + Loader.s("Homes.Created").replace("%player%", p.getName())
									.replace("%playername%", p.getDisplayName()).replace("%home%", args[0]), s);
							return true;
						}
						d.setAndSave("Homes." + args[0], new Position(p.getLocation()).toString());
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Homes.Created").replace("%player%", p.getName())
								.replace("%playername%", p.getDisplayName()).replace("%home%", args[0]), s);
						return true;
				}
			}
			return true;
		}
		TheAPI.msg(Loader.s("Prefix") + Loader.s("ConsoleErrorMessage"), s);
		return true;
	}
}

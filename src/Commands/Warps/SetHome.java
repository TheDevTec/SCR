package Commands.Warps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class SetHome implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if (s instanceof Player) {
			Player p = (Player) s;
			if (Loader.has(s, "SetHome", "Warps")) {
				if (Loader.vault == null) {
					if (args.length == 0) {
						TheAPI.getUser(s.getName()).setAndSave("Homes.home",
								new Position(p.getLocation()).toString());
						Loader.sendMessages(s, "Home.Create", Placeholder.c()
								.add("%home%", "home"));
						return true;
					}
						User d = TheAPI.getUser(s.getName());
						d.setAndSave("Homes." + args[0], new Position(p.getLocation()).toString());
						Loader.sendMessages(s, "Home.Create", Placeholder.c()
								.add("%home%", "home"));
						return true;
				} else {
					if (args.length == 0) {
						TheAPI.getUser(s.getName()).setAndSave("Homes.home",
								new Position(p.getLocation()).toString());
						Loader.sendMessages(s, "Home.Create", Placeholder.c()
								.add("%home%", "home"));
						return true;
					}
						User d = TheAPI.getUser(s.getName());
						if (Loader.config.exists("Homes." + Loader.vault.getPrimaryGroup(p))) {
							if (d.getKeys("Homes").size() >= Loader.config.getInt("Homes." + Loader.vault.getPrimaryGroup(p))) {
								Loader.sendMessages(s, "Home.Limit");
								return true;
							}
							d.setAndSave("Homes." + args[0],
									new Position(p.getLocation()).toString());
							Loader.sendMessages(s, "Home.Create", Placeholder.c()
									.add("%home%", args[0]));
							return true;
						}
						d.setAndSave("Homes." + args[0], new Position(p.getLocation()).toString());
						Loader.sendMessages(s, "Home.Create", Placeholder.c()
								.add("%home%", args[0]));
						return true;
				}
			}
			Loader.noPerms(s, "SetHome", "Warps");
			return true;
		}
		return true;
	}
}

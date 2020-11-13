package me.DevTec.ServerControlReloaded.Commands.Warps;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class SetHome implements CommandExecutor, TabCompleter {

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
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

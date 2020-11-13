package me.DevTec.ServerControlReloaded.Commands.BanSystem;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;

public class SetJail implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "SetJail", "BanSystem")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					Loader.Help(s, "SetJail", "BanSystem");
					return true;
				}
				if (args.length == 1) {
					if (PunishmentAPI.getjails().contains(args[0])) {
						Loader.sendMessages(s, "Jail.Exists", Placeholder.c().replace("%jail%", args[0]));
						return true;
					}
					Player p = (Player) s;
					PunishmentAPI.setjail(p.getLocation(), args[0]);
					Loader.sendMessages(s, "Jail.Create", Placeholder.c().replace("%jail%", args[0]));
					return true;
				}
			}
			return true;
		}
		Loader.noPerms(s, "SetJail", "BanSystem");
		return true;
	}

}

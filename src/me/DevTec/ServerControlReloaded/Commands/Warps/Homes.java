package me.DevTec.ServerControlReloaded.Commands.Warps;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class Homes implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				if (Loader.has(s, "Homes", "Warps")) {
					User d = TheAPI.getUser(p);
					Set<String> ne = d.getKeys("Homes");
					if (!ne.isEmpty()) {
						Loader.sendMessages(s, "Home.List", Placeholder.c()
								.add("%homes%", StringUtils.join(ne, ", ")));
						return true;
					}
					Loader.sendMessages(s, "Home.EmptyList");
					return true;
				}
				Loader.noPerms(s, "Homes", "Warps");
				return true;
			}
			return true;
		}
		if (Loader.has(s, "Homes", "Warps", "Other")) {
			User d = TheAPI.getUser(args[0]);
			Set<String> ne = d.getKeys("Homes");
			if (!ne.isEmpty()) {
				Loader.sendMessages(s, "Home.Other.List", Placeholder.c()
						.add("%homes%", StringUtils.join(ne, ", "))
						.add("%player%", args[0])
						.add("%playername%", args[0]));
				return true;
			}
			Loader.sendMessages(s, "Home.Other.EmptyList", Placeholder.c()
					.add("%player%", args[0])
					.add("%playername%", args[0]));
			return true;
		}
		Loader.noPerms(s, "Homes", "Warps", "Other");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "Homes", "Warps", "Other") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

package me.DevTec.ServerControlReloaded.Commands.Info;

import com.google.common.collect.Lists;
import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.API.SeenType;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Seen implements CommandExecutor, TabCompleter {
	List<String> getS(String a) {
		if(a==null)return Lists.newArrayList();
		List<String> l = Lists.newArrayList();
		for (UUID s : TheAPI.getUsers()) {
			if (TheAPI.getUser(s).getName()!=null && TheAPI.getUser(s).getName().equalsIgnoreCase(a))
				l.add(TheAPI.getUser(s).getName());
		}
		return l;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Seen", "Info")) {
			if (args.length == 0) {
				Loader.Help(s, "/Seen <player>", "Info");
				return true;
			}
			if (TheAPI.existsUser(args[0])) {
				if (TheAPI.getPlayerOrNull(args[0]) != null) {
					Loader.sendMessages(s, "Seen.Online", Placeholder.c()
							.add("%player%", args[0])
							.add("%playername%", args[0])
							.add("%online%", API.getSeen(args[0], SeenType.Online)));
					return true;
				}
				
				Loader.sendMessages(s, "Seen.Offline", Placeholder.c()
						.add("%player%", args[0])
						.add("%playername%", args[0])
						.add("%offline%", API.getSeen(args[0], SeenType.Offline)));
				return true;
			}
			List<String> sim = getS(args[0]);
			if (sim.isEmpty()) {
				Loader.sendMessages(s, "Missing.Player.NotExist", Placeholder.c()
						.add("%player%", args[0]));
				return true;
			} else {
				Loader.sendMessages(s, "Seen.Similar", Placeholder.c()
						.add("%names%", StringUtils.join(sim, ", ")));
			}
			return true;

		}
		Loader.noPerms(s, "Seen", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "Seen", "Info"))
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

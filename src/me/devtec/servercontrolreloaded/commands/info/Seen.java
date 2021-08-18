package me.devtec.servercontrolreloaded.commands.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.API.SeenType;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class Seen implements CommandExecutor, TabCompleter {
	List<String> getS(String a) {
		if(a==null)return new ArrayList<>();
		List<String> l = new ArrayList<>();
		for (UUID s : TheAPI.getUsers()) {
			User d = TheAPI.getUser(s);
			if (d!=null && d.getName()!=null && d.getName().equalsIgnoreCase(a))
				l.add(d.getName());
		}
		return l;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Seen", "Info")) {
			if(!CommandsManager.canUse("Info.Seen", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Info.Seen", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "/Seen <player>", "Info");
				return true;
			}
			if (TheAPI.existsUser(args[0])) { //na toto se ještě mrknu, chce to trochu předělat
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

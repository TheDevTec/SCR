package me.devtec.servercontrolreloaded.commands.other;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class Uuid implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Uuid", "Other") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}

	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "Uuid", "Other")) {
			if(!CommandsManager.canUse("Other.Uuid", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Uuid", s))));
				return true;
			}
			if(args.length==0) {
				Loader.Help(s, "Uuid", "Other");
				return true;
			}
			Player pl = TheAPI.getPlayerOrNull(args[0]);
			if (pl!=null)
				Loader.sendMessages(s, pl,"Uuid.Message", Placeholder.c().replace("%uuid%", pl.getUniqueId().toString()));
			else {
				User d = TheAPI.getUser(args[0]);
				String uuid = d.getUUID().toString(), name = d.getName();
				Loader.sendMessages(s,"Uuid.Message", Placeholder.c().replace("%player%", name)
						.replace("%playername%", name).replace("%customname%", name).replace("%uuid%", uuid));
			}
			return true;
		}
		Loader.noPerms(s, "Uuid", "Other");
		return true;
	}
}

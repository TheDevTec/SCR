package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.utils.StringUtils;

public class DelJail implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "DelJail", "BanSystem")) {
			if(!CommandsManager.canUse("BanSystem.DelJail", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("BanSystem.DelJail", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "DelJail", "BanSystem");
				return true;
			}
			if (args.length == 1) {
				if (!PunishmentAPI.getjails().contains(args[0])) {
					Loader.sendMessages(s, "Jail.NotExist", Placeholder.c().add("%jail%", args[0]));
					return true;
				}
				PunishmentAPI.deljail(args[0]);
				Loader.sendMessages(s, "Jail.Delete", Placeholder.c().add("%jail%", args[0]));
				return true;
			}
		}
		Loader.noPerms(s, "DelJail", "BanSystem");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "DelJail", "BanSystem"))
			return StringUtils.copyPartialMatches(args[0], PunishmentAPI.getjails());
		return Arrays.asList();
	}
}
package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Arrays;
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
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.utils.StringUtils;

public class SetJail implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "SetJail", "BanSystem") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "SetJail", "BanSystem")) {
			if(!CommandsManager.canUse("BanSystem.SetJail", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("BanSystem.SetJail", s))));
				return true;
			}
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

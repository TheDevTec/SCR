package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.utils.StringUtils;


public class Accounts implements CommandExecutor, TabCompleter{
	@Override
	public boolean onCommand(CommandSender cs, Command arg1, String arg2, String[] args) {
		if(Loader.has(cs, "Accounts", "BanSystem")) {
			if(args.length==0) {
				Loader.Help(cs, "Accounts", "BanSystem");
				return true; 
			}
			if(!TheAPI.existsUser(args[0]))
				Loader.sendMessages(cs, "Accounts.NoAccounts", Placeholder.c().add("%player%", args[0]));
			else
				Loader.sendMessages(cs, "Accounts.Users", Placeholder.c().add("%player%", args[0])
					.add("%players%", StringUtils.join(PunishmentAPI.getPlayersOnIP(TheAPI.getUser(args[0]).getString("ip")), ", ")));
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "Accounts", "BanSystem"))
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

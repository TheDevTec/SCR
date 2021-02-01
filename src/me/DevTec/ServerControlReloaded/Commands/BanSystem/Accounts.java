package me.DevTec.ServerControlReloaded.Commands.BanSystem;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.PunishmentAPI;


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
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return null;
	}
	
}

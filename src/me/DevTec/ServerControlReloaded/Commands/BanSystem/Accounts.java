package me.DevTec.ServerControlReloaded.Commands.BanSystem;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;


public class Accounts implements CommandExecutor, TabCompleter{
	@Override
	public boolean onCommand(CommandSender cs, Command arg1, String arg2, String[] args) {
		if(Loader.has(cs, "Accounts", "BanSystem")) {
			if(args.length==0) {
				Loader.Help(cs, "Accounts", "BanSystem");
				return true; 
			}
			Player a = Bukkit.getPlayer(args[2]);
			Loader.sendMessages(cs, "Accounts.Users");
			for (String b : PunishmentAPI.getPlayersOnIP(a.getPlayer().getAddress().toString())) {				
				Loader.sendMessages(cs, "Accounts.List", Placeholder.c().add("%accounts%", b));
				return true;
			}
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return null;
	}
	
}

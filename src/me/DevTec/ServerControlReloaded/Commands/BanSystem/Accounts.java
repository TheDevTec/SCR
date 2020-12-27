package me.DevTec.ServerControlReloaded.Commands.BanSystem;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
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
			/*Loader.sendMessages(cs, "Accounts.List", Placeholder.c().add("%accounts%", b));
				TheAPI.bcMsg(b);
				TheAPI.bcMsg(PunishmentAPI.getPlayersOnIP(a.getName()));*/
			Player a = Bukkit.getPlayer(args[0]);
			if(a==null) {Loader.sendMessages(cs, "Missing.Player.Offline");return true;}			
			Loader.sendMessages(cs, "Accounts.Users");
			//TheAPI.bcMsg(TheAPI.getUser(a).getString("ip").replace("_", "."));
			TheAPI.bcMsg(PunishmentAPI.getPlayersOnIP(TheAPI.getUser(a).getString("ip")));
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return null;
	}
	
}

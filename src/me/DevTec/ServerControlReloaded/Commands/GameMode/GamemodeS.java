package me.DevTec.ServerControlReloaded.Commands.GameMode;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;

public class GamemodeS implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "GameModeSurvival", "GameMode")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					p.setGameMode(GameMode.SURVIVAL);
					Loader.sendMessages(p, "GameMode.Your.Survival");
					return true;
				}
				Loader.Help(s, "GameModeSurvival", "GameMode");
				return true;
			}
			if (args.length == 1) {
				if(Loader.has(s,"GameModeSurvival","GameMode","Other")){
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						p.setGameMode(GameMode.SURVIVAL);
						Loader.sendMessages(p, "GameMode.Other.Survival.Receiver");

						Loader.sendMessages(s, "GameMode.Other.Survival.Sender", Placeholder.c()
								.add("%player%", p.getName())
								.add("%playername%", p.getDisplayName()));
						return true;
					}
					Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
							.add("%player%", args[0]));
					return true;
				}
				Loader.noPerms(s, "GameModeSurvival", "GameMode","Other");
				return true;
			}
		}
		Loader.noPerms(s, "GameModeSurvival", "GameMode");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

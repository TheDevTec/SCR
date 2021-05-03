package me.devtec.servercontrolreloaded.commands.gamemode;

import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class GamemodeC implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "GameModeCreative", "GameMode")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					p.setGameMode(GameMode.CREATIVE);
					Loader.sendMessages(p, "GameMode.Your.Creative");
					return true;
				}
				Loader.Help(s, "GameModeCreative", "GameMode");
				return true;
			}
			if (args.length == 1) {
				if(Loader.has(s,"GameModeCreative","GameMode","Other")){
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						p.setGameMode(GameMode.CREATIVE);
						Loader.sendMessages(p, "GameMode.Other.Creative.Receiver");

						Loader.sendMessages(s, "GameMode.Other.Creative.Sender", Placeholder.c()
								.add("%player%", p.getName())
								.add("%playername%", p.getDisplayName()));
						return true;
					}
					Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
							.add("%player%", args[0]));
					return true;
				}
				Loader.noPerms(s, "GameModeCreative", "GameMode","Other");
				return true;
			}
		}
		Loader.noPerms(s, "GameModeCreative", "GameMode");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "GameModeCreative", "GameMode") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}
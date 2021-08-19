package me.devtec.servercontrolreloaded.commands.gamemode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.GameMode;
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

public class GamemodeSP implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(TheAPI.isOlderThan(8)) {
			TheAPI.msg("&cUnsupported GameMode type", s);
			return true;
		}
		if (Loader.has(s, "GameModeSpectator", "GameMode")) {
			if(!CommandsManager.canUse("GameMode.GameModeSpectator", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("GameMode.GameModeSpectator", s))));
				return true;
			}
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					p.setGameMode(GameMode.SPECTATOR);
					Loader.sendMessages(p, "GameMode.Your.Spectator");
					return true;
				}
				Loader.Help(s, "GameModeSpectator", "GameMode");
				return true;
			}
			if (args.length == 1) {
				if(Loader.has(s,"GameModeSpectator","GameMode","Other")){
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						p.setGameMode(GameMode.SPECTATOR);
						Loader.sendMessages(p, "GameMode.Other.Spectator.Receiver");

						Loader.sendMessages(s, "GameMode.Other.Spectator.Sender", Placeholder.c()
								.add("%player%", p.getName())
								.add("%playername%", p.getDisplayName()));
						return true;
					}
					Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
							.add("%player%", args[0]));
					return true;
				}
				Loader.noPerms(s, "GameModeSpectator", "GameMode","Other");
				return true;
			}
		}
		Loader.noPerms(s, "GameModeSpectator", "GameMode");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "GameModeSpectator", "GameMode") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}
}
package me.devtec.servercontrolreloaded.commands.gamemode;

import java.util.Arrays;
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

public class Gamemode implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (Loader.has(s, "GameMode", "GameMode")) {
				Loader.Help(s, "GameMode", "GameMode");
				return true;
			}
			Loader.noPerms(s, "GameMode", "GameMode");
			return true;
		}
		String gamemode = null;
		if (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0"))
			gamemode = "Survival";
		else

		if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1"))
			gamemode = "Creative";
		else

		if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2"))
			gamemode = "Adventure";
		else

		if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectator")
				|| args[0].equalsIgnoreCase("3"))
			gamemode = "Spectator";
		else {
			gamemode = null;

		}
		
		if(TheAPI.isOlderThan(8)) {
			if(gamemode.equals("Spectator")) {
			TheAPI.msg("&cUnsupported GameMode type", s);
			return true;
			}
		}
		if (args.length == 1) {
			if(gamemode == null) {
				Loader.sendMessages(s, "Missing.GameMode", Placeholder.c()
						.add("%gamemode%", args[0]));
				return true;
			}
			if (s instanceof Player) {
				if (Loader.has(s, "GameMode"+gamemode, "GameMode")) {
					if(!CommandsManager.canUse("GameMode.GameMode"+gamemode, s)) {
						Loader.sendMessages(s, "Cooldowns.Commands");
						return true;
					}
					((Player) s).setGameMode(GameMode.valueOf(gamemode.toUpperCase()));
					Loader.sendMessages(s, "GameMode.Your."+gamemode, Placeholder.c()
							.add("%gamemode%", gamemode));
					return true;
				}
				Loader.noPerms(s, "GameMode" + gamemode, "GameMode");
				return true;
			}
			Loader.Help(s, "GameMode", "GameMode");
			return true;
		}
		if (Loader.has(s, "GameMode" + gamemode, "GameMode","Other")) {
			if(!CommandsManager.canUse("GameMode.GameMode"+gamemode, s)) {
				Loader.sendMessages(s, "Cooldowns.Commands");
				return true;
			}
			Player p = TheAPI.getPlayer(args[1]);
				if (p != null) {
					p.setGameMode(GameMode.valueOf(gamemode.toUpperCase()));
					Loader.sendMessages(p, "GameMode.Other."+gamemode+".Receiver", Placeholder.c()
							.add("%gamemode%", gamemode));
					
					Loader.sendMessages(s, "GameMode.Other."+gamemode+".Sender", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName())
							.add("%gamemode%", gamemode));
					return true;
				}
				Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
						.add("%player%", args[1]));
				return true;
		}
		Loader.noPerms(s, "GameMode" + gamemode, "GameMode","Other");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "GameMode", "GameMode")) {
			if(args.length==1)
				return StringUtils.copyPartialMatches(args[0], Arrays.asList("Survival","Creative","Spectator","Adventure"));
			if(args.length==2 && Loader.has(s, "GameMode" + args[0], "GameMode","Other"))
				return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
		}
		return Arrays.asList();
	}
}
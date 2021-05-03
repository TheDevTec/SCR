package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.API.TeleportLocation;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.BanList;
import me.devtec.theapi.punishmentapi.PlayerBanList;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.utils.StringUtils;

public class UnJail implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "UnJail", "BanSystem")) {
			List<String> jail = BanList.getJailed();
			jail.addAll(BanList.getTempJailed());
			return StringUtils.copyPartialMatches(args[0], jail);
		}
		return Arrays.asList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "UnJail", "BanSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "UnJail", "BanSystem");
				return true;
			}
			PlayerBanList p = PunishmentAPI.getBanList(args[0]);
			if (p.isJailed() || p.isTempJailed()) {
				PunishmentAPI.unjail(args[0]);
				if (TheAPI.getPlayer(args[0]) != null)
					API.teleportPlayer(TheAPI.getPlayer(args[0]), TeleportLocation.SPAWN);
				else TheAPI.getUser(args[0]).setAndSave("request-spawn", 1);
				Loader.sendMessages(s, "BanSystem.UnJail.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				Loader.sendBroadcasts(s, "BanSystem.UnJail.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				return true;
			}
			Loader.sendMessages(s, "BanSystem.Not.Arrested", Placeholder.c().replace("%playername%", args[0]).replace("%player%", args[0]));
			return true;
		}
		Loader.noPerms(s, "UnJail", "BanSystem");
		return true;
	}
}

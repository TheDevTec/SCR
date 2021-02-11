package me.DevTec.ServerControlReloaded.Commands.BanSystem;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.utils.StringUtils;

public class Warn implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Warn", "BanSystem"))
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		return Arrays.asList();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Warn", "BanSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "Warn", "BanSystem");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.warn(args[0], Loader.config.getString("BanSystem.Warn.Text").replace("%reason%",
						Loader.config.getString("BanSystem.Warn.Reason")));
				Loader.sendMessages(s, "BanSystem.Warn.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.Warn.Reason")));
				Loader.sendBroadcasts(s, "BanSystem.Warn.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.Warn.Reason")));
				return true;
			}
			if (TheAPI.getUser(args[0]).getBoolean("Immune")
					|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
				Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
				return true;
			}
			String msg = StringUtils.buildString(1, args);
			if(msg.endsWith("-s")||msg.endsWith("- s")) {
				msg = msg.endsWith("- s")?msg.substring(0, msg.length()-3):msg.substring(0, msg.length()-2);
				PunishmentAPI.warn(args[0], Loader.config.getString("BanSystem.Warn.Text").replace("%reason%",msg));
				Loader.sendMessages(s, "BanSystem.Warn.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]"));
				Loader.sendBroadcasts(s, "BanSystem.Warn.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]"), "servercontrol.silent");
				return true;
			}
			PunishmentAPI.warn(args[0], Loader.config.getString("BanSystem.Warn.Text").replace("%reason%",msg));
			Loader.sendMessages(s, "BanSystem.Warn.Sender", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg));
			Loader.sendBroadcasts(s, "BanSystem.Warn.Admins", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg));
			return true;
		}
		Loader.noPerms(s, "Warn", "BanSystem");
		return true;
	}
}

package me.DevTec.ServerControlReloaded.Commands.BanSystem;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.utils.StringUtils;

public class TempBan implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TempBan", "BanSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "TempBan", "BanSystem");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempban(args[0], Loader.config.getString("BanSystem.TempBan.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempBan.Reason")), StringUtils.getTimeFromString(Loader.config.getString("BanSystem.TempBan.Time")));
				Loader.sendMessages(s, "BanSystem.TempBan.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBan.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(Loader.config.getString("BanSystem.TempBan.Time")))));
				Loader.sendBroadcasts(s, "BanSystem.TempBan.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBan.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(Loader.config.getString("BanSystem.TempBan.Time")))));
				return true;
			}
			if (args.length == 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempban(args[0], Loader.config.getString("BanSystem.TempBan.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempBan.Reason")), StringUtils.getTimeFromString(args[1]));
				Loader.sendMessages(s, "BanSystem.TempBan.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBan.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
				Loader.sendBroadcasts(s, "BanSystem.TempBan.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBan.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
				return true;
			}
			if (TheAPI.getUser(args[0]).getBoolean("Immune")
					|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
				Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
				return true;
			}
			
			String msg = StringUtils.buildString(2, args);
			if(msg.endsWith("-s")||msg.endsWith("- s")) {
				msg = msg.endsWith("- s")?msg.substring(0, msg.length()-3):msg.substring(0, msg.length()-2);
				PunishmentAPI.tempban(args[0], Loader.config.getString("BanSystem.TempBan.Text").replace("%reason%",msg), StringUtils.timeFromString(args[1]));
				Loader.sendMessages(s, "BanSystem.TempBan.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
				Loader.sendBroadcasts(s, "BanSystem.TempBan.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))), "servercontrol.silent");
				return true;
			}
			PunishmentAPI.tempban(args[0], Loader.config.getString("BanSystem.TempBan.Text").replace("%reason%",msg), StringUtils.timeFromString(args[1]));
			Loader.sendMessages(s, "BanSystem.TempBan.Sender", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
			Loader.sendBroadcasts(s, "BanSystem.TempBan.Admins", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
			return true;
		}
		Loader.noPerms(s, "TempBan", "BanSystem");
		return true;
	}
}

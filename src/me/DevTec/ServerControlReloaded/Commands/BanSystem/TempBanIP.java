package me.DevTec.ServerControlReloaded.Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class TempBanIP implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TempBanIP", "BanSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "TempBanIP", "BanSystem");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempbanIP(args[0], Loader.config.getString("BanSystem.TempBanIP.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempBanIP.Reason")), StringUtils.getTimeFromString(Loader.config.getString("BanSystem.TempBanIP.Time")));
				Loader.sendMessages(s, "BanSystem.TempBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBanIP.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				Loader.sendBroadcasts(s, "BanSystem.TempBanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBanIP.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				return true;
			}
			if (args.length == 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempbanIP(args[0], Loader.config.getString("BanSystem.TempBanIP.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempBanIP.Reason")), StringUtils.getTimeFromString(args[2]));
				Loader.sendMessages(s, "BanSystem.TempBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBanIP.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				Loader.sendBroadcasts(s, "BanSystem.TempBanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempBanIP.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
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
				PunishmentAPI.tempbanIP(args[0], Loader.config.getString("BanSystem.TempBanIP.Text").replace("%reason%",msg), StringUtils.timeFromString(args[2]));
				Loader.sendMessages(s, "BanSystem.TempBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				Loader.sendBroadcasts(s, "BanSystem.TempBanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))), "servercontrol.silent");
				return true;
			}
			PunishmentAPI.tempbanIP(args[0], Loader.config.getString("BanSystem.TempBanIP.Text").replace("%reason%",msg), StringUtils.timeFromString(args[2]));
			Loader.sendMessages(s, "BanSystem.TempBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
					.replace("%ip%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
			Loader.sendBroadcasts(s, "BanSystem.TempBanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
					.replace("%ip%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
			return true;
		}
		Loader.noPerms(s, "TempBanIP", "BanSystem");
		return true;
	}
}
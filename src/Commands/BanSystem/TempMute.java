package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class TempMute implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.TempMute")) {
			if (args.length == 0) {
				TheAPI.msg("/TempMute <player> <time> <reason>", s);
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempmute(args[0], Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempMute.Reason")), StringUtils.getTimeFromString(Loader.config.getString("BanSystem.TempMute.Time")));
				Loader.sendMessages(s, "BanSystem.TempBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempMute.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				Loader.sendBroadcasts(s, "BanSystem.TempMute.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempMute.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				return true;
			}
			if (args.length == 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempmute(args[0], Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempMute.Reason")), StringUtils.getTimeFromString(args[2]));
				Loader.sendMessages(s, "BanSystem.TempMute.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempMute.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				Loader.sendBroadcasts(s, "BanSystem.TempMute.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempMute.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
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
				PunishmentAPI.tempmute(args[0], Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%",msg), StringUtils.timeFromString(args[2]));
				Loader.sendMessages(s, "BanSystem.TempMute.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				Loader.sendBroadcasts(s, "BanSystem.TempMute.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))), "servercontrol.silent");
				return true;
			}
			PunishmentAPI.tempmute(args[0], Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%",msg), StringUtils.timeFromString(args[2]));
			Loader.sendMessages(s, "BanSystem.TempMute.Sender", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
			Loader.sendBroadcasts(s, "BanSystem.TempMute.Admins", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
			return true;
		}
		return true;
	}

}

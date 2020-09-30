package Commands.BanSystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class TempJail implements CommandExecutor, TabCompleter {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.TempJail")) {
			if (args.length == 0) {
				TheAPI.msg("/TempJail <player> <time> <reason>", s);
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempjail(args[0], Loader.config.getString("BanSystem.TempJail.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempJail.Reason")), StringUtils.getTimeFromString(Loader.config.getString("BanSystem.TempJail.Time")));
				Loader.sendMessages(s, "BanSystem.TempBanIP", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempJail.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				Loader.sendBroadcasts(s, "BanSystem.Broadcast.TempJail", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempJail.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				return true;
			}
			if (args.length == 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempjail(args[0], Loader.config.getString("BanSystem.TempBanIP.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempJail.Reason")), StringUtils.getTimeFromString(args[2]));
				Loader.sendMessages(s, "BanSystem.TempJail", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempJail.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				Loader.sendBroadcasts(s, "BanSystem.Broadcast.TempJail", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempJail.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				return true;
			}
			if (args.length >= 3) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				String msg = TheAPI.buildString(args);
				msg = msg.replaceFirst(args[0] + " "+args[1]+" ", "");
				if(msg.endsWith("-s")||msg.endsWith("- s")) {
					msg = msg.endsWith("- s")?msg.substring(0, msg.length()-3):msg.substring(0, msg.length()-2);
					PunishmentAPI.tempjail(args[0], Loader.config.getString("BanSystem.TempJail.Text").replace("%reason%",msg), StringUtils.timeFromString(args[2]));
					Loader.sendMessages(s, "BanSystem.TempJail", Placeholder.c().replace("%operator%", s.getName())
							.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
					Loader.sendBroadcasts(s, "BanSystem.Broadcast.TempJail", Placeholder.c().replace("%operator%", s.getName())
							.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))), "servercontrol.silent");
					return true;
				}
				PunishmentAPI.tempjail(args[0], Loader.config.getString("BanSystem.TempJail.Text").replace("%reason%",msg), StringUtils.timeFromString(args[2]));
				Loader.sendMessages(s, "BanSystem.TempJail", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				Loader.sendBroadcasts(s, "BanSystem.Broadcast.TempJail", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[2]))));
				return true;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (s.hasPermission("ServerControl.TempJail")) {
			if (args.length == 1) {
				return null;
			}
		}
		return c;
	}
}

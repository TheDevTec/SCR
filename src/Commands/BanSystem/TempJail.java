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
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class TempJail implements CommandExecutor, TabCompleter {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.TempJail")) {
			if (args.length == 0 || args.length == 1) {
				Loader.Help(s, "/TempJail <player> <time> <reason>", "BanSystem.TempJail");
				return true;
			}
			if (TheAPI.getUser(args[0]).getBoolean("Immune")
					|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
				TheAPI.msg(Loader.s("Prefix")
						+ Loader.s("Immune.NoPunish").replace("%punishment%", "Mute").replace("%target%", args[0]), s);
				return true;
			}
			if (Loader.config.getString("Jails") == null) {
				TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.MissingJail"), s);
				return true;
			}
			if (args.length == 2) {
				String msg = Loader.config.getString("BanSystem.Jail.Reason");
				long time = StringUtils.getTimeFromString(args[1]);
				PunishmentAPI.tempjail(args[0],
						Loader.config.getString("BanSystem.TempJail.Text").replace("%reason%", msg), time);
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempJail")
						.replace("%playername%", args[0])
						.replace("%reason%", msg)
						.replace("%operator%", s.getName())
						.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1])))
						));
				TheAPI.sendMessage(Loader.s("BanSystem.TempJail")
						.replace("%playername%", args[0])
						.replace("%reason%",msg)
						.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1]))), s);
				return true;
			}
			if (args.length >= 3) {
				String msg = TheAPI.buildString(args);
				msg = msg.replaceFirst(args[0] + " " + args[1] + " ", "");
				long time = StringUtils.getTimeFromString(args[1]);
				if(msg.endsWith("-s")) {
					msg = msg.replace("-s", "");
					Bukkit.broadcast(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempJail")
							.replace("%playername%", args[0]) //TODO - upravit path
							.replace("%reason%", msg)
							.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1])))
							.replace("%operator%", s.getName())+" &f[Silent]"
							),"servercontrol.seesilent");
					
					TheAPI.sendMessage(Loader.s("BanSystem.TempJail")
							.replace("%playername%", args[0])
							.replace("%reason%", msg)
							.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1])))
							.replace("%operator%", s.getName()), s);
					return true;
				}
				PunishmentAPI.tempjail(args[0],
						Loader.config.getString("BanSystem.TempJail.Text").replace("%reason%", msg), time);
				
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempJail")
						.replace("%playername%", args[0])
						.replace("%reason%", msg)
						.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1])))
						.replace("%operator%", s.getName())
						));
				TheAPI.sendMessage(Loader.s("BanSystem.TempJail")
						.replace("%playername%", args[0])
						.replace("%reason%", msg)
						.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1])))
						.replace("%operator%", s.getName()), s);
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

package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;
import me.DevTec.Bans.PunishmentAPI;
import me.DevTec.Other.StringUtils;

public class TempMute implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.TempMute")) {
			if (args.length == 0) {
				Loader.Help(s, "/TempMute <player> <time> <reason>", "BanSystem.TempMute");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "TempMute")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempMute.Reason");
				long cooldownTime = StringUtils
						.getTimeFromString(Loader.config.getString("BanSystem.TempMute.Time"));

				PunishmentAPI.tempmute(args[0],
						Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%", msg), cooldownTime);
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempMute")
						.replace("%playername%", args[0])
						.replace("%reason%", msg).replace("%operator%", s.getName())
						.replace("%time%", ""+StringUtils.setTimeToString(cooldownTime))
						));
				TheAPI.sendMessage(Loader.s("BanSystem.TempMute")
						.replace("%playername%", args[0])
						.replace("%reason%", msg)
						.replace("%operator%", s.getName())
						.replace("%time%", ""+StringUtils.setTimeToString(cooldownTime)), s);
				return true;

			}
			if (args.length == 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "TempMute")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempMute.Reason");

				PunishmentAPI.tempmute(args[0],
						Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%", msg),
						StringUtils.getTimeFromString(args[1]));
				
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempMute")
						.replace("%playername%", args[0])
						.replace("%reason%", msg)
						.replace("%operator%", s.getName())
						.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1])))
						));
				TheAPI.sendMessage(Loader.s("BanSystem.TempMute")
						.replace("%playername%", args[0])
						.replace("%reason%", msg).replace("%operator%", s.getName())
						.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1]))), s);
				return true;
			}
			if (args.length >= 3) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "TempMute")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = TheAPI.buildString(args);
				msg = msg.replaceFirst(args[0] + " " + args[1] + " ", "");
				if(msg.endsWith("-s")) {
					msg = msg.replace("-s", "");
					Bukkit.broadcast(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempMute")
							.replace("%playername%", args[0])
							.replace("%reason%", msg)
							.replace("%operator%", s.getName())
							.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1])))+" &f[Silent]"
							),"servercontrol.seesilent");
					
					TheAPI.sendMessage(Loader.s("BanSystem.TempMute")
							.replace("%playername%", args[0])
							.replace("%reason%", msg)
							.replace("%operator%", s.getName())
							.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1]))), s);
					return true;
				}
				PunishmentAPI.tempmute(args[0],
						Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%", msg),
						StringUtils.getTimeFromString(args[1]));
				
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempMute")
						.replace("%playername%", args[0])
						.replace("%reason%", msg)
						.replace("%operator%", s.getName())
						.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1])))
						));
				TheAPI.sendMessage(Loader.s("BanSystem.TempMute")
						.replace("%playername%", args[0])
						.replace("%reason%", msg)
						.replace("%operator%", s.getName())
						.replace("%time%", StringUtils.setTimeToString(StringUtils.getTimeFromString(args[1]))), s);
				return true;
			}
		}
		return true;
	}

}

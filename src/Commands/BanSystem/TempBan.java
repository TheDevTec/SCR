package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class TempBan implements CommandExecutor {
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.TempBan")) {
			if (args.length == 0) {
				Loader.Help(s, "/TempBan <player> <time> <reason>", "BanSystem.TempBan");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "TempBan")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempBan.Reason");
				TheAPI.getPunishmentAPI().tempban(args[0],
						Loader.config.getString("BanSystem.TempBan.Text").replace("%reason%", msg),
						TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("BanSystem.TempBan.Time")));
				
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempBan").replace("%playername%", args[0])
						.replace("%reason%", Loader.config.getString("BanSystem.TempBan.Reason")).replace("%operator%", s.getName())//toto se pošle všem
						));
				TheAPI.sendMessage(Loader.s("BanSystem.TempBan").replace("%playername%", args[0])
						.replace("%reason%", Loader.config.getString("BanSystem.BanIP.Reason")).replace("%operator%", s.getName()), s);//toto tobì
				return true;


			}
			if (args.length == 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "TempBan")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempBan.Reason");
				TheAPI.getPunishmentAPI().tempban(args[0],
						Loader.config.getString("BanSystem.TempBan.Text").replace("%reason%", msg),
						TheAPI.getStringUtils().getTimeFromString(args[1]));
				
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempBan").replace("%playername%", args[0])
						.replace("%reason%", msg).replace("%operator%", s.getName()).replace("%time%", args[1])
						));
				TheAPI.sendMessage(Loader.s("BanSystem.TempBan").replace("%playername%", args[0])
						.replace("%reason%",msg).replace("%time%", args[1]), s);
				return true;
			}
			if (args.length >= 3) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "TempBan")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = TheAPI.buildString(args);
				msg = msg.replaceFirst(args[0] + " " + args[1] + " ", "");
				TheAPI.getPunishmentAPI().tempban(args[0],
						Loader.config.getString("BanSystem.TempBan.Text").replace("%reason%", msg),
						TheAPI.getStringUtils().getTimeFromString(args[1]));
				
				if(msg.endsWith("-s")) {
					msg = msg.replace("-s", "");
					Bukkit.broadcast(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempBan").replace("%playername%", args[0]) //TODO - upravit path
							.replace("%reason%", msg).replace("%operator%", s.getName())+" &f[Silent]"
							),"servercontrol.seesilent");
					
					TheAPI.sendMessage(Loader.s("BanSystem.TempBan").replace("%playername%", args[0])
							.replace("%reason%", msg).replace("%time%", args[1]).replace("%operator%", s.getName()), s);
					return true;
				}
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.TempBan").replace("%playername%", args[0])
						.replace("%reason%", msg).replace("%time%", args[1]).replace("%operator%", s.getName())
						));
				TheAPI.sendMessage(Loader.s("BanSystem.TempBan").replace("%playername%", args[0])
						.replace("%reason%", msg).replace("%time%", args[1]).replace("%operator%", s.getName()), s);
				return true;
			}
		}
		return true;
	}
}

package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class Mute implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Mute")) {
			if (args.length == 0) {
				Loader.Help(s, "/Mute <player> <reason>", "BanSystem.Mute");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					TheAPI.msg(Loader.s("Prefix")
							+ Loader.s("Immune.NoPunish").replace("%punishment%", "Mute").replace("%target%", args[0]),
							s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.Mute.Reason");
				TheAPI.getPunishmentAPI().mute(args[0],
						Loader.config.getString("BanSystem.Mute.Text").replace("%reason%", msg));
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.Mute").replace("%playername%", args[0])
						.replace("%reason%", Loader.config.getString("BanSystem.Mute.Reason")).replace("%operator%", s.getName())//toto se pošle všem
						));
				TheAPI.sendMessage(Loader.s("BanSystem.Mute").replace("%playername%", args[0])
						.replace("%reason%", Loader.config.getString("BanSystem.Mute.Reason")).replace("%operator%", s.getName()), s);//toto tobì
				return true;
			}
			if (args.length >= 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					TheAPI.msg(Loader.s("Prefix")
							+ Loader.s("Immune.NoPunish").replace("%punishment%", "Mute").replace("%target%", args[0]),
							s);
					return true;
				}
				String msg = TheAPI.buildString(args);
				msg = msg.replaceFirst(args[0] + " ", "");
				TheAPI.getPunishmentAPI().mute(args[0],
						Loader.config.getString("BanSystem.Mute.Text").replace("%reason%", msg));
				if(msg.endsWith("-s")) {
					msg = msg.replace("-s", "");
					Bukkit.broadcast(TheAPI.colorize(Loader.s("BanSystem.Broadcast.Mute").replace("%playername%", args[0]) //TODO - upravit path
							.replace("%reason%", msg).replace("%operator%", s.getName())+" &f[Silent]"
							),"servercontrol.seesilent");
					
					TheAPI.sendMessage(Loader.s("BanSystem.Mute").replace("%playername%", args[0])
							.replace("%reason%", msg).replace("%operator%", s.getName()), s);
					return true;
				}
				return true;
			}
		}
		return true;
	}

}

package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class Warn implements CommandExecutor {
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Warn")) {
			if (args.length == 0) {
				Loader.Help(s, "/Warn <player> <reason>", "BanSystem.Warn");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.existsUser(args[0])) {
					if (TheAPI.getUser(args[0]).getBoolean("Immune")
							|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "Warn")
								.replace("%target%", args[0]), s);
						return true;
					}
					TheAPI.getUser(args[0]).setAndSave("warns", 1 + TheAPI.getUser(args[0]).getInt("warns"));
					TheAPI.getPunishmentAPI().warn(args[0], Loader.config.getString("BanSystem.Warn.Text")
							.replace("%reason%", Loader.config.getString("BanSystem.Warn.Reason")));
					Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.Warn").replace("%playername%", args[0])
							.replace("%reason%", Loader.config.getString("BanSystem.Warn.Reason")).replace("%operator%", s.getName())
							));
					TheAPI.sendMessage(Loader.s("BanSystem.Warn").replace("%playername%", args[0])
							.replace("%reason%", Loader.config.getString("BanSystem.BanIP.Reason")).replace("%operator%", s.getName()), s);
					return true;
				}
				if (TheAPI.existsUser(args[0]))
					TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.PlayerNotWarned").replace("%player%", args[0])
							.replace("%playername%", args[0]), s);
				else
					TheAPI.msg(Loader.PlayerNotEx(args[0]), s);
				return true;
			}
			if (args.length >= 2) {
				if (TheAPI.existsUser(args[0])) {
					if (TheAPI.getUser(args[0]).getBoolean("Immune")
							|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "Warn")
								.replace("%target%", args[0]), s);
						return true;
					}
					String msg = TheAPI.buildString(args);
					msg = msg.replaceFirst(args[0] + " ", "");
					TheAPI.getUser(args[0]).setAndSave("warns", 1 + TheAPI.getUser(args[0]).getInt("warns"));
					TheAPI.getPunishmentAPI().warn(args[0],
							Loader.config.getString("BanSystem.Warn.Text").replace("%reason%", msg));
					if(msg.endsWith("-s")) {
						msg = msg.replace("-s", "");
						Bukkit.broadcast(TheAPI.colorize(Loader.s("BanSystem.Broadcast.Warn").replace("%playername%", args[0]) //TODO - upravit path
								.replace("%reason%", msg).replace("%operator%", s.getName())+" &f[Silent]"
								),"servercontrol.seesilent");
						
						TheAPI.sendMessage(Loader.s("BanSystem.Warn").replace("%playername%", args[0])
								.replace("%reason%", msg).replace("%operator%", s.getName()), s);
						return true;
					}
					Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.Warn").replace("%playername%", args[0])
							.replace("%reason%", msg).replace("%operator%", s.getName())
							));
					TheAPI.sendMessage(Loader.s("BanSystem.Warn").replace("%playername%", args[0])
							.replace("%reason%", msg).replace("%operator%", s.getName()), s);
					return true;
				}
				if (TheAPI.existsUser(args[0]))
					TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.PlayerHasNotBan").replace("%player%", args[0])
							.replace("%playername%", args[0]), s);
				else
					TheAPI.msg(Loader.PlayerNotEx(args[0]), s);
				return true;

			}
		}
		return true;
	}
}

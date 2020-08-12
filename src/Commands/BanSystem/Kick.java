package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class Kick implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Kick")) {
			if (args.length == 0) {
				Loader.Help(s, "/Kick <player> <reason>", "BanSystem.Kick");
				return true;
			}
			if (args.length == 1) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null) {
					if (TheAPI.getUser(args[0]).getBoolean("Immune")
							|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "kick")
								.replace("%target%", p.getName()), s);
						return true;
						
					}
					TheAPI.getPunishmentAPI().kick(args[0], Loader.config.getString("BanSystem.Kick.Text")
							.replace("%reason%", Loader.config.getString("BanSystem.Kick.Reason")));
					
					Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.Kick").replace("%playername%", args[0])
							.replace("%reason%", Loader.config.getString("BanSystem.Kick.Reason")).replace("%operator%", s.getName())
							));
					TheAPI.sendMessage(Loader.s("BanSystem.Kick").replace("%playername%", args[0])
							.replace("%reason%", Loader.config.getString("BanSystem.Kick.Reason")).replace("%operator%", s.getName()), s);
					return true;
				}
				TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
			if (args.length >= 2) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null) {
					if (TheAPI.getUser(args[0]).getBoolean("Immune")
							|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "kick")
								.replace("%target%", p.getName()), s);
						return true;
					}
					String msg = TheAPI.buildString(args);
					msg = msg.replaceFirst(args[0] + " ", "");
					TheAPI.getPunishmentAPI().kick(args[0],
							Loader.config.getString("BanSystem.Kick.Text").replace("%reason%", msg));
					if(msg.endsWith("-s")) {
						msg = msg.replace("-s", "");
						Bukkit.broadcast(TheAPI.colorize(Loader.s("BanSystem.Broadcast.Kick").replace("%playername%", args[0]) //TODO - upravit path
								.replace("%reason%", msg).replace("%operator%", s.getName())+" &f[Silent]"
								),"servercontrol.seesilent");
						
						TheAPI.sendMessage(Loader.s("BanSystem.Kick").replace("%playername%", args[0])
								.replace("%reason%", msg).replace("%operator%", s.getName()), s);
						return true;
					}
					Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.Kick").replace("%playername%", args[0])
							.replace("%reason%", msg).replace("%operator%", s.getName())
							));
					TheAPI.sendMessage(Loader.s("BanSystem.Kick").replace("%playername%", args[0])
							.replace("%reason%", msg).replace("%operator%", s.getName()), s);
					return true;
				}
				TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
		}
		return true;
	}

}

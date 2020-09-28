
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

public class Jail implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Jail")) {
			if (args.length == 0) {
				TheAPI.msg("/Jail <player> <reason>", s);
				return true;
			}
			if (args.length == 1) {
				if (!PunishmentAPI.getjails().isEmpty()) {
					if (TheAPI.getUser(args[0]).getBoolean("Immune")
							|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
						return true;
					}
					PunishmentAPI.jail(args[0], Loader.config.getString("BanSystem.Jail.Text").replace("%reason%", Loader.config.getString("BanSystem.Jail.Reason")));
					Loader.sendMessages(s, "BanSystem.Jail", Placeholder.c().replace("%operator%", s.getName())
							.replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.Jail.Reason")));
					Loader.sendBroadcasts(s, "BanSystem.Broadcast.Jail", Placeholder.c().replace("%operator%", s.getName())
							.replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.Jail.Reason")));
					return true;
				}
				Loader.sendMessages(s, "Jail.Empty");
				return true;
			}
			if (args.length >= 2) {
				if (!PunishmentAPI.getjails().isEmpty()) {
					if (TheAPI.getUser(args[0]).getBoolean("Immune")
							|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
						return true;
					}
					String msg = TheAPI.buildString(args);
					msg = msg.replaceFirst(args[0] + " ", "");
					if(msg.endsWith("-s")||msg.endsWith("- s")) {
						msg = msg.endsWith("- s")?msg.substring(0, msg.length()-3):msg.substring(0, msg.length()-2);
						PunishmentAPI.jail(args[0], Loader.config.getString("BanSystem.Jail.Text").replace("%reason%", msg));
						Loader.sendMessages(s, "BanSystem.Jail", Placeholder.c().replace("%operator%", s.getName())
								.replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]"));
						Loader.sendBroadcasts(s, "BanSystem.Broadcast.Jail", Placeholder.c().replace("%operator%", s.getName())
								.replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]"));
						return true;
					}
					PunishmentAPI.jail(args[0], Loader.config.getString("BanSystem.Jail.Text").replace("%reason%", msg));
					Loader.sendMessages(s, "BanSystem.Jail", Placeholder.c().replace("%operator%", s.getName())
							.replace("%player%", args[0]).replace("%reason%", msg));
					Loader.sendBroadcasts(s, "BanSystem.Broadcast.Jail", Placeholder.c().replace("%operator%", s.getName())
							.replace("%player%", args[0]).replace("%reason%", msg));
					return true;
				}
				Loader.sendMessages(s, "Jail.Empty");
				return true;
			}

		}
		return true;
	}

}

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

public class Kick implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Kick", "BanSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "Kick", "BanSystem");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.kick(args[0], Loader.config.getString("BanSystem.Kick.Text").replace("%reason%",
						Loader.config.getString("BanSystem.Kick.Reason")));
				Loader.sendMessages(s, "BanSystem.Kick.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.Kick.Reason")));
				Loader.sendBroadcasts(s, "BanSystem.Kick.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.Kick.Reason")));
				return true;
			}
			if (TheAPI.getUser(args[0]).getBoolean("Immune")
					|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
				Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
				return true;
			}
			String msg = StringUtils.buildString(1, args);
			if(msg.endsWith("-s")||msg.endsWith("- s")) {
				msg = msg.endsWith("- s")?msg.substring(0, msg.length()-3):msg.substring(0, msg.length()-2);
				PunishmentAPI.kick(args[0], Loader.config.getString("BanSystem.Kick.Text").replace("%reason%",msg));
				Loader.sendMessages(s, "BanSystem.Kick.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]"));
				Loader.sendBroadcasts(s, "BanSystem.Kick.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]"), "servercontrol.silent");
				return true;
			}
			PunishmentAPI.kick(args[0], Loader.config.getString("BanSystem.Kick.Text").replace("%reason%",msg));
			Loader.sendMessages(s, "BanSystem.Kick.Sender", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg));
			Loader.sendBroadcasts(s, "BanSystem.Kick.Admins", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg));
			return true;
		}
		Loader.noPerms(s, "Kick", "BanSystem");
		return true;
	}
}

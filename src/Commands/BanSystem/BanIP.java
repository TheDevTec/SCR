package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class BanIP implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "BanIP", "BanSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "BanIP", "BanSystem");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.banIP(args[0], Loader.config.getString("BanSystem.BanIP.Text").replace("%reason%",Loader.config.getString("BanSystem.BanIP.Reason")));
				Loader.sendMessages(s, "BanSystem.BanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.BanIP.Reason")));
				Loader.sendBroadcasts(s, "BanSystem.BanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.BanIP.Reason")));
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
				PunishmentAPI.banIP(args[0], Loader.config.getString("BanSystem.BanIP.Text").replace("%reason%",msg));
				Loader.sendMessages(s, "BanSystem.BanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", msg+" &f[Silent]"));
				Loader.sendBroadcasts(s, "BanSystem.BanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]).replace("%reason%", msg+" &f[Silent]"),"servercontrol.silent");
				return true;
			}
			PunishmentAPI.banIP(args[0], Loader.config.getString("BanSystem.BanIP.Text").replace("%reason%",msg));
			Loader.sendMessages(s, "BanSystem.BanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
					.replace("%ip%", args[0]).replace("%reason%", msg));
			Loader.sendBroadcasts(s, "BanSystem.BanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
					.replace("%ip%", args[0]).replace("%reason%", msg));
			return true;
		}
		return true;
	}

}

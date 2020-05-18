package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

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
					Loader.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "TempMute")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempMute.Reason");
				long cooldownTime = TheAPI.getStringUtils()
						.getTimeFromString(Loader.config.getString("BanSystem.TempMute.Time"));

				TheAPI.getPunishmentAPI().tempmute(args[0],
						Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%", msg), cooldownTime);
				return true;

			}
			if (args.length == 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "TempMute")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempMute.Reason");

				TheAPI.getPunishmentAPI().tempmute(args[0],
						Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%", msg),
						TheAPI.getStringUtils().getTimeFromString(args[1]));
				return true;
			}
			if (args.length >= 3) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.msg(Loader.s("Prefix") + Loader.s("Immune.NoPunish").replace("%punishment%", "TempMute")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = TheAPI.buildString(args);
				msg = msg.replaceFirst(args[0] + " " + args[1] + " ", "");
				TheAPI.getPunishmentAPI().tempmute(args[0],
						Loader.config.getString("BanSystem.TempMute.Text").replace("%reason%", msg),
						TheAPI.getStringUtils().getTimeFromString(args[1]));
				return true;
			}
		}
		return true;
	}

}

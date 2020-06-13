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
				return true;
			}
		}
		return true;
	}
}

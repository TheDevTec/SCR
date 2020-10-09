package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.PunishmentAPI.PlayerBanList;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;

public class UnBan implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "UnBan", "BanSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "UnBan", "BanSystem");
				return true;
			}
			PlayerBanList p = PunishmentAPI.getBanList(args[0]);
			if (p.isBanned() || p.isTempBanned()) {
				PunishmentAPI.unban(args[0]);
				Loader.sendMessages(s, "BanSystem.UnBan.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				Loader.sendBroadcasts(s, "BanSystem.UnBan.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				return true;
			}
			Loader.sendMessages(s, "BanSystem.Not.IPBanned", Placeholder.c().replace("%ip%", args[0]));
			return true;
		}
		return true;
	}
}

package me.DevTec.ServerControlReloaded.Commands.BanSystem;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.punishmentapi.PlayerBanList;
import me.devtec.theapi.punishmentapi.PunishmentAPI;

public class UnBanIP implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "UnBanIP", "BanSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "UnBanIP", "BanSystem");
				return true;
			}
			PlayerBanList p = PunishmentAPI.getBanList(args[0]);
			if (p.isIPBanned() || p.isTempIPBanned()) {
				PunishmentAPI.unbanIP(args[0]);
				Loader.sendMessages(s, "BanSystem.UnBanIP.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]));
				Loader.sendBroadcasts(s, "BanSystem.UnBanIP.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%ip%", args[0]));
				return true;
			}
			Loader.sendMessages(s, "BanSystem.Not.IPBanned", Placeholder.c().replace("%ip%", args[0]));
			return true;
		}
		Loader.noPerms(s, "UnBanIP", "BanSystem");
		return true;
	}
}

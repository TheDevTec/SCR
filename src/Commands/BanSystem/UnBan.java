package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;
import me.DevTec.Bans.PlayerBanList;

public class UnBan implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.UnBan")) {
			if (args.length == 0) {
				Loader.Help(s, "/UnBan <player>", "BanSystem.UnBan");
				return true;
			}
			PlayerBanList p = TheAPI.getPunishmentAPI().getBanList(args[0]);
			if (p.isBanned() || p.isTempBanned()) {
				TheAPI.msg(Loader.s("Prefix")
						+ Loader.s("BanSystem.UnBan").replace("%player%", args[0]).replace("%playername%", args[0]), s);
				TheAPI.getPunishmentAPI().unban(args[0]);
				
				Bukkit.broadcastMessage(TheAPI.colorize(Loader.s("BanSystem.Broadcast.UnBan").replace("%playername%", args[0])
						.replace("%operator%", s.getName())
						));
				TheAPI.sendMessage(Loader.s("BanSystem.BanIP").replace("%playername%", args[0])
						.replace("%operator%", s.getName()), s);
				return true;
			}
			if (TheAPI.existsUser(args[0]))
				TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.PlayerHasNotBan").replace("%player%", args[0])
						.replace("%playername%", args[0]), s);
			else
				TheAPI.msg(Loader.PlayerNotEx(args[0]), s);
			return true; 
					

		}
		return true;
	}

}

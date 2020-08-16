package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;
import me.DevTec.Bans.PlayerBanList;
import me.DevTec.Bans.PunishmentAPI;

public class UnBanIP implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.UnBanIP")) {
			if (args.length == 0) {
				Loader.Help(s, "/UnBanIP <player>", "BanSystem.UnBanIP");
				return true;
			}
			PlayerBanList p = PunishmentAPI.getBanList(args[0]);
			if (p.isIPBanned() || p.isTempIPBanned()) {
				TheAPI.msg(Loader.s("Prefix")
						+ Loader.s("BanSystem.UnBanIP").replace("%player%", args[0]).replace("%playername%", args[0]),
						s);

				PunishmentAPI.unbanIP(args[0]);
				Bukkit.broadcast(TheAPI.colorize(Loader.s("BanSystem.Broadcast.UnBanIP").replace("%playername%", args[0])
						.replace("%operator%", s.getName())),"servercontrol.seesilent");
				
				TheAPI.sendMessage(Loader.s("BanSystem.UnBanIP").replace("%playername%", args[0])
						.replace("%operator%", s.getName())
						, s);
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

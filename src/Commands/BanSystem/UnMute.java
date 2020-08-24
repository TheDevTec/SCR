package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PlayerBanList;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;

public class UnMute implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.UnMute")) {
			if (args.length == 0) {
				Loader.Help(s, "/UnMute <player>", "BanSystem.UnMute");
				return true;
			}
			if (args.length == 1) {
				PlayerBanList p = PunishmentAPI.getBanList(args[0]);
				if (p.isMuted() || p.isTempMuted()) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.UnMute").replace("%player%", args[0])
							.replace("%playername%", args[0]), s);

					PunishmentAPI.unmute(args[0]);
					if (TheAPI.getPlayer(args[0]) != null)
						TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.UnMuted").replace("%player%", args[0])
								.replace("%playername%", args[0]), TheAPI.getPlayer(args[0]));
					Bukkit.broadcast(TheAPI.colorize(Loader.s("BanSystem.Broadcast.UnMute").replace("%playername%", args[0])
							.replace("%operator%", s.getName())),"servercontrol.seesilent");
					
					TheAPI.sendMessage(Loader.s("BanSystem.UnMute").replace("%playername%", args[0])
							.replace("%operator%", s.getName())
							, s);
					return true;
				}
				TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.PlayerNotMuted").replace("%player%", args[0])
						.replace("%playername%", args[0]), s);
				return true;

			}
		}
		return true;
	}

}

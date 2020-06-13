package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;
import me.DevTec.Bans.PlayerBanList;

public class UnMute implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.UnMute")) {
			if (args.length == 0) {
				Loader.Help(s, "/UnMute <player>", "BanSystem.UnMute");
				return true;
			}
			if (args.length == 1) {
				PlayerBanList p = TheAPI.getPunishmentAPI().getBanList(args[0]);
				if (p.isMuted() || p.isTempMuted()) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.UnMute").replace("%player%", args[0])
							.replace("%playername%", args[0]), s);

					TheAPI.getPunishmentAPI().unmute(args[0]);
					if (TheAPI.getPlayer(args[0]) != null)
						TheAPI.msg(Loader.s("Prefix") + Loader.s("BanSystem.UnMuted").replace("%player%", args[0])
								.replace("%playername%", args[0]), TheAPI.getPlayer(args[0]));
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

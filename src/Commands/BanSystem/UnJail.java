package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PlayerBanList;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;

public class UnJail implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.UnJail")) {
			if (args.length == 0) {
				TheAPI.msg("/UnJail <player>", s);
				return true;
			}
			PlayerBanList p = PunishmentAPI.getBanList(args[0]);
			if (p.isJailed() || p.isTempJailed()) {
				PunishmentAPI.unjail(args[0]);
				if (TheAPI.getPlayer(args[0]) != null)
					API.teleportPlayer(TheAPI.getPlayer(args[0]), TeleportLocation.SPAWN);
				else TheAPI.getUser(args[0]).setAndSave("request-spawn", 1);
				Loader.sendMessages(s, "BanSystem.UnJail.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				Loader.sendBroadcasts(s, "BanSystem.UnJail.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				return true;
			}
			Loader.sendMessages(s, "BanSystem.Not.Arrested", Placeholder.c().replace("%playername%", args[0]).replace("%player%", args[0]));
			return true;
		}
		return true;
	}
}

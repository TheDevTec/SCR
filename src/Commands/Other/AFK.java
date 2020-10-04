package Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import ServerControl.Loader.Placeholder;
import Utils.Repeat;
import me.DevTec.TheAPI.TheAPI;

public class AFK implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (API.hasPerm(s, "ServerControl.AFK")) {
				if (s instanceof Player) {
					SPlayer p = API.getSPlayer((Player) s);
					if (p.isAFK()) {
						p.setAFK(false);
						if (!p.hasVanish())
							Loader.sendBroadcasts(p.getPlayer(), "AFK.End");
					} else {
						p.setAFK(true);
					}
					return true;
				}
				TheAPI.msg("/AFK <player>", s);
				return true;
			}
			return true;
		}
		if (args.length == 1) {
			if (API.hasPerm(s, "ServerControl.AFK.Other")) {
				if (args[0].equals("*")) {
					Repeat.a(s, "AFK *");
					return true;
				}
				SPlayer p = API.getSPlayer(TheAPI.getPlayer(args[0]));
				if (p.getPlayer() != null) {
					if (p.isAFK()) {
						Loader.sendMessages(s, "AFK.Command.Other.End");
						Loader.sendMessages(p.getPlayer(), "AFK.Command.End");
						p.setAFK(false);
						if (!p.hasVanish())
							Loader.sendBroadcasts(p.getPlayer(), "AFK.End");
					} else {
						Loader.sendMessages(s, "AFK.Command.Other.Start");
						Loader.sendMessages(p.getPlayer(), "AFK.Command.Start");
						p.setAFK(true);
					}
					return true;
				}
				Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c().add("%player%", args[0]).add("%playername%", args[0]));
				return true;
			}
			return true;
		}
		return false;
	}
}

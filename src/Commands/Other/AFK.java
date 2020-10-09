package Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.Repeat;
import me.DevTec.TheAPI.TheAPI;

public class AFK implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (Loader.has(s, "AFK", "Other")) {
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
				Loader.Help(s, "AFK", "Other");
				return true;
			}
			Loader.noPerms(s, "AFK", "Other");
			return true;
		}
		if (Loader.has(s, "AFK", "Other", "Other")) {
			if (args[0].equals("*")) {
				Repeat.a(s, "AFK *");
				return true;
			}
			Player player = TheAPI.getPlayer(args[0]);
			if(player==null) {
				Loader.notOnline(s, args[0]);
				return true;
			}
			SPlayer p = API.getSPlayer(player);
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
		Loader.noPerms(s, "AFK", "Other", "Other");
		return true;
	}
}

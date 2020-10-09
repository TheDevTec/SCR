package Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class Vanish implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Vanish", "Other")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					if (!TheAPI.isVanished(p)) {
						TheAPI.vanish(p, "ServerControl.Vanish", true);
						Loader.sendMessages(s, "Vanish.Enabled.You");
						return true;
					}
					TheAPI.vanish(p, "ServerControl.Vanish", false);
					Loader.sendMessages(s, "Vanish.Disabled.You");
					return true;
				}
				Loader.Help(s, "Vanish", "Other");
				return true;
			}
			if (args.length == 1) {
				Player t = TheAPI.getPlayer(args[0]);
				if (t != null) {
					if (!TheAPI.isVanished(t)) {
						TheAPI.vanish(t, "ServerControl.Vanish", true);
						Loader.sendMessages(s, "Vanish.Enabled.Other.Sender", Placeholder.c().add("%player%", t.getName()).add("%playername%", t.getDisplayName()));
						Loader.sendMessages(s, "Vanish.Enabled.Other.Receiver", Placeholder.c().add("%player%", s.getName()).add("%playername%", s.getName()));
						return true;
					}
					TheAPI.vanish(t, "ServerControl.Vanish", false);
					Loader.sendMessages(s, "Vanish.Disabled.Other.Sender", Placeholder.c().add("%player%", t.getName()).add("%playername%", t.getDisplayName()));
					Loader.sendMessages(s, "Vanish.Disabled.Other.Receiver", Placeholder.c().add("%player%", s.getName()).add("%playername%", s.getName()));
					return true;
				}
				Loader.notOnline(s, args[0]);
				return true;
			}
		}
		return true;
	}
}
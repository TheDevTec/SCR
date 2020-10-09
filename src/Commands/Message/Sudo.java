package Commands.Message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.TheAPI.SudoType;

public class Sudo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Sudo", "Message")) {
			if (args.length == 0) {
				Loader.Help(s, "/Sudo <player> <arguments>", "Message");
				return true;
			}
			if (args.length == 1) {
				Player target = TheAPI.getPlayer(args[0]);
				if (target != null) {
					Loader.Help(s, "/Sudo " + target.getName() + " <arguments>", "Sudo");
					return true;
				}
				Loader.Help(s, "/Sudo <player> <arguments>", "Sudo");
				return true;
			}
			if (args.length >= 2) {
				Player target = TheAPI.getPlayer(args[0]);
				if (args[0] != null) {
					String msg = TheAPI.buildString(args);
					msg = msg.replaceFirst(args[0] + " ", "");
					if (msg.startsWith("/")) {
						msg = msg.replaceFirst("/", "");
						TheAPI.sudo(target, SudoType.COMMAND, msg);
						Loader.sendMessages(s, "Sudo.Command", Placeholder.c().add("%command%", msg).add("%player%", target.getName()));
						return true;
					} else {
						TheAPI.sudo(target, SudoType.CHAT, msg);
						Loader.sendMessages(s, "Sudo.Message", Placeholder.c().add("%message%", msg).add("%player%", target.getName()));
						return true;
					}
				}
				Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c().add("%player%", target.getName()));
				return true;
			}
		}
		return true;

	}
}

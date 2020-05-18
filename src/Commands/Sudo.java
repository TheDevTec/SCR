package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;

public class Sudo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Sudo")) {
			if (args.length == 0) {
				Loader.Help(s, "/Sudo <player> <arguments>", "Sudo");
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
				if (target != null) {
					String msg = TheAPI.buildString(args);
					msg = msg.replaceFirst(args[0] + " ", "");
					if (msg.startsWith("/")) {
						msg = msg.replaceFirst("/", "");
						TheAPI.sudo(target, SudoType.COMMAND, msg);
						String st = API.replacePlayerName(Loader.s("Sudo.SendCommand"), target);
						Loader.msg(Loader.s("Prefix") + st.replace("%command%", msg), s);
						return true;
					} else {
						TheAPI.sudo(target, SudoType.CHAT, msg);
						String st = API.replacePlayerName(Loader.s("Sudo.SendMessage"), target);
						Loader.msg(Loader.s("Prefix") + st.replace("%message%", msg), s);
						return true;
					}
				}
				Loader.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
		}
		return true;

	}
}

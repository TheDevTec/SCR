package Commands.Tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;

public class Tpadeny implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpDeny", "TpSystem")) {
			if (s instanceof Player) {
				RequestMap.deny((Player)s);
				return true;
			}
		}
		Loader.noPerms(s, "TpDeny", "TpSystem");
		return true;
	}
}
package Commands.Tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;

public class Tpcancel implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpCancel", "TpSystem")) {
			if (s instanceof Player) {
				RequestMap.cancel((Player)s);
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "TpCancel", "TpCancel");
		return true;
	}

}

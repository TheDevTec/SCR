package Commands.Kill;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;

public class Suicide implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Suicide", "Kill")) {
			if (s instanceof Player) {
				Player p = (Player) s;
				p.setHealth(0);
				if (p.isDead())
					Loader.sendBroadcasts(s, "Kill.Suicide");
				return true;
			}
			Loader.Help(s, "Kill", "Kill");
			return true;
		}
		return true;
	}

}

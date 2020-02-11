package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Suicide implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Suicide")){
			if(s instanceof Player) {
				Player p = (Player)s;
				p.setHealth(0);
				if(p.isDead())
					TheAPI.broadcastMessage(API.replacePlayerName(Loader.s("Kill.Suicide"),p));
				return true;
			}
			Loader.Help(s, "/Kill <player>", "Kill");
			return true;
		}
		return true;
	}

}

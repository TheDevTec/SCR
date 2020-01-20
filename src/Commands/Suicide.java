package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;

public class Suicide implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Suicide")){
			if(s instanceof Player) {
				Player p = (Player)s;
				p.damage(p.getMaxHealth());
				if(p.isDead())
				Loader.msg(API.replacePlayerName(Loader.s("Kill.Suicide"),p), s);
				return true;
			}
			Loader.Help(s, "/Kill <player>", "Kill");
			return true;
		}
		return true;
	}

}

package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class Kill implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Kill")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					boolean i = p.isDead();
					p.setHealth(0);
					if (p.isDead() && !i)
						TheAPI.msg(API.replacePlayerName(Loader.s("Kill.Killed"), p), s);
					return true;
				} else {
					Loader.Help(s, "/Kill <player>", "Kill");
					return true;
				}
			}
			Player p = TheAPI.getPlayer(args[0]);
			if (p == null) {
				TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
			boolean i = p.isDead();
			p.setHealth(0);
			if (p.isDead() && !i)
				TheAPI.msg(API.replacePlayerName(Loader.s("Kill.Killed"), p), s);
		}
		return true;
	}
}

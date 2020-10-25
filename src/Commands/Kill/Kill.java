package Commands.Kill;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class Kill implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Kill", "Kill")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					boolean i = p.isDead();
					p.setHealth(0);
					if (p.isDead() && !i)
						Loader.sendMessages(s, "Kill.Killed", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName()));
					return true;
				} else {
					Loader.Help(s, "Kill", "Kill");
					return true;
				}
			}
			Player p = TheAPI.getPlayer(args[0]);
			if (p == null) {
				Loader.notOnline(s,args[0]);
				return true;
			}
			boolean i = p.isDead();
			p.setHealth(0);
			if (p.isDead() && !i)
				Loader.sendMessages(s, "Kill.Killed", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName()));
		}
		Loader.noPerms(s, "Kill", "Kill");
		return true;
	}
}

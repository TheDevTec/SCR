package me.DevTec.ServerControlReloaded.Commands.Kill;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;

public class Kill implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Kill", "Kill")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					boolean i = p.isDead() || p.getHealth()==0;
					p.setHealth(0);
					if ((p.isDead() || p.getHealth()==0) && !i)
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
			boolean i = p.isDead() || p.getHealth()==0;
			p.setHealth(0);
			if ((p.isDead() || p.getHealth()==0) && !i)
				Loader.sendMessages(s, "Kill.Killed", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName()));
			return true;
		}
		Loader.noPerms(s, "Kill", "Kill");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

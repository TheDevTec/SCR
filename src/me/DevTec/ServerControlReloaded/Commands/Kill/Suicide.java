package me.DevTec.ServerControlReloaded.Commands.Kill;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;

public class Suicide implements CommandExecutor, TabCompleter {

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
		Loader.noPerms(s, "Suicide", "Kill");
		return true;
	}


	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

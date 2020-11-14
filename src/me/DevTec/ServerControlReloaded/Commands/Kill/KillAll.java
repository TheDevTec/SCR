package me.DevTec.ServerControlReloaded.Commands.Kill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class KillAll implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "KillAll", "Kill")) {
			List<String> pl = new ArrayList<String>();
			for (Player p : TheAPI.getOnlinePlayers()) {
				boolean i = p.isDead() || p.getHealth()==0;
				p.setHealth(0);
				if ((p.isDead() || p.getHealth()==0) && !i) {
					pl.add(p.getName());
				}
			}
			Loader.sendMessages(s, "Kill.KilledMore", Placeholder.c()
					.add("%list%", pl.isEmpty()?"none":StringUtils.join(pl, ","))
					.replace("%amount%", pl.size()+""));
			return true;
		}
		Loader.noPerms(s, "KillAll", "Kill");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

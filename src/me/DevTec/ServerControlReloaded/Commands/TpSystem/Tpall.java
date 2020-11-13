package me.DevTec.ServerControlReloaded.Commands.TpSystem;

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
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class Tpall implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpAll", "TpSystem")) {
			if (s instanceof Player) {
				ArrayList<String> list = new ArrayList<String>();
				for (Player p : TheAPI.getOnlinePlayers()) {
					if (p == s)
						continue;
					User d = TheAPI.getUser(p);
					if (!d.getBoolean("TpBlock." + s.getName()) && !d.getBoolean("TpBlock-Global")
							|| d.getBoolean("TpBlock." + s.getName()) && !d.getBoolean("TpBlock-Global")
									&& s.hasPermission("ServerControl.Tpall.Blocked")
							|| d.getBoolean("TpBlock." + s.getName()) && d.getBoolean("TpBlock-Global")
									&& s.hasPermission("ServerControl.Tpall.Blocked")
							|| !d.getBoolean("TpBlock." + s.getName()) && d.getBoolean("TpBlock-Global")
									&& s.hasPermission("ServerControl.Tpall.Blocked")) {
						list.add(p.getName());
						p.teleport(((Player) s));
					}
				}
				Loader.sendMessages(s, "TpSystem.TpAll", Placeholder.c().replace("%list%",
						list.isEmpty()?"none":StringUtils.join(list, ", ")).replace("%amount%",
								list.size()+""));
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "TpAll", "TpSystem");
		return true;
	}
}
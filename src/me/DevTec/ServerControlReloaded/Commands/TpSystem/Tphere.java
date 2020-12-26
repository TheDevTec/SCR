package me.DevTec.ServerControlReloaded.Commands.TpSystem;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;

public class Tphere implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpHere", "TpSystem")) {
			if (args.length == 0) {
				Loader.Help(s, "TpHere", "TpSystem");
				return true;
			}
			Player target = TheAPI.getPlayer(args[0]);
			if (target == null) {
				Loader.notOnline(s,args[0]);
				return true;
			}
			if (Loader.has(s, "TpHere", "TpSystem", "Blocked") || !Loader.has(s, "TpHere", "TpSystem", "Blocked") && !RequestMap.isBlocking(target.getName(), s.getName())) {
				Loader.sendMessages(s, "TpSystem.TpHere.Sender", Placeholder.c().replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()));
				Loader.sendMessages(target, "TpSystem.TpHere.Receiver", Placeholder.c().replace("%player%", s.getName()).replace("%playername%", ((Player)s).getDisplayName()));
				API.setBack(target);
				if (setting.tp_safe)
					API.safeTeleport(target,((Player)s).getLocation());
				else target.teleport((Player)s);
				return true;
			}
			Loader.sendMessages(s, "TpSystem.Block.IsBlocked.Teleport", Placeholder.c().replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()));
			return true;
		}
		Loader.noPerms(s, "TpHere", "TpSystem");
		return true;
	}

}
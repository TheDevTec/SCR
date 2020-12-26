package me.DevTec.ServerControlReloaded.Commands.TpSystem;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;

public class Tpa implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender f, Command arg1, String arg2, String[] args) {
		if (Loader.has(f, "Tpa", "TpSystem")) {
			if (f instanceof Player) {
				Player s = (Player) f;
				if (args.length == 0) {
					Loader.Help(s, "Tpa", "TpSystem");
					return true;
				}
				Player d = TheAPI.getPlayer(args[0]);
				if (d == null) {
					Loader.notOnline(s,args[0]);
					return true;
				}
				if (s != d) {
					RequestMap.add(s, d.getName(), 0);
					return true;
				}
				Loader.sendMessages(s, "TpSystem.SendRequestToSelf");
				return true;
			}
			return true;
		}
		Loader.noPerms(f, "Tpa", "TpSystem");
		return true;
	}
}

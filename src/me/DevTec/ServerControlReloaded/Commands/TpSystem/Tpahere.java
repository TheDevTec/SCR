package me.DevTec.ServerControlReloaded.Commands.TpSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.TheAPI;

public class Tpahere implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender f, Command arg1, String arg2, String[] args) {
		if (Loader.has(f, "TpaHere", "TpSystem")) {
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
					RequestMap.add(s, d.getName(), 1);
					return true;
				}
				Loader.sendMessages(s, "TpSystem.SendRequestToSelf");
				return true;
			}
			return true;
		}
		Loader.noPerms(f, "TpaHere", "TpSystem");
		return true;
	}
}
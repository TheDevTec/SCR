package me.devtec.servercontrolreloaded.commands.tpsystem;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Tpahere implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "TpaHere", "TpSystem") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}

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
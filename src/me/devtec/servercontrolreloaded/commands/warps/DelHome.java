package me.devtec.servercontrolreloaded.commands.warps;


import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class DelHome implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			if (Loader.has(s, "DelHome", "Warps")) {
				if(!CommandsManager.canUse("Warps.DelHome", s)) {
					Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Warps.DelHome", s))));
					return true;
				}
				if (args.length == 0) {
					Loader.Help(s, "DelHome", "Warps");
					return true;
				}
				User d = TheAPI.getUser(s.getName());
				if (d.exist("Homes." + args[0])) {
					d.setAndSave("Homes." + args[0], null);
					Loader.sendMessages(s, "Home.Delete", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName())
							.add("%home%", args[0]));
					return true;
				}
				Loader.sendMessages(s, "Home.NotExist", Placeholder.c()
						.add("%home%", args[0]));
				return true;
			}
			Loader.noPerms(s, "DelHome", "Warps");
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (s instanceof Player && args.length == 1 && Loader.has(s, "DelHome", "Warps"))
			return StringUtils.copyPartialMatches(args[0], TheAPI.getUser(s.getName()).getKeys("Homes"));
		return Arrays.asList();
	}
}
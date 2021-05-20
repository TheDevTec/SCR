package me.devtec.servercontrolreloaded.commands.tpsystem;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;

public class Tphere implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "TpHere", "TpSystem") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpHere", "TpSystem")) {
			if(!CommandsManager.canUse("TpSystem.TpHere", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("TpSystem.TpHere", s))));
				return true;
			}
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
					API.safeTeleport(target,target.isFlying(),new Position(((Player)s).getLocation()));
				else
					API.teleport(target, ((Player) s).getLocation());
				return true;
			}
			Loader.sendMessages(s, "TpSystem.Block.IsBlocked.Teleport", Placeholder.c().replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()));
			return true;
		}
		Loader.noPerms(s, "TpHere", "TpSystem");
		return true;
	}

}
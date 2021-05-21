package me.devtec.servercontrolreloaded.commands.kill;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Suicide implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Suicide", "Kill")) {
		if(!CommandsManager.canUse("Kill.Suicide", s)) {
			Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Kill.Suicide", s))));
			return true;
		}
		if(args.length==0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				p.setHealth(0);
				if (p.isDead())
					Loader.sendBroadcasts(s, "Kill.Suicide", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
				return true;
			}
			Loader.Help(s, "Suicide", "Kill");
			return true;
		}
			if(Loader.has(s, "Suicide", "Kill", "Other")) {
				Player o = TheAPI.getPlayer(args[0]);
				if(o==null) {
					Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
							.add("%player%", args[0])
							.add("%playername%", args[0]));
					return true;
				}
				o.setHealth(0);
				if(o.isDead())
					Loader.sendBroadcasts(s, "Kill.Suicide", Placeholder.c()
							.add("%player%", o.getName())
							.add("%playername%", o.getDisplayName()));
				return true;
			}
			Loader.noPerms(s, "Suicide", "Kill", "Other");
			return true;
		}
		Loader.noPerms(s, "Suicide", "Kill");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "Kill", "Kill"))
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

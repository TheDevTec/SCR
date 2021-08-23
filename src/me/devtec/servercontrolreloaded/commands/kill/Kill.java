package me.devtec.servercontrolreloaded.commands.kill;

import java.util.Collections;
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
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Kill implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Kill", "Kill")) {
			if(!CommandsManager.canUse("Kill.Kill", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Kill.Kill", s))));
				return true;
			}
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					boolean i = p.isDead() || p.getHealth()==0;
					p.setHealth(0);
					if ((p.isDead() || p.getHealth()==0) && !i)
						Loader.sendMessages(s, "Kill.Killed", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName()));
					return true;
				}
				Loader.Help(s, "Kill", "Kill");
				return true;
			}
			Player p = TheAPI.getPlayer(args[0]);
			if (p == null) {
				Loader.notOnline(s,args[0]);
				return true;
			}
			boolean i = p.isDead() || p.getHealth()==0;
			p.setHealth(0);
			if ((p.isDead() || p.getHealth()==0) && !i)
				Loader.sendMessages(s, "Kill.Killed", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName()));
			return true;
		}
		Loader.noPerms(s, "Kill", "Kill");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "Kill", "Kill"))
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}
}

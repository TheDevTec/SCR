package me.DevTec.ServerControlReloaded.Commands.Kill;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class Suicide implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length==0) {
		if (Loader.has(s, "Suicide", "Kill")) {
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
		Loader.noPerms(s, "Suicide", "Kill");
		}
		if(args.length==1) {
			if(Loader.has(s, "Suicide", "Kill", "Other")) {
				Player o = TheAPI.getPlayer(args[0]);
				if(o==null) {
					Loader.sendMessages(s, "Missing.Player.Offline");
					return true;
				}
				o.setHealth(0);
				if(o.isDead())
					Loader.sendBroadcasts(s, "Kill.Suicide", Placeholder.c()
							.add("%player", o.getName())
							.add("%playername%", o.getDisplayName()));
				return true;
			}
		}
		if(args.length>1) {
			if(Loader.has(s, "Suicide", "Kill", "Other"))
				Loader.advancedHelp(s, "Suicide", "Kill", "Other");
			if(Loader.has(s, "Suicide", "Kill")) 
				Loader.Help(s, "Suicide","Kill");
			return true;
		}
		return true;
	}


	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

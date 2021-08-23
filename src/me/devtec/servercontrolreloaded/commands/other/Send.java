package me.devtec.servercontrolreloaded.commands.other;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
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

public class Send implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Send", "Other") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command var2, String var3, String[] args) {
		if (Loader.has(s, "Send", "Other")) {
			if(!CommandsManager.canUse("Other.Send", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Send", s))));
				return true;
			}
			if(args.length==0) {
				Loader.Help(s, "Send", "Other");
				return true;
			}
			if(args[0].equals("*")) {
				for(Player p : TheAPI.getOnlinePlayers())
					Bukkit.dispatchCommand(s, "send "+p.getName()+" "+args[1]);
				return true;
			}
			Player p = TheAPI.getPlayer(args[0]);
			if(p==null) {
				Loader.notOnline(s, args[0]);
				return true;
			}
			API.send(p,args[1]);
			Loader.sendMessages(s, "Send", Placeholder.c().add("%server%", args[1]).add("%player%", p.getName())
					.add("%playername%", p.getDisplayName()==null?p.getName():p.getDisplayName()).add("%displayname%", p.getDisplayName()==null?p.getName():p.getDisplayName()).add("%customname%", p.getCustomName()+""));
			return true;
		}
		Loader.noPerms(s, "Send", "Other");
		return true;
	}
}

package me.devtec.servercontrolreloaded.commands.message;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.bungeecord.BungeeListener;
import me.devtec.theapi.utils.StringUtils;

public class PrivateMessageIgnore implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command var2, String var3, String[] args) {
		if(Loader.has(s, "Ignore", "Message")) {
			if(!CommandsManager.canUse("Message.Ignore", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Message.Ignore", s))));
				return true;
			}
			if(args.length==0) {
				Loader.Help(s, "Ignore", "Message");
				return true;
			}
			PrivateMessageManager.ignore(s, args[0]);
			return true;
		}
		Loader.noPerms(s, "Ignore", "Message");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Reply", "Message"))
			return StringUtils.copyPartialMatches(args[args.length-1], Loader.hasBungee?BungeeListener.getOnline():API.getPlayerNames(s));
		return Collections.emptyList();
	}
}

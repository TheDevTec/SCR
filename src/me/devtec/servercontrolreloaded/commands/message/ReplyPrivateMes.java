package me.devtec.servercontrolreloaded.commands.message;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class ReplyPrivateMes implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Reply", "Message")) {
			if(!CommandsManager.canUse("Message.Reply", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Message.Reply", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "Reply", "Message");
				return true;
			}
			PrivateMessageManager.reply(s, StringUtils.buildString(args));
			String r = PrivateMessageManager.getReply(s);
			if(r!=null)
			PrivateMessageManager.setReply(r.equalsIgnoreCase("console")?TheAPI.getConsole():TheAPI.getPlayerOrNull(r), s.getName());
			return true;
		}
		Loader.noPerms(s, "Reply", "Message");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Reply", "Message"))
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

package me.devtec.servercontrolreloaded.commands.message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;

public class PrivateMessageIgnore implements CommandExecutor {

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
}

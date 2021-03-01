package me.DevTec.ServerControlReloaded.Commands.Message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.DevTec.ServerControlReloaded.SCR.Loader;

public class PrivateMessageIgnore implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command var2, String var3, String[] args) {
		if(Loader.has(s, "Ignore", "Message")) {
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

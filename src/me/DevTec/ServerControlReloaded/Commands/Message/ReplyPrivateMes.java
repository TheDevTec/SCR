package me.DevTec.ServerControlReloaded.Commands.Message;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class ReplyPrivateMes implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Reply", "Message")) {
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
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

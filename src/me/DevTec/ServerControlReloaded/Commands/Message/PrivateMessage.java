package me.DevTec.ServerControlReloaded.Commands.Message;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.Colors;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class PrivateMessage implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "PrivateMessage", "Message")) {
			if (args.length == 0) {
				Loader.Help(s, "PrivateMessage", "Message");
				return true;
			}
			if (args.length == 1) {
				if(setting.pm && s instanceof Player) {
					if (Loader.has(s, "PrivateMessage", "Message", "Lock")) {
					CommandSender d = TheAPI.getPlayer(args[0]);
					if(d==null) {
						if(args[0].equalsIgnoreCase("console"))d=TheAPI.getConsole();
						else {
							Loader.notOnline(s, args[0]);
							return true;
						}
					}
					PrivateMessageManager.setReply(s, d.getName());
					PrivateMessageManager.setChatLock((Player)s, !PrivateMessageManager.hasChatLock((Player)s));
					PrivateMessageManager.setLockType((Player)s, "msg");
					Loader.sendMessages(s, "PrivateMessage.ChatLock."+PrivateMessageManager.hasChatLock((Player)s));
					return true;
					}
					Loader.noPerms(s, "PrivateMessage", "Message", "Lock");
					return true;
				}
				Loader.Help(s, "PrivateMessage", "Message");
				return true;
			}
			if (args.length >= 2) {
				String msg = Colors.colorize(StringUtils.buildString(1, args), false, s);
				CommandSender d = TheAPI.getPlayer(args[0]);
				if(d==null) {
					if(args[0].equalsIgnoreCase("console"))d=TheAPI.getConsole();
					else {
						Loader.notOnline(s, args[0]);
						return true;
					}
				}
				PrivateMessageManager.sendMessage(s, d, msg);
				PrivateMessageManager.setReply(s, d.getName());
				PrivateMessageManager.setReply(d, s.getName());
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "PrivateMessage", "Message");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

package me.devtec.servercontrolreloaded.commands.message;

import java.util.Arrays;
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
import me.devtec.servercontrolreloaded.utils.Colors;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class PrivateMessage implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "PrivateMessage", "Message")) {
			if(!CommandsManager.canUse("Message.PrivateMessage", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Message.PrivateMessage", s))));
				return true;
			}
			if (args.length == 0) {
				if(setting.pm && s instanceof Player && PrivateMessageManager.hasChatLock((Player)s)) {
					PrivateMessageManager.setChatLock((Player)s, !PrivateMessageManager.hasChatLock((Player)s));
					PrivateMessageManager.setLockType((Player)s, "msg");
					Loader.sendMessages(s, "PrivateMessage.ChatLock."+PrivateMessageManager.hasChatLock((Player)s));
					return true;
				}
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
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "PrivateMessage", "Message"))
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

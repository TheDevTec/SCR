package me.devtec.servercontrolreloaded.commands.message;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Helpop implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Helpop", "Message")) {
			if (args.length == 0) {
				if(s instanceof Player && setting.helpop) {
					if (Loader.has(s, "Helpop", "Message", "Lock")) {
					PrivateMessageManager.setChatLock((Player)s, !PrivateMessageManager.hasChatLock((Player)s));
					Loader.sendMessages(s, "Helpop.ChatLock."+PrivateMessageManager.hasChatLock((Player)s));
					PrivateMessageManager.setLockType((Player)s, "helpop");
					return true;
					}
					Loader.noPerms(s, "Helpop", "Message", "Lock");
					return true;
				}else
				Loader.Help(s, "Helpop", "Message");
				return true;
			}
			TheAPI.broadcast(Loader.config.getString("Format.HelpOp").replace("%sender%", s.getName())
					.replace("%sendername%", TheAPI.getPlayerOrNull(s.getName())!=null?TheAPI.getPlayerOrNull(s.getName()).getDisplayName():s.getName()).replace("%message%", TheAPI.buildString(args)), Loader.cmds.exists("Message.Helpop.SubPermissions.Receive")?Loader.cmds.getString("Message.Helpop.SubPermissions.Receive"):"SCR.Command.Helpop.Receive");
			if (!Loader.has(s, "Helpop", "Message", "Receive"))
				TheAPI.msg(Loader.config.getString("Format.HelpOp").replace("%sender%", s.getName()).replace("%sendername%", TheAPI.getPlayerOrNull(s.getName())!=null?TheAPI.getPlayerOrNull(s.getName()).getDisplayName():s.getName()).replace("%message%", TheAPI.buildString(args)), s);
			return true;
		}
		Loader.noPerms(s, "Helpop", "Message");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Helpop", "Message"))
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

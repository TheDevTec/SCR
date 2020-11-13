package me.DevTec.ServerControlReloaded.Commands.Message;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.TheAPI;

public class Helpop implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Helpop", "Message")) {
			if (args.length == 0) {
				Loader.Help(s, "Helpop", "Message");
				return true;
			}
			TheAPI.broadcast(Loader.config.getString("Format.HelpOp").replace("%sender%", s.getName())
					.replace("%sendername%", TheAPI.getPlayerOrNull(s.getName())!=null?TheAPI.getPlayerOrNull(s.getName()).getDisplayName():s.getName()).replace("%message%", TheAPI.buildString(args)), Loader.cmds.exists("Message.Helpop.SubPermissions.Receive")?Loader.cmds.getString("Message.Helpop.SubPermissions.Receive"):"SCR.Command.Helpop.Receive");
			if (!Loader.has(s, "Helpop", "Message", "Receive")) {
				TheAPI.msg(Loader.config.getString("Format.HelpOp").replace("%sender%", s.getName()).replace("%sendername%", TheAPI.getPlayerOrNull(s.getName())!=null?TheAPI.getPlayerOrNull(s.getName()).getDisplayName():s.getName()).replace("%message%", TheAPI.buildString(args)), s);
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "Helpop", "Message");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

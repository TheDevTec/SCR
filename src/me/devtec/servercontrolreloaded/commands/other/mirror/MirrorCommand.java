package me.devtec.servercontrolreloaded.commands.other.mirror;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MirrorCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		if(Loader.has(s, "Mirror", "Other")) {
			if(!CommandsManager.canUse("Other.Mirror", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Mirror", s))));
				return true;
			}
			if(args.length==0) {
				Loader.Help(s, "Mirror", "Other");
				Loader.sendMessages(s, "Mirror.Types", Placeholder.c().add("%types%", "AxisX, AxisZ, Center"));
				return true;
			}
			if(args.length==1) {
				if(args[0].equalsIgnoreCase("none")) {
					MirrorManager.remove( ((Player)s) );
					Loader.sendMessages(s, "Mirror.Disabled");
					return true;
				}
				MirrorManager.add( ((Player)s), args[0]);
	
				Loader.sendMessages(s, "Mirror.Enabled", Placeholder.c().add("%type%", args[0]) );
				return true;
			}
			return true;
		}
		else Loader.noPerms(s, "Mirror", "Other");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "Mirror", "Other")) {
			if(args.length==1)
				return StringUtils.copyPartialMatches(args[0], Arrays.asList("AxisX","AxisZ","Center", "NONE"));
		}
		return Collections.emptyList();
	}
}

package me.DevTec.ServerControlReloaded.Commands.Other.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.ChatFormatter;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class ChatNotify implements CommandExecutor, TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "ChatNotify", "Other") && args.length==1) {
			List<String> d = Loader.has(s, "ChatNotify", "Other", "ToggleOther")?API.getPlayerNames(s):new ArrayList<>();
			d.add("toggle");
			return StringUtils.copyPartialMatches(args[0], d);
		}
		return Arrays.asList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "ChatNotify", "Other")) {
			if(args.length==0) {
				if(s instanceof Player) {
					ChatFormatter.setNotify((Player)s, !ChatFormatter.getNotify((Player)s));
					Loader.sendMessages(s, "Notify."+(ChatFormatter.getNotify((Player)s)?"Enable":"Disable"));
					return true;
				}
				Loader.Help(s, "ChatNotify", "Other");
				return true;
			}
			if(args[0].equalsIgnoreCase("toggle")) {
				if(args.length==1) {
					if(s instanceof Player) {
						ChatFormatter.setNotify((Player)s, !ChatFormatter.getNotify((Player)s));
						Loader.sendMessages(s, "Notify."+(ChatFormatter.getNotify((Player)s)?"Enable":"Disable"));
						return true;
					}
					Loader.advancedHelp(s, "ChatNotify", "Other", "Toggle");
					return true;
				}
				Player d = TheAPI.getPlayer(args[1]);
				if(d==null) {
					Loader.notOnline(s, args[1]);
					return true;
				}
				if(d==s) {
					ChatFormatter.setNotify((Player)s, !ChatFormatter.getNotify((Player)s));
					Loader.sendMessages(s, "Notify."+(ChatFormatter.getNotify((Player)s)?"Enable":"Disable"));
					return true;
				}
				if(Loader.has(s, "ChatNotify", "Other", "ToggleOther")) {
					ChatFormatter.setNotify(d, !ChatFormatter.getNotify(d));
					Loader.sendMessages(s, "Notify."+(ChatFormatter.getNotify(d)?"Enable":"Disable"));
					return true;
				}
				Loader.advancedHelp(s, "ChatNotify", "Other", "ToggleOther");
				return true;
			}
		}
		Loader.noPerms(s, "ChatNotify", "Other");
		return true;
	}
}

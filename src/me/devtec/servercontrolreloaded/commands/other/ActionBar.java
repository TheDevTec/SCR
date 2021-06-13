package me.devtec.servercontrolreloaded.commands.other;

import java.util.ArrayList;
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
import me.devtec.servercontrolreloaded.utils.DisplayManager;
import me.devtec.servercontrolreloaded.utils.DisplayManager.DisplayType;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class ActionBar implements CommandExecutor, TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "ActionBar", "Other") && args.length==1) {
			List<String> d = Loader.has(s, "ActionBar", "Other", "ToggleOther")?API.getPlayerNames(s):new ArrayList<>();
			d.add("toggle");
			return StringUtils.copyPartialMatches(args[0], d);
		}
		return Arrays.asList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "ActionBar", "Other")) {
			if(!CommandsManager.canUse("Other.ActionBar", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.ActionBar", s))));
				return true;
			}
			if(args.length==0) {
				if(s instanceof Player) {
				if(DisplayManager.hasToggled((Player)s, DisplayType.ACTIONBAR)) {
					DisplayManager.show((Player)s, DisplayType.ACTIONBAR, true);
				}else {
					DisplayManager.hide((Player)s, DisplayType.ACTIONBAR);
				}
				return true;
				}
				Loader.Help(s, "ActionBar", "Other");
				return true;
			}
			if(args[0].equalsIgnoreCase("toggle")) {
				if(args.length==1) {
					if(s instanceof Player) {
						if(DisplayManager.hasToggled((Player)s, DisplayType.ACTIONBAR)) {
							DisplayManager.show((Player)s, DisplayType.ACTIONBAR, true);
						}else {
							DisplayManager.hide((Player)s, DisplayType.ACTIONBAR);
						}
						return true;
					}
					Loader.advancedHelp(s, "ActionBar", "Other", "Toggle");
					return true;
				}
				Player d = TheAPI.getPlayer(args[1]);
				if(d==null) {
					Loader.notOnline(s, args[1]);
					return true;
				}
				if(d==s) {
					if(DisplayManager.hasToggled((Player)s, DisplayType.ACTIONBAR)) {
						DisplayManager.show((Player)s, DisplayType.ACTIONBAR, true);
					}else {
						DisplayManager.hide((Player)s, DisplayType.ACTIONBAR);
					}
					return true;
				}
				if(Loader.has(s, "ActionBar", "Other", "ToggleOther")) {
					if(DisplayManager.hasToggled(d, DisplayType.ACTIONBAR)) {
						DisplayManager.show(d, DisplayType.ACTIONBAR, true);
						Loader.sendMessages(s, "ActionBar.Show", Placeholder.c().add("%player%", d.getName()).add("%playername%", d.getDisplayName()));
						}else {
						DisplayManager.hide(d, DisplayType.ACTIONBAR);
						Loader.sendMessages(s, "ActionBar.Hide", Placeholder.c().add("%player%", d.getName()).add("%playername%", d.getDisplayName()));
					}
					return true;
				}
				Loader.advancedHelp(s, "ActionBar", "Other", "ToggleOther");
				return true;
			}
		}
		Loader.noPerms(s, "ActionBar", "Other");
		return true;
	}
}

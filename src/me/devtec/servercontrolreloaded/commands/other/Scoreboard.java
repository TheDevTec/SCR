package me.devtec.servercontrolreloaded.commands.other;

import java.util.ArrayList;
import java.util.Collections;
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

public class Scoreboard implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "ScoreBoard", "Other") && args.length==1) {
			List<String> d = Loader.has(s, "ScoreBoard", "Other", "ToggleOther")?API.getPlayerNames(s):new ArrayList<>();
			d.add("toggle");
			return StringUtils.copyPartialMatches(args[0], d);
		}
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "ScoreBoard", "Other")) {
			if(!CommandsManager.canUse("Other.ScoreBoard", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.ScoreBoard", s))));
				return true;
			}
			if(args.length==0) {
				if(s instanceof Player) {
				if(DisplayManager.hasToggled((Player)s, DisplayType.SCOREBOARD)) {
					DisplayManager.show((Player)s, DisplayType.SCOREBOARD, true);
				}else {
					DisplayManager.hide((Player)s, DisplayType.SCOREBOARD);
				}
				return true;
				}
				Loader.Help(s, "ScoreBoard", "Other");
				return true;
			}
			if(args[0].equalsIgnoreCase("toggle")) {
				if(args.length==1) {
					if(s instanceof Player) {
						if(DisplayManager.hasToggled((Player)s, DisplayType.SCOREBOARD)) {
							DisplayManager.show((Player)s, DisplayType.SCOREBOARD, true);
						}else {
							DisplayManager.hide((Player)s, DisplayType.SCOREBOARD);
						}
						return true;
					}
					Loader.advancedHelp(s, "ScoreBoard", "Other", "Toggle");
					return true;
				}
				Player d = TheAPI.getPlayer(args[1]);
				if(d==null) {
					Loader.notOnline(s, args[1]);
					return true;
				}
				if(d==s) {
					if(DisplayManager.hasToggled((Player)s, DisplayType.SCOREBOARD)) {
						DisplayManager.show((Player)s, DisplayType.SCOREBOARD, true);
					}else {
						DisplayManager.hide((Player)s, DisplayType.SCOREBOARD);
					}
					return true;
				}
				if(Loader.has(s, "ScoreBoard", "Other", "ToggleOther")) {
					if(DisplayManager.hasToggled(d, DisplayType.SCOREBOARD)) {
						DisplayManager.show(d, DisplayType.SCOREBOARD, true);
						Loader.sendMessages(s, "Scoreboard.Show", Placeholder.c().add("%player%", d.getName()).add("%playername%", d.getDisplayName()));
						}else {
						DisplayManager.hide(d, DisplayType.SCOREBOARD);
						Loader.sendMessages(s, "Scoreboard.Hide", Placeholder.c().add("%player%", d.getName()).add("%playername%", d.getDisplayName()));
					}
					return true;
				}
				Loader.advancedHelp(s, "ScoreBoard", "Other", "ToggleOther");
				return true;
			}
		}
		Loader.noPerms(s, "ScoreBoard", "Other");
		return true;
	}
}

package me.devtec.servercontrolreloaded.commands.other;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.DisplayManager;
import me.devtec.servercontrolreloaded.utils.DisplayManager.DisplayType;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BossBar implements CommandExecutor, TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "BossBar", "Other") && args.length==1) {
			List<String> d = Loader.has(s, "BossBar", "Other", "ToggleOther")?API.getPlayerNames(s):new ArrayList<>();
			d.add("toggle");
			return StringUtils.copyPartialMatches(args[0], d);
		}
		return Arrays.asList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "BossBar", "Other")) {
			if(!CommandsManager.canUse("Other.BossBar", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.BossBar", s))));
				return true;
			}
			if(args.length==0) {
				if(s instanceof Player) {
				if(DisplayManager.hasToggled((Player)s, DisplayType.BOSSBAR)) {
					DisplayManager.show((Player)s, DisplayType.BOSSBAR, true);
				}else {
					DisplayManager.hide((Player)s, DisplayType.BOSSBAR);
				}
				return true;
				}
				Loader.Help(s, "BossBar", "Other");
				return true;
			}
			if(args[0].equalsIgnoreCase("toggle")) {
				if(args.length==1) {
					if(s instanceof Player) {
						if(DisplayManager.hasToggled((Player)s, DisplayType.BOSSBAR)) {
							DisplayManager.show((Player)s, DisplayType.BOSSBAR, true);
						}else {
							DisplayManager.hide((Player)s, DisplayType.BOSSBAR);
						}
						return true;
					}
					Loader.advancedHelp(s, "BossBar", "Other", "Toggle");
					return true;
				}
				Player d = TheAPI.getPlayer(args[1]);
				if(d==null) {
					Loader.notOnline(s, args[1]);
					return true;
				}
				if(d==s) {
					if(DisplayManager.hasToggled((Player)s, DisplayType.BOSSBAR)) {
						DisplayManager.show((Player)s, DisplayType.BOSSBAR, true);
					}else {
						DisplayManager.hide((Player)s, DisplayType.BOSSBAR);
					}
					return true;
				}
				if(Loader.has(s, "BossBar", "Other", "ToggleOther")) {
					if(DisplayManager.hasToggled(d, DisplayType.BOSSBAR)) {
						DisplayManager.show(d, DisplayType.BOSSBAR, true);
						Loader.sendMessages(s, "BossBar.Show", Placeholder.c().add("%player%", d.getName()).add("%playername%", d.getDisplayName()));
						}else {
						DisplayManager.hide(d, DisplayType.BOSSBAR);
						Loader.sendMessages(s, "BossBar.Hide", Placeholder.c().add("%player%", d.getName()).add("%playername%", d.getDisplayName()));
					}
					return true;
				}
				Loader.advancedHelp(s, "BossBar", "Other", "ToggleOther");
				return true;
			}
		}
		Loader.noPerms(s, "BossBar", "Other");
		return true;
	}
}

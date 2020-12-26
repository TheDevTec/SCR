package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.DisplayManager;
import me.DevTec.ServerControlReloaded.Utils.DisplayManager.DisplayType;
import me.devtec.theapi.TheAPI;

public class ActionBar implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "ActionBar", "Other")) {
			if(args.length==0) {
				if(s instanceof Player) {
				if(DisplayManager.hasToggled((Player)s, DisplayType.ACTIONBAR)) {
					DisplayManager.show((Player)s, DisplayType.ACTIONBAR);
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
							DisplayManager.show((Player)s, DisplayType.ACTIONBAR);
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
						DisplayManager.show((Player)s, DisplayType.ACTIONBAR);
					}else {
						DisplayManager.hide((Player)s, DisplayType.ACTIONBAR);
					}
					return true;
				}
				if(Loader.has(s, "ActionBar", "Other", "ToggleOther")) {
					if(DisplayManager.hasToggled(d, DisplayType.ACTIONBAR)) {
						DisplayManager.show(d, DisplayType.ACTIONBAR);
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

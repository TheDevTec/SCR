package me.devtec.servercontrolreloaded.commands.inventory;

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
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Craft implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "Workbench", "Inventory"))
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Workbench", "Inventory")) {
			if(!CommandsManager.canUse("Inventory.Workbench", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Inventory.Workbench", s))));
				return true;
			}
			if (args.length == 0) {
				if (s instanceof Player) {
					Loader.sendMessages(s, "Inventory.Workbench.You");
					((Player) s).openWorkbench(((Player) s).getLocation(), true);
					return true;
				}
				return true;
			}
			if (args.length == 1) {
				if(Loader.has(s, "Workbench", "Inventory", "Other")) {
					Player t = TheAPI.getPlayer(args[0]);
					if (t == null) {
						Loader.notOnline(s, args[0]);
						return true;
					}
					Loader.sendMessages(s, "Inventory.Workbench.Other.Sender", Placeholder.c().add("%player%", t.getName()).add("%playername%", t.getDisplayName()));
					Loader.sendMessages(t, "Inventory.Workbench.Other.Target", Placeholder.c().add("%player%", s.getName()).add("%playername%", s.getName()));
					t.openWorkbench(t.getLocation(), true);
					return true;
				}
				Loader.noPerms(s, "Workbench", "Inventory", "Other");
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "Workbench", "Inventory");
		return true;
	}

}

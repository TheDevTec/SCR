package me.devtec.servercontrolreloaded.commands.inventory;

import java.util.Arrays;
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

public class Invsee implements CommandExecutor, TabCompleter {

	//TODO - Custom invsee (Bukkit is buggy)
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Invsee", "Inventory")) {
			if(!CommandsManager.canUse("Inventory.Invsee", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Inventory.Invsee", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "InvSee", "Inventory");
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p == null) {
						Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
								.add("%player%", args[0])
								.add("%playername%", args[0]));
						return true;
					}
					Loader.sendMessages(s, "Inventory.Invsee.You", Placeholder.c()
							.add("%player%", p.getName()));
					((Player)s).openInventory(p.getInventory());
					return true;
				}
				Loader.Help(s, "InvSee", "Inventory");
				return true;
			}
			Player p = TheAPI.getPlayer(args[0]);
			Player t = TheAPI.getPlayer(args[1]);
			if (p == null) {
				Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
						.add("%player%", args[0])
						.add("%playername%", args[0]));
				return true;
			}
			if (t == null) {
				Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
						.add("%player%", args[1])
						.add("%playername%", args[1]));
				return true;
			}
			Loader.sendMessages(s, "Inventory.Invsee.Other.Sender", Placeholder.c()
					.add("%player%", p.getName())
					.add("%target%", t.getName()));
			Loader.sendMessages(p, "Inventory.Invsee.Other.Receiver", Placeholder.c()
					.add("%player%", p.getName()));
			t.openInventory(p.getInventory());
			return true;
		}
		Loader.noPerms(s, "Invsee", "Inventory");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Invsee", "Inventory")) {
			if(args.length==1)
				return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
			if(args.length==2)
				return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
		}
		return Collections.emptyList();
	}
}

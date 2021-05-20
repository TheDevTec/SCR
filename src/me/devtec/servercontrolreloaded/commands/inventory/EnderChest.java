package me.devtec.servercontrolreloaded.commands.inventory;

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
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class EnderChest implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "EnderChest", "Inventory")) {
			if(!CommandsManager.canUse("Inventory.EnderChest", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Inventory.EnderChest", s))));
				return true;
			}
			if (s instanceof Player) {
				if (args.length == 0) {
					Loader.sendMessages(s, "Inventory.EnderChest.You");
					((Player)s).openInventory(((Player)s).getEnderChest());
					return true;
				}
				if (args.length == 1) {
					if(Loader.has(s, "EnderChest", "Inventory", "Other")) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p == null) {
						Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
								.add("%player%", args[0])
								.add("%playername%", args[0]));
						return true;
					}
					Loader.sendMessages(s, "Inventory.EnderChest.You", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					((Player)s).openInventory(p.getEnderChest());
					return true;
					}
					Loader.noPerms(s, "EnderChest", "Inventory", "Other");
					return true;
				}
			}
			if (Loader.has(s, "EnderChest", "Inventory", "OpenOther")) {
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
				Loader.sendMessages(s, "Inventory.EnderChest.Other.Sender", Placeholder.c()
						.add("%player%", p.getName())
						.add("%playername%", p.getDisplayName()));
				t.openInventory(p.getEnderChest());
				return true;
			}
			Loader.noPerms(s, "EnderChest", "Inventory", "OpenOther");
			return true;
		}
		Loader.noPerms(s, "EnderChest", "Inventory");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "EnderChest", "Inventory")) {
		if(args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		if(args.length==2 && Loader.has(s, "EnderChest", "Inventory", "OpenOther"))
			return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
		}
		return Arrays.asList();
	}
}
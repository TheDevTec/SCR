package me.devtec.servercontrolreloaded.commands.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.Repeat;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class CloseInventory implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "CloseInventory", "Inventory")) {
			if (args.length == 0) {
				Loader.Help(s, "CloseInventory", "Inventory");
				return true;
			}
			if (args.length == 1) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p == null) {
					if (args[0].equals("*")) {
						Repeat.a(s, "closeinv *");
						return true;
					}
					Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
							.add("%player%", args[0])
							.add("%playername%", args[0]));
					return true;
				}
					Loader.sendMessages(s, "Inventory.CloseInventory.Other.Sender", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					Loader.sendMessages(p, "Inventory.CloseInventory.Other.Receiver", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
				p.closeInventory();
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "CloseInventory", "Inventory");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "CloseInventory", "Inventory"))
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}

}

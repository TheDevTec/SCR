package me.DevTec.ServerControlReloaded.Commands.Inventory;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class EnderChest implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "EnderChest", "Inventory")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					Loader.sendMessages(s, "Inventory.EnderChest.You");
					((Player)s).openInventory(((Player)s).getEnderChest());
					return true;
				}
				if (args.length == 1) {
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
				if (args.length == 2) {
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
			}
			return true;
		}
		Loader.noPerms(s, "EnderChest", "Inventory");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}
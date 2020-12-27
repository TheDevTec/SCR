package me.DevTec.ServerControlReloaded.Commands.Inventory;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;

public class EnderSee implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "EnderChest", "Inventory")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					Loader.sendMessages(s, "Inventory.EnderSee.Sender");
					((Player)s).openInventory(((Player)s).getEnderChest());
					return true;
				}
				if (args.length == 1) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						if (p == s) {
							Loader.sendMessages(s, "Inventory.EnderSee.Sender");
							((Player)s).openInventory(((Player)s).getEnderChest());
							return true;
						} else {
							if (Loader.has(s, "EnderChest", "Inventory")) {
								Loader.sendMessages(s, "Inventory.EnderSee.Sender", Placeholder.c()
										.add("%player%", p.getName())
										.add("%playername%", p.getDisplayName()));
								((Player)s).openInventory(p.getEnderChest());
								return true;
							}
							return true;
						}
					}
					Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
							.add("%player%", args[0])
							.add("%playername%", args[0]));
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
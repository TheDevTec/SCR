package me.DevTec.ServerControlReloaded.Commands.Inventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class Invsee implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Invsee", "Inventory")) {
			if (args.length == 0) {
				Loader.Help(s, "/Invsee <player> <target>", "Inventory");
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
					Loader.sendMessages(s, "Inventory.InvSee.Opening.You", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					((Player)s).openInventory(p.getInventory());
					return true;
				}
				Loader.Help(s, "/Invsee <player> <target>", "Inventory");
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
				Loader.sendMessages(s, "Inventory.InvSee.Opening.Other.Sender", Placeholder.c()
						.add("%target%", t.getName())
						.add("%player%", p.getName()));
				t.openInventory(p.getInventory());
				return true;

			}
		}
		Loader.noPerms(s, "Invsee", "Inventory");
		return true;
	}

}

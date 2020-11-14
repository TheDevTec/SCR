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

public class Invsee implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Invsee", "Inventory")) {
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
				Loader.sendMessages(s, "Inventory.Invsee.Other.Sender", Placeholder.c()
						.add("%player%", p.getName())
						.add("%target%", t.getName()));
				Loader.sendMessages(p, "Inventory.Invsee.Other.Receiver", Placeholder.c()
						.add("%player%", p.getName()));
				t.openInventory(p.getInventory());
				return true;
			}
			if(args.length>=3) {
				Loader.Help(s, "InvSee", "Inventory");
				return true;
			}
		}
		Loader.noPerms(s, "Invsee", "Inventory");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

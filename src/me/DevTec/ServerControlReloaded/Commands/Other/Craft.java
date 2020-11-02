package me.DevTec.ServerControlReloaded.Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class Craft implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Workbench", "Other")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					Loader.sendMessages(s, "Inventory.Workbench.You");
					((Player) s).openWorkbench(((Player) s).getLocation(), true);
					return true;
				}
				if (args.length == 1) {
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
			}
			return true;
		}
		Loader.noPerms(s, "Workbemch", "Other");
		return true;
	}

}

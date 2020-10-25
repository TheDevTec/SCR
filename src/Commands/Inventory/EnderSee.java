package Commands.Inventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class EnderSee implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "EnderChest", "Inventory")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					Loader.sendMessages(s, "Inventory.EnderChest.Opening.You");
					((Player)s).openInventory(((Player)s).getEnderChest());
					return true;
				}
				if (args.length == 1) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						if (p == s) {
							Loader.sendMessages(s, "Inventory.EnderChest.Opening.You");
							((Player)s).openInventory(((Player)s).getEnderChest());
							return true;
						} else {
							if (Loader.has(s, "EnderChest", "Inventory")) {
								Loader.sendMessages(s, "Inventory.EnderChest.Opening.Other.Sender", Placeholder.c()
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

}
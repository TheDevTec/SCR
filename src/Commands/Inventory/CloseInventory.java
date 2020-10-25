package Commands.Inventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.Repeat;
import me.DevTec.TheAPI.TheAPI;

public class CloseInventory implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "CloseInventory", "Inventory")) {
			if (args.length == 0) {
				Loader.Help(s, "/closeinv <player> ", "Inventory");
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
					Loader.sendMessages(s, "Inventory.Closed", Placeholder.c()
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

}

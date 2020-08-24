package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class EnderSee implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.EnderChest")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Inventory.OpeningEnderChest"), s);
					((Player)s).openInventory(((Player)s).getEnderChest());
					return true;
				}
				if (args.length == 1) {
					Player p = TheAPI.getPlayer(args[0]);
					if (p != null) {
						if (p == s) {
							TheAPI.msg(Loader.s("Prefix") + Loader.s("Inventory.OpeningEnderChest"), s);
							((Player)s).openInventory(((Player)s).getEnderChest());
							return true;
						} else {
							if (API.hasPerm(s, "ServerControl.EnderSee")) {
								TheAPI.msg(Loader.s("Prefix") + Loader.s("Inventory.OpeningEnderChestOther")
										.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()),
										s);

								((Player)s).openInventory(p.getEnderChest());
								return true;
							}
							return true;
						}
					}
					TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
					return true;
				}
			}
			TheAPI.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}

		return true;
	}

}
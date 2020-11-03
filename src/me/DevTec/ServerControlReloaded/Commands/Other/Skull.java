package me.DevTec.ServerControlReloaded.Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.ItemCreatorAPI;

public class Skull implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Skull", "Other")) {
			if (args.length == 0) {
				Loader.Help(s, "Skull", "Other");
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					TheAPI.giveItem((Player) s, ItemCreatorAPI.createHead(1, "&7" + args[0] + "'s Head", args[0]));
					Loader.sendMessages(s, "Give.Skull.You", Placeholder.c().replace("%head%", args[0]));
					return true;
				}
				Loader.Help(s, "Skull", "Other");
				return true;
			}
			if (args.length == 2) {
				Player p = TheAPI.getPlayer(args[1]);
				if (p != null) {
					TheAPI.giveItem(p, ItemCreatorAPI.createHead(1, "&7" + args[0] + "'s Head", args[0]));
					Loader.sendMessages(s, "Give.Skull.Other.Sender", Placeholder.c().replace("%head%", args[0]));
					Loader.sendMessages(p, "Give.Skull.Other.Receiver", Placeholder.c().replace("%head%", args[0]));
					return true;
				}
				Loader.notOnline(s, args[1]);
				return true;
			}
		}
		Loader.noPerms(s, "Skull", "Other");
		return true;
	}

}

package me.DevTec.ServerControlReloaded.Commands.Inventory;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class ClearConfirmToggle implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "ClearInventory", "Inventory")) {
			if (args.length == 0) {
				if (s instanceof Player == false) {
					return true;
				} else {
					if (Loader.has(s, "ClearInventory", "Inventory", "Other")) {
						User d = TheAPI.getUser(s.getName());
						if (d.getBoolean("ClearInvConfirm") == true) {
							d.setAndSave("ClearInvConfirm", false);
							Loader.sendMessages(s, "ClearInventory.Confirm.Enabled");
							return true;
						} else {
							d.setAndSave("ClearInvConfirm", true);
							Loader.sendMessages(s, "ClearInventory.Confirm.Disabled");
							return true;
						}
					}
					return true;
				}
			}
		}
		Loader.noPerms(s, "ClearInventory", "Inventory");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

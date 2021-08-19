package me.devtec.servercontrolreloaded.commands.inventory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class ClearConfirmToggle implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "ClearConfirmToggle", "Inventory")) {
			if(!CommandsManager.canUse("Inventory.ClearConfirmToggle", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Inventory.ClearConfirmToggle", s))));
				return true;
			}
			if (args.length == 0) {
				if (!(s instanceof Player))
					return true;
				User d = TheAPI.getUser(s.getName());
				if (d.getBoolean("ClearInvConfirm")) {
					d.setAndSave("ClearInvConfirm", false);
					Loader.sendMessages(s, "Inventory.ClearConfirmToggle.Enabled");
					return true;
				}
				d.setAndSave("ClearInvConfirm", true);
				Loader.sendMessages(s, "Inventory.ClearConfirmToggle.Disabled");
				return true;
			}
			if (Loader.has(s, "ClearConfirmToggle", "Inventory", "Other")) {
				User d = TheAPI.getUser(args[0]);
				if (d.getBoolean("ClearInvConfirm")) {
					d.setAndSave("ClearInvConfirm", false);
					Loader.sendMessages(s, "Inventory.ClearConfirmToggle.Enabled");
					return true;
				} else {
					d.setAndSave("ClearInvConfirm", true);
					Loader.sendMessages(s, "Inventory.ClearConfirmToggle.Disabled");
					return true;
				}
			}
			Loader.noPerms(s, "ClearConfirmToggle", "Inventory", "Other");
			return true;
		}
		Loader.noPerms(s, "ClearConfirmToggle", "Inventory");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Collections.emptyList();
	}
}

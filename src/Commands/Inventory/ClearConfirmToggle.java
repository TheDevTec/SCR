package Commands.Inventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class ClearConfirmToggle implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "ClearInventory", "Inventory")) {
			if (args.length == 0) {
				if (s instanceof Player == false) {
					return true;
				} else {
					if (Loader.has(s, "ClearInventory", "Inventory", "Clear")) {
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
		return true;
	}
}

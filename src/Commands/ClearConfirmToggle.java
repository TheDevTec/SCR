package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class ClearConfirmToggle implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.ClearInventory")) {
			if (args.length == 0) {
				if (s instanceof Player == false) {
					TheAPI.msg(Loader.s("Prefix") + Loader.s("ConsoleErrorMessage"), s);
					return true;
				} else {
					if (API.hasPerm(s, "ServerControl.ClearInv.Clear")) {
						User d = TheAPI.getUser(s.getName());
						if (d.getBoolean("ClearInvConfirm") == true) {
							d.setAndSave("ClearInvConfirm", false);
							TheAPI.msg(Loader.s("Prefix") + Loader.s("ClearInventory.ConfirmEnabled"), s);
							return true;
						} else {
							d.setAndSave("ClearInvConfirm", true);
							TheAPI.msg(Loader.s("Prefix") + Loader.s("ClearInventory.ConfirmDisabled"), s);
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

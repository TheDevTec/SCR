package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI;

public class Maintenance implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
			if (API.hasPerm(s, "ServerControl.Maintenance")) {
				if (setting.lock_server) {
					Loader.config.set("Options.Maintenance.Enabled", false);
					Loader.config.save();
					setting.lock_server = false;
					TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bMaintenance is Disabled &e-----------------",
							s);
					TheAPI.msg("", s);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("MaintenanceMode.TurnOff"), s);
					return true;
				}

				Loader.config.set("Options.Maintenance.Enabled", true);
				Loader.config.save();
				setting.lock_server = true;
				TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bMaintenance is Enabled &e-----------------", s);
				TheAPI.msg("", s);
				TheAPI.msg(Loader.s("Prefix") + Loader.s("MaintenanceMode.TurnOn"), s);
				return true;
			}
			return true;
	}
}

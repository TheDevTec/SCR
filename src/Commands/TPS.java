package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class TPS implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (API.hasPerm(s, "ServerControl.TPS")) {
			TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bTPS &e-----------------", s);
			TheAPI.msg("", s);
			TheAPI.msg(Loader.s("Prefix") + Loader.s("TPS").replace("%tps%", TheAPI.getServerTPS() + ""), s);
			return true;

		}
		return true;
	}

}

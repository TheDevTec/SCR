package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class TPS implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (API.hasPerm(s, "ServerControl.TPS")) {
			Loader.msg(Loader.s("Prefix") + "&e----------------- &bTPS &e-----------------", s);
			Loader.msg("", s);
			Loader.msg(Loader.s("Prefix") + Loader.s("TPS").replace("%tps%", TheAPI.getServerTPS() + ""), s);
			return true;

		}
		return true;
	}

}

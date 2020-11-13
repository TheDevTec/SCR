package me.DevTec.ServerControlReloaded.Commands.Info;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.TheAPI.TPSType;

public class TPS implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (Loader.has(s, "TPS", "Info")) {
			if (args.length == 0) {
			Loader.sendMessages(s, "TPS", Placeholder.c()
					.add("%tps%", TheAPI.getServerTPS() + "")
					.add("%tps-from-1%", TheAPI.getServerTPS(TPSType.ONE_MINUTE) + "")
					.add("%tps-from-5%", TheAPI.getServerTPS(TPSType.FIVE_MINUTES) + "")
					.add("%tps-from-15%", TheAPI.getServerTPS(TPSType.FIFTEEN_MINUTES) + ""));
				return true;
			}
		}
		Loader.noPerms(s, "TPS", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

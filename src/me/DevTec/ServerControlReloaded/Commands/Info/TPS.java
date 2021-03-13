package me.DevTec.ServerControlReloaded.Commands.Info;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.TPSType;
import me.devtec.theapi.utils.StringUtils;

public class TPS implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (Loader.has(s, "TPS", "Info")) {
			if (args.length == 0) {
			Loader.sendMessages(s, "TPS", Placeholder.c()
					.add("%tps%", StringUtils.fixedFormatDouble(TheAPI.getServerTPS()))
					.add("%tps-from-1%", StringUtils.fixedFormatDouble(TheAPI.getServerTPS(TPSType.ONE_MINUTE)))
					.add("%tps-from-5%", StringUtils.fixedFormatDouble(TheAPI.getServerTPS(TPSType.FIVE_MINUTES)))
					.add("%tps-from-15%", StringUtils.fixedFormatDouble(TheAPI.getServerTPS(TPSType.FIFTEEN_MINUTES))));
				return true;
			}
		}
		Loader.noPerms(s, "TPS", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Arrays.asList();
	}
}

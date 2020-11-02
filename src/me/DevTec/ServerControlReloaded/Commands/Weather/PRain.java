package me.DevTec.ServerControlReloaded.Commands.Weather;

import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class PRain implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "PRain", "Weather")) {
					((Player) s).setPlayerWeather(WeatherType.DOWNFALL);
					Loader.sendMessages(s, "Weather.PRain", Placeholder.c()
							.add("%player%", s.getName()).add("%player%", ((Player) s).getDisplayName()));
					return true;
				}
				Loader.noPerms(s, "PRain", "Weather");
				return true;
			}
			Loader.Help(s, "PRain", "Weather");
			return true;
		}
		if (Loader.has(s, "PRain", "Weather")) {
			if (TheAPI.getPlayer(args[0]) != null) {
				TheAPI.getPlayer(args[0]).setPlayerWeather(WeatherType.DOWNFALL);
				Loader.sendMessages(s, "Weather.PRain", Placeholder.c()
						.add("%player%", TheAPI.getPlayer(args[0]).getName())
						.add("%playername%", TheAPI.getPlayer(args[0]).getDisplayName()));
				return true;
			}
			Loader.notOnline(s, args[0]);
			return true;
		}
		Loader.noPerms(s, "PRain", "Weather");
		return true;
	}

}

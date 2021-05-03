package me.devtec.servercontrolreloaded.commands.weather;

import java.util.Arrays;
import java.util.List;

import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class PRain implements CommandExecutor, TabCompleter {

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
		if (Loader.has(s, "PRain", "Weather","Other")) {
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
		Loader.noPerms(s, "PRain", "Weather","Other");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (args.length == 1)
			if (Loader.has(s, "PlayerRain", "Weather", "Other"))
				return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

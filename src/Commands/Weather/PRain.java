package Commands.Weather;

import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class PRain implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "PlayerWeather", "Weather")) {
					((Player) s).setPlayerWeather(WeatherType.DOWNFALL);
					Loader.sendMessages(s, "Weather.Rain", Placeholder.c()
							.add("%world%", ((Player) s).getLocation().getWorld().getName()));
					return true;
				}
				return true;
			}
			Loader.Help(s, "/Rain <world>", "Weather");
			return true;
		}
		if (args.length == 1) {
			if (Loader.has(s, "PlayerWeather", "Weather")) {
				if (TheAPI.getPlayer(args[0]) != null) {
					TheAPI.getPlayer(args[0]).setPlayerWeather(WeatherType.DOWNFALL);
					Loader.sendMessages(s, "Weather.Rain", Placeholder.c()
							.add("%world%", args[0]));
					return true;
				}
				Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
						.add("%player%", args[0]));
				return true;
			}
			return true;
		}
		return false;
	}

}

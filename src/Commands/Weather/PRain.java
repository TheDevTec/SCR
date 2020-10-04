package Commands.Weather;

import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class PRain implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				if (API.hasPerm(s, "ServerControl.PlayerWeather")) {
					((Player) s).setPlayerWeather(WeatherType.DOWNFALL);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Weather.Rain").replace("%world%",
							((Player) s).getLocation().getWorld().getName()), s);
					return true;
				}
				return true;
			}
			Loader.Help(s, "/Rain <world>", "Weather");
			return true;
		}
		if (args.length == 1) {
			if (API.hasPerm(s, "ServerControl.PlayerWeather")) {
				if (TheAPI.getPlayer(args[0]) != null) {
					TheAPI.getPlayer(args[0]).setPlayerWeather(WeatherType.DOWNFALL);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Weather.Rain").replace("%world%", args[0]), s);
					return true;
				}
				TheAPI.msg(Loader.s("Prefix") + Loader.PlayerNotOnline(args[0]).replace("%world%", args[0]), s);
				return true;
			}
			return true;
		}
		return false;
	}

}

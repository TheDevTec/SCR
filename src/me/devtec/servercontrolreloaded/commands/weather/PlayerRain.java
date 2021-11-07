package me.devtec.servercontrolreloaded.commands.weather;


import java.util.Collections;
import java.util.List;

import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandHolder;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;

public class PlayerRain extends CommandHolder {

	public PlayerRain(String section, String name) {
		super(section, name);
	}

	@Override
	public List<String> tabCompleter(CommandSender s, String[] args) {
		if (args.length == 1)
			return null;
		return Collections.emptyList();
	}

	@Override
	public void command(CommandSender s, String[] args, boolean loop) {
		if (args.length == 0) {
			if (s instanceof Player) {
				apply((Player) s);
				Loader.sendMessages(s, "Weather.PRain");
				return;
			}
			help(s);
			return;
		}
		if (Loader.has(s, "PlayerRain", "Weather","Other")) {
			if(loop) {
				for(Player player : TheAPI.getOnlinePlayers()) {
					apply(player);
					Loader.sendMessages(s, player, "Weather.PRain");
				}
			}else {
				Player player = TheAPI.getPlayer(args[0]);
				if (player != null) {
					apply(player);
					Loader.sendMessages(s, player, "Weather.PRain");
					return;
				}
				Loader.notOnline(s, args[0]);
			}
			return;
		}
		Loader.noPerms(s, "PlayerRain", "Weather","Other");
	}
	
	public static void apply(Player player) {
		player.setPlayerWeather(WeatherType.DOWNFALL);
	}

	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?new int[]{1}:null;
	}
}
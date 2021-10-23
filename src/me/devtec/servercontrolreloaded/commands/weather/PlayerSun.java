package me.devtec.servercontrolreloaded.commands.weather;


import java.util.Collections;
import java.util.List;

import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandHolder;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;

public class PlayerSun extends CommandHolder {

	public PlayerSun(String section, String name) {
		super(section, name);
	}

	@Override
	public List<String> tabCompleter(CommandSender s, String[] args) {
		if (args.length == 1)
			return null;
		return Collections.emptyList();
	}

	@Override
	public void command(CommandSender s, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				apply((Player) s);
				Loader.sendMessages(s, "Weather.PSun", Placeholder.c().add("%player%", ((Player)s).getName()).add("%playername%", ((Player)s).getDisplayName()));
				return;
			}
			help(s);
			return;
		}
		if (Loader.has(s, "PlayerSun", "Weather","Other")) {
			Player player = TheAPI.getPlayer(args[0]);
			if (player != null) {
				apply(player);
				Loader.sendMessages(s, "Weather.PSun", Placeholder.c().add("%player%", player.getName()).add("%playername%", player.getDisplayName()));
				return;
			}
			Loader.notOnline(s, args[0]);
			return;
		}
		Loader.noPerms(s, "PlayerSun", "Weather","Other");
	}
	
	public static void apply(Player player) {
		player.setPlayerWeather(WeatherType.CLEAR);
	}
}
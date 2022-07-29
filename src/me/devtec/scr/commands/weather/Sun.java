package me.devtec.scr.commands.weather;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;

import me.devtec.scr.Loader;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.SCommand;
import me.devtec.scr.utils.Messages.Placeholder;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.shared.utility.StringUtils;

public class Sun extends SCommand {

	public Sun(String name) {
		super(name);
	}

	@Override
	public void command(User user, Command cmd, String[] args) {
		if(args.length==0) {
			if(user.isConsole()) {
				help(user, "different_world");
				return;
			}
			setSun(user.player.getWorld());
			msg(user, "Weather.Sun", Placeholder.c().replace( "%world%", user.player.getWorld().getName() ));
			return;
		}
		for(String s : args) { //sun world world2 world3
			World world = Bukkit.getWorld(s);
			if(world!=null) {
				setSun(world);
				msg(user, "Weather.Sun", Placeholder.c().replace( "%world%", world.getName() ));
				continue;
			}
			msg(user, "Missing.World", Placeholder.c().replace( "%world%", s));
			continue;
		}
	}

	@Override
	public List<String> tabCompleter(User user, Command cmd, String[] args) {
		if (args.length >= 1)
			return StringUtils.copyPartialMatches(args[(args.length-1)], API.worldNames());
		return Collections.emptyList();
	}

	
	public static void setSun(World world) {
		world.setStorm(false);
		world.setThundering(false);
		world.setWeatherDuration((300+Loader.random.nextInt(600)) * 50);
	}
}

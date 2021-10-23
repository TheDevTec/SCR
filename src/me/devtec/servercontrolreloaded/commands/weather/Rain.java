package me.devtec.servercontrolreloaded.commands.weather;


import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandHolder;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;

public class Rain extends CommandHolder {

	public Rain(String section, String name) {
		super(section, name);
	}

	@Override
	public List<String> tabCompleter(CommandSender s, String[] args) {
		if (args.length == 1)
			return StringUtils.copyPartialMatches(args[0], API.worldNames());
		return Collections.emptyList();
	}

	@Override
	public void command(CommandSender s, String[] args, boolean loop) {
		if (args.length == 0) {
			if (s instanceof Player) {
				apply(((Player) s).getWorld());
				Loader.sendMessages(s, "Weather.Rain", Placeholder.c().add("%world%", ((Player) s).getWorld().getName()));
				return;
			}
			help(s);
			return;
		}
		World world = Bukkit.getWorld(args[0]);
		if (world != null) {
			apply(world);
			Loader.sendMessages(s, "Weather.Rain", Placeholder.c().add("%world%", world.getName()));
			return;
		}
		Loader.sendMessages(s, "Missing.World", Placeholder.c().add("%world%", args[0]));
	}

	static Random random = new Random();
	
	public static void apply(World world) {
		world.setStorm(true);
		world.setThundering(false);
		world.setWeatherDuration((300+random.nextInt(600)) * 50);
	}

	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
}
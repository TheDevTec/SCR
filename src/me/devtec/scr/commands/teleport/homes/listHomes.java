package me.devtec.scr.commands.teleport.homes;

import java.util.Set;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.utils.PlaceholderBuilder;

/**
 * @author StraikerinaCZ
 * 30.12. 2021
 **/
public class listHomes extends CommandHolder {
	public listHomes(String command) {
		super(command, -1);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		if(args.length==0) {
			Set<String> homes = Home.getHomes(sender.getName());
			if(homes.isEmpty()) {
				Loader.send(sender, "teleport.home.list-empty", PlaceholderBuilder.make(sender, "sender"));
				return;
			}
			Loader.send(sender, "teleport.home.list", PlaceholderBuilder.make(sender, "sender").add("%homes%", ""+homes.size()));
			int id = 1;
			for(String home : homes)
				Loader.send(sender, "teleport.home.list-home", PlaceholderBuilder.make(sender, "sender").add("%home%", home).add("%id%", (id++)+""));
			return;
		}
		String player = args[0];
		Set<String> homes = Home.getHomes(player);
		if(homes.isEmpty()) {
			Loader.send(sender, "teleport.home.list-empty", PlaceholderBuilder.make(sender, "sender"));
			return;
		}
		Loader.send(sender, "teleport.home.list-other", PlaceholderBuilder.make(sender, "sender").add("%homes%", ""+homes.size()).add("%target%", player));
		int id = 1;
		for(String home : homes)
			Loader.send(sender, "teleport.home.list-home", PlaceholderBuilder.make(sender, "sender").add("%home%", home).add("%id%", (id++)+""));
		return;
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}
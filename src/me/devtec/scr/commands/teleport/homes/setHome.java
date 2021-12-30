package me.devtec.scr.commands.teleport.homes;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.TheAPI;

/**
 * @author StraikerinaCZ
 * 30.12. 2021
 **/
public class setHome extends CommandHolder {
	public setHome(String command) {
		super(command, -1);
	}
	
	public static boolean existsHome(String player, String home){
		for(String foundHome : Home.getHomes(player))
			if(foundHome.equalsIgnoreCase(home)) {
				return true;
			}
		return false;
	}
	
	public static void set(String player, String home, Location where){
		TheAPI.getUser(player).setAndSave("home."+home, where);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		if(args.length==0) {
			set(sender.getName(), "main", ((Player)sender).getLocation());
			if(!silent)
				Loader.send(sender, "teleport.home.created", PlaceholderBuilder.make(sender, "sender").add("%home%", "main"));
			return;
		}
		if(existsHome(sender.getName(), args[0])) {
			if(!silent)
				Loader.send(sender, "teleport.home.already-exists", PlaceholderBuilder.make(sender, "sender").add("%home%", args[0]));
			return;
		}
		set(sender.getName(), args[0], ((Player)sender).getLocation());
		if(!silent)
			Loader.send(sender, "teleport.home.created", PlaceholderBuilder.make(sender, "sender").add("%home%", args[0]));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}
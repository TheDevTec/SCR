package me.devtec.scr.commands.teleport.homes;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.TheAPI;

/**
 * @author StraikerinaCZ
 * 30.12. 2021
 **/
public class delHome extends CommandHolder {
	public delHome(String command) {
		super(command, 0);
	}
	
	public static String getCorrectName(String player, String home){
		for(String found : Home.getHomes(player))
			if(found.equalsIgnoreCase(home)) {
				return found;
			}
		return null;
	}
	
	public static void remove(String player, String home){
		TheAPI.getUser(player).setAndSave("home."+home, null);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		String name = getCorrectName(sender.getName(), args[0]);
		if(name==null) {
			if(!silent)
				Loader.send(sender, "teleport.home.not-exists", PlaceholderBuilder.make(sender, "sender").add("%home%", args[0]));
			return;
		}
		remove(sender.getName(), name);
		if(!silent)
			Loader.send(sender, "teleport.home.removed", PlaceholderBuilder.make(sender, "sender").add("%home%", name));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}
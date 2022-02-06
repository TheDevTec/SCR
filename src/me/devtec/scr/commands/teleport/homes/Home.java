package me.devtec.scr.commands.teleport.homes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.commands.teleport.spawn.Spawn;
import me.devtec.scr.utils.PlaceholderBuilder;
import me.devtec.theapi.TheAPI;

/**
 * @author StraikerinaCZ
 * 30.12. 2021
 **/
public class Home extends CommandHolder {
	public Home(String command) {
		super(command, -1);
	}
	
	public static Set<String> getHomes(String player){
		return TheAPI.getUser(player).getKeys("home");
	}
	
	public static Location getHome(String player, String home){
		return (Location)TheAPI.getUser(player).get("home."+home);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		if(args.length==0) {
			String home;
			Set<String> homes = getHomes(sender.getName());
			if(homes.isEmpty())home=null;
			else {
				if(homes.contains("main"))home="main";
				else
				home=homes.iterator().next();
			}
			if(home==null) {
				((Player)sender).teleport(Spawn.spawn);
				if(!silent)
					Loader.send(sender, "teleport.home.not-exist-tp-spawn", PlaceholderBuilder.make(sender, "sender").add("%home%", "main"));
				return;
			}
			((Player)sender).teleport(getHome(sender.getName(), home));
			if(!silent)
				Loader.send(sender, "teleport.home.own", PlaceholderBuilder.make(sender, "sender").add("%home%", home));
			return;
		}
		String home = delHome.getCorrectName(sender.getName(), args[0]);
		if(home==null) {
			if(!silent)
				Loader.send(sender, "teleport.home.not-exist", PlaceholderBuilder.make(sender, "sender").add("%home%", args[0]));
			return;
		}
		((Player)sender).teleport(getHome(sender.getName(), home));
		if(!silent)
			Loader.send(sender, "teleport.home.own", PlaceholderBuilder.make(sender, "sender").add("%home%", home));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
	@Override
	public List<String> tabValues(CommandSender sender, String[] args, String value) {
		if(value.equalsIgnoreCase("{homes}")) {
			return new ArrayList<>(Home.getHomes(sender.getName()));
		}
		return super.tabValues(sender, args, value);
	}
	
}
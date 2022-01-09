package me.devtec.scr.commands.teleport.homes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;

/**
 * @author StraikerinaCZ
 * 30.12. 2021
 **/
public class otherHome extends CommandHolder {
	// otherHome PLAYER HOME [TARGET]
	public otherHome(String command) {
		super(command, 1);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		String target = args[0];
		String home = delHome.getCorrectName(target, args[1]);
		if(home==null) {
			if(!silent)
				Loader.send(sender, "teleport.home.not-exist", PlaceholderBuilder.make(sender, "sender").add("%home%", args[1]));
			return;
		}
		if(args.length==2) {
			((Player)sender).teleport(Home.getHome(sender.getName(), home));
			if(!silent)
				Loader.send(sender, "teleport.home.other", PlaceholderBuilder.make(sender, "sender").add("%home%", home).add("%target%", target));
			return;
		}
		Player player;
		if((player=requireOnline(sender, args[2]))==null)return;
		player.teleport(Home.getHome(sender.getName(), home));
		if(!silent)
			Loader.send(sender, "teleport.home.other-tp-other", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("%home%", home).add("%target%", target));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
	@Override
	public List<String> tabValues(CommandSender sender, String[] args, String value) {
		if(value.equalsIgnoreCase("{homes}") && args.length>1) {
			return new ArrayList<>(Home.getHomes(args[0]));
		}
		return super.tabValues(sender, args, value);
	}
}
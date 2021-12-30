package me.devtec.scr.commands.teleport.spawn;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;

/**
 * @author StraikerinaCZ
 * 28.12. 2021
 **/
public class setSpawn extends CommandHolder {
	public setSpawn(String command) {
		super(command, -1);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		Spawn.spawn=((Player)sender).getLocation();
		if(!silent)
			Loader.send(sender, "teleport.spawn.set", PlaceholderBuilder.make(sender, "sender"));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}
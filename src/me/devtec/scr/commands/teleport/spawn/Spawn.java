package me.devtec.scr.commands.teleport.spawn;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;

/**
 * @author StraikerinaCZ
 * 28.12. 2021
 **/
public class Spawn extends CommandHolder {
	public static Location spawn;
	public Spawn(String command) {
		super(command, -1);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		if(args.length==0) {
			((Player)sender).teleport(spawn);
			if(!silent) {
				Loader.send(sender, "teleport.spawn.self", PlaceholderBuilder.make(sender, "sender"));
			}
			return;
		}
		if(loop) {
			for(Player player : Loader.onlinePlayers(sender)) {
				player.teleport(spawn);
				if(!silent) {
					Loader.send(sender, "teleport.spawn.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target"));
					Loader.send(player, "teleport.spawn.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target"));
				}
			}
			return;
		}
		Player player;
		if((player=requireOnline(sender, args[0]))==null)return;
		player.teleport(spawn);
		if(!silent) {
			Loader.send(sender, "teleport.spawn.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target"));
			Loader.send(player, "teleport.spawn.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target"));
		}
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return placeholder_FIRST;
	}
	
}
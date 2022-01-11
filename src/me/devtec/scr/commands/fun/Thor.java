package me.devtec.scr.commands.fun;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.blocksapi.BlocksAPI;

/**
 * @author StraikerinaCZ
 * 10.1 2022
 **/
public class Thor extends CommandHolder {
	
	public Thor(String command) {
		super(command, -1);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		if(args.length==0) {
			Block b = BlocksAPI.getLookingBlock((Player)s, 100);
			b.getWorld().strikeLightning(b.getLocation());
			if(!silent)
				Loader.send(s, "fun.thor.location", PlaceholderBuilder.make(s, "sender").add("x", b.getX()+"")
						.add("y", b.getY()+"").add("z", b.getZ()+"").add("world", b.getWorld().getName()));
			return;
		}
		if(loop) {
			for(Player player : Loader.onlinePlayers(s)) {
				player.getWorld().strikeLightning(player.getLocation());
				if(player==s && !silent) {
					Loader.send(s, "fun.thor.self", PlaceholderBuilder.make(s, "sender"));
				}
				if(!silent) {
					Loader.send(s, "fun.thor.sender", PlaceholderBuilder.make(s, "sender").player(player, "target"));
					Loader.send(player, "fun.thor.target", PlaceholderBuilder.make(s, "sender").player(player, "target"));
				}
			}
			return;
		}
		Player player;
		if((player=requireOnline(s, args[0]))==null)return;
		player.getWorld().strikeLightning(player.getLocation());
		if(player==s && !silent) {
			Loader.send(s, "fun.thor.self", PlaceholderBuilder.make(s, "sender"));
		}
		if(!silent) {
			Loader.send(s, "fun.thor.sender", PlaceholderBuilder.make(s, "sender").player(player, "target"));
			Loader.send(player, "fun.thor.target", PlaceholderBuilder.make(s, "sender").player(player, "target"));
		}
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}
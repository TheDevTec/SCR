package me.devtec.scr.commands.teleport.warps;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.commands.teleport.warps.WarpManager.Instance;
import me.devtec.theapi.economyapi.EconomyAPI;

/**
 * @author StraikerinaCZ
 * 30.12. 2021
 **/
public class Warp extends CommandHolder {
	public Warp(String command) {
		super(command, -1);
	}
	
	public static Set<Instance> getAvailableWarps(CommandSender sender) {
		Set<Instance> available = new HashSet<>();
		for(Instance warp : WarpManager.values())
			if((!warp.perms||warp.perms && sender.hasPermission("scr.command.warp.perm."+warp.name)) && EconomyAPI.has(sender.getName(), warp.cost))
				available.add(warp);
		return available;
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		if(args.length==0) {
			Set<Instance> available = getAvailableWarps(sender);
			Loader.send(sender, "teleport.warp.available-warps", PlaceholderBuilder.make(sender, "sender").add("%warps%", available.size()));
			int id = 1;
			for(Instance warp : available)
				Loader.send(sender, "teleport.warp.available-warp-format", PlaceholderBuilder.make(sender, "sender").add("%id%", (id++)+"").add("%warp%", warp.name).add("%cost%", warp.cost).add("%perms%", warp.perms));
			return;
		}
		Instance warp = WarpManager.getFrom(args[0], getAvailableWarps(sender));
		if(warp==null) {
			if(!silent)
				Loader.send(sender, "teleport.warp.not-exist", PlaceholderBuilder.make(sender, "sender").add("%warp%", args[0]));
			return;
		}
		Location where = warp.spawn;
		if(args.length==1) {
			((Player)sender).teleport(where);
			if(!silent)
				Loader.send(sender, "teleport.warp.self", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp.name));
			return;
		}
		if(loop) {
			for(Player player : Loader.onlinePlayers(sender)) {
				player.teleport(where);
				if(!silent) {
					Loader.send(sender, "teleport.warp.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("%warp%", warp.name));
					Loader.send(player, "teleport.warp.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("%warp%", warp.name));
				}
			}
			return;
		}
		Player player;
		if((player=requireOnline(sender, args[1]))==null)return;
		player.teleport(where);
		if(!silent) {
			Loader.send(sender, "teleport.warp.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("%warp%", warp.name));
			Loader.send(player, "teleport.warp.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("%warp%", warp.name));
		}
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=2?placeholder_TWO:null;
	}
	
}
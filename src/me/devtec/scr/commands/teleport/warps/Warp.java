package me.devtec.scr.commands.teleport.warps;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Formatters;
import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.economyapi.EconomyAPI;

/**
 * @author StraikerinaCZ
 * 30.12. 2021
 **/
public class Warp extends CommandHolder {
	public Warp(String command) {
		super(command, 0);
	}
	
	public static Set<String> getWarps() {
		return ConfigManager.data.getKeys("warp");
	}
	
	public static Set<String> getAvailableWarps(CommandSender sender) {
		Set<String> available = new HashSet<>();
		for(String warp : getWarps()) {
			if(ConfigManager.data.getBoolean("warp."+warp+".perm") && sender.hasPermission("scr.command.warp.perm."+warp)) {
				if(EconomyAPI.has(sender.getName(), ConfigManager.data.getDouble("warp."+warp+".cost")))
					available.add(warp);
			}else {
				if(!ConfigManager.data.getBoolean("warp."+warp+".perm") && EconomyAPI.has(sender.getName(), ConfigManager.data.getDouble("warp."+warp+".cost")))
					available.add(warp);
			}
		}
		return available;
	}
	
	public static String getCorrectName(String warp, Set<String> available) {
		for(String found : available)
			if(found.equalsIgnoreCase(warp)) {
				return found;
			}
		return null;
	}
	
	public static Location getWarp(String warp){
		return Formatters.locFromString(ConfigManager.data.getString("warp."+warp+".loc"));
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		String warp = getCorrectName(args[0], getAvailableWarps(sender));
		if(warp==null) {
			if(!silent)
				Loader.send(sender, "teleport.warp.not-exist", PlaceholderBuilder.make(sender, "sender").add("%warp%", args[0]));
			return;
		}
		Location where = getWarp(warp);
		if(args.length==1) {
			((Player)sender).teleport(where);
			if(!silent)
				Loader.send(sender, "teleport.warp.self", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp));
			return;
		}
		if(loop) {
			for(Player player : Loader.onlinePlayers(sender)) {
				player.teleport(where);
				if(!silent) {
					Loader.send(sender, "teleport.warp.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("%warp%", warp));
					Loader.send(player, "teleport.warp.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("%warp%", warp));
				}
			}
			return;
		}
		Player player;
		if((player=requireOnline(sender, args[0]))==null)return;
		player.teleport(where);
		if(!silent) {
			Loader.send(sender, "teleport.warp.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("%warp%", warp));
			Loader.send(player, "teleport.warp.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("%warp%", warp));
		}
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}
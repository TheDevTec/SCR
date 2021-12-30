package me.devtec.scr.commands.teleport.warps;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.commands.teleport.warps.WarpManager.Instance;

/**
 * @author StraikerinaCZ
 * 31.12. 2021
 **/
public class setWarp extends CommandHolder {
	public setWarp(String command) { //setWarp NAME
		super(command, 0);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		Instance warp = WarpManager.getFrom(args[0], Warp.getAvailableWarps(sender));
		if(warp!=null) {
			if(!silent)
				Loader.send(sender, "teleport.warp.already-exists", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp.name));
			return;
		}
		warp=WarpManager.create(args[0]);
		warp.spawn=((Player)sender).getLocation();
		if(!silent)
			Loader.send(sender, "teleport.warp.created", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp.name));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}
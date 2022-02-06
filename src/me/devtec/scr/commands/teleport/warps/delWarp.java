package me.devtec.scr.commands.teleport.warps;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.commands.teleport.warps.WarpManager.Instance;
import me.devtec.scr.utils.PlaceholderBuilder;

/**
 * @author StraikerinaCZ
 * 31.12. 2021
 **/
public class delWarp extends CommandHolder {
	public delWarp(String command) { //delWarp NAME
		super(command, 0);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		Instance warp = WarpManager.getFrom(args[0], Warp.getAvailableWarps(sender));
		if(warp==null) {
			if(!silent)
				Loader.send(sender, "teleport.warp.not-exist", PlaceholderBuilder.make(sender, "sender").add("%warp%", args[0]));
			return;
		}
		warp=WarpManager.delete(args[0]);
		if(!silent)
			Loader.send(sender, "teleport.warp.deleted", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp.name));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}
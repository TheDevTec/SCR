package me.devtec.scr.commands.teleport.warps;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.commands.teleport.warps.WarpManager.Instance;
import me.devtec.scr.utils.PlaceholderBuilder;
import me.devtec.theapi.utils.StringUtils;

/**
 * @author StraikerinaCZ
 * 31.12. 2021
 **/
public class WarpEditor extends CommandHolder {
	public WarpEditor(String command) { //warpEditor [WARP] cost/perms/spawn [value]
		super(command, 1);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		Instance warp = WarpManager.getFrom(args[0], Warp.getAvailableWarps(sender));
		if(warp==null) {
			Loader.send(sender, "teleport.warp.not-exist", PlaceholderBuilder.make(sender, "sender").add("%warp%", args[0]));
			return;
		}
		if(args[1].equalsIgnoreCase("spawn")) {
			warp.spawn=((Player)sender).getLocation();
			Loader.send(sender, "warpeditor.set.spawn", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp.name));
			return;
		}
		if(args.length==2) {
			if(args[1].equalsIgnoreCase("cost")) {
				Loader.send(sender, "warpeditor.get.cost", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp.name).add("%value%", warp.cost));
				return;
			}
			if(args[1].equalsIgnoreCase("perms")) {
				Loader.send(sender, "warpeditor.get.perms", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp.name).add("%value%", warp.perms));
				return;
			}
			help(sender, 0);
			return;
		}
		if(args[1].equalsIgnoreCase("cost")) {
			warp.cost=StringUtils.getDouble(args[2]);
			Loader.send(sender, "warpeditor.set.cost", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp.name).add("%value%", warp.cost));
			return;
		}
		if(args[1].equalsIgnoreCase("perms")) {
			warp.perms=StringUtils.getBoolean(args[2]);
			Loader.send(sender, "warpeditor.set.perms", PlaceholderBuilder.make(sender, "sender").add("%warp%", warp.name).add("%value%", warp.perms));
			return;
		}
		help(sender, 0);
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}
package me.devtec.scr.commands.main;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.utils.PlaceholderBuilder;
import me.devtec.theapi.utils.StringUtils;

/**
 * @author StraikerinaCZ
 * 25.12. 2021
 **/
public class setCustomName extends CommandHolder {
	
	public setCustomName(String command) {
		super(command, 0);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		Player player;
		if((player=requireOnline(s, args[0]))==null)return;
		String name = StringUtils.colorize(StringUtils.buildString(args));
		boolean reset = name.equalsIgnoreCase("reset");
		if(reset) {
			player.setCustomName(null);
		}else
			player.setCustomName(name);
		if(!silent) {
			if(player==s) {
				Loader.send(s, "setCustomName."+(reset?"reset.self":"set.self"), PlaceholderBuilder.make(s, "sender").add("value", name));
			}else {
				Loader.send(s, "setCustomName."+(reset?"reset.other":"set.other")+".sender", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", name));
				Loader.send(player, "setCustomName."+(reset?"reset.other":"set.other")+".target", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", name));
			}
		}
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}
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
public class setAir extends CommandHolder {
	
	public setAir(String command) {
		super(command, 1);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		int setLevel = args[1].equalsIgnoreCase("max")?-1:StringUtils.getInt(args[1]);
		if(loop) {
			for(Player player : Loader.onlinePlayers(s)) {
				int level = setLevel == -1 ? player.getMaximumAir() : setLevel > player.getMaximumAir() ? player.getMaximumAir() : setLevel;
				player.setRemainingAir(level);
				if(player==s && !silent) {
					Loader.send(s, "setAir.self", PlaceholderBuilder.make(s, "sender").add("value", level+""));
				}
				if(!silent) {
					Loader.send(s, "setAir.other.sender", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", level+""));
					Loader.send(player, "setAir.other.target", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", level+""));
				}
			}
			return;
		}
		Player player;
		if((player=requireOnline(s, args[0]))==null)return;

		int level = setLevel == -1 ? player.getMaximumAir() : setLevel > player.getMaximumAir() ? player.getMaximumAir() : setLevel;
		player.setRemainingAir(level);
		if(!silent) {
			if(player==s) {
				Loader.send(s, "setAir.self", PlaceholderBuilder.make(s, "sender").add("value", level+""));
			}else {
				Loader.send(s, "setAir.other.sender", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", level+""));
				Loader.send(player, "setAir.other.target", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", level+""));
			}
		}
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return placeholder_FIRST;
	}
	
}
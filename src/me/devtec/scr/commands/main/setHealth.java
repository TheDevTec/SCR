package me.devtec.scr.commands.main;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Formatters;
import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.utils.StringUtils;

/**
 * @author StraikerinaCZ
 * 25.12. 2021
 **/
public class setHealth extends CommandHolder {
	
	public setHealth(String command) {
		super(command, 1);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		double setLevel = args[1].equalsIgnoreCase("max")?-1:StringUtils.getDouble(args[1]);
		if(loop) {
			for(Player player : Loader.onlinePlayers(s)) {
				double level = setLevel == -1 ? player.getMaxHealth() : setLevel > player.getMaxHealth() ? player.getMaxHealth() : setLevel;
				player.setHealth(level);
				if(player==s && !silent) {
					Loader.send(s, "setHealth.self", PlaceholderBuilder.make(s, "sender").add("value", Formatters.formatDouble(level)));
				}
				if(!silent) {
					Loader.send(s, "setHealth.other.sender", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", Formatters.formatDouble(level)));
					Loader.send(player, "setHealth.other.target", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", Formatters.formatDouble(level)));
				}
			}
			return;
		}
		Player player;
		if((player=requireOnline(s, args[0]))==null)return;

		double level = setLevel == -1 ? player.getMaxHealth() : setLevel > player.getMaxHealth() ? player.getMaxHealth() : setLevel;
		player.setHealth(level);
		if(!silent) {
			if(player==s) {
				Loader.send(s, "setHealth.self", PlaceholderBuilder.make(s, "sender").add("value", Formatters.formatDouble(level)));
			}else {
				Loader.send(s, "setHealth.other.sender", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", Formatters.formatDouble(level)));
				Loader.send(player, "setHealth.other.target", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", Formatters.formatDouble(level)));
			}
		}
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return placeholder_FIRST;
	}
	
}
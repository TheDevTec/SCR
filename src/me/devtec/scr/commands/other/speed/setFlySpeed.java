package me.devtec.scr.commands.other.speed;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.utils.Formatters;
import me.devtec.scr.utils.PlaceholderBuilder;
import me.devtec.theapi.utils.StringUtils;

/**
 * @author StraikerinaCZ
 * 25.12. 2021
 **/
public class setFlySpeed extends CommandHolder {
	
	public setFlySpeed(String command) {
		super(command, 0);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		if(args.length==1) {
			if(s instanceof Player) {
				double level = StringUtils.getDouble(args[0]);
				apply(s, (Player)s, level, silent);
			}
			help(s, 1);
			return;
		}
		Player player;
		if((player=requireOnline(s, args[1]))==null)return;
		double level = StringUtils.getDouble(args[1]);
		apply(s, player, level, silent);
	}
	
	private void apply(CommandSender s, Player player, double level, boolean silent) {
		if(level < 0)level=0;
		if(level > 10)level=10;
		player.setFlySpeed((float)level/10);
		if(!silent) {
			if(player==s) {
				Loader.send(s, "setFlySpeed.self", PlaceholderBuilder.make(s, "sender").add("value", Formatters.formatDouble(level)));
			}else {
				Loader.send(s, "setFlySpeed.other.sender", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", Formatters.formatDouble(level)));
				Loader.send(player, "setFlySpeed.other.target", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", Formatters.formatDouble(level)));
			}
		}
	}

	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return placeholder_FIRST;
	}
	
}
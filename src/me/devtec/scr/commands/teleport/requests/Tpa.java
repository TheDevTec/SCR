package me.devtec.scr.commands.teleport.requests;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.commands.teleport.requests.TeleportManager.TeleportType;

/**
 * @author StraikerinaCZ
 * 27.12. 2021
 **/
public class Tpa extends CommandHolder {
	
	public Tpa(String command) {
		super(command, 0);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		if(loop) {
			for(Player player : Loader.onlinePlayers(s))
				TeleportManager.request((Player)s, player, TeleportType.TPA);
			return;
		}
		Player player;
		if((player=requireOnline(s, args[0]))==null)return;
		TeleportManager.request((Player)s, player, TeleportType.TPA);
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return placeholder_FIRST;
	}
	
}
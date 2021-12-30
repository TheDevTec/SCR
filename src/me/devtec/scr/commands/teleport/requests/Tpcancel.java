package me.devtec.scr.commands.teleport.requests;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.commands.CommandHolder;

/**
 * @author StraikerinaCZ
 * 27.12. 2021
 **/
public class Tpcancel extends CommandHolder {
	
	public Tpcancel(String command) {
		super(command, -1);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		TeleportManager.cancel((Player)s, loop);
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}
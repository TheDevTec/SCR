package me.devtec.scr.commands.other;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.utils.PlaceholderBuilder;

/**
 * @author StraikerinaCZ
 * 25.12. 2021
 **/
public class setFly extends CommandHolder {
	
	public setFly(String command) {
		super(command, -1);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		switch(args.length) {
		case 0:
			if(s instanceof Player) {
				apply(s, (Player)s, !((Player)s).getAllowFlight(), silent);
			}else
				help(s, 0);
			break;
		case 1:
			if(s instanceof Player && (Loader.isPositive(args[0])||Loader.isNegative(args[0]))) {
				apply(s, (Player)s, Loader.isPositive(args[0]), silent);
				break;
			}
			Player player;
			if((player=requireOnline(s, args[0]))==null)return;
			apply(s, player, !player.getAllowFlight(), silent);
			break;
		default:
			if((player=requireOnline(s, args[0]))==null)return;
			apply(s, player, Loader.isPositive(args[1]), silent);
			break;
		}
	}
	
	private void apply(CommandSender s, Player player, boolean status, boolean silent) {
		if(status) {
			player.setAllowFlight(true);
			if(!player.isOnGround() && !player.isClimbing() && !player.isGliding())
				player.setFlying(true);
		}else {
			if(!player.isOnGround() && !player.isClimbing() && !player.isGliding())
				player.setNoDamageTicks(60); //3s
			player.setFlying(false);
			player.setAllowFlight(false);
		}
		if(!silent) {
			if(player==s) {
				Loader.send(s, "setFly.self."+(status?"on":"off"), PlaceholderBuilder.make(s, "sender"));
			}else {
				Loader.send(s, "setFly.other.sender."+(status?"on":"off"), PlaceholderBuilder.make(s, "sender").player(player, "target"));
				Loader.send(player, "setFly.other.target."+(status?"on":"off"), PlaceholderBuilder.make(s, "sender").player(player, "target"));
			}
		}
	}

	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}
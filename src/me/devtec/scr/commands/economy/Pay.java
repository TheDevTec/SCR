package me.devtec.scr.commands.economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.utils.theapiutils.Cache.Query;

/**
 * @author StraikerinaCZ
 * 16.1 2022
 **/
public class Pay extends CommandHolder {
	
	public Pay(String command) {
		super(command, 1);
	}

	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		if(args[1].startsWith("-")||args[1].equals("0")) { //fast skip
			Loader.send(s, "economy.pay.negative", PlaceholderBuilder.make(s, "sender"));
			return;
		}
		double money = Loader.moneyFromString(args[1]);
		if(money<=0) { //just.. check
			Loader.send(s, "economy.pay.negative", PlaceholderBuilder.make(s, "sender"));
			return;
		}
		if(!EconomyAPI.has((Player)s, money)) {
			Loader.send(s, "economy.pay.enought", PlaceholderBuilder.make(s, "sender").add("value", money));
			return;
		}
		Player target = TheAPI.getPlayer(args[0]);
		if(target!=null) {
			EconomyAPI.depositPlayer(target, money);
			if(!silent) {
				Loader.send(s, "economy.pay.sender", PlaceholderBuilder.make(s, "sender").player(target, "target").add("value", money));
				Loader.send(target, "economy.pay.target", PlaceholderBuilder.make(s, "sender").player(target, "target").add("value", money));
			}
			return;
		}
		Query query = TheAPI.getCache().lookupQuery(args[0]);
		if(query==null) {
			Loader.send(s, "missing.user", PlaceholderBuilder.make(s, "sender").add("value", args[0]));
			return;
		}
		EconomyAPI.depositPlayer(query.getName(), money);
		if(!silent)
			Loader.send(s, "economy.pay.sender", PlaceholderBuilder.make(s, "sender").player(target, "target").add("value", money));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}
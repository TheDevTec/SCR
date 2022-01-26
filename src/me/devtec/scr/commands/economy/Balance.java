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
 * 25.1 2022
 **/
public class Balance extends CommandHolder {
	
	public Balance(String command) {
		super(command, -1);
	}

	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		if(args.length==0) {
			if(!(s instanceof Player)) {
				help(s, 0);
				return;
			}
			Loader.send(s, "economy.balance.self", PlaceholderBuilder.make(s, "sender"));
			return;
		}
		Query find = TheAPI.getCache().lookupQuery(args[0]);
		if(find==null) {
			Loader.send(s, "missing.user", PlaceholderBuilder.make(s, "sender").add("name", args[0]));
			return;
		}
		Loader.send(s, "economy.balance.other", PlaceholderBuilder.make(s, "sender").add("name", find.getName()).add("uuid", find.getUUID().toString()).add("balance", EconomyAPI.getBalance(find.getName())));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}
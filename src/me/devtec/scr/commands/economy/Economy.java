package me.devtec.scr.commands.economy;

import org.bukkit.command.CommandSender;

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
public class Economy extends CommandHolder {
	
	public Economy(String command) {
		super(command, 2);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		if(args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("give")) {
			Query find = TheAPI.getCache().lookupQuery(args[1]);
			if(find==null) {
				Loader.send(s, "missing.user", PlaceholderBuilder.make(s, "sender").add("name", args[0]));
				return;
			}
			double money = Loader.moneyFromString(args[2]);
			EconomyAPI.depositPlayer(find.getName(), money);
			Loader.send(s, "economy.add", PlaceholderBuilder.make(s, "sender").add("name", find.getName()).add("uuid", find.getUUID().toString()).add("value", money).add("balance", EconomyAPI.getBalance(find.getName())));
			return;
		}
		if(args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("rem")||args[0].equalsIgnoreCase("take")) {
			Query find = TheAPI.getCache().lookupQuery(args[1]);
			if(find==null) {
				Loader.send(s, "missing.user", PlaceholderBuilder.make(s, "sender").add("name", args[0]));
				return;
			}
			double money = Loader.moneyFromString(args[2]);
			EconomyAPI.withdrawPlayer(find.getName(), money);
			Loader.send(s, "economy.remove", PlaceholderBuilder.make(s, "sender").add("name", find.getName()).add("uuid", find.getUUID().toString()).add("value", money).add("balance", EconomyAPI.getBalance(find.getName())));
			return;
		}
		if(args[0].equalsIgnoreCase("set")) {
			Query find = TheAPI.getCache().lookupQuery(args[1]);
			if(find==null) {
				Loader.send(s, "missing.user", PlaceholderBuilder.make(s, "sender").add("name", args[0]));
				return;
			}
			EconomyAPI.withdrawPlayer(find.getName(), EconomyAPI.getBalance(find.getName())); //reset balance
			double money = Loader.moneyFromString(args[2]);
			EconomyAPI.depositPlayer(find.getName(), money); //set new balance
			Loader.send(s, "economy.set", PlaceholderBuilder.make(s, "sender").add("name", find.getName()).add("uuid", find.getUUID().toString()).add("value", money).add("balance", money));
			return;
		}
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=2?placeholder_TWO:null;
	}
	
}
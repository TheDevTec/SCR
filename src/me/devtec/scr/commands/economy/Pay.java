package me.devtec.scr.commands.economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;
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
		Player target = TheAPI.getPlayerOrNull(args[0]);
		if(target!=null) {
			EconomyAPI.withdrawPlayer((Player)s, money);
			EconomyAPI.depositPlayer(target, money-calculateFee(s, money));
			if(!silent) {
				Loader.send(s, "economy.pay.online.sender", PlaceholderBuilder.make(s, "sender").player(target, "target").add("value", money).add("fee", calculateFee(s, money)));
				Loader.send(target, "economy.pay.online.target", PlaceholderBuilder.make(s, "sender").player(target, "target").add("value", money).add("fee", calculateFee(s, money)));
			}
			return;
		}
		Query query = TheAPI.getCache().lookupQuery(args[0]);
		if(query==null) {
			Loader.send(s, "missing.user", PlaceholderBuilder.make(s, "sender").add("value", args[0]));
			return;
		}
		EconomyAPI.withdrawPlayer((Player)s, money);
		EconomyAPI.depositPlayer(query.getName(), money-calculateFee(s, money));
		if(!silent)
			Loader.send(s, "economy.pay.offline.sender", PlaceholderBuilder.make(s, "sender").add("target_name", query.getName()).add("value", money).add("fee", calculateFee(s, money)));
	}
	
	public static double calculateFee(CommandSender to, double money) {
		if(to==null||money<=0)return 0;
		double fee = getFee(to);
		if(fee<=0)return 0;
		return StringUtils.calculate(ConfigManager.economy.getString("payment.fees.calculator").replace("%fee%", fee+"").replace("%money%", money+""));
	}
	
	public static double getFee(CommandSender s) {
		if(s==null || !(s instanceof Player))
			return 0;
		String path = "payment.fees.per-player."+s.getName();
		String group = Loader.perms!=null?Loader.perms.getPrimaryGroup((Player)s):"default";
		/*
		 * 1) players
		 */
		if(ConfigManager.economy.exists(path)) {
			//global
			String get = ConfigManager.economy.getString(path);
			if(get!=null)return StringUtils.getDouble(PlaceholderAPI.setPlaceholders((Player)s, get));
		}
		/*
		 * 2) groups
		 */
		if(ConfigManager.economy.exists(path="payment.fees.per-group."+group)) {
			//global
			String get = ConfigManager.economy.getString(path);
			if(get!=null)return StringUtils.getDouble(PlaceholderAPI.setPlaceholders((Player)s, get));
		}
		/*
		 * 3) global
		 */
		return StringUtils.getDouble(PlaceholderAPI.setPlaceholders((Player)s, ConfigManager.economy.getString("payment.fees.per-group.default")));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}
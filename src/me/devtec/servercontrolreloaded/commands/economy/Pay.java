package me.devtec.servercontrolreloaded.commands.economy;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.Repeat;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.utils.StringUtils;

public class Pay implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			return true;
		}
		if (Loader.has(s, "Pay", "Economy")) {
			if(!CommandsManager.canUse("Economy.Pay", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Economy.Pay", s))));
				return true;
			}
			if (s instanceof Player) {
				Player p = (Player) s;
				if (args.length <= 1) {
					Loader.Help(s, "Pay", "Economy");
					return true;
				}
				if (args.length == 2) {
					if (args[0].equals("*")) {
						Repeat.a(s, "pay * " + API.convertMoney(args[1]));
						return true;
					}
					if (TheAPI.existsUser(args[0])) {
						double money = API.convertMoney(args[1]);
						if(money<0)money=0;
						if (EconomyAPI.has(p.getName(), money)
								|| s.hasPermission("ServerControl.Economy.InMinus")) {
							String w = p.getWorld().getName();
							EconomyAPI.withdrawPlayer(p.getName(),w, money);
							EconomyAPI.depositPlayer(args[0],w, money);
							Loader.sendMessages(s, "Economy.Pay.Sender", Placeholder.c().replace("%player%", args[0]).replace("%playername%", args[0])
									.replace("%money%", API.setMoneyFormat(money, true)));
							if(TheAPI.getPlayerOrNull(args[0])!=null)
							Loader.sendMessages(TheAPI.getPlayerOrNull(args[0]), "Economy.Pay.Receiver", Placeholder.c().replace("%player%", s.getName()).replace("%playername%", s.getName())
									.replace("%money%", API.setMoneyFormat(money, true)));
							return true;
						}
						Loader.sendMessages(s, "Economy.NotEnought");
						return true;
					}
					Loader.notExist(s,args[0]);
					return true;
					}
			}
		}
		Loader.noPerms(s, "Pay", "Economy");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Pay", "Economy")) {
			if(args.length==1)
				return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
			if(args.length==2)
				return StringUtils.copyPartialMatches(args[1], Arrays.asList("250","500","1k","2.5k","5k"));
		}
		return Arrays.asList();
	}
}
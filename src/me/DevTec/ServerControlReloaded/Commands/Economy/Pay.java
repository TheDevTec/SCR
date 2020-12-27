package me.DevTec.ServerControlReloaded.Commands.Economy;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.Repeat;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;

public class Pay implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			return true;
		}

		if (Loader.has(s, "Pay", "Economy")) {
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
						String moneyfromargs = args[1];
						if (moneyfromargs.startsWith("-"))
							moneyfromargs = "0.0";
						double money = API.convertMoney(args[1]);
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
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}
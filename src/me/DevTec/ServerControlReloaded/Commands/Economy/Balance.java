package me.DevTec.ServerControlReloaded.Commands.Economy;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;

public class Balance implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			return true;
		}
		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "Economy", "Economy", "Balance")) {
					Loader.sendMessages(s, "Economy.Balance.Your");
					return true;
				}
				Loader.noPerms(s, "Economy", "Economy", "Balance");
				return true;
			}
			return true;
		}
		if (TheAPI.existsUser(args[0])) {
			if (Loader.has(s, "Economy", "Economy", "BalanceOther")) {
				String world = s instanceof Player ? ((Player) s).getWorld().getName() :Bukkit.getWorlds().get(0).getName();
				Loader.sendMessages(s, "Economy.Balance.Other", Placeholder.c()
						.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(args[0], world), true))
						.replace("%currently%",API.setMoneyFormat(EconomyAPI.getBalance(args[0], world), true))
						.replace("%player%", args[0]).replace("%playername%", args[0]));
				return true;
			}
			Loader.noPerms(s, "Economy", "Economy", "BalanceOther");
			return true;
		}
		Loader.notExist(s, args[0]);
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

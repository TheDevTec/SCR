package me.devtec.servercontrolreloaded.commands.economy;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.utils.StringUtils;

public class Balance implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			return true;
		}
		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "Economy", "Economy", "Balance")) {
					if(!CommandsManager.canUse("Economy.Economy", s)) {
						Loader.sendMessages(s, "Cooldowns.Commands");
						return true;
					}
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
				if(!CommandsManager.canUse("Economy.Economy", s)) {
					Loader.sendMessages(s, "Cooldowns.Commands");
					return true;
				}
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
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "Economy", "Economy", "BalanceOther"))
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

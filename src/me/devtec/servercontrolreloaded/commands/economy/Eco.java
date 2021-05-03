package me.devtec.servercontrolreloaded.commands.economy;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.Repeat;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.utils.StringUtils;

public class Eco implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			s.sendMessage("Missing Vault or Economy plugin.");
			return true;
		}
		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "Economy", "Economy")) {
					Loader.sendMessages(s, "Economy.Balance.Your");
					return true;
				}
				Loader.noPerms(s, "Economy", "Economy");
				return true;
			}
			Loader.Help(s, "Economy", "Economy");
			return true;
		}
		if (TheAPI.existsUser(args[0])) {
			if (Loader.has(s, "Economy", "Economy", "BalanceOther")) {
				String world = s instanceof Player ? ((Player) s).getWorld().getName() :Bukkit.getWorlds().get(0).getName();
				Loader.sendMessages(s, "Economy.Balance.Other", Placeholder.c()
						.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(args[0], world), true))
						.replace("%player%", args[0]).replace("%playername%", args[0]));
				return true;
			}
			Loader.noPerms(s, "Economy", "Economy", "BalanceOther");
			return true;
		}
		if (args[0].equalsIgnoreCase("Give")) {
			if (Loader.has(s, "Economy", "Economy", "Give")) {
				if (args.length == 1 || args.length == 2) {
					Loader.Help(s, "Economy", "Economy");
					return true;
				}
				if (args[1].contains("*")) {
					Repeat.a(s, "eco give * " + API.convertMoney(args[2]));
					return true;
				}
				if (TheAPI.existsUser(args[1])) {
					double given = API.convertMoney(args[2]);
					if(given<0)
						given=0;
					EconomyAPI.depositPlayer(args[1], given);
					Loader.sendMessages(s, "Economy.Give.Sender", Placeholder.c().replace("%player%", args[1]).replace("%playername%", args[1])
							.replace("%money%", API.setMoneyFormat(given, true)));
					if(TheAPI.getPlayerOrNull(args[1])!=null)
					Loader.sendMessages(TheAPI.getPlayerOrNull(args[1]), "Economy.Give.Receiver", Placeholder.c().replace("%player%", s.getName()).replace("%playername%", s.getName())
							.replace("%money%", API.setMoneyFormat(given, true)));
					return true;
				}
				Loader.notExist(s, args[1]);
				return true;
			}
			Loader.noPerms(s, "Economy", "Economy", "Give");
			return true;
		}
		if (args[0].equalsIgnoreCase("Take")) {
			if (Loader.has(s, "Economy", "Economy", "Take")) {
				if (args.length == 1 || args.length == 2) {
					Loader.Help(s, "Economy", "Economy");
					return true;
				}
				if (args.length == 3) {
					if (args[1].contains("*")) {
						Repeat.a(s, "eco take * " + API.convertMoney(args[2]));
						return true;
					}
					if (TheAPI.existsUser(args[1])) {
						double taken = API.convertMoney(args[2]);
						if(taken<0)
							taken=0;
						EconomyAPI.withdrawPlayer(args[1], taken);
						Loader.sendMessages(s, "Economy.Take.Sender", Placeholder.c().replace("%player%", args[1]).replace("%playername%", args[1])
								.replace("%money%", API.setMoneyFormat(taken, true)));
						if(TheAPI.getPlayerOrNull(args[1])!=null)
						Loader.sendMessages(TheAPI.getPlayerOrNull(args[1]), "Economy.Take.Receiver", Placeholder.c().replace("%player%", s.getName()).replace("%playername%", s.getName())
								.replace("%money%", API.setMoneyFormat(taken, true)));
						return true;
					}
					Loader.notExist(s, args[1]);
					return true;
				}
			}
			Loader.noPerms(s, "Economy", "Economy", "Take");
			return true;
		}
		if (args[0].equalsIgnoreCase("Pay")) {
			if (Loader.has(s, "Economy", "Economy", "Pay")) {
				if (s instanceof Player) {
					Player p = (Player) s;
					if (args.length == 1 || args.length == 2) {
						Loader.Help(s, "Economy", "Economy");
						return true;
					}
					if (TheAPI.existsUser(args[1])) {
						double money = API.convertMoney(args[2]);
						if(money<0)money=0;
						if (EconomyAPI.has(p.getName(), money)
								|| s.hasPermission("ServerControl.Economy.InMinus")) {
							String w = p.getWorld().getName();
							EconomyAPI.withdrawPlayer(p.getName(),w, money);
							EconomyAPI.depositPlayer(args[1],w, money);
							Loader.sendMessages(s, "Economy.Pay.Sender", Placeholder.c().replace("%player%", args[1]).replace("%playername%", args[1])
									.replace("%money%", API.setMoneyFormat(money, true)));
							if(TheAPI.getPlayerOrNull(args[1])!=null)
							Loader.sendMessages(TheAPI.getPlayerOrNull(args[1]), "Economy.Pay.Receiver", Placeholder.c().replace("%player%", s.getName()).replace("%playername%", s.getName())
									.replace("%money%", API.setMoneyFormat(money, true)));
							return true;
						}
						Loader.sendMessages(s, "Economy.NotEnought");
						return true;
					}
					Loader.notExist(s, args[1]);
					return true;
				}
				return true;
			}
			Loader.noPerms(s, "Economy", "Economy", "Pay");
			return true;
		}
		if (args[0].equalsIgnoreCase("Reset")) {
			if (Loader.has(s, "Economy", "Economy", "Reset")) {
				if (args.length == 1) {
					Loader.Help(s, "Economy", "Economy");
					return true;
				}
				if (TheAPI.existsUser(args[1])) {
					reset(args[1], Loader.config.getDouble("Economy.DefaultMoney"));
					Loader.sendMessages(s, "Economy.Reset.Sender", Placeholder.c().replace("%player%", args[1]).replace("%playername%", args[1])
							.replace("%money%", API.setMoneyFormat(Loader.config.getDouble("Economy.DefaultMoney"), true)));
					if(TheAPI.getPlayerOrNull(args[1])!=null)
					Loader.sendMessages(TheAPI.getPlayerOrNull(args[1]), "Economy.Reset.Receiver", Placeholder.c().replace("%player%", s.getName()).replace("%playername%", s.getName())
							.replace("%money%", API.setMoneyFormat(Loader.config.getDouble("Economy.DefaultMoney"), true)));
					return true;
				}
				Loader.notExist(s, args[1]);
				return true;
			}
			Loader.noPerms(s, "Economy", "Economy", "Reset");
			return true;
		}
		if (args[0].equalsIgnoreCase("Set")) {
			if (Loader.has(s, "Economy", "Economy", "Set")) {
				if (args.length == 1 || args.length == 2) {
					Loader.Help(s, "Economy", "Economy");
					return true;
				}
				if (TheAPI.existsUser(args[1])) {
					reset(args[1], API.convertMoney(args[2]));
					Loader.sendMessages(s, "Economy.Set.Sender", Placeholder.c().replace("%player%", args[1]).replace("%playername%", args[1])
							.replace("%money%", API.setMoneyFormat(API.convertMoney(args[2]), true)));
					if(TheAPI.getPlayerOrNull(args[1])!=null)
					Loader.sendMessages(TheAPI.getPlayerOrNull(args[1]), "Economy.Set.Receiver", Placeholder.c().replace("%player%", s.getName()).replace("%playername%", s.getName())
							.replace("%money%", API.setMoneyFormat(API.convertMoney(args[2]), true)));
					return true;
				}
				Loader.notExist(s, args[1]);
				return true;
			}
			Loader.noPerms(s, "Economy", "Economy", "Set");
			return true;
		}
		return true;
	}

	private void reset(String i, double money) {
		if (EconomyAPI.getBalance(i) < 0) {
			EconomyAPI.depositPlayer(i, EconomyAPI.getBalance(i) * -1);
		} else
			EconomyAPI.withdrawPlayer(i, EconomyAPI.getBalance(i));
		if (money < 0) {
			EconomyAPI.withdrawPlayer(i, money * -1);
		} else
			EconomyAPI.depositPlayer(i, money);
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command command, String alias, String[] args) {
		if (args.length == 1) {
			List<String> c = new ArrayList<>();
			if (Loader.has(s, "Economy", "Economy", "Pay"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Pay")));
			if (Loader.has(s, "Economy", "Economy", "Take"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Take")));
			if (Loader.has(s, "Economy", "Economy", "Reset"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Reset")));
			if (Loader.has(s, "Economy", "Economy", "Give"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Give")));
			if (Loader.has(s, "Economy", "Economy", "Set"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Set")));
			return c;
		}
		if (args[0].equalsIgnoreCase("Pay") && Loader.has(s, "Economy", "Economy", "Pay")) {
			if (args.length == 2)
				return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
			if (args.length == 3)
				return StringUtils.copyPartialMatches(args[2], Arrays.asList("?"));
		}
		if (args[0].equalsIgnoreCase("Take") && Loader.has(s, "Economy", "Economy", "Take")) {
			if (args.length == 2)
				return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
			if (args.length == 3)
				return StringUtils.copyPartialMatches(args[2], Arrays.asList("?"));
		}
		if (args[0].equalsIgnoreCase("Reset") && Loader.has(s, "Economy", "Economy", "Reset")) {
			if (args.length == 2)
				return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
			if (args.length == 3)
				return StringUtils.copyPartialMatches(args[2], Arrays.asList("?"));
		}
		if (args[0].equalsIgnoreCase("Set") && Loader.has(s, "Economy", "Economy", "Set")) {
			if (args.length == 2)
				return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
			if (args.length == 3)
				return StringUtils.copyPartialMatches(args[2], Arrays.asList("?"));
		}
		if (args[0].equalsIgnoreCase("Give") && Loader.has(s, "Economy", "Economy", "Give")) {
			if (args.length == 2)
				return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
			if (args.length == 3)
				return StringUtils.copyPartialMatches(args[2], Arrays.asList("?"));
		}
		return Arrays.asList();
	}

}

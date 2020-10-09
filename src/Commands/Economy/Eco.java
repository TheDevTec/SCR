package Commands.Economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.Repeat;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;

public class Eco implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			return true;
		}

		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "Economy", "Economy", "Balance")) {
					Loader.sendMessages(s, "Economy.Balance.You");
					return true;
				}
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
			return true;
		}
		if (args[0].equalsIgnoreCase("Give")) {
			if (Loader.has(s, "Economy", "Economy", "Give")) {
				if (args.length == 1 || args.length == 2) {
					Loader.Help(s, "Economy", "Economy");
					return true;
				}
				if (args.length == 3) {
					if (args[1].contains("*")) {
						Repeat.a(s, "eco give * " + API.convertMoney(args[2]));
						return true;
					}
					if (TheAPI.existsUser(args[1])) {
						double given = API.convertMoney(args[2]);
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
			}
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
					if (args.length == 3) {
						if (TheAPI.existsUser(args[1])) {
							String moneyfromargs = args[2];
							if (moneyfromargs.startsWith("-"))
								moneyfromargs = "0.0";
							double money = API.convertMoney(args[2]);
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
				}
				return true;
			}
			return true;
		}
		if (args[0].equalsIgnoreCase("Reset")) {
			if (Loader.has(s, "Economy", "Economy", "Reset")) {
				if (args.length == 1) {
					Loader.Help(s, "Economy", "Economy");
					return true;
				}
				if (args.length == 2) {
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
			}
			return true;
		}
		if (args[0].equalsIgnoreCase("Set")) {
			if (Loader.has(s, "Economy", "Economy", "Set")) {
				if (args.length == 1 || args.length == 2) {
					Loader.Help(s, "Economy", "Economy");
					return true;
				}
				if (args.length == 3) {
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
			}
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
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		List<String> Pay = Arrays.asList("Pay");
		List<String> Give = Arrays.asList("Give");
		List<String> Take = Arrays.asList("Take");
		List<String> Reset = Arrays.asList("Reset");
		List<String> Set = Arrays.asList("Set");
		List<String> numbers = Arrays.asList("?");
		if (args.length == 1) {
			if (sender.hasPermission("ServerControl.Pay")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Pay, new ArrayList<>()));

			}
			if (sender.hasPermission("ServerControl.Economy.Take")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Take, new ArrayList<>()));

			}
			if (sender.hasPermission("ServerControl.Economy.Reset")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Reset, new ArrayList<>()));

			}
			if (sender.hasPermission("ServerControl.Economy.Give")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Give, new ArrayList<>()));

			}
			if (sender.hasPermission("ServerControl.Economy.Set")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Set, new ArrayList<>()));

			}
		}
		if (args[0].equalsIgnoreCase("Pay")) {
			if (sender.hasPermission("ServerControl.Pay")) {
				if (args.length == 2)
					return null;
				if (args.length == 3)
					c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
			}
		}
		if (args[0].equalsIgnoreCase("Take")) {
			if (sender.hasPermission("ServerControl.Economy.Take")) {
				if (args.length == 2)
					return null;
				if (args.length == 3)
					c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
			}
		}
		if (args[0].equalsIgnoreCase("Reset")) {
			if (sender.hasPermission("ServerControl.Economy.Reset")) {
				if (args.length == 2)
					return null;
				if (args.length == 3)
					c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
			}
		}
		if (args[0].equalsIgnoreCase("Set")) {
			if (sender.hasPermission("ServerControl.Economy.Set")) {
				if (args.length == 2)
					return null;
				if (args.length == 3)
					c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
			}
		}
		if (args[0].equalsIgnoreCase("Give")) {
			if (sender.hasPermission("ServerControl.Economy.Give")) {
				if (args.length == 2)
					return null;
				if (args.length == 3)
					c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
			}
		}
		return c;
	}

}

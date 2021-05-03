package me.devtec.servercontrolreloaded.commands.economy;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.utils.StringUtils;

public class MultiEconomy implements CommandExecutor, TabCompleter {
	private String getEconomyGroup(String ss) {
		for (String s : Loader.config.getKeys("Options.Economy.MultiEconomy.Types"))
			if (s.toLowerCase().equalsIgnoreCase(ss))
				return s;
		return null;

	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			return true;
		}
		if (Loader.has(s, "MultiEconomy", "Economy")) {
			if (args.length == 0) {
				Loader.Help(s, "MultiEconomy", "Economy");
				return true;
			}

			if (args[0].equalsIgnoreCase("Worlds")) {
				if (args.length == 1) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				String group = getEconomyGroup(args[1]);
				if (group == null) {
					Loader.sendMessages(s, "Economy.Multi.NotExist", Placeholder.c().replace("%group%", args[1])
							.replace("%economy-group%", args[1]));
					return true;
				}
				Loader.sendMessages(s, "Economy.Multi.Worlds.List", Placeholder.c()
						.replace("%group%", getEconomyGroup(args[1]))
						.replace("%economy-group%", getEconomyGroup(args[1]))
						.replace("%worlds%", StringUtils.join(Loader.config.getStringList("Options.Economy.MultiEconomy.Types." + getEconomyGroup(args[1])), ", ")));
				return true;
			}
			if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 1) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				if (getEconomyGroup(args[1]) != null) {
					Loader.sendMessages(s, "Economy.Multi.Exists", Placeholder.c()
							.replace("%group%", getEconomyGroup(args[1]))
							.replace("%economy-group%", getEconomyGroup(args[1])));
					return true;
				}
				Loader.config.set("Options.Economy.MultiEconomy.Types." + args[1], "");
				Loader.config.save();
				Loader.sendMessages(s, "Economy.Multi.Create", Placeholder.c()
						.replace("%group%", args[1])
						.replace("%economy-group%", args[1]));
				return true;

			}
			if (args[0].equalsIgnoreCase("delete")) {
				if (args.length == 1) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				if (getEconomyGroup(args[1]) == null) {
					Loader.sendMessages(s, "Economy.Multi.NotExist", Placeholder.c()
							.replace("%group%", args[1])
							.replace("%economy-group%", args[1]));
					return true;
				}
				String a = getEconomyGroup(args[1]);
				Loader.config.set("Options.Economy.MultiEconomy.Types." + a, null);
				Loader.config.save();
				Loader.sendMessages(s, "Economy.Multi.Delete", Placeholder.c()
						.replace("%group%", a)
						.replace("%economy-group%", a));
				return true;

			}

			if (args[0].equalsIgnoreCase("add")) {
				if (args.length == 1) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				if (getEconomyGroup(args[1]) == null) {
					Loader.sendMessages(s, "Economy.Multi.NotExist", Placeholder.c()
							.replace("%group%", args[1])
							.replace("%economy-group%", args[1]));
					return true;
				}
				if (args.length == 2) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				if (Bukkit.getWorld(args[2]) == null) {
					Loader.sendMessages(s, "Missing.World", Placeholder.c().replace("%world%", args[2]));
					return true;
				}
					List<String> list = Loader.config.getStringList("Options.Economy.MultiEconomy.Types." + getEconomyGroup(args[1]));
					if (!list.contains(args[2])) {
						list.add(args[2]);
						Loader.config.set("Options.Economy.MultiEconomy.Types." + getEconomyGroup(args[1]), list);
						Loader.config.save();
						Loader.sendMessages(s, "Economy.Multi.Worlds.Added", Placeholder.c().replace("%world%", args[2]).replace("%group%", getEconomyGroup(args[1]))
								.replace("%economy-group%", getEconomyGroup(args[1])));
						return true;
					}
					Loader.sendMessages(s, "Economy.Multi.Worlds.AlreadyInGroup", Placeholder.c().replace("%world%", args[2]).replace("%group%", getEconomyGroup(args[1]))
							.replace("%economy-group%", getEconomyGroup(args[1])));
					return true;
			}
			if (args[0].equalsIgnoreCase("remove")) {
				if (args.length == 1) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				if (getEconomyGroup(args[1]) == null) {
					Loader.sendMessages(s, "Economy.Multi.NotExist", Placeholder.c()
							.replace("%group%", args[1])
							.replace("%economy-group%", args[1]));
					return true;
				}
				if (args.length == 2) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				if (Bukkit.getWorld(args[2]) == null) {
					Loader.sendMessages(s, "Missing.World", Placeholder.c().replace("%world%", args[2]));
					return true;
				}
					List<String> list = Loader.config.getStringList("Options.Economy.MultiEconomy.Types." + getEconomyGroup(args[1]));
					if (list.contains(args[2])) {
						list.remove(args[2]);
						Loader.config.set("Options.Economy.MultiEconomy.Types." + getEconomyGroup(args[1]), list);
						Loader.config.save();
						Loader.sendMessages(s, "Economy.Multi.Worlds.Removed", Placeholder.c().replace("%world%", args[2]).replace("%group%", getEconomyGroup(args[1]))
								.replace("%economy-group%", getEconomyGroup(args[1])));
						return true;
					}
					Loader.sendMessages(s, "Economy.Multi.Worlds.NotInGroup", Placeholder.c().replace("%world%", args[2]).replace("%group%", getEconomyGroup(args[1]))
							.replace("%economy-group%", getEconomyGroup(args[1])));
					return true;
			}
			if (args[0].equalsIgnoreCase("money")) {
				if (args.length == 1 || args.length == 2) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				if (args.length == 3) {
					if (!TheAPI.existsUser(args[1])) {
						Loader.notExist(s,args[1]);
						return true;
					}
					if(getEconomyGroup(args[2]) == null) {
						Loader.sendMessages(s, "Economy.Multi.NotExist", Placeholder.c()
								.replace("%group%", args[2])
								.replace("%economy-group%", args[2]));
						return true;
					}
					String group = TheAPI.getUser(args[1]).getString("Money." + getEconomyGroup(args[2]));
					if (group == null) {
						Loader.sendMessages(s, "Economy.Multi.NoMoney", Placeholder.c().replace("%group%", getEconomyGroup(args[2]))
								.replace("%economy-group%", getEconomyGroup(args[2]))
								.replace("%player%", args[1])
								.replace("%playername%", args[1]));
						return true;
					}
					Loader.sendMessages(s, "Economy.Multi.Money", Placeholder.c().replace("%group%", getEconomyGroup(args[2]))
							.replace("%economy-group%", getEconomyGroup(args[2]))
							.replace("%money%", API.setMoneyFormat(StringUtils.getDouble(group), true))
							.replace("%player%", args[1])
							.replace("%playername%", args[1]));
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("groups")) {
				Loader.sendMessages(s, "Economy.Multi.Groups", Placeholder.c().replace("%groups%",
						StringUtils.join(Loader.config.getKeys("Options.Economy.MultiEconomy.Types"), ", ")));
				return true;
			}
			if (args[0].equalsIgnoreCase("transfer")) {
				if (args.length == 1) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if (p == null) {
					Loader.notExist(s,args[1]);
					return true;
				}
				if (p != null && args.length == 2) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				if(getEconomyGroup(args[2]) == null) {
					Loader.sendMessages(s, "Economy.Multi.NotExist", Placeholder.c()
							.replace("%group%", args[2])
							.replace("%economy-group%", args[2]));
					return true;
				}
				if (args.length == 3) {
					Loader.Help(s, "MultiEconomy", "Economy");
					return true;
				}
				if(getEconomyGroup(args[3]) == null) {
					Loader.sendMessages(s, "Economy.Multi.NotExist", Placeholder.c()
							.replace("%group%", args[3])
							.replace("%economy-group%", args[3]));
					return true;
				}
				double money = TheAPI.getUser(p).getDouble("Money." + getEconomyGroup(args[2]));
				double money2 = TheAPI.getUser(p).getDouble("Money." + getEconomyGroup(args[3]));
				TheAPI.getUser(p).set("Money." + getEconomyGroup(args[3]), money);
				TheAPI.getUser(p).setAndSave("Money." + getEconomyGroup(args[2]), money2);
				Loader.sendMessages(s, "Economy.Multi.Transfer", Placeholder.c()
					.replace("%from%", args[2])
					.replace("%to%", args[3]).replace("%player%", args[1])
					.replace("%playername%", args[1]));
				return true;
			}
		}
		Loader.noPerms(s, "MultiEconomy", "Economy");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "MultiEconomy", "Economy")) {
			if (args.length == 1) {
				return StringUtils.copyPartialMatches(args[0],
						Arrays.asList("Money", "Transfer", "Create", "Delete", "Add", "Remove", "Worlds", "Groups"));
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("Create")) {
					return StringUtils.copyPartialMatches(args[1], Arrays.asList("?"));
				}
				if (args[0].equalsIgnoreCase("Delete") || args[0].equalsIgnoreCase("Add")
						|| args[0].equalsIgnoreCase("Remove") || args[0].equalsIgnoreCase("Worlds")) {
					return StringUtils.copyPartialMatches(args[1],
							Loader.config.getKeys("Options.Economy.MultiEconomy.Types"));
				}
				if (args[0].equalsIgnoreCase("Transfer") || args[0].equalsIgnoreCase("Money"))
					return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
			}
			if (args.length == 3) {
				if (args[0].equalsIgnoreCase("Add") || args[0].equalsIgnoreCase("Remove")) {
					for (String world : Loader.config.getKeys("Options.Economy.MultiEconomy.Types"))
						if (args[1].equalsIgnoreCase(world))
							return StringUtils.copyPartialMatches(args[2], worlds());
				}
				if (args[0].equalsIgnoreCase("Money") || args[0].equalsIgnoreCase("Transfer")) {
					Player p = TheAPI.getPlayer(args[1]);
					if (p != null)
						return StringUtils.copyPartialMatches(args[2], Loader.config
								.getKeys("Options.Economy.MultiEconomy.Types"));
				}
			}
			if (args.length == 4) {
				if (args[0].equalsIgnoreCase("Transfer")) {
					Player p = TheAPI.getPlayer(args[1]);
					if (p != null)
						if (Loader.config.getKeys("Options.Economy.MultiEconomy.Types")
								.contains(args[2]))
							return StringUtils.copyPartialMatches(args[3], Loader.config
									.getKeys("Options.Economy.MultiEconomy.Types"));
				}
			}
		}
		return Arrays.asList();
	}

	private List<String> worlds() {
		ArrayList<String> worlds = new ArrayList<String>();
		for (World s : Bukkit.getWorlds())
			worlds.add(s.getName());
		return worlds;
	}

}

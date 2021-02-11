package me.DevTec.ServerControlReloaded.Commands.Other;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.Kit;
import me.DevTec.ServerControlReloaded.Utils.Repeat;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.cooldownapi.CooldownAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;

public class Kits implements CommandExecutor, TabCompleter {
	public List<String> kits(CommandSender p) {
		List<String> list = new ArrayList<String>();
		for (String name : getKits())
			if (Loader.hasKits(p, name))
				list.add(name);
		return list;
	}

	public static Set<String> getKits() {
		return Loader.kit.getKeys("Kits");
	}

	public static void giveKit(Player p, Kit kit, boolean cooldown, boolean economy, boolean messages) {
		if(kit==null)return;
			if (!cooldown) {
				if (!economy) {
					if(kit.getItems()!=null)
					TheAPI.giveItem(p, kit.getItems());
					if(kit.getItemsWithSlots()!=null)
					for(Entry<Integer, ItemStack> s : kit.getItemsWithSlots().entrySet())
						p.getInventory().setItem(s.getKey(), s.getValue());
					for(String s : kit.getCommands())
						TheAPI.sudoConsole(PlaceholderAPI.setPlaceholders(p, s).replace("%player%", p.getName()));
					for(String s : kit.getMessages())
						TheAPI.msg(PlaceholderAPI.setPlaceholders(p, s).replace("%player%", p.getName()),p);
					return;
				} else {
					if (EconomyAPI.has(p, kit.getCost())) {
						if(kit.getItems()!=null)
						TheAPI.giveItem(p, kit.getItems());
						if(kit.getItemsWithSlots()!=null)
						for(Entry<Integer, ItemStack> s : kit.getItemsWithSlots().entrySet())
							p.getInventory().setItem(s.getKey(), s.getValue());
						if(messages)
						Loader.sendMessages(p, "Kits.Used", Placeholder.c().add("%kit%", kit.getName()));
						EconomyAPI.withdrawPlayer(p, kit.getCost());
						for(String s : kit.getCommands())
							TheAPI.sudoConsole(PlaceholderAPI.setPlaceholders(p, s).replace("%player%", p.getName()));
						for(String s : kit.getMessages())
							TheAPI.msg(PlaceholderAPI.setPlaceholders(p, s).replace("%player%", p.getName()),p);
						return;
					}
					if(messages)
					Loader.sendMessages(p, "Economy.NotEnought");
					return;
				}
			} else {
				if (economy) {
					if (EconomyAPI.has(p, kit.getCost())) {
						CooldownAPI a = TheAPI.getCooldownAPI(p);
						if (!a.expired("Kit." + kit.getName())) {
							if(messages)
							Loader.sendMessages(p, "Kits.Cooldown", Placeholder.c().add("%kit%", kit.getName()).add("%time%", StringUtils.setTimeToString(a.getTimeToExpire("Kit." + kit.getName())/20)));
							return;
						}
						if(kit.getItems()!=null)
						TheAPI.giveItem(p, kit.getItems());
						if(kit.getItemsWithSlots()!=null)
						for(Entry<Integer, ItemStack> s : kit.getItemsWithSlots().entrySet())
							p.getInventory().setItem(s.getKey(), s.getValue());
						if(messages)
						Loader.sendMessages(p, "Kits.Used", Placeholder.c().add("%kit%", kit.getName()));
						a.createCooldown("Kit." + kit.getName(), kit.getDelay()*20);
						EconomyAPI.withdrawPlayer(p, kit.getCost());
						for(String s : kit.getCommands())
							TheAPI.sudoConsole(PlaceholderAPI.setPlaceholders(p, s).replace("%player%", p.getName()));
						for(String s : kit.getMessages())
							TheAPI.msg(PlaceholderAPI.setPlaceholders(p, s).replace("%player%", p.getName()),p);
						return;
					}
					if(messages)
					Loader.sendMessages(p, "Economy.NotEnought");
					return;
				} else {
					CooldownAPI a = TheAPI.getCooldownAPI(p);
					if (!a.expired("Kit." + kit.getName())) {
						if(messages)
						Loader.sendMessages(p, "Kits.Cooldown", Placeholder.c().add("%kit%", kit.getName()).add("%time%", StringUtils.setTimeToString(a.getTimeToExpire("Kit." + kit.getName())/20)));
						return;
					}
					if(kit.getItems()!=null)
					TheAPI.giveItem(p, kit.getItems());
					if(kit.getItemsWithSlots()!=null)
					for(Entry<Integer, ItemStack> s : kit.getItemsWithSlots().entrySet())
						p.getInventory().setItem(s.getKey(), s.getValue());
					if(messages)
					Loader.sendMessages(p, "Kits.Used", Placeholder.c().add("%kit%", kit.getName()));
					a.createCooldown("Kit." + kit.getName(), kit.getDelay()*20);
					for(String s : kit.getCommands())
						TheAPI.sudoConsole(PlaceholderAPI.setPlaceholders(p, s).replace("%player%", p.getName()));
					for(String s : kit.getMessages())
						TheAPI.msg(PlaceholderAPI.setPlaceholders(p, s).replace("%player%", p.getName()),p);
					return;
				}
			}
	}

	public String kit(String[] args) {
		if (Loader.kit.exists("Kits"))
			for (String s : Loader.kit.getKeys("Kits")) {
				if (s.toLowerCase().equalsIgnoreCase(args[0])) {
					return s;
				}
			}
		return null;
	}
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if(Loader.has(s, "Kits", "Other", "List")) {
			Loader.sendMessages(s, "Kits.List", Placeholder.c().replace("%kits%", StringUtils.join(kits(s), ", ")));
			return true;
			}
			Loader.noPerms(s, "Kits", "Other", "List");
			return true;
		}
		if (args.length == 1) {
			if (getKits().contains(args[0].toLowerCase())) {
				if (Loader.hasKits(s, kit(args))) {
					if (s instanceof Player) {
						giveKit((Player)s, Loader.getKit(args[0].toLowerCase()), true, true, true);
						return true;
					}
					Loader.Help(s, "Kit", "Other");
					return true;
				}
				Loader.noPerms(s, "Kit", "Other");
				return true;
			}
			Loader.sendMessages(s, "Kits.NotExist", Placeholder.c().replace("%kit%", args[0].toLowerCase()));
			return true;
		}
		if (args.length == 2) {
			if (getKits().contains(args[0].toLowerCase())) {
				if (Loader.hasKits(s, kit(args))) {
					if (args[0].equals("*")) {
						Repeat.a(s, "kit " + args[0].toLowerCase() + " *");
						return true;
					}
					Player t = TheAPI.getPlayer(args[1]);
					if(t==null) {
						Loader.notOnline(s, args[1]);
						return true;
					}
					if (s == t) {
						giveKit(t, Loader.getKit(args[0].toLowerCase()), true, true, true);
						return true;
					}
					if(Loader.has(s, "Kits", "Other", "Give")) {
					giveKit(t, Loader.getKit(args[0].toLowerCase()), false, false, false);
					Loader.sendMessages(s, "Kits.Give.Sender", Placeholder.c().replace("%kit%", args[0].toLowerCase())
							.replace("%player%", t.getName()).replace("%playername%", t.getDisplayName()));
					Loader.sendMessages(t, "Kits.Give.Receiver", Placeholder.c().replace("%kit%", args[0].toLowerCase())
							.replace("%player%", s.getName()).replace("%playername%", s.getName()));
					return true;
					}
					Loader.noPerms(s, "Kit", "Other", "Give");
					return true;
				}
				Loader.noPerms(s, "Kit", "Other");
				return true;
			}
			Loader.sendMessages(s, "Kits.NotExist", Placeholder.c().replace("%kit%", args[0]));
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (args.length == 1)
			return StringUtils.copyPartialMatches(args[0], kits(s));
		if (args.length == 2)
			return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
		return Arrays.asList();
	}

}

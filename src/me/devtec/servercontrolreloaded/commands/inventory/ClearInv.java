package me.devtec.servercontrolreloaded.commands.inventory;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class ClearInv implements CommandExecutor, TabCompleter {

	public final HashMap<CommandSender, ItemStack[]> undo = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (!(s instanceof Player)) {
			if (args.length == 0) {
				Loader.Help(s, "ClearInventory", "Inventory");
				return true;
			}
			if (args.length == 1) {
				Player target = TheAPI.getPlayer(args[0]);
				if (target == null) {
					Loader.notOnline(s, args[0]);
					return true;
				}
				undo.put(target, target.getInventory().getContents());
				Loader.sendMessages(s, "Inventory.ClearInventory.Other.Sender", Placeholder.c()
						.add("%player%", target.getName())
						.add("%playername%", target.getName()));
				Loader.sendMessages(target, "Inventory.ClearInventory.Other.Receiver", Placeholder.c()
						.add("%player%", s.getName())
						.add("%playername%", s.getName()));
				target.getInventory().clear();
				return true;
			}
		} else {
			if (Loader.has(s, "ClearInventory", "Inventory")) {
			if(!CommandsManager.canUse("Inventory.ClearInventory", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Inventory.ClearInventory", s))));
				return true;
			}
			Player p = (Player) s;
			User d = TheAPI.getUser(p);
			int take = Loader.config.getInt("Options.Cost-ClearInvUndo");

			if (args.length == 0) {
					if (!d.getBoolean("ClearInvConfirm")) {
						d.setAndSave("ClearInvCooldown", System.currentTimeMillis() / 1000);
						Loader.sendMessages(s, "Inventory.ClearInventory.ClearConfirm");
						return true;
					} else {
						if (d.getString("ClearInvCooldown") != null)
							d.setAndSave("ClearInvCooldown", null);
						undo.put(p, p.getInventory().getContents());
						Loader.sendMessages(s, "Inventory.ClearInventory.ClearConfirm");
						p.getInventory().clear();
						return true;
					}
			}
			if (args[0].equalsIgnoreCase("Other")) {
				if (Loader.has(s, "ClearInventory", "Inventory", "Other")) {
					if (args.length == 1) {
						Loader.Help(s, "ClearInventory", "Inventory");
						return true;
					}
					Player target = TheAPI.getPlayer(args[1]);
					if (target == null) {
						Loader.notOnline(s, args[1]);
						return true;
					}
					undo.put(target, target.getInventory().getContents());
					Loader.sendMessages(s, "Inventory.ClearInventory.Other.Sender", Placeholder.c()
							.add("%player%", s.getName())
							.add("%playername%", s.getName()));
					Loader.sendMessages(target, "Inventory.ClearInventory.Other.Receiver", Placeholder.c()
							.add("%player%", s.getName())
							.add("%playername%", s.getName()));
					target.getInventory().clear();
					return true;
				}
				Loader.noPerms(s, "ClearInventory", "Inventory", "Other");
				return true;
			}
			if (!d.getBoolean("ClearInvConfirm")) {
				if (args[0].equalsIgnoreCase("Confirm")) {
					if (Loader.has(s, "ClearInventory", "Inventory", "Inventory")) {
						long reset = d.getLong("ClearInvCooldown") - System.currentTimeMillis() / 1000;
						reset = reset * -1;
						if (reset < 60) {
							undo.put(p, p.getInventory().getContents());
							Loader.sendMessages(s, "Inventory.ClearInventory.You");
							d.setAndSave("ClearInvCooldown", null);
							p.getInventory().clear();
							return true;
						}
						return true;
					}
					Loader.noPerms(s, "ClearInventory", "Inventory", "Inventory");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("Clear")) {
				if (Loader.has(s, "ClearInventory", "Inventory", "Inventory")) {
					if (!d.getBoolean("ClearInvConfirm")) {
						d.setAndSave("ClearInvCooldown", System.currentTimeMillis() / 1000);
						Loader.sendMessages(s, "Inventory.ClearInventory.ClearConfirm");
						return true;
					} else {
						if (d.exists("ClearInvCooldown"))
							d.setAndSave("ClearInvCooldown", null);
						undo.put(p, p.getInventory().getContents());
						Loader.sendMessages(s, "Inventory.ClearInventory.You");
						p.getInventory().clear();
						return true;
					}
				}
				Loader.noPerms(s, "ClearInventory", "Inventory", "Inventory");
			}
			if (args[0].equalsIgnoreCase("Undo")) {
				if (Loader.has(s, "ClearInventory", "Inventory", "Undo")) {
					if (undo.containsKey(p)) {

						if (take <= 0 || EconomyAPI.getEconomy() == null) {
							for (ItemStack item : undo.get(p)) {
								if (item != null)
									TheAPI.giveItem(p, item);
							}
							undo.remove(p);
							Loader.sendMessages(s, "Inventory.ClearInventory.Undo", Placeholder.c()
									.add("%money%", "0"));
							return true;
						} else if (EconomyAPI.getEconomy() != null) {
							if (EconomyAPI.has(p, take)) {
								for (ItemStack item : undo.get(p)) {
									TheAPI.giveItem(p, item);
								}
								EconomyAPI.withdrawPlayer(p, take);
								undo.remove(p);
								Loader.sendMessages(s, "Inventory.ClearInventory.Undo", Placeholder.c()
										.add("%money%", ""+take));
							} else {
								Loader.sendMessages(s, "Inventory.ClearInventory.NoMoney", Placeholder.c()
										.add("%money%", ""+take));
							}
							return true;
						}
					} else if (!undo.containsKey(p)) {
						Loader.sendMessages(s, "Inventory.ClearInventory.CantUndo");
						return true;
					}
				}
				Loader.noPerms(s, "ClearInventory", "Inventory", "Undo");
				return true;
			}
			return true;
			}
			Loader.noPerms(s, "ClearInventory", "Inventory");
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if(Loader.has(s, "ClearInventory", "Inventory")) {
		if(args.length==1) {
			List<String> c = new ArrayList<>();
			if (Loader.has(s, "ClearInventory", "Inventory", "Inventory")) {
				if (!TheAPI.getUser(s.getName()).getBoolean("ClearInvConfirm"))
					c.addAll(StringUtils.copyPartialMatches(args[0], Collections.singletonList("Confirm")));
				c.addAll(StringUtils.copyPartialMatches(args[0], Collections.singletonList("Clear")));
			}
			if (Loader.has(s, "ClearInventory", "Inventory", "Undo")) {
				c.addAll(StringUtils.copyPartialMatches(args[0], Collections.singletonList("Undo")));
			}
			if (Loader.has(s, "ClearInventory", "Inventory", "Other"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Collections.singletonList("Other")));
			return c;
		}
		if (args.length == 2 && args[0].equalsIgnoreCase("other") && Loader.has(s, "ClearInventory", "Inventory", "Other"))
			return StringUtils.copyPartialMatches(args[1], API.getPlayerNames(s));
		}
		return Collections.emptyList();
	}
}
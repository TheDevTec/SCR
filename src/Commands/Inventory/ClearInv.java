package Commands.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class ClearInv implements CommandExecutor, TabCompleter {


	public String value(int i) {
		return String.valueOf(i);
	}

	public HashMap<CommandSender, ItemStack[]> undo = new HashMap<CommandSender, ItemStack[]>();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (s instanceof Player == false) {
			if (args.length == 0) {
				Loader.Help(s, "/ClearInv <player>", "Inventory");
				return true;
			}
			if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == null) {
					Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
							.add("%player%", args[1])
							.add("%playername%", args[1]));
					return true;
				}
				Loader.sendMessages(s, "Inventory.InvClear.Clear.Other.Sender", Placeholder.c()
						.add("%player%", target.getName())
						.add("%playername%", target.getDisplayName()));
				undo.put(target, target.getInventory().getContents());
				Loader.sendMessages(target, "Inventory.InvClear.Clear.Other.Receiver", Placeholder.c()
						.add("%player%", s.getName())
						.add("%playername%", s.getName()));
				target.getInventory().clear();
				return true;
			}
			return true;
		} else {
			Player p = (Player) s;
			User d = TheAPI.getUser(p);
			int take = Loader.config.getInt("Options.Cost-ClearInvUndo");

			if (args.length == 0) {
				if (Loader.has(s, "ClearInventory", "Inventory")) {
					if (!d.getBoolean("ClearInvConfirm")) {
						d.setAndSave("ClearInvCooldown", System.currentTimeMillis() / 1000);
						Loader.sendMessages(s, "Inventory.ClearInv.Confirm");
						return true;
					} else {
						if (d.getString("ClearInvCooldown") != null)
							d.setAndSave("ClearInvCooldown", null);
						undo.put(p, p.getInventory().getContents());
						Loader.sendMessages(s, "Inventory.InvCleared");
						p.getInventory().clear();
						return true;
					}
				}
				Loader.noPerms(s, "ClearInventory", "Inventory");
				return true;
			}
			if (args[0].equalsIgnoreCase("Other")) {
				if (Loader.has(s, "ClearInventory", "Inventory", "Inventory")) {
					if (args.length == 1) {
						Loader.Help(s, "/Clear Other <player>", "Inventory");
						return true;
					}
					Player target = Bukkit.getServer().getPlayer(args[1]);
					if (target == null) {
						Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
								.add("%player%", args[1])
								.add("%playername%", args[1]));
						return true;
					}
					if (target != null) {
						Loader.sendMessages(s, "Inventory.InvClear.Clear.Other.Sender", Placeholder.c()
								.add("%player%", target.getName())
								.add("%playername%", target.getDisplayName()));
						undo.put(target, target.getInventory().getContents());
						Loader.sendMessages(target, "Inventory.InvClear.Clear.Other.Receiver", Placeholder.c()
								.add("%player%", s.getName())
								.add("%playername%", s.getName()));
						target.getInventory().clear();
						return true;
					}
				}
				Loader.noPerms(s, "ClearInventory", "Inventory", "Inventory");
				return true;
			}
			if (!d.getBoolean("ClearInvConfirm")) {
				if (args[0].equalsIgnoreCase("Confirm")) {

					if (Loader.has(s, "ClearInventory", "Inventory", "Inventory")) {
						long reset = d.getLong("ClearInvCooldown") - System.currentTimeMillis() / 1000;
						reset = reset * -1;
						if (reset < 60) {
							undo.put(p, p.getInventory().getContents());
							Loader.sendMessages(s, "Inventory.InvClear.Clear.You");
							d.setAndSave("ClearInvCooldown", null);
							p.getInventory().clear();
							return true;
						}
						Loader.sendMessages(s, "Inventory.ClearInv.NoConfirm");
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
						Loader.sendMessages(s, "Inventory.ClearConfirm");
						return true;
					} else {
						if (d.getString("ClearInvCooldown") != null)
							d.setAndSave("ClearInvCooldown", null);
						undo.put(p, p.getInventory().getContents());
						Loader.sendMessages(s, "Inventory.InvClear.Clear.You");
						p.getInventory().clear();
						return true;
					}
				}
				Loader.noPerms(s, "ClearInventory", "Inventory", "Inventory");
			}
			if (args[0].equalsIgnoreCase("Undo")) {
				if (Loader.has(s, "ClearInventory", "Inventory", "Undo")) {
					if (undo.containsKey(p)) {

						if (take == 0 || EconomyAPI.getEconomy() == null) {
							for (ItemStack item : undo.get(p)) {
								TheAPI.giveItem(p, item);
							}
							undo.remove(p);
							Loader.sendMessages(s, "Inventory.InvClear.Retrieve.ForFree");

							return true;
						} else if (take != 0 && EconomyAPI.getEconomy() != null) {

							if (EconomyAPI.has(p, take)) {
								for (ItemStack item : undo.get(p)) {
									TheAPI.giveItem(p, item);
								}
								EconomyAPI.withdrawPlayer(p, take);
								undo.remove(p);
								Loader.sendMessages(s, "Inventory.InvClear.Retrieve.ForMoney", Placeholder.c()
										.add("%money%", value(take)));
								return true;
							} else {
								Loader.sendMessages(s, "Inventory.InvClear.Retrieve.NoMoney", Placeholder.c()
										.add("%money%", value(take)));
								return true;
							}
						}
					} else if (!undo.containsKey(p)) {
						Loader.sendMessages(s, "Invenotory.InvClear.Retrieve.NoInventory");
						return true;
					}
				}
				Loader.noPerms(s, "ClearInventory", "Inventory", "Undo");
				return true;
			}
			if (args[0].equalsIgnoreCase("Help")) {
				if (Loader.has(s, "ClearInventory", "Inventory")) {
					Loader.Help(s, "/Clear", "Inventory");
					if (!d.getBoolean("ClearInvConfirm"))
						Loader.Help(s, "/Clear Confirm", "Inventory");
				}
				if (Loader.has(s, "ClearInventory", "Inventory", "Other"))
					Loader.Help(s, "/Clear Other <player>", "Inventory");
				if (Loader.has(s, "ClearInventory", "Inventory", "Undo"))
					Loader.Help(s, "/Clear Undo", "Inventory");
				return true;
			}
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (s.hasPermission("ServerControl.ClearInventory")) {

			if (!TheAPI.getUser(s.getName()).getBoolean("ClearInvConfirm")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Confirm"), new ArrayList<>()));
			}
			c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Clear"), new ArrayList<>()));

		}
		c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Help"), new ArrayList<>()));

		if (s.hasPermission("ServerControl.ClearInventory.Undo")) {
			c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Undo"), new ArrayList<>()));
		}
		if (s.hasPermission("ServerControl.ClearInventory.Other")) {
			if (args.length == 1)
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Other"), new ArrayList<>()));
			if (args.length == 2 && args[0].equalsIgnoreCase("other"))
				return null;
		}
		return c;
	}
}
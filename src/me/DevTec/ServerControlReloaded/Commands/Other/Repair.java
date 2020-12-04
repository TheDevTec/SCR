package me.DevTec.ServerControlReloaded.Commands.Other;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.DataKeeper.Collections.UnsortedList;

public class Repair implements CommandExecutor, TabCompleter {

	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length<2) {
		if (s instanceof Player) {
			if (Loader.has(s, "Repair", "Other")) {
				Player p = (Player) s;
				if (args.length == 0) {
					Material hand = p.getItemInHand().getType();
					if (hand != Material.AIR) {
						p.getItemInHand().setDurability((short) 0);
					Loader.sendMessages(s, "Repair.Hand.You");
					return true;
					}
					Loader.sendMessages(s, "Missing.HandEmpty");
					return true;
				}
				if (args[0].equalsIgnoreCase("all")) {
					ItemStack[] items = p.getInventory().getContents();
					for (ItemStack t : items) {
						if (t != null && t.getType()!=Material.AIR)
							t.setDurability((short) 0);
					}
					Loader.sendMessages(s, "Repair.All.You");
					return true;
				}
					Material hand = p.getItemInHand().getType();
					if (hand != Material.AIR) {
						p.getItemInHand().setDurability((short) 0);
					Loader.sendMessages(s, "Repair.Hand.You");
					return true;
					}
					Loader.sendMessages(s, "Missing.HandEmpty");
					return true;
			}
			Loader.noPerms(s, "Repair", "Other");
			return true;
		}
		Loader.Help(s, "Repair", "Other");
		return true;
		}
		if (Loader.has(s, "Repair", "Other", "Other")) {
			Player p = TheAPI.getPlayer(args[2]);
			if(p==null) {
				Loader.notOnline(s, args[2]);
				return true;
			}
			if (args[0].equalsIgnoreCase("all")) {
				ItemStack[] items = p.getInventory().getContents();
				for (ItemStack t : items) {
					if (t != null && t.getType()!=Material.AIR)
						t.setDurability((short) 0);
				}
				Loader.sendMessages(s, "Repair.All.Other.Sender");
				Loader.sendMessages(p, "Repair.All.Other.Receiver", Placeholder.c().replace("%player%", s.getName())
						.replace("%playername%", s.getName()));
				return true;
			}
			Material hand = p.getItemInHand().getType();
			if (hand != Material.AIR) {
				p.getItemInHand().setDurability((short) 0);
				Loader.sendMessages(s, "Repair.Hand.Other.Sender");
				Loader.sendMessages(p, "Repair.Hand.Other.Receiver", Placeholder.c().replace("%player%", s.getName())
						.replace("%playername%", s.getName()));
			return true;
			}
			Loader.sendMessages(s, "Missing.TargetHandEmpty", Placeholder.c().replace("%player%", p.getName())
					.replace("%playername%", p.getDisplayName()));
			return true;
		}
		Loader.noPerms(s, "Repair", "Other", "Other");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] args) {
		List<String> c = new UnsortedList<String>();
		List<String> s = Arrays.asList("Hand", "All");
		if (args.length == 1)
			if (Loader.has(arg0, "Repair", "Other"))
				c.addAll(StringUtil.copyPartialMatches(args[0], s, new UnsortedList<>()));
		if(args.length==2)return null;
		return c;
	}

}

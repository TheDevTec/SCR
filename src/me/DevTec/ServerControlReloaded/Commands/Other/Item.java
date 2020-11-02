package me.DevTec.ServerControlReloaded.Commands.Other;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.DevTec.ServerControlReloaded.SCR.Loader;

public class Item implements CommandExecutor, TabCompleter{

	public enum flag{
		Hide_Enchants,
		Unbreakable;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Item", "Other")) {
			if(s instanceof Player) {
				if(args.length==0) {
					Loader.Help(s, "Item", "Other");
					List<String> c = new ArrayList<>();
					List<String> l = new ArrayList<>();		
					int count=0;
					if (s instanceof Player && ((Player)s).getItemInHand().getItemMeta().hasLore()) {
						for (String d : ((Player)s).getItemInHand().getItemMeta().getLore()) {
							l.add(String.valueOf(count).toString());
							count++;
						}
						if (!l.isEmpty()) {
							c.addAll(l); 
						}
					}
					onTabComplete(s, arg1, arg2, args);
					//TheAPI.bcMsg(((Player)s).getItemInHand().getItemMeta().getLore().toString());
				}
			}
			return true;
		}
		Loader.noPerms(s, "Item", "Other");		
		return true;
	}
	@SuppressWarnings("deprecation")
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		List<String> l = new ArrayList<>();		
		if (args.length==1) {
			if(s.hasPermission("SCR.Item.Name")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("name"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Lore")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("lore"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Flag")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("flag"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Nbt")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("nbt"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Durability")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("durability"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Type")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("type"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Info")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("info"), new ArrayList<>()));
			}
		}
		if(args.length==2) {
			if(s.hasPermission("SCR.Item.Name")&&args[0].toString().contains("name")) {
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("<value>"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Lore")&&args[0].toString().contains("lore")) {
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("add", "lines", "remove","set"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Flag")&&args[0].toString().contains("flag")) {
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList(flag.Hide_Enchants.toString(),flag.Unbreakable.toString()), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Durability")&&args[0].toString().contains("durability")) {
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("<int>"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Type")&&args[0].toString().contains("type")) {
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("<material>"), new ArrayList<>()));
			}
		}
		if(args.length==3) {
			if(args.toString().contains("add")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					c.addAll(StringUtil.copyPartialMatches(args[2], Arrays.asList("?"), new ArrayList<>()));
				}
			}
			if(args.toString().contains("remove")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					int count=0;
					if (s instanceof Player && ((Player)s).getItemInHand().getItemMeta().hasLore()) {
						for (String d : ((Player)s).getItemInHand().getItemMeta().getLore()) {
							l.add(String.valueOf(count++).toString());
						}
						if (!l.isEmpty()) {
							c.addAll(l); 
						}
					}
				}
			}
		}
		return c;
	}
}
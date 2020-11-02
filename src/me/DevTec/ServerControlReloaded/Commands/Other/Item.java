package me.DevTec.ServerControlReloaded.Commands.Other;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Item implements CommandExecutor, TabCompleter{
	private static List<String> flags = new ArrayList<>();
	private static List<String> f = new ArrayList<>();
	static {
		for(ItemFlag a : ItemFlag.values())flags.add(a.name());
		flags.add("UNBREAKABLE");
		f.add("add");
		f.add("remove");
		f.add("list");
		f.add("set");
	}
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Item", "Other")||Loader.has(s, "Item", "Other", "SetName")||Loader.has(s, "Item", "Other", "SetLore")
				||Loader.has(s, "Item", "Other", "Flag")||Loader.has(s, "Item", "Other", "Unbreakable")
				||Loader.has(s, "Item", "Other", "HideEnchants")||Loader.has(s, "Item", "Other", "Nbt")
				||Loader.has(s, "Item", "Other", "Durability")||Loader.has(s, "Item", "Other", "Type")
				||Loader.has(s, "Item", "Other", "Info")) {
			if(s instanceof Player) {
				if(args.length==0) {
					if(s.hasPermission("SCR.Item.Name")) {
						TheAPI.msg(Loader.getTranslation("Item.NameHelp").toString(), s);
					}
					if(s.hasPermission("SCR.Item.Lore")) {
						TheAPI.msg(Loader.getTranslation("Item.NameHelp").toString(), s);
					}
					if(s.hasPermission("SCR.Item.Flag")) {
						TheAPI.msg(Loader.getTranslation("Item.NameHelp").toString(), s);
					}
					if(s.hasPermission("SCR.Item.Nbt")) {
						TheAPI.msg(Loader.getTranslation("Item.NameHelp").toString(), s);
					}
					if(s.hasPermission("SCR.Item.Durability")) {
						TheAPI.msg(Loader.getTranslation("Item.NameHelp").toString(), s);
					}
					if(s.hasPermission("SCR.Item.Type")) {
						TheAPI.msg(Loader.getTranslation("Item.NameHelp").toString(), s);
					}
					if(s.hasPermission("SCR.Item.Info")) {
						TheAPI.msg(Loader.getTranslation("Item.NameHelp").toString(), s);
					}
					return true; 
				}
				if(args[0].equalsIgnoreCase("name")) {
					if(args.length==1) {
						Loader.Help(s, "Item", "Other");
					}
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
				
		if (args.length==1) {
			if(s.hasPermission("SCR.Item.Name")) {
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("name")));
			}
			if(s.hasPermission("SCR.Item.Lore")) {
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("lore")));
			}
			if(s.hasPermission("SCR.Item.Flag")) {
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("flag")));
			}
			if(s.hasPermission("SCR.Item.Nbt")) {
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("nbt")));
			}
			if(s.hasPermission("SCR.Item.Durability")) {
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("durability")));
			}
			if(s.hasPermission("SCR.Item.Type")) {
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("type")));
			}
			if(s.hasPermission("SCR.Item.Info")) {
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("info")));
			}
		}
		if(args.length==2) {
			if(s.hasPermission("SCR.Item.Name")&&args[0].toString().contains("name")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("<value>")));
			}
			if(s.hasPermission("SCR.Item.Lore")&&args[0].toString().contains("lore")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], f));
			}
			if(s.hasPermission("SCR.Item.Flag")&&args[0].toString().contains("flag")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], flags));
			}
			if(s.hasPermission("SCR.Item.Durability")&&args[0].toString().contains("durability")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("<int>")));
			}
			if(s.hasPermission("SCR.Item.Type")&&args[0].toString().contains("type")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("<material>")));
			}
		}
		if(args.length==3) {
			if(args[1].contains("add")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					c.addAll(StringUtils.copyPartialMatches(args[2], Arrays.asList("?")));
				}
			}//???
			if(args[1].contains("remove")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					List<String> l = new ArrayList<>();
					if (s instanceof Player && ((Player)s).getItemInHand().getItemMeta().hasLore())
						for (int count = 0; count < ((Player)s).getItemInHand().getItemMeta().getLore().size(); ++count)
							l.add(count+"");
						c.addAll(StringUtils.copyPartialMatches(args[2], l)); //debil :D xDD vím jak tì nasrat :DD

				}
			}//zakomponuj všude toto
		}
		return c;
	}
}

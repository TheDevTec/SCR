package Commands.Other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.Loader;

public class Item implements CommandExecutor, TabCompleter{

	public enum flag{
		Hide_Enchants,
		Unbreakable;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Item", "Other")) {
			if(s instanceof Player) {
				if(args.length==0) {
					Loader.Help(s, "Item", "Other");
				}
			}
			return true;
		}
		Loader.noPerms(s, "Item", "Other");		
		return true;
	}
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes", "unused" })
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
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("ItemName"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Lore")&&args[0].toString().contains("lore")) {
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("add", "lines", "remove"), new ArrayList<>()));
			}
			if(s.hasPermission("SCR.Item.Flag")&&args[0].toString().contains("flag")) {
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList(flag.Hide_Enchants.toString(),flag.Unbreakable.toString()), new ArrayList<>()));
			}
		}
		if(args.length==3) {
			if(args.toString().contains("add")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("ItemLore"), new ArrayList<>()));
				}
			}
			if(args.toString().contains("remove")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					int count=0;
					if (s instanceof Player && ((Player)s).getItemInHand() != null
							&& ((Player)s).getItemInHand().hasItemMeta() && ((Player)s).getItemInHand().getItemMeta().hasLore()) {
						for (String d : ((Player)s).getItemInHand().getItemMeta().getLore()) {
							l.add((new StringBuilder(String.valueOf(count))).toString());
							count++;
						}
						if (!l.isEmpty()) {
							c.addAll(StringUtil.copyPartialMatches(args[2], l, new ArrayList())); 
						}
					}
				}
			}
		}
		return c;
	}
}

package me.DevTec.ServerControlReloaded.Commands.Other;



import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.Collections.UnsortedList;

public class Item implements CommandExecutor, TabCompleter{
	private static List<String> flags = new UnsortedList<>();
	private static List<String> f = new UnsortedList<>();
	static {
		try {
		for(ItemFlag a : ItemFlag.values())flags.add(a.name());
		}catch(Exception | NoSuchFieldError | NoSuchMethodError e) {}
		flags.add("UNBREAKABLE");
		f.add("add");
		f.add("remove");
		f.add("list");
		f.add("set");
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Item", "Other")&&(Loader.has(s, "Item", "Other", "SetName")||Loader.has(s, "Item", "Other", "SetLore")
				||Loader.has(s, "Item", "Other", "Flag")||Loader.has(s, "Item", "Other", "Nbt")
				||Loader.has(s, "Item", "Other", "Durability")||Loader.has(s, "Item", "Other", "Type")
				||Loader.has(s, "Item", "Other", "Info"))) {
			if(s instanceof Player) {
				if(args.length==0) {
					checker(s);
					return true; 
				}
				Player p = (Player)s;
		        ItemStack item = p.getItemInHand();
		        ItemMeta m = item.getItemMeta();
		        if(item.getType()==Material.AIR) {
		        	TheAPI.sendTitle(p, Loader.getTranslation("Item.NoItem").toString(), "");
		        	return true;
		        }
				if(args[0].equalsIgnoreCase("name")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Name");
						return true;
					}
					String name = StringUtils.buildString(1, args);
					m.setDisplayName(TheAPI.colorize(name));
					item.setItemMeta(m);
		            Loader.sendMessages(s, "Item.Name", Placeholder.c().replace("%item%", item.getType().name()).replace("%name%", name));
					return true;
				}
				if(args[0].equalsIgnoreCase("lore")) {
					if(args.length==1) {
						for (String c : f) Loader.advancedHelp(s,"Item", "Other", "Lore", c.toUpperCase());
						return true;
					}
					if(args[1].equalsIgnoreCase("add")) {
					  if(args.length==2) {
						  Loader.advancedHelp(s, "Item", "Other","Lore" ,"Add");
						  return true;
					  }
					  String name = StringUtils.buildString(2, args);
		              List<String> lore = new UnsortedList<>();
		              if (m.getLore() != null)lore = m.getLore();	              
		              lore.add(TheAPI.colorize(name));
		              m.setLore(lore);
		              item.setItemMeta(m);
		              Loader.sendMessages(s, "Item.Lore.Added", Placeholder.c().replace("%item%", item.getType().name()).replace("%line%", name));
		              return true;
					}
					if(args[1].equalsIgnoreCase("remove")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other","Lore" ,"Remove");
							return true;
						}
						try {
			                List<String> lore = m.getLore();
			                if(lore.isEmpty()) {return true;}
			                lore.remove(StringUtils.getInt(args[2]));
			                m.setLore(lore);
			                item.setItemMeta(m);
			                Loader.sendMessages(s, "Item.Lore.Removed", Placeholder.c().replace("%item%", item.getType().name()).replace("%line%", args[2].toString()));
			                return true;
			              } catch (Exception e) {
			            	  Loader.sendMessages(s, "Item.Lore.Error", Placeholder.c().replace("%error%", e.getMessage().trim()));
			            	  return true;
			              } 
					}
					if(args[1].equalsIgnoreCase("set")) {
						if(args.length==2||args.length==3) {
							Loader.advancedHelp(s, "Item", "Other","Lore" ,"Set");
							return true;
						}
						try {
						String name = StringUtils.buildString(3, args);
			              List<String> lore = new UnsortedList<>();
			              if (m.getLore() != null)
			                for (String ss : m.getLore())
			                  lore.add(TheAPI.colorize(ss));
			              int line = Integer.parseInt(args[2])==0?0:Integer.parseInt(args[2]);
			              lore.set(line, name);
			              m.setLore(lore);			              
			              item.setItemMeta(m);
			              Loader.sendMessages(s, "Item.Lore.Set", Placeholder.c().replace("%item%", item.getType().name()).replace("%line%", line+"").replace("%lore%", name));
			              return true;
						}catch(Exception e) {
							Loader.sendMessages(s, "Item.Lore.Error", Placeholder.c().replace("%error%", e.getMessage().trim()));
			            	return true;
						}
					}
					if (args[1].equalsIgnoreCase("list")) {
			              int tests = 0;
			              Loader.sendMessages(s, "Item.Lore.ListItem", Placeholder.c().replace("%item%", item.getType().name()).replace("%lines%", (m.getLore()!=null?m.getLore().size():0)+""));
			              List<String> lore = m.getLore();
			              if (lore != null)
			                for (String ss : lore) {
			                	Loader.sendMessages(s, "Item.Lore.ListLore", Placeholder.c().replace("%item%", item.getType().name()).replace("%lines%", (m.getLore()!=null?m.getLore().size():0)+"").replace("%position%", tests+"").replace("%lore%", ss));
			                	tests++;
			                }
			              return true;
			            }
					return true;
				}
				if(args[0].equalsIgnoreCase("flag")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Flag");
						return true;
					}
					//list
					//set <flag> <value>
					//remove <flag>
					//add <flag>
					//get <flag>
					return true;
				}
				if(args[0].equalsIgnoreCase("durability")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Durability");
						return true;
					}
					//get
					//set <int>
					return true;
				}
				if(args[0].equalsIgnoreCase("nbt")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "NBT");
						return true;
					}
					//get
					//set <value>
				}
				if(args[0].equalsIgnoreCase("type")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Type");
						return true;
					}
					//get
					//set <value>
				}
			}
			return true;
		}
		Loader.noPerms(s, "Item", "Other");		
		return true;
	}
	
	private void checker(CommandSender s) {
		if(Loader.has(s, "Item", "Other", "SetName")) Loader.advancedHelp(s, "Item","Other" ,"Name");		
		if(Loader.has(s, "Item", "Other", "SetLore"))for (String c : f) Loader.advancedHelp(s,"Item", "Other", "Lore", c);
		if(Loader.has(s, "Item", "Other", "Flag")) Loader.advancedHelp(s, "Item","Other" ,"Flag");		
		if(Loader.has(s, "Item", "Other", "Nbt")) Loader.advancedHelp(s, "Item","Other" ,"Nbt");		
		if(Loader.has(s, "Item", "Other", "Durability")) Loader.advancedHelp(s, "Item","Other" ,"Durability");		
		if(Loader.has(s, "Item", "Other", "Type")) Loader.advancedHelp(s, "Item","Other" ,"Name");		
		if(Loader.has(s, "Item", "Other", "Info")) Loader.advancedHelp(s, "Item","Other" ,"Info");return;
	}	
	
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new UnsortedList<>();
		Player p = (Player)s;
		if(p.getItemInHand().getType()==Material.AIR) {
			TheAPI.sendTitle(p, Loader.getTranslation("Item.NoItem").toString()," "); 			
			c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("")));
			return c;
		}
		if (args.length==1) {			
			if(s.hasPermission("SCR.Item.Name")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("name")));			
			if(s.hasPermission("SCR.Item.Lore")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("lore")));			
			if(s.hasPermission("SCR.Item.Flag")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("flag")));			
			if(s.hasPermission("SCR.Item.Nbt")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("nbt")));			
			if(s.hasPermission("SCR.Item.Durability")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("durability")));			
			if(s.hasPermission("SCR.Item.Type")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("type")));			
			if(s.hasPermission("SCR.Item.Info")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("info")));			
		}
		if(args.length==2) {
			if(s.hasPermission("SCR.Item.Name")&&args[0].contains("name"))c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("?")));
			if(s.hasPermission("SCR.Item.Lore")&&args[0].contains("lore"))c.addAll(StringUtils.copyPartialMatches(args[1], f));
			if(s.hasPermission("SCR.Item.Flag")&&args[0].contains("flag"))c.addAll(StringUtils.copyPartialMatches(args[1], flags));
			if(s.hasPermission("SCR.Item.Durability")&&args[0].contains("durability"))c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("?")));
			if(s.hasPermission("SCR.Item.Type")&&args[0].contains("type"))c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("?")));
		}		
		if(args.length==3) {			
			if(args[1].contains("add"))if(s.hasPermission("SCR.Item.Lore"))c.addAll(StringUtils.copyPartialMatches(args[2], Arrays.asList("?")));
			if(args[1].contains("remove")) {				
				if(s.hasPermission("SCR.Item.Lore")) {					
					List<String> l = new UnsortedList<>();																																																																																																						
					if (s instanceof Player && ((Player)s).getItemInHand().getItemMeta().hasLore())
						for (int count = 0; count < ((Player)s).getItemInHand().getItemMeta().getLore().size(); ++count)
							l.add(count+"");
						c.addAll(StringUtils.copyPartialMatches(args[2], l)); 

				}
			}
			if(args[1].contains("set")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					List<String> l = new UnsortedList<>();
					if (s instanceof Player && ((Player)s).getItemInHand().getItemMeta().hasLore())
						for (int count = 0; count < ((Player)s).getItemInHand().getItemMeta().getLore().size(); ++count)
							l.add(count+"");
						c.addAll(StringUtils.copyPartialMatches(args[2], l)); 
				}
			}
		}
		return c;
	}
}

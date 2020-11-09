package me.DevTec.ServerControlReloaded.Commands.Other;


import java.util.ArrayList;
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
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Item", "Other")||Loader.has(s, "Item", "Other", "SetName")||Loader.has(s, "Item", "Other", "SetLore")
				||Loader.has(s, "Item", "Other", "Flag")||Loader.has(s, "Item", "Other", "Unbreakable")
				||Loader.has(s, "Item", "Other", "HideEnchants")||Loader.has(s, "Item", "Other", "Nbt")
				||Loader.has(s, "Item", "Other", "Durability")||Loader.has(s, "Item", "Other", "Type")
				||Loader.has(s, "Item", "Other", "Info")) {
			if(s instanceof Player) {
				if(args.length==0) {
					checker(s);
					return true; 
				}
				Player p = (Player)s;
		        ItemStack item = p.getItemInHand();
		        ItemMeta m = item.getItemMeta();
		        if(item.getType()==Material.AIR) {TheAPI.sendTitle(p, Loader.getTranslation("Item.NoItem").toString(), " ");return true;}
				if(args[0].equalsIgnoreCase("name")) {
					if(args.length==1) {
						TheAPI.msg(Loader.getTranslation("Item.NameHelp").toString(), s);
						return true;
					}
					String name = "";
					for (int i=0;i<args.length;i++)						
						name=String.valueOf(name)+args[i]+" ";
					name=name.replaceFirst(String.valueOf(args[0])+" ", "");
					name=name.substring(0, name.length()-1);
					m.setDisplayName(TheAPI.colorize(name));
					item.setItemMeta(m);
					TheAPI.msg(Loader.getTranslation("Item.Name").toString().replace("%item%", item.getType().name()).replace("%name%", name), s);
					return true;
				}
				if(args[0].equalsIgnoreCase("lore")) {
					if(args.length==1) {
						TheAPI.msg(Loader.getTranslation("Item.LoreHelp1").toString(), s);
						TheAPI.msg(Loader.getTranslation("Item.LoreHelp2").toString(), s);
						TheAPI.msg(Loader.getTranslation("Item.LoreHelp3").toString(), s);
						TheAPI.msg(Loader.getTranslation("Item.LoreHelp4").toString(), s);
						return true;
					}
					if(args[1].contains("add")) {
						if(args.length==2) {
							TheAPI.msg(Loader.getTranslation("Item.LoreHelp1").toString(), s);							
							return true;
						}
					String name = "";
		              for (int i = 0; i < args.length; i++)
		                name = String.valueOf(name) + args[i] + " "; 
		              name = name.replaceFirst(String.valueOf(args[0]) + " " + args[1] + " ", "");
		              name = name.substring(0, name.length() - 1);
		              List<String> lore = new ArrayList<>();
		              if (m.getLore() != null)
		                for (String ss : m.getLore())
		                  lore.add(TheAPI.colorize(ss));		              
		              lore.add(TheAPI.colorize(name));
		              m.setLore(lore);
		              item.setItemMeta(m);
		              TheAPI.msg(Loader.getTranslation("Item.Lore.Added").toString().replace("%line%",name ), s);
		              return true;
					}
					if(args[1].contains("remove")) {
						if(args.length==2) {
							TheAPI.msg(Loader.getTranslation("Item.LoreHelp2").toString(), s);
							return true;
						}
						try {
			                List<String> lore = m.getLore();
			                lore.remove(StringUtils.getInt(args[2]));
			                m.setLore(lore);
			                item.setItemMeta(m);
			                TheAPI.msg(Loader.getTranslation("Item.Lore.Removed").toString().toString().replace("%line%", args[2].toString()), s);
			                return true;
			              } catch (Exception e) {
			            	  TheAPI.msg(Loader.getTranslation("Item.Lore.Error").toString().replace("%error%", e.getMessage().trim()), s);
			            	  return true;
			              } 
					}
					if(args[1].contains("set")) {
						if(args.length==2||args.length==3) {
							TheAPI.msg(Loader.getTranslation("Item.LoreHelp3").toString(), s);
							return true;
						}
						try {
						String name = "";
			              for (int i = 0; i < args.length; i++)
			                name = String.valueOf(name) + args[i] + " "; 
			              name = name.replaceFirst(String.valueOf(args[0]) + " " + args[1] + " "+args[2]+" ", "");
			              name = name.substring(0, name.length() - 1);
			              List<String> lore = new ArrayList<>();
			              if (m.getLore() != null)
			                for (String ss : m.getLore())
			                  lore.add(TheAPI.colorize(ss));
			              int line = Integer.parseInt(args[2])==0?0:Integer.parseInt(args[2]);
			              lore.set(line, name);
			              m.setLore(lore);			              
			              item.setItemMeta(m);
			              TheAPI.msg(Loader.getTranslation("Item.Lore.Set").toString()
			            		  .replace("%lore%", name).replace("%line%", String.valueOf(line)), s);
			              return true;
						}catch(Exception e) {
			            	  TheAPI.msg(Loader.getTranslation("Item.Lore.Error").toString()
			            			  .replace("%error%", e.getMessage().trim()), s);
			            	  return true;
						}
					}
					if (args[1].equalsIgnoreCase("list")) {
			              int tests = 0;
			              TheAPI.msg(Loader.getTranslation("Item.Lore.ListItem").toString()
			            		  .replace("%item%", item.getType().name().toString()),s);
			              List<String> lore = m.getLore();
			              if (lore != null)
			                for (String ss : lore) {
			                  TheAPI.msg(Loader.getTranslation("Item.Lore.ListLore").toString()
			                		  .replace("%position%", Integer.toString(tests)).replace("%lore%", ss), s);
			                  tests++;
			                }  
			              return true;
			            }
					return true;
				}
				if(args[1].equalsIgnoreCase("flag")) {
					
				}
			}
			return true;
		}
		Loader.noPerms(s, "Item", "Other");		
		return true;
	}
	
	private void checker(CommandSender s) {
		if(s.hasPermission("SCR.Item.Name")) {
			TheAPI.msg(Loader.getTranslation("Item.NameHelp").toString(), s);
		}
		if(s.hasPermission("SCR.Item.Lore")) {
			TheAPI.msg(Loader.getTranslation("Item.LoreHelp1").toString(), s);
			TheAPI.msg(Loader.getTranslation("Item.LoreHelp2").toString(), s);
			TheAPI.msg(Loader.getTranslation("Item.LoreHelp3").toString(), s);
			TheAPI.msg(Loader.getTranslation("Item.LoreHelp4").toString(), s);
		}
		if(s.hasPermission("SCR.Item.Flag")) {
			TheAPI.msg(Loader.getTranslation("Item.FlagHelp").toString(), s);
		}
		if(s.hasPermission("SCR.Item.Nbt")) {
			TheAPI.msg(Loader.getTranslation("Item.NbtHelp").toString(), s);
		}
		if(s.hasPermission("SCR.Item.Durability")) {
			TheAPI.msg(Loader.getTranslation("Item.DurabilityHelp").toString(), s);
		}
		if(s.hasPermission("SCR.Item.Type")) {
			TheAPI.msg(Loader.getTranslation("Item.TypeHelp").toString(), s);
		}
		if(s.hasPermission("SCR.Item.Info")) {
			TheAPI.msg(Loader.getTranslation("Item.InfoHelp").toString(), s);
		}
	}	

	
	
	@SuppressWarnings("deprecation")
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		Player p = (Player)s;
		if(p.getItemInHand().getType()==Material.AIR) {
			TheAPI.sendTitle(p, Loader.getTranslation("Item.NoItem").toString()," "); 			
			c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("")));
			return c;
		}
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
			if(s.hasPermission("SCR.Item.Name")&&args[0].contains("name")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("?")));
			}
			if(s.hasPermission("SCR.Item.Lore")&&args[0].contains("lore")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], f));
			}
			if(s.hasPermission("SCR.Item.Flag")&&args[0].contains("flag")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], flags));
			}
			if(s.hasPermission("SCR.Item.Durability")&&args[0].contains("durability")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("?")));
			}
			if(s.hasPermission("SCR.Item.Type")&&args[0].contains("type")) {
				c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("?")));
			}
		}
		
		if(args.length==3) {
			if(args[1].contains("add")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					c.addAll(StringUtils.copyPartialMatches(args[2], Arrays.asList("?")));
				}
			}
			if(args[1].contains("remove")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					List<String> l = new ArrayList<>();
					if (s instanceof Player && ((Player)s).getItemInHand().getItemMeta().hasLore())
						for (int count = 0; count < ((Player)s).getItemInHand().getItemMeta().getLore().size(); ++count)
							l.add(count+"");
						c.addAll(StringUtils.copyPartialMatches(args[2], l)); 

				}
			}
			if(args[1].contains("set")) {
				if(s.hasPermission("SCR.Item.Lore")) {
					List<String> l = new ArrayList<>();
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

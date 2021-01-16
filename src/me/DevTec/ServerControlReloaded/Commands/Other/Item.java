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
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.reflections.Ref;

public class Item implements CommandExecutor, TabCompleter{
	private static List<String> flags = new ArrayList<>();
	private static List<String> f = new ArrayList<>();
	static {
		try {
		for(ItemFlag a : ItemFlag.values())flags.add(a.name());
		}catch(Exception | NoSuchFieldError | NoSuchMethodError e) {}
		f.add("add");
		f.add("remove");
		f.add("list");
		f.add("set");
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Item", "Other")&&(Loader.has(s, "Item", "Other", "Name")||Loader.has(s, "Item", "Other", "Lore")
				||Loader.has(s, "Item", "Other", "Flag")||Loader.has(s, "Item", "Other", "Nbt")
				||Loader.has(s, "Item", "Other", "Durability")||Loader.has(s, "Item", "Other", "Type"))) {
			if(s instanceof Player) {
				if(args.length==0) {
					if(Loader.has(s, "Item", "Other", "Name")) Loader.advancedHelp(s, "Item","Other" ,"Name");		
					if(Loader.has(s, "Item", "Other", "Lore"))for (String c : f) Loader.advancedHelp(s,"Item", "Other", "Lore", c);
					if(Loader.has(s, "Item", "Other", "Flag")) Loader.advancedHelp(s, "Item","Other" ,"Flag");		
					if(Loader.has(s, "Item", "Other", "Nbt")) Loader.advancedHelp(s, "Item","Other" ,"Nbt");		
					if(Loader.has(s, "Item", "Other", "Durability")) Loader.advancedHelp(s, "Item","Other" ,"Durability");		
					if(Loader.has(s, "Item", "Other", "Type")) Loader.advancedHelp(s, "Item","Other" ,"Type");
					return true; 
				}
				Player p = (Player)s;
		        ItemStack item = p.getItemInHand();
		        if(item==null||item.getType()==Material.AIR) {
		        	Loader.sendMessages(s, "Item.NoItem");
		        	return true;
		        }
		        ItemMeta m = item.getItemMeta();
				if(args[0].equalsIgnoreCase("name") && Loader.has(s, "Item", "Other", "Name")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Name");
						return true;
					}
					String name = TheAPI.colorize(StringUtils.buildString(1, args));
					m.setDisplayName(name);
					item.setItemMeta(m);
		            Loader.sendMessages(s, "Item.Name", Placeholder.c().replace("%item%", item.getType().name()).replace("%name%", name));
					return true;
				}
				if(args[0].equalsIgnoreCase("lore") && Loader.has(s, "Item", "Other", "Lore")) {
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
		              List<String> lore = m.getLore();
		              if (lore == null)lore = new ArrayList<>();	           
		              String text = TheAPI.colorize(name);
		              lore.add(text);
		              m.setLore(lore);
		              item.setItemMeta(m);
		              Loader.sendMessages(s, "Item.Lore.Added", Placeholder.c().replace("%item%", item.getType().name()).replace("%line%", text));
		              return true;
					}
					if(args[1].equalsIgnoreCase("remove")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other","Lore" ,"Remove");
							return true;
						}
						try {
			                List<String> lore = m.getLore();
			                if(lore==null||lore.isEmpty()) {
			                	Loader.sendMessages(s, "Item.Lore.Removed", Placeholder.c().replace("%item%", item.getType().name()).replace("%line%", args[2].toString()));
				                return true;
			                }
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
						String name = TheAPI.colorize(StringUtils.buildString(3, args));
			              List<String> lore = m.getLore();
			              if(lore==null || lore.isEmpty()) {
								Loader.sendMessages(s, "Item.Lore.Error", Placeholder.c().replace("%error%", "Lore is empty"));
				            	return true;
			              }
			              int line = StringUtils.getInt(args[2]);
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
			              List<String> lore = m.getLore();
			              if(lore==null || lore.isEmpty()) {
								Loader.sendMessages(s, "Item.Lore.Error", Placeholder.c().replace("%error%", "Lore is empty"));
				            	return true;
			              }
			              Loader.sendMessages(s, "Item.Lore.ListItem", Placeholder.c().replace("%item%", item.getType().name()).replace("%lines%", lore.size()+""));
			              for (String ss : lore) {
			                	Loader.sendMessages(s, "Item.Lore.ListLore", Placeholder.c().replace("%item%", item.getType().name()).replace("%lore%", ss).replace("%position%", tests+""));
			                	++tests;
			                }
			              return true;
			            }
					return true;
				}
				
				if(args[0].equalsIgnoreCase("flag") && Loader.has(s, "Item", "Other", "Flag")) {
					if(!TheAPI.isNewerThan(7))return true;
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Flag", "Add");
						Loader.advancedHelp(s, "Item", "Other", "Flag", "Remove");
						Loader.advancedHelp(s, "Item", "Other", "Flag", "List");
						return true;
					}
					if(args[1].equalsIgnoreCase("list")) {
						ItemStack it = new ItemStack(((Player) s).getItemInHand());		
						String flags = "";
						for (ItemFlag itf : it.getItemMeta().getItemFlags())
							flags+=", "+itf.name();
						flags=flags.substring(2);
						Loader.sendMessages(s, "Item.Flag.List", Placeholder.c().replace("%item%", it.getType().name()).replace("%flags%", flags));
						return true;
					}
					if(args[1].equalsIgnoreCase("add")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Flag", "Add");
							return true;
						}
						try {
							ItemFlag type = ItemFlag.valueOf(args[2]);
							ItemMeta ma = p.getItemInHand().getItemMeta();
							m.addItemFlags(type);
							p.getItemInHand().setItemMeta(ma);
							Loader.sendMessages(s,"Item.Flag.Added",Placeholder.c().add("%flag%", type.name()));
						}catch(Exception | NoSuchFieldError e) {
							Loader.sendMessages(s,"Missing.Flag",Placeholder.c().add("%flag%", args[2]));
						}
						return true;
					}
					if(args[1].equalsIgnoreCase("remove")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Flag", "Remove");
							return true;
						}
						try {
							ItemFlag type = ItemFlag.valueOf(args[2]);
							ItemMeta ma = p.getItemInHand().getItemMeta();
							m.removeItemFlags(type);
							p.getItemInHand().setItemMeta(ma);
							Loader.sendMessages(s,"Item.Flag.Removed",Placeholder.c().add("%flag%", type.name()));
						}catch(Exception | NoSuchFieldError e) {
							Loader.sendMessages(s,"Missing.Flag",Placeholder.c().add("%flag%", args[2]));
						}
						return true;
					}
					Loader.advancedHelp(s, "Item", "Other", "Flag", "Add");
					Loader.advancedHelp(s, "Item", "Other", "Flag", "Remove");
					Loader.advancedHelp(s, "Item", "Other", "Flag", "List");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("durability") && Loader.has(s, "Item", "Other", "Durability")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Durability", "Get");
						Loader.advancedHelp(s, "Item", "Other", "Durability", "Set");
						return true;
					}
					if(args[1].equalsIgnoreCase("get")) {
						Loader.sendMessages(s,"Item.Durability.Get",Placeholder.c().add("%durability%", ((Player) s).getItemInHand().getDurability()+""));
						return true;
					}
					if(args[1].equalsIgnoreCase("set")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Durability", "Set");
							return true;
						}
						short i = StringUtils.getShort(args[2]);
						p.getItemInHand().setDurability(i);
						Loader.sendMessages(s,"Item.Durability.Set",Placeholder.c().add("%durability%", i+""));
						return true;
					}
				}
				
				if(args[0].equalsIgnoreCase("nbt") && Loader.has(s, "Item", "Other", "Nbt")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Nbt", "Get");
						Loader.advancedHelp(s, "Item", "Other", "Nbt", "Set");
						return true;
					}
					if(args[1].equalsIgnoreCase("get")) {
						Object stack = Ref.invokeNulled(Ref.method(Ref.craft("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class),((Player) s).getItemInHand());
						Loader.sendMessages(s,"Item.Nbt.Get",Placeholder.c().add("%nbt%", Ref.invoke(stack, "getOrCreateTag").toString()));
						return true;
					}
					if(args[1].equalsIgnoreCase("set")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Nbt", "Set");
							return true;
						}
						Object itemstack = Ref.invokeNulled(Ref.method(Ref.craft("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class), ((Player)s).getItemInHand());
						Object nbt = Ref.invokeNulled(Ref.method(Ref.nms("MojangsonParser"), "parse", String.class), StringUtils.buildString(2, args));
						Ref.invoke(itemstack, Ref.method(Ref.nms("NBTTagCompound"), "setTag", Ref.nms("NBTTagCompound")), nbt);
						p.getItemInHand().setItemMeta((ItemMeta) Ref.invokeNulled(Ref.method(Ref.craft("inventory.CraftItemStack"), "getItemMeta", Ref.nms("ItemStack")), itemstack));
						Loader.sendMessages(s,"Item.Nbt.Set",Placeholder.c().add("%nbt%", nbt.toString()));
						return true;
					}
				}
				
				if(args[0].equalsIgnoreCase("type") && Loader.has(s, "Item", "Other", "Type")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Type", "Get");
						Loader.advancedHelp(s, "Item", "Other", "Type", "Set");
						return true;
					}
					if(args[1].equalsIgnoreCase("get")) {
						Loader.sendMessages(s,"Item.Type.Get",Placeholder.c().add("%type%", ((Player) s).getItemInHand().getType().name()));
						return true;
					}
					if(args[1].equalsIgnoreCase("set")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Nbt", "Set");
							return true;
						}
						try {
							Material type = Material.valueOf(args[2]);
							p.getItemInHand().setType(type);
							Loader.sendMessages(s,"Item.Type.Set",Placeholder.c().add("%type%", type.name()));
						}catch(Exception e) {
							Loader.sendMessages(s,"Missing.Material",Placeholder.c().add("%material%", args[2]));
						}
						return true;
					}
				}
				if(Loader.has(s, "Item", "Other", "Name")) Loader.advancedHelp(s, "Item","Other" ,"Name");		
				if(Loader.has(s, "Item", "Other", "Lore"))for (String c : f) Loader.advancedHelp(s,"Item", "Other", "Lore", c);
				if(Loader.has(s, "Item", "Other", "Flag")) Loader.advancedHelp(s, "Item","Other" ,"Flag");		
				if(Loader.has(s, "Item", "Other", "Nbt")) Loader.advancedHelp(s, "Item","Other" ,"Nbt");		
				if(Loader.has(s, "Item", "Other", "Durability")) Loader.advancedHelp(s, "Item","Other" ,"Durability");		
				if(Loader.has(s, "Item", "Other", "Type")) Loader.advancedHelp(s, "Item","Other" ,"Type");
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "Item", "Other");		
		return true;
	}
	
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length==1) {			
			if(Loader.has(s, "Item", "Other", "Name")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Name")));			
			if(Loader.has(s, "Item", "Other", "Lore")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Lore")));			
			if(Loader.has(s, "Item", "Other", "Flag") && TheAPI.isNewerThan(7)) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Flag")));			
			if(Loader.has(s, "Item", "Other", "Nbt")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Nbt")));			
			if(Loader.has(s, "Item", "Other", "Durability"))c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Durability")));			
			if(Loader.has(s, "Item", "Other", "Type")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Type")));			
			return c;
		}
		if(args[0].equalsIgnoreCase("name") && Loader.has(s, "Item", "Other", "Name"))
			return Arrays.asList("?");
		if(args[0].equalsIgnoreCase("lore") && Loader.has(s, "Item", "Other", "Lore")) {
			if(args.length==2) {
				c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("Add","Remove","Set","List")));
				return c;
			}
			if(args[1].equalsIgnoreCase("Add"))
				return Arrays.asList("?");
			if(args.length==3) {
				if(args[1].equalsIgnoreCase("Remove") || args[1].equalsIgnoreCase("Set")) {
					List<String> lines = new ArrayList<>();
					Player p = (Player)s;
			        ItemStack item = p.getItemInHand();
			        if(item==null||item.getType()==Material.AIR)
			        	return lines;
			        if(item.hasItemMeta() && item.getItemMeta().getLore()!=null)
					for(int i = 0; i < item.getItemMeta().getLore().size(); ++i)
						lines.add(i+"");
					c.addAll(StringUtils.copyPartialMatches(args[2], lines));
					return c;
				}
			}
			if(args[1].equalsIgnoreCase("Set"))
				return Arrays.asList("?");
			return c;
		}
		if(args[0].equalsIgnoreCase("flag") && TheAPI.isNewerThan(7) && Loader.has(s, "Item", "Other", "Flag")) {
			if(args.length==2) {
				c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("Add","Remove","List")));
				return c;
			}
			if(args[1].equalsIgnoreCase("Add")){
				c.addAll(StringUtils.copyPartialMatches(args[2], flags));
				return c;
			}
			if(args.length==3) {
				if(args[1].equalsIgnoreCase("Remove")) {
					List<String> lines = new ArrayList<>();
					Player p = (Player)s;
			        ItemStack item = p.getItemInHand();
			        if(item==null||item.getType()==Material.AIR)
			        	return lines;
			        if(item.hasItemMeta())
					for(ItemFlag f : item.getItemMeta().getItemFlags())
						lines.add(f.name()+"");
					c.addAll(StringUtils.copyPartialMatches(args[2], lines));
					return c;
				}
			}
			return c;
		}
		if(args[0].equalsIgnoreCase("nbt") && Loader.has(s, "Item", "Other", "Nbt") || args[0].equalsIgnoreCase("durability") && Loader.has(s, "Item", "Other", "Durability")||args[0].equalsIgnoreCase("type") && Loader.has(s, "Item", "Other", "Type")) {
			if(args.length==2) {
				c.addAll(StringUtils.copyPartialMatches(args[1], Arrays.asList("Set","Get")));
				return c;
			}
			return c;
		}
		return c;
	}
}

package me.devtec.servercontrolreloaded.commands.other;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.json.Reader;
import me.devtec.theapi.utils.json.Writer;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.nms.nbt.NBTEdit;

public class Item implements CommandExecutor, TabCompleter{
	private static List<String> flags = new ArrayList<>();
	private static List<String> f = new ArrayList<>();
	static {
		try {
		for(ItemFlag a : ItemFlag.values())flags.add(a.name());
		}catch(Exception | NoSuchFieldError | NoSuchMethodError | NoClassDefFoundError e) {}
		f.add("add");
		f.add("remove");
		f.add("list");
		f.add("set");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Item", "Other")&&(Loader.has(s, "Item", "Other", "Name")||Loader.has(s, "Item", "Other", "Lore")
				||Loader.has(s, "Item", "Other", "Flag")||Loader.has(s, "Item", "Other", "Nbt")
				||Loader.has(s, "Item", "Other", "Durability")||Loader.has(s, "Item", "Other", "Type")||Loader.has(s, "Item", "Other", "Process"))) {
			if(s instanceof Player) {
				if(args.length==0) {
					if(Loader.has(s, "Item", "Other", "Name")) Loader.advancedHelp(s, "Item","Other" ,"Name");		
					if(Loader.has(s, "Item", "Other", "Lore"))for (String c : f) Loader.advancedHelp(s,"Item", "Other", "Lore", c);
					if(Loader.has(s, "Item", "Other", "Flag")) Loader.advancedHelp(s, "Item","Other" ,"Flag");		
					if(Loader.has(s, "Item", "Other", "Nbt")) Loader.advancedHelp(s, "Item","Other" ,"Nbt");		
					if(Loader.has(s, "Item", "Other", "Durability")) Loader.advancedHelp(s, "Item","Other" ,"Durability");		
					if(Loader.has(s, "Item", "Other", "Type")) Loader.advancedHelp(s, "Item","Other" ,"Type");
					if(Loader.has(s, "Item", "Other", "Amount")) Loader.advancedHelp(s, "Item","Other" ,"Amount");
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
			                	Loader.sendMessages(s, "Item.Lore.Error", Placeholder.c().replace("%error%", "Lore is empty"));
				            	  return true;
			                }
			                int r = StringUtils.getInt(args[2]);
			                if(lore.size()<r||r<0) {
			                	Loader.sendMessages(s, "Item.Lore.Error", Placeholder.c().replace("%error%", "Out of bound: "+r+"/"+lore.size()));
				            	  return true;
			                }
			                lore.remove(r);
			                m.setLore(lore);
			                item.setItemMeta(m);
			                Loader.sendMessages(s, "Item.Lore.Removed", Placeholder.c().replace("%item%", item.getType().name()).replace("%line%", r+""));
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
			              if(lore==null||lore.isEmpty()) {
			            	  Loader.sendMessages(s, "Item.Lore.Error", Placeholder.c().replace("%error%", "Lore is empty"));
			            	  return true;
			              }
			              int r = StringUtils.getInt(args[2]);
			              if(lore.size()<r||r<0) {
			            	  Loader.sendMessages(s, "Item.Lore.Error", Placeholder.c().replace("%error%", "Out of bound: "+r+"/"+lore.size()));
				              return true;
			              }
			              lore.set(r, name);
			              m.setLore(lore);			              
			              item.setItemMeta(m);
			              Loader.sendMessages(s, "Item.Lore.Set", Placeholder.c().replace("%item%", item.getType().name()).replace("%line%", r+"").replace("%lore%", name));
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
			              for (String ss : lore)
			                	Loader.sendMessages(s, "Item.Lore.ListLore", Placeholder.c().replace("%item%", item.getType().name()).replace("%lore%", ss).replace("%position%", (tests++)+""));
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
						String flags = "";
						for (ItemFlag itf : m.getItemFlags())
							flags+=", "+itf.name();
						if(!flags.equals(""))
						flags=flags.substring(2);
						Loader.sendMessages(s, "Item.Flag.List", Placeholder.c().replace("%item%", item.getType().name()).replace("%flags%", flags));
						return true;
					}
					if(args[1].equalsIgnoreCase("add")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Flag", "Add");
							return true;
						}
						try {
							ItemFlag type = ItemFlag.valueOf(args[2]);
							m.addItemFlags(type);
							item.setItemMeta(m);
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
							m.removeItemFlags(type);
							item.setItemMeta(m);
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
						Loader.sendMessages(s,"Item.Durability.Get",Placeholder.c().add("%durability%", item.getDurability()+""));
						return true;
					}
					if(args[1].equalsIgnoreCase("set")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Durability", "Set");
							return true;
						}
						short i = StringUtils.getShort(args[2]);
						item.setDurability(i);
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
						Loader.sendMessages(s,"Item.Nbt.Get",Placeholder.c().add("%nbt%", NMSAPI.getNBT(item)+""));
						return true;
					}
					if(args[1].equalsIgnoreCase("set")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Nbt", "Set");
							return true;
						}
						NMSAPI.setNBT(item, StringUtils.buildString(2,args));
						Loader.sendMessages(s,"Item.Nbt.Set",Placeholder.c().add("%nbt%", StringUtils.buildString(2,args)));
						return true;
					}
				}
				
				if(args[0].equalsIgnoreCase("process") && Loader.has(s, "Item", "Other", "Process")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Process","Global");
						return true;
					}
					if(args[1].equalsIgnoreCase("cmd")) {
						if(args.length<=3) {
							Loader.advancedHelp(s, "Item", "Other", "Process", "Cmd");
							return true;
						}
						String target = args[3].toLowerCase();
						if(!target.equals("console")&&!target.equals("player")) {
							Loader.sendMessages(s, "Item.Process.Cmd.InvalidTarget");
							return true;
						}
						if(args[2].equalsIgnoreCase("list")) {
							String val = new NBTEdit(item).getString("process."+target+".cmd");
							if(val==null) {
								Loader.sendMessages(s, "Item.Process.Cmd.List.Empty");
								return true;
							}
							Object o = Reader.read(val);
							if(o instanceof Collection == false) {
								Loader.sendMessages(s, "Item.Process.Cmd.List.Empty");
								return true;
							}
							Collection<String> text = (Collection<String>)o;
							Loader.sendMessages(s, "Item.Process.Cmd.List.Above", Placeholder.c().add("%total%", text.size()));
							int id = 0;
							for(String d : text)
								Loader.sendMessages(s, "Item.Process.Cmd.List.Item", Placeholder.c().add("%position%", id++)
										.add("%value%", d).add("%total%", text.size()));
							return true;
						}
						if(args[2].equalsIgnoreCase("add")) {
							if(args.length<5) {
								Loader.advancedHelp(s, "Item", "Other", "Process", "Cmd");
								return true;
							}
							NBTEdit e = new NBTEdit(item);
							String val = e.getString("process."+target+".cmd");
							Object o = null;
							Collection<String> text = val==null?new ArrayList<>():((o=Reader.read(val)) instanceof Collection?(Collection<String>)o:new ArrayList<>());
							String i = StringUtils.buildString(4,args);
							text.add(i);
							e.setString("process", "0");
							e.setString("process."+target+".cmd", Writer.write(text));
							NMSAPI.setNBT(item, e);
							Loader.sendMessages(s, "Item.Process.Cmd.Add", Placeholder.c().add("%value%", i).add("%position%", text.size()-1));
							return true;
						}
						if(args[2].equalsIgnoreCase("remove")) {
							if(args.length<5) {
								Loader.advancedHelp(s, "Item", "Other", "Process", "Cmd");
								return true;
							}
							NBTEdit e = new NBTEdit(item);
							String val = e.getString("process."+target+".cmd");
							Object o = Reader.read(val);
							if(o instanceof Collection == false) {
								Loader.sendMessages(s, "Item.Process.Cmd.List.Empty");
								return true;
							}
							Collection<String> text = val==null?new ArrayList<>():(Collection<String>)o;
							if(text.isEmpty()) {
								Loader.sendMessages(s, "Item.Process.Cmd.List.Empty");
								return true;
							}
							String removed = "";
							try {
								if(text instanceof List == false) {
									text=new ArrayList<>(text);
								}
								removed=((List<String>)text).remove(StringUtils.getInt(args[4]));
							}catch(Exception outOfBoud) {}
							if(text.isEmpty()) {
								e.remove("process."+target+".cmd");
								if(!!e.hasKey("process.msg") && !e.hasKey("process.console.cmd") && !e.hasKey("process.console.cmd")
										&& !e.hasKey("process.cooldown") && !e.hasKey("process.usage")) {
									e.remove("process");
									e.remove("process.uses");
									e.remove("process.last");
								}
							}else {
								e.setString("process", "0");
								e.setString("process."+target+".cmd", Writer.write(text));
							}
							NMSAPI.setNBT(item, e);
							Loader.sendMessages(s, "Item.Process.Cmd.Remove", Placeholder.c().add("%value%", removed).add("%position%", StringUtils.getInt(args[4])));
							return true;
						}
					}
					if(args[1].equalsIgnoreCase("msg")) {
						if(args.length<=2) {
							Loader.advancedHelp(s, "Item", "Other", "Process", "Msg");
							return true;
						}
						if(args[2].equalsIgnoreCase("list")) {
							String val = new NBTEdit(item).getString("process.msg");
							if(val==null) {
								Loader.sendMessages(s, "Item.Process.Msg.List.Empty");
								return true;
							}
							Object o = Reader.read(val);
							if(o instanceof Collection == false) {
								Loader.sendMessages(s, "Item.Process.Msg.List.Empty");
								return true;
							}
							Collection<String> text = (Collection<String>)o;
							Loader.sendMessages(s, "Item.Process.Msg.List.Above", Placeholder.c().add("%total%", text.size()));
							int id = 0;
							for(String d : text)
								Loader.sendMessages(s, "Item.Process.Msg.List.Item", Placeholder.c().add("%position%", id++)
										.add("%value%", d).add("%total%", text.size()));
							return true;
						}
						if(args[2].equalsIgnoreCase("add")) {
							if(args.length<4) {
								Loader.advancedHelp(s, "Item", "Other", "Process", "Msg");
								return true;
							}
							NBTEdit e = new NBTEdit(item);
							String val = e.getString("process.msg");
							Object o = null;
							Collection<String> text = val==null?new ArrayList<>():((o=Reader.read(val)) instanceof Collection?(Collection<String>)o:new ArrayList<>());
							String i = StringUtils.buildString(3,args);
							text.add(i);
							e.setString("process", "0");
							e.setString("process.msg", Writer.write(text));
							NMSAPI.setNBT(item, e);
							Loader.sendMessages(s, "Item.Process.Msg.Add", Placeholder.c().add("%value%", i).add("%position%", text.size()-1));
							return true;
						}
						if(args[2].equalsIgnoreCase("remove")) {
							if(args.length<4) {
								Loader.advancedHelp(s, "Item", "Other", "Process", "Msg");
								return true;
							}
							NBTEdit e = new NBTEdit(item);
							String val = e.getString("process.msg");
							Object o = Reader.read(val);
							if(o instanceof Collection == false) {
								Loader.sendMessages(s, "Item.Process.Msg.List.Empty");
								return true;
							}
							Collection<String> text = val==null?new ArrayList<>():(Collection<String>)o;
							if(text.isEmpty()) {
								Loader.sendMessages(s, "Item.Process.Msg.List.Empty");
								return true;
							}
							String removed = "";
							try {
								if(text instanceof List == false) {
									text=new ArrayList<>(text);
								}
								removed=((List<String>)text).remove(StringUtils.getInt(args[3]));
							}catch(Exception outOfBoud) {}
							if(text.isEmpty()) {
								e.remove("process.msg");
								if(!e.hasKey("process.msg") && !e.hasKey("process.console.cmd") && !e.hasKey("process.console.cmd")
										&& !e.hasKey("process.cooldown") && !e.hasKey("process.usage")) {
									e.remove("process");
									e.remove("process.uses");
									e.remove("process.last");
								}
							}else {
								e.setString("process", "0");
								e.setString("process.msg", Writer.write(text));
							}
							NMSAPI.setNBT(item, e);
							Loader.sendMessages(s, "Item.Process.Msg.Remove", Placeholder.c().add("%value%", removed).add("%position%", StringUtils.getInt(args[3])));
							return true;
						}
					}
					if(args[1].equalsIgnoreCase("cooldown")||args[1].equalsIgnoreCase("usage")) {
						String path = args[1].equalsIgnoreCase("cooldown")?"Cooldown":"Usage";
						if(args.length<=2) {
							Loader.advancedHelp(s, "Item", "Other", "Process", path);
							return true;
						}
						if(args[2].equalsIgnoreCase("get")) {
							long val = new NBTEdit(item).getLong("process."+path.toLowerCase());
							Loader.sendMessages(s, "Item.Process."+path+".Get", Placeholder.c().add("%value%", val));
							return true;
						}
						if(args[2].equalsIgnoreCase("set")) {
							if(args.length<4) {
								Loader.advancedHelp(s, "Item", "Other", "Process", path);
								return true;
							}
							NBTEdit e = new NBTEdit(item);
							if((args[1].equalsIgnoreCase("cooldown")?StringUtils.timeFromString(args[3]):StringUtils.getInt(args[3])) <= 0) {
								e.remove("process."+path.toLowerCase());
								if(path.equalsIgnoreCase("usage"))
								e.remove("process.uses");
								else
									e.remove("process.last");
								if(!e.hasKey("process.msg") && !e.hasKey("process.console.cmd") && !e.hasKey("process.console.cmd")
										&& !e.hasKey("process.cooldown") && !e.hasKey("process.usage")) {
									e.remove("process");
									e.remove("process.uses");
									e.remove("process.last");
								}
							}else {
								e.setString("process", "0");
								e.setLong("process."+path.toLowerCase(), args[1].equalsIgnoreCase("cooldown")?StringUtils.timeFromString(args[3]):StringUtils.getInt(args[3]));
							}
							NMSAPI.setNBT(item, e);
							Loader.sendMessages(s, "Item.Process."+path+".Set", Placeholder.c().add("%value%", args[1].equalsIgnoreCase("cooldown")?StringUtils.timeFromString(args[3])+"":StringUtils.getInt(args[3])));
							return true;
						}
					}
				}
				
				if(args[0].equalsIgnoreCase("type") && Loader.has(s, "Item", "Other", "Type")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Type", "Get");
						Loader.advancedHelp(s, "Item", "Other", "Type", "Set");
						return true;
					}
					if(args[1].equalsIgnoreCase("get")) {
						Loader.sendMessages(s,"Item.Type.Get",Placeholder.c().add("%type%", item.getType().name()));
						return true;
					}
					if(args[1].equalsIgnoreCase("set")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Nbt", "Set");
							return true;
						}
						try {
							Material type = Material.valueOf(args[2]);
							item.setType(type);
							Loader.sendMessages(s,"Item.Type.Set",Placeholder.c().add("%type%", type.name()));
						}catch(Exception e) {
							Loader.sendMessages(s,"Missing.Material",Placeholder.c().add("%material%", args[2]));
						}
						return true;
					}
				}
				
				if(args[0].equalsIgnoreCase("amount") && Loader.has(s, "Item", "Other", "Amount")) {
					if(args.length==1) {
						Loader.advancedHelp(s, "Item", "Other", "Amount", "Get");
						Loader.advancedHelp(s, "Item", "Other", "Amount", "Set");
						return true;
					}
					if(args[1].equalsIgnoreCase("get")) {
						Loader.sendMessages(s,"Item.Amount.Get",Placeholder.c().add("%amount%", item.getAmount()+""));
						return true;
					}
					if(args[1].equalsIgnoreCase("set")) {
						if(args.length==2) {
							Loader.advancedHelp(s, "Item", "Other", "Amount", "Set");
							return true;
						}
						item.setAmount(StringUtils.getInt(args[2]));
						Loader.sendMessages(s,"Item.Amount.Set",Placeholder.c().add("%amount%", ""+StringUtils.getInt(args[2])));
						return true;
					}
				}
				if(Loader.has(s, "Item", "Other", "Name")) Loader.advancedHelp(s, "Item","Other" ,"Name");		
				if(Loader.has(s, "Item", "Other", "Lore"))for (String c : f) Loader.advancedHelp(s,"Item", "Other", "Lore", c);
				if(Loader.has(s, "Item", "Other", "Flag")) Loader.advancedHelp(s, "Item","Other" ,"Flag");		
				if(Loader.has(s, "Item", "Other", "Nbt")) Loader.advancedHelp(s, "Item","Other" ,"Nbt");		
				if(Loader.has(s, "Item", "Other", "Durability")) Loader.advancedHelp(s, "Item","Other" ,"Durability");		
				if(Loader.has(s, "Item", "Other", "Type")) Loader.advancedHelp(s, "Item","Other" ,"Type");
				if(Loader.has(s, "Item", "Other", "Amount")) Loader.advancedHelp(s, "Item","Other" ,"Amount");
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "Item", "Other");		
		return true;
	}
	
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "Item", "Other")) {
		if (args.length==1) {	
			List<String> c = new ArrayList<>();
			if(Loader.has(s, "Item", "Other", "Name")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Name")));
			if(Loader.has(s, "Item", "Other", "Lore")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Lore")));
			if(Loader.has(s, "Item", "Other", "Flag") && TheAPI.isNewerThan(7)) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Flag")));	
			if(Loader.has(s, "Item", "Other", "Nbt")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Nbt")));
			if(Loader.has(s, "Item", "Other", "Durability"))c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Durability")));	
			if(Loader.has(s, "Item", "Other", "Type")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Type")));
			if(Loader.has(s, "Item", "Other", "Process")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Process")));
			if(Loader.has(s, "Item", "Other", "Amount")) c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Amount")));
			return c;
		}
		if(args[0].equalsIgnoreCase("process") && Loader.has(s, "Item", "Other", "Process")) {
			if(args.length==2) {
				return StringUtils.copyPartialMatches(args[1], Arrays.asList("Cmd","Msg","Cooldown","Usage"));
			}
			if(args.length==3) {
				if(args[1].equalsIgnoreCase("usage")||args[1].equalsIgnoreCase("cooldown"))
					return StringUtils.copyPartialMatches(args[2], Arrays.asList("Get","Set"));
				if(args[1].equalsIgnoreCase("cmd")||args[1].equalsIgnoreCase("msg"))
					return StringUtils.copyPartialMatches(args[2], Arrays.asList("Add","Remove","List"));
				return Arrays.asList();
			}
			if(args.length==4) {
				if((args[1].equalsIgnoreCase("usage")||args[1].equalsIgnoreCase("cooldown")) && args[2].equalsIgnoreCase("set"))
					return StringUtils.copyPartialMatches(args[3], Arrays.asList("0","3","5","10","15"));
				if(args[1].equalsIgnoreCase("cmd"))
					return StringUtils.copyPartialMatches(args[3], Arrays.asList("Player","Console"));
				if(args[1].equalsIgnoreCase("msg")) {
					if(args[2].equalsIgnoreCase("add")) {
						return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
					}
					if(args[2].equalsIgnoreCase("remove")) {
						return Arrays.asList("?");
					}
				}
				return Arrays.asList();
			}
			if(args[1].equalsIgnoreCase("msg")) {
				if(args[2].equalsIgnoreCase("add"))
				return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
				return Arrays.asList();
			}
			if(!(args[3].equalsIgnoreCase("player")||args[3].equalsIgnoreCase("console")))return Arrays.asList();
			if(args[1].equalsIgnoreCase("cmd")) {
				if(args[2].equalsIgnoreCase("add")) {
					return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
				}
				if(args[2].equalsIgnoreCase("remove") && args.length==5) {
					return Arrays.asList("?");
				}
			}
			return Arrays.asList();
		}
		if(args[0].equalsIgnoreCase("name") && Loader.has(s, "Item", "Other", "Name"))
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		if(args[0].equalsIgnoreCase("lore") && Loader.has(s, "Item", "Other", "Lore")) {
			if(args.length==2) {
				return StringUtils.copyPartialMatches(args[1], Arrays.asList("Add","Remove","Set","List"));
			}
			if(args[1].equalsIgnoreCase("Add"))
				return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
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
					return StringUtils.copyPartialMatches(args[2], lines);
				}
			}
			if(args[1].equalsIgnoreCase("Set"))
				return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
			return Arrays.asList();
		}
		if(args[0].equalsIgnoreCase("flag") && TheAPI.isNewerThan(7) && Loader.has(s, "Item", "Other", "Flag")) {
			if(args.length==2) {
				return StringUtils.copyPartialMatches(args[1], Arrays.asList("Add","Remove","List"));
			}
			if(args[1].equalsIgnoreCase("Add")){
				return StringUtils.copyPartialMatches(args[2], flags);
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
					return StringUtils.copyPartialMatches(args[2], lines);
				}
			}
			return Arrays.asList();
		}
		if(args[0].equalsIgnoreCase("nbt") && Loader.has(s, "Item", "Other", "Nbt") ||args[0].equalsIgnoreCase("amount") && Loader.has(s, "Item", "Other", "Amount") || args[0].equalsIgnoreCase("durability") && Loader.has(s, "Item", "Other", "Durability")||args[0].equalsIgnoreCase("type") && Loader.has(s, "Item", "Other", "Type")) {
			if(args.length==2) {
				return StringUtils.copyPartialMatches(args[1], Arrays.asList("Set","Get"));
			}
		}
		}
		return Arrays.asList();
	}
}

package Commands;

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
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Item implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s,"ServerControl.Item")) {
			if(s instanceof Player) {
			if(args.length==0) {
				Loader.Help(s, "/Item SetName <name>", "Item.SetName");
				Loader.Help(s, "/Item SetLore Add <lore>", "Item.SetLore.Add");
				Loader.Help(s, "/Item SetLore Remove <line>", "Item.SetLore.Remove");
				Loader.Help(s, "/Item SetLore Lines", "Item.SetLore.Lines");
				Loader.Help(s, "/Item HideEnchants <yes/no>", "Item.HideEnchants");
				Loader.Help(s, "/Item Unbreakable <yes/no>", "Item.Unbreakable");
				return true;
			}
			Player p = (Player)s;
			@SuppressWarnings("deprecation")
			ItemStack item = p.getItemInHand();
			ItemMeta m = item.getItemMeta();
			if(item.getType()!=Material.AIR) {
			if(args[0].equalsIgnoreCase("Unbreakable")) {
				if(args.length==1) {
					Loader.Help(s, "/Item Unbreakable <yes/no>", "Item.Unbreakable");
					return true;
				}
						if(args.length==1) {
							Loader.Help(s, "/Item Unbreakable <yes/no>", "Item.Unbreakable");
							return true;
						}
						if(args[1].equalsIgnoreCase("true")||args[1].equalsIgnoreCase("yes")) {
							if(TheAPI.getServerVersion().contains("1_10")
									||TheAPI.getServerVersion().contains("1_9")
									||TheAPI.getServerVersion().contains("1_8")) {
							List<String> lore = new ArrayList<String>();
							if(m.getLore()!=null) {
								for(String ss:m.getLore()) {
									lore.add(TheAPI.colorize(ss));
								}
							}
							lore.add("&9UNBREAKABLE");
							m.setLore(lore);
							}else {
								m.setUnbreakable(true);
							}
							item.setItemMeta(m);
							
						Loader.msg(Loader.s("Prefix")+Loader.s("Item.Unbreakable.true")
						.replace("%item%", item.getType().name()), s);
						return true;
						}else
						if(args[1].equalsIgnoreCase("false")||args[1].equalsIgnoreCase("no")) {
							if(TheAPI.getServerVersion().contains("1_10")
									||TheAPI.getServerVersion().contains("1_9")
									||TheAPI.getServerVersion().contains("1_8")) {
							List<String> lore = new ArrayList<String>();
							if(m.getLore()!=null) {
								for(String ss:m.getLore()) {
									lore.add(TheAPI.colorize(ss));
								}
							}
							lore.remove("&9UNBREAKABLE");
							m.setLore(lore);
							}else {
								m.setUnbreakable(false);
							}
							item.setItemMeta(m);
						Loader.msg(Loader.s("Prefix")+Loader.s("Item.Unbreakable.false")
						.replace("%item%", item.getType().name()), s);
						return true;
						}else {
							Loader.Help(s, "/Item Unbreakable <yes/no>", "Item.Unbreakable");
							return true;
						
					
				}}
			if(args[0].equalsIgnoreCase("HideEnchants")) {
				if(args.length==1) {
					Loader.Help(s, "/Item HideEnchants <yes/no>", "Item.HideEnchants");
					return true;
				}
						if(args.length==1) {
							Loader.Help(s, "/Item HideEnchants <yes/no>", "Item.HideEnchants");
							return true;
						}
						if(args[1].equalsIgnoreCase("true")||args[1].equalsIgnoreCase("yes")) {
							m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
							item.setItemMeta(m);
						Loader.msg(Loader.s("Prefix")+Loader.s("Item.HideEnchants.true")
						.replace("%item%", item.getType().name()), s);
						return true;
						}else
						if(args[1].equalsIgnoreCase("false")||args[1].equalsIgnoreCase("no")) {
							m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
							item.setItemMeta(m);
						Loader.msg(Loader.s("Prefix")+Loader.s("Item.HideEnchants.false")
						.replace("%item%", item.getType().name()), s);
						return true;
						}else {
							Loader.Help(s, "/Item HideEnchants <yes/no>", "Item.HideEnchants");
							return true;
						
					
				}
			}
			if(args[0].equalsIgnoreCase("SetLore")) {
				if(args.length==1) {
					Loader.Help(s, "/Item SetLore Add <lore>", "Item.SetLore.Add");
					Loader.Help(s, "/Item SetLore Remove <line>", "Item.SetLore.Remove");
					Loader.Help(s, "/Item SetLore Lines", "Item.SetLore.Lines");
					return true;
				}
				
				if(args[1].equalsIgnoreCase("Lines")) {
					int tests=0;
					Loader.msg(Loader.s("Prefix")+Loader.s("Item.SetLore.Lines-List").replace("%item%", item.getType().name()),s);
					List<String> lore = m.getLore();
					if(lore!=null)
					for(String ss:lore) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Item.SetLore.Lines-Format").replace("%position%", tests+"").replace("%lore%",ss),s);
						tests = tests+1;
					}
					return true;
					}
				
				if(args[1].equalsIgnoreCase("add")) {
					if(args.length==2) {
						Loader.Help(s, "/Item SetLore Add <lore>", "Item.SetLore.Add");
						return true;
					}
						String name = "";
						for (int i = 0; i < args.length; i++) {
							name = name + args[i]+" ";
						}
						name = name.replaceFirst(args[0]+" "+args[1]+" ", "");
						name=name.substring(0,name.length()-1);
						List<String> lore = new ArrayList<String>();
						if(m.getLore()!=null) {
							for(String ss:m.getLore()) {
								lore.add(TheAPI.colorize(ss));
							}
						}
						lore.add(TheAPI.colorize(name));
						
						m.setLore(lore);
						item.setItemMeta(m);
						Loader.msg(Loader.s("Prefix")+Loader.s("Item.SetLore.Add").replace("%item%", item.getType().name()).replace("%lore%", name),s);
						return true;
					}
				if(args[1].equalsIgnoreCase("remove")) {
					if(args.length==2) {
						Loader.Help(s, "/Item SetLore Remove <lore>", "Item.SetLore.Remove");
						return true;
					}
						try {
							List<String> lore = m.getLore();
							lore.remove(TheAPI.getStringUtils().getInt(args[2]));
							m.setLore(lore);
							item.setItemMeta(m);
						Loader.msg(Loader.s("Prefix")+Loader.s("Item.SetLore.Remove").replace("%item%", item.getType().name()).replace("%line%", ""+TheAPI.getStringUtils().getInt(args[2])),s);
						return true;
						}catch(Exception e) {
							Loader.msg(Loader.s("Prefix")+Loader.s("Item.SetLore.RemoveError").replace("%item%", item.getType().name()).replace("%line%", ""+TheAPI.getStringUtils().getInt(args[2])),s);
							return true;
						}
					}
				if(!is(args,"add")||!is(args,"remove")||!is(args,"lines")) {
					Loader.Help(s, "/Item SetLore Add <lore>", "Item.SetLore.Add");
					Loader.Help(s, "/Item SetLore Remove <line>", "Item.SetLore.Remove");
					Loader.Help(s, "/Item SetLore Lines", "Item.SetLore.Lines");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("setname")) {
				if(args.length==1) {
					Loader.Help(s, "/Item SetName <name>", "Item.SetName");
					return true;
				}
				String name = "";
				for (int i = 0; i < args.length; i++) {
					name = name + args[i]+" ";
				}
				name = name.replaceFirst(args[0]+" ", "");
				name=name.substring(0,name.length()-1);
				
					m.setDisplayName(TheAPI.colorize(name));
					item.setItemMeta(m);
				Loader.msg(Loader.s("Prefix")+Loader.s("Item.SetName").replace("%item%", item.getType().name())
						.replace("%name%", name).replace("%displayname%", name), s);
				return true;
				
				}
			if(!is(args,"setname")||!is(args,"setlore")||!is(args,"HideEnchants")||!is(args,"unbreakable")) {
				Loader.Help(s, "/Item SetName <name>", "Item.SetName");
				Loader.Help(s, "/Item SetLore Add <line> <lore>", "Item.SetLore.Add");
				Loader.Help(s, "/Item SetLore Remove <line>", "Item.SetLore.Remove");
				Loader.Help(s, "/Item SetLore Lines", "Item.SetLore.Lines");
				Loader.Help(s, "/Item HideEnchants <true/false>", "Item.HideEnchants");
				Loader.Help(s, "/Item Unbreakable <true/false>", "Item.Unbreakable");
				return true;
			}
			}
			Loader.msg(Loader.s("Item.HandIsEmpty"), s);
			return true;
			}
			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}
	private boolean is(String[] args,String string) {
		if(args[0].equalsIgnoreCase(string))return true;
		return false;
	}
	@SuppressWarnings("deprecation")
	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		List<String> l = new ArrayList<String>();
    	if(s.hasPermission("ServerControl.Item")) {
    	if(args.length==1) {
                c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("SetName", "SetLore", "Unbreakable", "HideEnchants"), new ArrayList<>()));
            }
        	if(args.length==2) {
        		if(args[0].equalsIgnoreCase("setname"))
            		c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("?"), new ArrayList<>()));
        		if(args[0].equalsIgnoreCase("Unbreakable")||args[0].equalsIgnoreCase("HideEnchants"))
            		c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("Yes", "No"), new ArrayList<>()));
        		if(args[0].equalsIgnoreCase("SetLore"))
            		c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("Add", "Remove", "Lines"), new ArrayList<>()));
            	}
        	if(args.length==3) {
        		
        		if(args[0].equalsIgnoreCase("SetLore"))
            		if(args[1].equalsIgnoreCase("Add"))
            		c.addAll(StringUtil.copyPartialMatches(args[2], Arrays.asList("?"), new ArrayList<>()));
    		if(args[1].equalsIgnoreCase("Remove")) {
        		int count = 0;
    			if(s instanceof Player && ((Player)s).getItemInHand()!= null&&
        				((Player)s).getItemInHand().getItemMeta().hasLore())
        		for(@SuppressWarnings("unused") String d:((Player)s).getItemInHand().getItemMeta().getLore()) {
        			l.add(count+"");
        			count++;
        		}
    			if(!l.isEmpty())
    		c.addAll(StringUtil.copyPartialMatches(args[2], l, new ArrayList<>()));
        	}
	}}
        return c;
}}

package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.GUI.ClickType;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;

public class GUICreator implements CommandExecutor {
    private static Config c = Loader.guicreator;
	public static Map<String, GUIMaker> maker = new HashMap<>();
    
    static {
    	for(String a : c.getKeys("GUI"))
    		maker.put(a, new GUIMaker(a));
    }
    
	public static class GUIMaker {
	    private String gui;
	    public GUIMaker(String a){
	    	gui=a;
			load();
	    }
	    
	    public Map<Integer, ItemGUI> map = new HashMap<>();
	    	
	    private void load() {
	        for(String j : c.getKeys("GUI."+gui+".items")){
	        	ItemGUI itemGUI = null;
	            if(c.exists("GUI."+gui+".items."+j+".headURL")){
	                if(c.exists("GUI."+gui+".items."+j+".lore")){
	                    itemGUI=MethodaHead(c.getString("GUI."+gui+".items."+j+".name"),c.getStringList("GUI."+gui+".items."+j+".lore"),c.getString("GUI."+gui+".items."+j+".headURL"),j,gui);
	                }else{
	                    itemGUI=MethodaHead(c.getString("GUI."+gui+".items."+j+".name"),c.getString("GUI."+gui+".items."+j+".headURL"),j,gui);
	                }
	            } else if(c.exists("GUI."+gui+".items."+j+".type")){
	                if(c.exists("GUI."+gui+".items."+j+".lore")){
	                    itemGUI=MethodaItem(c.getString("GUI."+gui+".items."+j+".name"),c.getString("GUI."+gui+".items."+j+".type").toUpperCase(),j,c.getStringList("GUI."+gui+".items."+j+".lore"),gui,c.getInt("GUI."+gui+".items."+j+".data"),c.getInt("GUI."+gui+".items."+j+".modelData"));
	                }else{
	                    itemGUI=MethodaItem(c.getString("GUI."+gui+".items."+j+".name"),c.getString("GUI."+gui+".items."+j+".type").toUpperCase(),j,gui,c.getInt("GUI."+gui+".items."+j+".data"),c.getInt("GUI."+gui+".items."+j+".modelData"));
	                }
	            }
	            if(itemGUI!=null)
	            	for(String slot : j.split(",[ ]*"))
	            		set(StringUtils.getInt(slot), itemGUI);
	        }
		}

		public String getName() {
	    	return gui;
	    }
	    
	    public void set(int i, ItemGUI g) {
	    	map.put(i, g);
	    }
	    	
	    public GUI make() {
	           GUI g = new GUI(c.getString("GUI."+gui+".title"),c.getInt("GUI."+gui+".size"));
	           for(Entry<Integer, ItemGUI> c : map.entrySet())
	           	g.setItem(c.getKey(), c.getValue());
	           return g;
	    }

	    public static void vecinator(HolderGUI g ,Player p, String b, String s, GUI.ClickType clickType){
	    	String perm = c.getString("GUI."+b+".items."+s+".permission");
	    	if(perm!=null && perm.trim().equals(""))perm=null;
        	if(perm!=null && (perm.startsWith("-")?p.hasPermission(perm.substring(1)):!p.hasPermission(perm))) {
	    		process(clickType, g,p,b,s,"noPermission");
	    		return;
	    	}
        	if(c.getDouble("GUI."+b+".items."+s+".cost")<=0)
    		process(clickType, g,p,b,s,"action");
        	else
	        if(EconomyAPI.has(p,c.getDouble("GUI."+b+".items."+s+".cost"))) {
	            if(c.getBoolean("GUI."+b+".items."+s+".takeMoney")){
	                EconomyAPI.withdrawPlayer(p,c.getDouble("GUI."+b+".items."+s+".cost"));
	            }
	    		process(clickType, g,p,b,s,"action");
	        }else {
	    		process(clickType, g,p,b,s,"noMoney");
	        }
	    }
	    private static void process(ClickType clickType, HolderGUI g, Player p, String b, String s, String string) {
            if (c.get("GUI." + b + ".items." + s + "."+string) instanceof Collection&&c.exists("GUI."+b+".items."+s+"."+string)) {
                for (String a : c.getStringList("GUI." + b + ".items." + s + "."+string)) {
                    if (a.startsWith("sleft")) {
                        if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("sright")) {
                        if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(7));
                        }
                    } else if (a.startsWith("left")) {
                        if (clickType == GUI.ClickType.LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(5));
                        }
                    } else if (a.startsWith("right")) {
                        if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("any")) {
                        methodenzi(g, p, a.substring(4));
                    }
                }
            } else {
                String a = c.getString("GUI." + b + ".items." + s + "."+string);
                if(a==null)return;
                if (a.startsWith("sleft")) {
                    if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("sright")) {
                    if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(7));
                    }
                } else if (a.startsWith("left")) {
                    if (clickType == GUI.ClickType.LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(5));
                    }
                } else if (a.startsWith("right")) {
                    if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("middle")) {
                    if (clickType == GUI.ClickType.MIDDLE_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("any")) {
                    methodenzi(g, p, a.substring(4));
                }
            }
		}

		public static void methodenzi(HolderGUI g ,Player p,String a){
	        if(a.startsWith("close")){
	            g.close(p);
	        } else if (a.startsWith("msg")){
	            TheAPI.msg(a.replace("%player%",p.getName()).substring(4),p);
	        } else if(a.startsWith("cmd")){
	            NMSAPI.postToMainThread(new Runnable() {
					public void run() {
						if(a.substring(4).startsWith("player")){
			            	TheAPI.sudo(p,SudoType.COMMAND,a.substring(11).replace("%player%",p.getName()));
			            } else if(a.substring(4).startsWith("console")){
			                TheAPI.sudoConsole(a.substring(12).replace("%player%",p.getName()));
			            }
					}
				});
	        } else if(a.startsWith("open")){
	        	GUIMaker f = maker.get(a.substring(5));
	        	if(f!=null)
	            f.make().open(p);
	        }
	    }

	    private static ItemGUI MethodaHead(String name,List<String> list,String headURL,String item,String other){
	        return new ItemGUI(ItemCreatorAPI.createHeadByValues(1,name,list,headURL)) {
	            @Override
	            public void onClick(Player player, HolderGUI hgui, GUI.ClickType click) {
	                try {vecinator(hgui,player,other,item,click);}catch (Exception e){e.printStackTrace();}
	            }
	        };
	    }
	    private static ItemGUI MethodaHead(String name, String headURL,String item,String other){
	        return new ItemGUI(ItemCreatorAPI.createHeadByValues(1,name,headURL)) {
	            @Override
	            public void onClick(Player player, HolderGUI hgui, GUI.ClickType click) {
	                try {vecinator(hgui,player,other,item,click);}catch (Exception e){e.printStackTrace();}
	            }
	        };
	    }
	    private static ItemGUI MethodaItem(String name, String material, String item, List<String> lore, String other, int data, int modelData){
	    	return new ItemGUI(setModel(ItemCreatorAPI.create(Material.getMaterial(material),1,name,lore,data),modelData)) {
	            @Override
	            public void onClick(Player player, HolderGUI hgui, GUI.ClickType click) {
	                try {vecinator(hgui,player,other,item,click);}catch (Exception e){e.printStackTrace();}
	            }
	        };
	    }
	    private static ItemGUI MethodaItem(String name, String material,String item,String other, int data, int modelData){
	    	return new ItemGUI(setModel(ItemCreatorAPI.create(Material.getMaterial(material),1,name,data),modelData)) {
	            @Override
	            public void onClick(Player player, HolderGUI hgui, GUI.ClickType click) {
	                try {vecinator(hgui,player,other,item,click);}catch (Exception e){e.printStackTrace();}
	            }
	        };
	    }
	    
		public static ItemStack setModel(ItemStack s, int model) {
			if(model==0)return s;
			try {
				ItemMeta meta = s.getItemMeta();
				meta.setCustomModelData(model);
				s.setItemMeta(meta);
				return s;
			}catch(Exception | NoSuchMethodError | NoSuchFieldError e) {
				s.setDurability((short)model);
				return s;
			}
		}
	}
	
    private String gui;

    public GUICreator(String a){
    	gui=a;
    }

    public boolean onCommand(CommandSender s, Command uu, String u, String[] args) {
    	if(args.length==0||!s.hasPermission("scr.command.gui")) {
    		if(s instanceof Player == false)return true;
        	String perm = c.getString("GUI."+gui+".permission");
        	if(perm!=null)perm=perm.trim();
        	if(perm!=null && perm.equals(""))perm=null;
        	if(perm==null||(perm.startsWith("-")?s.hasPermission(perm.substring(1)):!s.hasPermission(perm)))
            maker.get(gui).make().open((Player)s);
            return true;
    	}
    	Player target = TheAPI.getPlayer(args[0]);
    	if(target==null) {
    		Loader.notOnline(s, args[0]);
    		return true;
    	}
        maker.get(gui).make().open(target);
        return true;
    }
}
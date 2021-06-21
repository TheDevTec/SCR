package me.devtec.servercontrolreloaded.commands.other.guis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.other.guis.GUICreator.GUIMaker;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.GUI.ClickType;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.theapiutils.Validator;

public class Layout {
	String s;
	Map<Character, ItemBuilder> items = new HashMap<>(); 
	public GUI paste(GUI g, Player p, List<String> map) {
		int slot = 0;
		for(String s : map)
			for(char c : s.toCharArray()) {
				if(items.containsKey(c))
				g.setItem(slot++, new ItemGUI(items.get(c).build(p)) {
					public void onClick(Player player, HolderGUI gui, ClickType clickType) {
						new Tasker() {
							public void run() {
								onUse(player, gui, c+"", clickType);
							}
						}.runTask();
					}
				});
				else {
					if(g.getItemGUI(slot)!=null)
						g.remove(slot);
					++slot;
				}
			}
		return g;
	}
	
	public Layout upload(String path) {
		items.clear();
		this.s=path;
    	for(String s : Loader.guicreator.getKeys(path))
    		items.put(s.toCharArray()[0], createItem(path+"."+s));
		return this;
	}
    
    private ItemBuilder createItem(String string) {
    	if(Loader.guicreator.exists(string+".head")){
    		if(Loader.guicreator.exists(string+".lore")){
    			return new ItemBuilder(Loader.guicreator.getString(string+".name"),Loader.guicreator.getInt(string+".amount"),Loader.guicreator.getStringList(string+".lore"),Loader.guicreator.getString(string+".head"), Loader.guicreator.getStringList(string+".itemflags"), Loader.guicreator.getStringList(string+".enchants"));
            }else{
            	return new ItemBuilder(Loader.guicreator.getString(string+".name"),Loader.guicreator.getInt(string+".amount"),null,Loader.guicreator.getString(string+".head"), Loader.guicreator.getStringList(string+".itemflags"), Loader.guicreator.getStringList(string+".enchants"));
            }
    	} else if(Loader.guicreator.exists(string+".type")){
    		Material d = Material.getMaterial(Loader.guicreator.getString(string+".type").toUpperCase());
    		if(d==null) {
    			d=Material.STONE;
    			Validator.validate(true, "Material named '"+Loader.guicreator.getString(string+".type")+"' doesn't exist");
    		}
    		if(Loader.guicreator.exists(string+".lore")){
    			return new ItemBuilder(d,Loader.guicreator.getInt(string+".data"),Loader.guicreator.getInt(string+".amount"),Loader.guicreator.getString(string+".name"),Loader.guicreator.getStringList(string+".lore"), Loader.guicreator.getInt(string+".model"), Loader.guicreator.getStringList(string+".itemflags"), Loader.guicreator.getStringList(string+".enchants"));
            }else{
            	return new ItemBuilder(d,Loader.guicreator.getInt(string+".data"),Loader.guicreator.getInt(string+".amount"),Loader.guicreator.getString(string+".name"),null, Loader.guicreator.getInt(string+".model"), Loader.guicreator.getStringList(string+".itemflags"), Loader.guicreator.getStringList(string+".enchants"));
           }
        }
		return null;
	}

    private void onUse(Player p, HolderGUI g, String string, ClickType clickType){
    	String perm = Loader.guicreator.getString(s+"."+string+".permission");
    	if(perm!=null && perm.trim().equals(""))perm=null;
    	if(perm!=null && (perm.startsWith("-")?!p.hasPermission(perm.substring(1)):p.hasPermission(perm))) {
    		process(clickType, g,p,string+".noPermission");
    		return;
    	}
    	if(Loader.guicreator.getInt(s+"."+string+".tokens")<=0 && Loader.guicreator.getDouble(s+"."+string+".cost")<=0)
		process(clickType, g,p,string+".action");
    	else {
    		//boolean takeTokens = false, 
    		boolean takeMoney = false;
    		
    		//What about custom implement?
    		
    		/*if(Loader.guicreator.getInt(s+"."+string+".tokens")>0 && ExtManager.getExt("Tokens")!=null) {
	    		if(API.has(p.getName(),Loader.guicreator.getInt(s+"."+string+".tokens"))) {
		            if(Loader.guicreator.getBoolean(s+"."+string+".takeTokens")){
		            	takeTokens=true;
		            }
		        }else {
		    		process(clickType, g,p,string+".noTokens");
		    		return;
		        }
    		}*/
    		if(Loader.guicreator.getDouble(s+"."+string+".cost")>0) {
		        if(EconomyAPI.has(p,Loader.guicreator.getDouble(s+"."+string+".cost"))) {
		            if(Loader.guicreator.getBoolean(s+"."+string+".takeMoney")){
		            	takeMoney=true;
		            }
		        }else {
		    		process(clickType, g,p,string+".noMoney");
		    		return;
		        }
    		}
    		if(takeMoney)
    			EconomyAPI.withdrawPlayer(p,Loader.guicreator.getDouble(s+"."+string+".cost"));
    		/*if(takeTokens)
    			API.remove(null,p.getName(),Loader.guicreator.getInt(s+"."+string+".tokens"));*/
    		process(clickType, g,p,string+".action");
    	}
    }
    
    private void process(ClickType clickType, HolderGUI g, Player p, String string) {
        if (Loader.guicreator.get(s + "."+string) instanceof Collection&&Loader.guicreator.exists(s+"."+string)) {
            for (String a : Loader.guicreator.getStringList(s + "."+string)) {
                if(a.startsWith("any"))
                	prov(g, p, a.substring(4));
                else
                if (a.startsWith(fixedName(clickType)))
                	prov(g, p, a.substring(fixedName(clickType).length()+1));
            }
        } else {
            String a = Loader.guicreator.getString(s + "."+string);
            if(a==null)return;
            if(a.startsWith("any"))
            	prov(g, p, a.substring(4));
            else
            if (a.startsWith(fixedName(clickType)))
            	prov(g, p, a.substring(fixedName(clickType).length()+1));
        }
	}

	private String fixedName(ClickType clickType) {
        switch(clickType) {
        case LEFT_DROP:
        case LEFT_PICKUP:
        	return "left";
        case MIDDLE_PICKUP:
        	return "middle";
        case RIGHT_DROP:
        case RIGHT_PICKUP:
        	return "right";
        case SHIFT_LEFT_DROP:
        case SHIFT_LEFT_PICKUP:
        	return "shift_left";
        case SHIFT_RIGHT_DROP:
        case SHIFT_RIGHT_PICKUP:
        	return "shift_right";
        	default:
        		break;
        }
        return null;
	}

	private void prov(HolderGUI g ,Player p,String a){
        if(a.startsWith("close")){
            g.close(p);
        } else if (a.startsWith("msg")){
            TheAPI.msg(PlaceholderAPI.setPlaceholders(p, a.substring(4).replace("%player%",p.getName())),p);
        } else if(a.startsWith("cmd")){
            NMSAPI.postToMainThread(new Runnable() {
				public void run() {
					if(a.substring(4).startsWith("player")){
		            	TheAPI.sudo(p,SudoType.COMMAND,PlaceholderAPI.setPlaceholders(p, a.substring(11).replace("%player%",p.getName())));
		            } else if(a.substring(4).startsWith("console")){
		                TheAPI.sudoConsole(PlaceholderAPI.setPlaceholders(p, a.substring(12).replace("%player%",p.getName())));
		            }
				}
			});
        } else if(a.startsWith("open")){
        	GUIMaker f = GUICreator.maker.get(PlaceholderAPI.setPlaceholders(p, a.substring(5).replace("%player%",p.getName())));
        	if(f!=null)
            f.make(p).open(p);
        } else if(a.startsWith("wait")){
        	try {
				Thread.sleep(50*StringUtils.getLong(PlaceholderAPI.setPlaceholders(p, a.substring(5).replace("%player%",p.getName()))));
			} catch (Exception e) {
			}
        }
    }
}

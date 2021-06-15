package me.devtec.servercontrolreloaded.commands.other.guis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.other.guis.GUICreator.GUIMaker;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.XMaterial;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.GUI.ClickType;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;

public class Layout {
	static Config c = Loader.guicreator;
	
	String s;
	Map<Character, ItemBuilder> items = new HashMap<>(); 
	public GUI paste(GUI g, boolean perPlayer, List<String> map) {
		int slot = 0;
		for(String s : map)
			for(char c : s.toCharArray()) {
				if(items.containsKey(c))
				g.setItem(slot++, new ItemGUI(items.get(c).build(perPlayer?g.getPlayers().iterator().next():null)) {
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
    	for(String s : c.getKeys(path))
    		items.put(s.toCharArray()[0], createItem(path+"."+s));
		return this;
	}
    
    private ItemBuilder createItem(String string) {
    	if(c.exists(string+".head")){
    		if(c.exists(string+".lore")){
    			return new ItemBuilder(c.getString(string+".name"),c.getInt(string+".amount"),c.getStringList(string+".lore"),c.getString(string+".head"));
            }else{
            	return new ItemBuilder(c.getString(string+".name"),c.getInt(string+".amount"),null,c.getString(string+".head"));
            }
    	} else if(c.exists(string+".type")){
    		if(c.exists(string+".lore")){
    			return new ItemBuilder(XMaterial.matchXMaterial(c.getString(string+".type")),c.getInt(string+".amount"),c.getString(string+".name"),c.getStringList(string+".lore"), c.getInt(string+".model"));
            }else{
            	return new ItemBuilder(XMaterial.matchXMaterial(c.getString(string+".type")),c.getInt(string+".amount"),c.getString(string+".name"),null, c.getInt(string+".model"));
           }
        }
		return null;
	}

    private void onUse(Player p, HolderGUI g, String string, ClickType clickType){
    	String perm = c.getString(s+"."+string+".permission");
    	if(perm!=null && perm.trim().equals(""))perm=null;
    	if(perm!=null && (perm.startsWith("-")?!p.hasPermission(perm.substring(1)):p.hasPermission(perm))) {
    		process(clickType, g,p,string+".noPermission");
    		return;
    	}
    	if(c.getDouble(s+".cost")<=0)
		process(clickType, g,p,string+".action");
    	else
        if(EconomyAPI.has(p,c.getDouble(s+"."+string+".cost"))) {
            if(c.getBoolean(s+"."+string+".takeMoney")){
                EconomyAPI.withdrawPlayer(p,c.getDouble(s+"."+string+".cost"));
            }
    		process(clickType, g,p,string+".action");
        }else {
    		process(clickType, g,p,string+".noMoney");
        }
    }
    
    private void process(ClickType clickType, HolderGUI g, Player p, String string) {
        if (c.get(s + "."+string) instanceof Collection&&c.exists(s+"."+string)) {
            for (String a : c.getStringList(s + "."+string)) {
                if(a.startsWith("any"))
                	prov(g, p, a.substring(4));
                else
                if (a.startsWith(fixedName(clickType)))
                	prov(g, p, a.substring(fixedName(clickType).length()+1));
            }
        } else {
            String a = c.getString(s + "."+string);
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
        case MIDDLE_DROP:
        case MIDDLE_PICKUP:
        	return "middle";
        case RIGHT_DROP:
        case RIGHT_PICKUP:
        	return "right";
        case SHIFT_LEFT_DROP:
        case SHIFT_LEFT_PICKUP:
        	return "shift_left";
        case SHIFT_MIDDLE_PICKUP:
        	return "shift_middle";
        case SHIFT_RIGHT_DROP:
        case SHIFT_RIGHT_PICKUP:
        	return "shift_right";
        }
        return null;
	}

	private void prov(HolderGUI g ,Player p,String a){
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
        	GUIMaker f = GUICreator.maker.get(a.substring(5));
        	if(f!=null)
            f.make(p).open(p);
        } else if(a.startsWith("wait")){
        	try {
				Thread.sleep(50*StringUtils.getLong(a.substring(5)));
			} catch (Exception e) {
			}
        }
    }
}

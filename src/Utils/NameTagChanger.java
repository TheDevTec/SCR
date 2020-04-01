package Utils;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.Straiker123.NameTagAPI;
import me.Straiker123.TheAPI;
		
public class NameTagChanger {
	static HashMap<Player, NameTagAPI> t = new HashMap<Player, NameTagAPI> ();
	public static void setNameTag(Player p, String prefix, String suffix) {
        	if(setting.tab_sort) {
	            	if(setting.tab_nametag) {
	            		if(!t.containsKey(p))t.put(p, TheAPI.getNameTagAPI(p, TabList.replace(prefix,p), TabList.replace(suffix,p)));
	            		NameTagAPI n = t.get(p);
	            		n.setPrefix(TabList.replace(prefix,p));
	            		n.setSuffix(TabList.replace(suffix,p));
	            		n.setNameTag(TabList.getGroup(p)+Tasks.ss.get(p.getName()));
	            		
	            	}else {
	     				String pname = p.getName();
	     				if(pname.length() > 16)
	     					pname = pname.substring(0, 15);
	            		if(!t.containsKey(p))t.put(p, TheAPI.getNameTagAPI(p, null, null));
	            		NameTagAPI n = t.get(p);
	            		n.setPrefix(null);
		            		n.setSuffix(null);
		            		n.setNameTag(pname);
		            		
		}}else {
	            	if(setting.tab_nametag) {
	            		NameTagAPI n = t.get(p);
	            		n.setPrefix(TabList.replace(prefix,p));
		            		n.setSuffix(TabList.replace(suffix,p));
		            		n.setNameTag(Tasks.ss.get(p.getName()));
		            		
	            	}
        	}}
	public static void remove(Player p) {
		t.remove(p);
		TheAPI.getNameTagAPI(p, null, null).resetNameTag();
	}
}
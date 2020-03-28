package Utils;

import org.bukkit.entity.Player;

import me.Straiker123.TheAPI;
		
public class NameTagChanger {
	public static void setNameTag(Player p, String prefix, String suffix) {
        	if(setting.tab_sort) {
	            	if(setting.tab_nametag)
	            		TheAPI.getNameTagAPI(p, TabList.replace(prefix,p), TabList.replace(suffix,p))
	            		.setNameTag(TabList.getGroup(p)+Tasks.ss.get(p.getName()));
	            	else {
	     				String pname = p.getName();
	     				if(pname.length() > 16)
	     					pname = pname.substring(0, 15);
	            		TheAPI.getNameTagAPI(p, null, null).setNameTag(pname);
		}}else {
	            	if(setting.tab_nametag)
	            		TheAPI.getNameTagAPI(p, TabList.replace(prefix,p), TabList.replace(suffix,p))
	            		.setNameTag(Tasks.ss.get(p.getName()));
        	}}
	public static void remove(Player p) {
		TheAPI.getNameTagAPI(p, null, null).resetNameTag();
	}
}
package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import ServerControl.Loader;
import Utils.Configs;
/**
 * 
 * @author waskerSK
 * 
 * Edited by Straiker123
 */
public class RequestMap {
    public static void addRequest(String target, String sender, Type tp) {
    	Loader.me.set("Players."+sender+".TeleportType."+target, tp.toString());
    	Loader.me.set("Players."+sender+".TpaSender."+target, System.currentTimeMillis()/1000);
		Configs.chatme.save();
    }

    public static void removeRequest(String target, String sender) {
        Loader.me.set("Players."+target+".TpaSender."+sender, null);
        Loader.me.set("Players."+target+".TeleportType."+sender, null);
		Configs.chatme.save();    
    }
    
    public static String getRequest(String p) {
        if(Loader.me.getString("Players."+p+".TpaSender")!=null) {
            List<String> s = new ArrayList<String>();
            if(Loader.me.getString("Players."+p+".TpaSender")!=null)
            for(String d : Loader.me.getConfigurationSection("Players."+p+".TpaSender").getKeys(false))
            	if(Bukkit.getPlayer(d)!=null)s.add(d);
            if(!s.isEmpty())return s.get(0);
            return null;
        }
        return null;
    }
    
    public static enum Type {
    	TPA,
    	TPAHERE
    }

    public static Type getTeleportType(String target, String sender) {
        return Type.valueOf(Loader.me.getString("Players."+target+".TeleportType."+sender));
    }
    
    public static boolean containsRequest(String target, String sender) {
    	if(Loader.me.getLong("Players."+target+".TpaSender."+sender)
    			-System.currentTimeMillis()/1000+Loader.config.getInt("TpaRequestTime")>0)
    		return true;
    	else {
    		removeRequest(target, sender);
        	return false;	
    	}
    }
    
}
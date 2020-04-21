package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import ServerControl.Loader;
import Utils.setting;
import me.Straiker123.TheAPI;
/**
 * 
 * @author waskerSK
 * 
 * Updated by Straiker123
 */
public class RequestMap {
    public static void addRequest(String target, String sender, Type tp) {
    	Loader.me.set("Players."+sender+".Tp."+target+".Type", tp.toString());
    	Loader.me.set("Players."+sender+".Tp."+target+".Time", System.currentTimeMillis()/1000);
    	if(setting.tp_onreqloc)
    	Loader.me.set("Players."+sender+".Tp."+target+".Location", TheAPI.getStringUtils().getLocationAsString(TheAPI.getPlayer(target).getLocation()));
	}

    public static void removeRequest(String target, String sender) {
        Loader.me.set("Players."+target+".Tp."+sender, null);
	}
    
    public static String getRequest(String p) {
        if(Loader.me.getString("Players."+p+".Tp")!=null) {
            List<String> s = new ArrayList<String>();
            for(String d : Loader.me.getConfigurationSection("Players."+p+".Tp").getKeys(false))
            	if(TheAPI.getPlayer(d)!=null)s.add(d);
            if(!s.isEmpty())return s.get(0);
            return null;
        }
        return null;
    }
    
    public static Location getLocation(String p, String t) {
    	if(Loader.me.getString("Players."+p+".Tp."+t+".Location")!=null)
    		return TheAPI.getStringUtils().getLocationFromString(Loader.me.getString("Players."+p+".Tp."+t+".Location"));
    	return null;
    }
    
    public static enum Type {
    	TPA,
    	TPAHERE
    }

    public static Type getTeleportType(String target, String sender) {
        return Type.valueOf(Loader.me.getString("Players."+target+".Tp."+sender+".Type"));
    }
    
    public static boolean containsRequest(String target, String sender) {
    	if(Loader.me.getLong("Players."+target+".Tp."+sender+".Time")
    			-System.currentTimeMillis()/1000+TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("Options.Teleport.RequestTime"))>0)
    		return true;
    	else {
    		removeRequest(target, sender);
        	return false;	
    	}
    }
    
}
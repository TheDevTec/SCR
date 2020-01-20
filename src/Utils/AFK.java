package Utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ServerControl.Loader;

public class AFK {
	public static boolean isAFK(Player p) {
		int afk = 300; //5min
		if(Loader.config.getInt("AFK.Time")!=0)afk=Loader.config.getInt("AFK.Time");
		if(getAFKTime(p) != -1 && getAFKTime(p) >= afk && setting.afk_auto||Loader.me.getBoolean("Players."+p.getName()+".AFK-Manual")) {
			return true;
		}
		return false;
	}
	public static int getAFKTime(Player p) {
		int afk = -1;
		if(time.containsKey(p)) {
		long afks = time.get(p)/1000-System.currentTimeMillis()/1000;
		afks=-1*afks;
		afk=(int)afks;
		}
		return afk;
	}
	
	public static boolean wait(Player p) {
		boolean wa = false;
		if(w.get(p)!=null)wa=w.get(p);
		if(!wa) {
			
			w.put(p, true);
			Bukkit.getScheduler().runTaskLater(Loader.getInstance, new Runnable() {

				@Override
				public void run() {
					w.put(p, false);
				}

			}, 20);
			return true;
		}
		return false;
	}

	public static HashMap<Player, Long> time = new HashMap<Player, Long>();
	public static HashMap<Player, Boolean> w = new HashMap<Player, Boolean>();
	
	public static void save(Player p) {
		time.put(p,System.currentTimeMillis());
		Loader.me.set("Players."+p.getName()+".AFK-Broadcast",false);
	}
	public static boolean broadcast(Player p) {
		if(!Loader.me.getBoolean("Players."+p.getName()+".AFK-Broadcast")) {
			Loader.me.set("Players."+p.getName()+".AFK-Broadcast",true);
			return true;
		}
		return false;
	}
	

}

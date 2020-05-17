package Commands.Main;

import org.bukkit.Bukkit;
import org.bukkit.World;

import ServerControl.Loader;
import Utils.setting;
import me.Straiker123.Reflections;
import me.Straiker123.TheAPI;
import me.Straiker123.TheRunnable;

public class BigTask {
	public static TheRunnable r;
	public static boolean cancel() {
		if(r!=null) {
		r.cancel();
		return true;
		}
		return false;
	}
	
	public static enum TaskType {
		STOP,
		RESTART,
		RELOAD
	}
	private static TaskType s;
	public static boolean start(TaskType t,long h) {
		s=t;
		if((t==TaskType.STOP ? setting.warn_stop : (t==TaskType.RELOAD ? setting.warn_reload : setting.warn_restart))) {
			if(r==null) {
				r = new TheRunnable();
			r.runRepeating(new Runnable() {
				long f= h;
				public void run() {
					if(f<=0) {
						end();
						return;
					}else if(f == h%75 && h > 15  ||f == h%50 && h > 10 ||f == h%25 && h > 5  || f == 5 || f==4||f==3||f==2||f==1){
						for(String s:Loader.config.getStringList("Options.WarningSystem."+(t==TaskType.STOP ? "Stop" : (t==TaskType.RELOAD ? "Reload" : "Restart"))+".Messages"))
							TheAPI.broadcastMessage(s.replace("%time%", ""+TheAPI.getStringUtils().setTimeToString(f)));
					}
					--f;
				}}, 20);
			return true;
			}
		return false;	
		}else {
				end();
				return true;
			}
	}
	public static void cancel(TaskType t) {
		if(r!=null)
			r.cancel();
			r=null;
			TheAPI.broadcastMessage("&eCancelled "+(s==TaskType.STOP ? "stopping" : (s==TaskType.RELOAD ? "reloading" : "restarting"))+" of server!");
	}
	public static void end() {
		if(r!=null)
			r.cancel();
			r=null;
			TheAPI.broadcastMessage("&eSaving worlds and data of players..");
			for(World w: Bukkit.getWorlds()) {
				w.save();
			}
			Bukkit.savePlayers();
			TheAPI.broadcastMessage("&c"+(s==TaskType.STOP ? "Stopping" : (s==TaskType.RELOAD ? "Reloading" : "Restarting"))+" of server..");
			switch(s) {
			case RELOAD:
				Bukkit.reload();
				break;
			case RESTART:
				if(Reflections.existsClass("net.md_5.bungee.api.ChatColor"))
					Bukkit.spigot().restart();
				else
				Bukkit.shutdown();
				break;
			case STOP:
				Bukkit.shutdown();
				break;
			}
	}
}
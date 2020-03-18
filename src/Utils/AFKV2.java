package Utils;

import ServerControl.Loader;
import me.Straiker123.TheAPI;
import me.Straiker123.TheRunnable;

public class AFKV2 {
	String d;
	public AFKV2(String p) {
	d=p;
	}
	TheRunnable r = TheAPI.getRunnable();
	int kick = 0;
	public void start() {
		r.runRepeating(new Runnable() {
			@Override
			public void run() {
				if(setting.afk_auto) {
					if(getTime() <=0) {
						if(setting.afk_kick)
						++kick;
					}
				}
			}
		}, 20);
	}
	
	public long getTime() {
		return Loader.me.getLong("Players."+d+".AFK.Time")-System.currentTimeMillis()/1000+Loader.config.getInt("Options.AFK.Time");
	}
	
	
	public void stop() {
		r.cancel();
	}
	
	public void save() {
		kick=0;
		Loader.me.set("Players."+d+".AFK.Is",false);
		Loader.me.set("Players."+d+".AFK.Time",System.currentTimeMillis()/1000);
	}
}

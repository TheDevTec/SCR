package Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ServerControl.API;
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
	long time = TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("Options.AFK.TimeToAFK"));
	public void start() {
		r.runRepeating(new Runnable() {
			int f =0;
			@Override
			public void run() {
				Player s = Bukkit.getPlayer(d);
				if(s==null) {
					Loader.afk.remove(d);
					Loader.me.set("Players."+d+".AFK",null);
					r.cancel();
					return;
				}
				if(f==4) {
					f=0;
					if(setting.afk_kick)
					time = TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("Options.AFK.TimeToAFK"));
				}
				if(setting.afk_auto) {
					if(isAFK()|| manual) {
						if(!bc) {
							bc=true;
							if(!manual || manual && !mp) {
								if(manual)mp=true;
						  	  if(!API.getSPlayer(s).hasVanish())
						  		  TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("AFK.IsAFK").replace("%player%", d)
								   .replace("%playername%", s.getDisplayName()));
						}}
						if(setting.afk_kick && isAFK()) {
							if(kick>=time) {
								Bukkit.getPlayer(d).kickPlayer(TheAPI.colorize(Loader.config.getString("Options.AFK.KickMessage")));
								r.cancel();
								return;
							}else
						++kick;
						}
					}
				}
				if(bc && !isAFK()) {
					bc=false;
				  	  if(!API.getSPlayer(s).hasVanish())
				  		  TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("AFK.NoLongerAFK").replace("%player%", d)
						   .replace("%playername%", s.getDisplayName()));
				}
			}
		}, 40);
	}
	
	boolean manual,mp;
	
	public void setAFK() {
		kick=0;
		manual=true;
		mp=false;
		Loader.me.set("Players."+d+".AFK",System.currentTimeMillis()/1000);
	}
	
	public long getTime() {
		return Loader.me.getLong("Players."+d+".AFK")-System.currentTimeMillis()/1000+Loader.config.getInt("Options.AFK.Time");
	}
	boolean bc;
	public void save() {
		kick=0;
		manual=false;
		Loader.me.set("Players."+d+".AFK",System.currentTimeMillis()/1000);
	}

	public boolean isAFK() {
		return getTime()<=0;
	}
}

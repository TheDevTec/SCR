package Utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
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
	long rkick = TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("Options.AFK.TimeToKick"));
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
				if(f==5) {
					f=0;
					time = TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("Options.AFK.TimeToAFK"));
					if(setting.afk_kick)
					rkick = TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("Options.AFK.TimeToKick"));
				}
				boolean is = isAfk();
				if(setting.afk_auto) {
					if(is) {
						if(!bc && !mp) {
							bc=true;
							mp=true;
						  	  if(!API.getSPlayer(s).hasVanish())
						  		  TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("AFK.IsAFK").replace("%player%", d)
								   .replace("%playername%", s.getDisplayName()));
						  	  }
						if(setting.afk_kick && is) {
							if(kick>=rkick) {
								if(!s.hasPermission("servercontrol.afk.bypass"))
								s.kickPlayer(TheAPI.colorize(Loader.config.getString("Options.AFK.KickMessage")));
								r.cancel();
								return;
							}else
						++kick;
						}
					}
				}
			}
		},20,20);
	}
	
	boolean mp, manual;
	
	public void setAFK() {
		save();
		mp=true;
		manual=true;
		Player s = Bukkit.getPlayer(d);
		if(s!=null) {
	  	  if(!API.getSPlayer(s).hasVanish())
	  		  TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("AFK.IsAFK").replace("%player%", d)
			   .replace("%playername%", s.getDisplayName()));
	}}
	FileConfiguration a = Loader.me;
	public long getTime() {
		return a.getString("Players."+d+".AFK")!=null ? a.getLong("Players."+d+".AFK")-System.currentTimeMillis()/1000+time:time;
	}
	boolean bc;
	public void save() {
		a.set("Players."+d+".AFK",System.currentTimeMillis()/1000);
		kick=0;
		manual=false;
		bc=false;
	}
	
	public boolean isManualAfk() {
		return manual;
	}
	
	public boolean isAfk() {
		return getTime()<=0;
	}
}

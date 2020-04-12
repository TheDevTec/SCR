package Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.StringUtils;
import me.Straiker123.TheAPI;
import me.Straiker123.TheRunnable;

public class AFKV2 {
	String d;
	public AFKV2(String p) {
	d=p;
	}
	StringUtils ss = TheAPI.getStringUtils();
	TheRunnable r = TheAPI.getRunnable();
	String afkMsg = Loader.s("Prefix")+Loader.s("AFK.IsAFK");
	int afk,kick;
	long time = ss.getTimeFromString(Loader.config.getString("Options.AFK.TimeToAFK")),rkick = ss.getTimeFromString(Loader.config.getString("Options.AFK.TimeToKick"));
	public void start() {
		r.runRepeating(new Runnable() {
			int f =0;
			@Override
			public void run() {
				Player s = TheAPI.getPlayer(d);
				if(s==null) {
					Loader.afk.remove(d);
					r.cancel();
					return;
				}
				if(f==5) {
					f=0;
					time = ss.getTimeFromString(Loader.config.getString("Options.AFK.TimeToAFK"));
					afkMsg = Loader.s("Prefix")+Loader.s("AFK.IsAFK");
					if(setting.afk_kick)
					rkick = ss.getTimeFromString(Loader.config.getString("Options.AFK.TimeToKick"));
				}
				boolean is = isAfk();
				if(setting.afk_auto) {
					if(is) {
						if(!bc && !mp) {
							bc=true;
							mp=true;
						  	  if(!API.getSPlayer(s).hasVanish())
						  		  TheAPI.broadcastMessage(afkMsg.replace("%player%", d)
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
				}else
					++afk;
			}
		},20,20);
	}
	
	boolean mp, manual;
	
	public void setAFK() {
		save();
		mp=true;
		manual=true;
		Player s = TheAPI.getPlayer(d);
		if(s!=null) {
	  	  if(!API.getSPlayer(s).hasVanish())
	  		  TheAPI.broadcastMessage(afkMsg.replace("%player%", d)
			   .replace("%playername%", s.getDisplayName()));
	}}
	FileConfiguration a = Loader.me;
	public long getTime() {
		return time-afk;
	}
	boolean bc;
	public void save() {
		afk=0;
		kick=0;
		manual=false;
		mp=false;
		bc=false;
	}
	
	public boolean isManualAfk() {
		return manual;
	}
	
	public boolean isAfk() {
		return getTime()<=0;
	}
}

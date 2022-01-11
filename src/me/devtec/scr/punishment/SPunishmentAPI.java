package me.devtec.scr.punishment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import me.devtec.scr.api.events.BanlistBanEvent;
import me.devtec.scr.api.events.BanlistEvent;
import me.devtec.scr.api.events.BanlistJailEvent;
import me.devtec.scr.api.events.BanlistMuteEvent;
import me.devtec.scr.api.events.BanlistTempBanEvent;
import me.devtec.scr.api.events.BanlistTempJailEvent;
import me.devtec.scr.api.events.BanlistTempMuteEvent;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.punishmentapi.Punishment;
import me.devtec.theapi.punishmentapi.Punishment.PunishmentType;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.Data;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.listener.Cancellable;

public class SPunishmentAPI implements PunishmentAPI {

	public static Data data = new Data("plugins/ServerControlReloaded/Datas/BanList.dat");
	static Data jails = new Data("plugins/ServerControlReloaded/Datas/Jails.dat");
	
	//MUTE
	public Punishment mute(String user, long duration, String reason) {
		String type = duration == 0 ? "mute" : "tempmute";
		if(duration<0)duration=0;
		BanlistEvent e;
		if(duration!=0) {
			e = new BanlistTempMuteEvent(user, reason, duration);
		}else
			e = new BanlistMuteEvent(user, reason);
		TheAPI.callEvent(e);
		if(((Cancellable) e).isCancelled())return null;
		reason=e.getReason()+"";
		if(e instanceof BanlistTempMuteEvent) {
			duration=((BanlistTempMuteEvent)e).getDuration();
			if(duration<0)duration=0;
		}
		reason=e.getReason()+"";

		if(duration!=0)
		data.set("u."+user.toLowerCase()+"."+type+".duration", duration);
		data.set("u."+user.toLowerCase()+"."+type+".reason", reason);
		data.set("u."+user.toLowerCase()+"."+type+".start", System.currentTimeMillis()/1000);
		data.save();
		
		Punishment punish = new SPunishment(data, user, PunishmentType.MUTE, type);
		process(punish);
		return punish;
	}

	public Punishment muteIP(String ip, long duration, String reason) {
		if(!ip.contains("."))
			ip=getIp(ip);
		if(duration<0)duration=0;
		BanlistEvent e;
		if(duration!=0) {
			e = new BanlistTempMuteEvent(ip, reason, duration);
		}else
			e = new BanlistMuteEvent(ip, reason);
		TheAPI.callEvent(e);
		if(((Cancellable) e).isCancelled())return null;
		if(e instanceof BanlistTempMuteEvent) {
			duration=((BanlistTempMuteEvent)e).getDuration();
			if(duration<0)duration=0;
		}
		reason=e.getReason()+"";
		
		String type = duration == 0 ? "muteip" : "tempmuteip";
		if(duration!=0)
		data.set("i."+ip.replace(".", "_")+"."+type+".duration", duration);
		data.set("i."+ip.replace(".", "_")+"."+type+".reason", reason);
		data.set("i."+ip.replace(".", "_")+"."+type+".start", System.currentTimeMillis()/1000);
		data.save();
		
		Punishment punish = new SPunishment(data, ip, PunishmentType.MUTE, type);
		process(punish);
		return punish;
	}

	//BAN
	public Punishment ban(String user, long duration, String reason) {
		String type = duration == 0 ? "ban" : "tempban";
		if(duration<0)duration=0;
		BanlistEvent e;
		if(duration!=0) {
			e = new BanlistTempBanEvent(user, reason, duration);
		}else
			e = new BanlistBanEvent(user, reason);
		TheAPI.callEvent(e);
		if(((Cancellable) e).isCancelled())return null;
		if(e instanceof BanlistTempBanEvent) {
			duration=((BanlistTempBanEvent)e).getDuration();
			if(duration<0)duration=0;
		}
		reason=e.getReason()+"";

		if(duration!=0)
		data.set("u."+user.toLowerCase()+"."+type+".duration", duration);
		data.set("u."+user.toLowerCase()+"."+type+".reason", reason);
		data.set("u."+user.toLowerCase()+"."+type+".start", System.currentTimeMillis()/1000);
		data.save();
		
		Punishment punish = new SPunishment(data, user, PunishmentType.BAN, type);
		process(punish);
		return punish;
	}

	public Punishment banIP(String ip, long duration, String reason) {
		if(!ip.contains("."))
			ip=getIp(ip);
		if(duration<0)duration=0;
		BanlistEvent e;
		if(duration!=0) {
			e = new BanlistTempBanEvent(ip, reason, duration);
		}else
			e = new BanlistBanEvent(ip, reason);
		TheAPI.callEvent(e);
		if(((Cancellable) e).isCancelled())return null;
		if(e instanceof BanlistTempBanEvent) {
			duration=((BanlistTempBanEvent)e).getDuration();
			if(duration<0)duration=0;
		}
		reason=e.getReason()+"";
		
		String type = duration == 0 ? "banip" : "tempbanip";

		if(duration!=0)
		data.set("i."+ip.replace(".", "_")+"."+type+".duration", duration);
		data.set("i."+ip.replace(".", "_")+"."+type+".reason", reason);
		data.set("i."+ip.replace(".", "_")+"."+type+".start", System.currentTimeMillis()/1000);
		data.save();
		
		Punishment punish = new SPunishment(data, ip, PunishmentType.BAN, type);
		process(punish);
		return punish;
	}

	//JAIL
	public Punishment jail(String user, long duration, String reason) {
		String type = duration == 0 ? "jail" : "tempjail";

		String jailId = findAnyJail();

		if(duration<0)duration=0;
		BanlistEvent e;
		if(duration!=0) {
			e = new BanlistTempJailEvent(user, reason, jailId, duration);
		}else
			e = new BanlistJailEvent(user, reason, jailId);
		TheAPI.callEvent(e);
		if(((Cancellable) e).isCancelled())return null;
		jailId=((BanlistJailEvent)e).getJailId();
		if(jailId==null||getJail(jailId)==null)return null;
		if(e instanceof BanlistTempJailEvent) {
			duration=((BanlistTempJailEvent)e).getDuration();
			if(duration<0)duration=0;
		}
		reason=e.getReason()+"";

		if(duration!=0)
		data.set("u."+user.toLowerCase()+"."+type+".duration", duration);
		data.set("u."+user.toLowerCase()+"."+type+".reason", reason);
		data.set("u."+user.toLowerCase()+"."+type+".start", System.currentTimeMillis()/1000);
		data.set("u."+user.toLowerCase()+"."+type+".position", getJail(jailId).toString());
		data.save();
		
		Punishment punish = new SPunishment(data, user, PunishmentType.JAIL, type);
		process(punish);
		return punish;
	}

	public Punishment jailIP(String ip, long duration, String reason) {
		if(!ip.contains("."))
			ip=getIp(ip);
		String jailId = findAnyJail();

		if(duration<0)duration=0;
		BanlistEvent e;
		if(duration!=0) {
			e = new BanlistTempJailEvent(ip, reason, jailId, duration);
		}else
			e = new BanlistJailEvent(ip, reason, jailId);
		TheAPI.callEvent(e);
		if(((Cancellable) e).isCancelled())return null;
		jailId=((BanlistJailEvent)e).getJailId();
		if(jailId==null||getJail(jailId)==null)return null;
		if(e instanceof BanlistTempJailEvent) {
			duration=((BanlistTempJailEvent)e).getDuration();
			if(duration<0)duration=0;
		}
		reason=e.getReason()+"";
		
		String type = duration == 0 ? "jailip" : "tempjailip";

		if(duration!=0)
		data.set("i."+ip.replace(".", "_")+"."+type+".duration", duration);
		data.set("i."+ip.replace(".", "_")+"."+type+".reason", reason);
		data.set("i."+ip.replace(".", "_")+"."+type+".start", System.currentTimeMillis()/1000);
		data.set("i."+ip.replace(".", "_")+"."+type+".position", getJail(jailId).toString());
		data.save();
		
		Punishment punish = new SPunishment(data, ip, PunishmentType.JAIL, type);
		process(punish);
		return punish;
	}

	//JAILS
	//POSITION
	private String findAnyJail() {
		return jails.getKeys().isEmpty()?null:TheAPI.getRandomFromCollection(jails.getKeys());
	}
	
	public static Set<String> jails() {
		return jails.getKeys();
	}
	
	public static Position getJail(String name) {
		return Position.fromString(jails.getString(name));
	}
	
	public static void setJail(String name, Position pos) {
		jails.set(name, pos.toString());
		jails.save();
	}
	
	public static void removeJail(String name) {
		jails.remove(name);
		jails.save();
	}

	private void process(Punishment punish) {
		switch(punish.getType()) {
		case BAN:
			//message (just log to %appdata%/.minecraft/logs/latest.log ?) & kick
			if(punish.getTypeName().endsWith("ip")) {
				for(Player p : TheAPI.getOnlinePlayers()) {
					if(getIp(p.getName()).equals(punish.getUser())) {
						String reason = punish.getReason().replace("%player%", p.getName());
						if(punish.getDuration()!=0)reason=reason.replace("%time%", StringUtils.timeToString(punish.getExpire()));
						reason=PlaceholderAPI.setPlaceholders(p, reason);
						TheAPI.msg(reason, p);
						p.kickPlayer(TheAPI.colorize(reason));
					}
				}
			}else {
				Player p = TheAPI.getPlayerOrNull(punish.getUser());
				if(p==null)break;
				String reason = punish.getReason().replace("%player%", p.getName());
				if(punish.getDuration()!=0)reason=reason.replace("%time%", StringUtils.timeToString(punish.getExpire()));
				reason=PlaceholderAPI.setPlaceholders(p, reason);
				TheAPI.msg(reason, p);
				p.kickPlayer(TheAPI.colorize(reason));
			}
			break;
		case JAIL:
			//teleport & message
			if(punish.getTypeName().endsWith("ip")) {
				for(Player p : TheAPI.getOnlinePlayers()) {
					if(getIp(p.getName()).equals(punish.getUser())) {
						String reason = punish.getReason().replace("%player%", p.getName());
						if(punish.getDuration()!=0)reason=reason.replace("%time%", StringUtils.timeToString(punish.getExpire()));
						reason=PlaceholderAPI.setPlaceholders(p, reason);
						TheAPI.msg(reason, p);
						p.teleport(Position.fromString(punish.getValue("position").toString()).toLocation());
					}
				}
			}else {
				Player p = TheAPI.getPlayerOrNull(punish.getUser());
				if(p==null)break;
				String reason = punish.getReason().replace("%player%", p.getName());
				if(punish.getDuration()!=0)reason=reason.replace("%time%", StringUtils.timeToString(punish.getExpire()));
				reason=PlaceholderAPI.setPlaceholders(p, reason);
				TheAPI.msg(reason, p);
				p.teleport(Position.fromString(punish.getValue("position").toString()).toLocation());
			}
			break;
		case MUTE:
			//message
			if(punish.getTypeName().endsWith("ip")) {
				for(Player p : TheAPI.getOnlinePlayers()) {
					if(getIp(p.getName()).equals(punish.getUser())) {
						String reason = punish.getReason().replace("%player%", p.getName());
						if(punish.getDuration()!=0)reason=reason.replace("%time%", StringUtils.timeToString(punish.getExpire()));
						reason=PlaceholderAPI.setPlaceholders(p, reason);
						TheAPI.msg(reason, p);
					}
				}
			}else {
				Player p = TheAPI.getPlayerOrNull(punish.getUser());
				if(p==null)break;
				String reason = punish.getReason().replace("%player%", p.getName());
				if(punish.getDuration()!=0)reason=reason.replace("%time%", StringUtils.timeToString(punish.getExpire()));
				reason=PlaceholderAPI.setPlaceholders(p, reason);
				TheAPI.msg(reason, p);
			}
			break;
		case CUSTOM:
			//nothing
			break;
		}
	}

	//WARN
	public void warn(String user, String reason) {
		Player s = TheAPI.getPlayerOrNull(user);
		if(s!=null) {
			TheAPI.msg(reason, s);
			return;
		}
		User d = TheAPI.getUser(user);
		List<String> warnings = d.getStringList("warnings");
		warnings.add(reason);
		d.set("warnings", warnings);
		d.save();
	}
	
	public static void sendWarnings(Player s) {
		User d = TheAPI.getUser(s);
		for(String f : d.getStringList("warnings")) {
			TheAPI.msg(f, s);
		}
		d.remove("warnings");
		d.save();
	}

	//GETTERS
	public List<Punishment> getPunishments(String user) {
		List<Punishment> list = new ArrayList<>();
		for(String key : data.getKeys("u."+user.toLowerCase())) {
			list.add(new SPunishment(data,user,findType(key),key));
		}
		return list;
	}

	public List<Punishment> getPunishmentsIP(String ip) {
		List<Punishment> list = new ArrayList<>();
		for(String key : data.getKeys("i."+ip.replace(".", "_"))) {
			list.add(new SPunishment(data,ip,findType(key),key));
		}
		return list;
	}

	private PunishmentType findType(String key) {
		if(key.contains("mute"))return PunishmentType.MUTE;
		if(key.contains("ban"))return PunishmentType.BAN;
		if(key.contains("jail"))return PunishmentType.JAIL;
		return PunishmentType.CUSTOM;
	}
	
	public String toString() {
		return "PunishmentAPI provider from SCR";
	}
}
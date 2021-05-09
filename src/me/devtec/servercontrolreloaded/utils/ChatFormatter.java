package me.devtec.servercontrolreloaded.utils;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.events.ChatFormat;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Item;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.datakeeper.User;

public class ChatFormatter {
	public static String displayName(Player p) {
		Object[] format = getChatFormat(p, 0);
		if (format!=null)
			return Colors.colorize(ChatFormat.r(p, PlaceholderAPI.setPlaceholders(p, format[0].toString()), null, false, true).toString(), false, p);
		return Loader.getChatFormat(p, Item.PREFIX) + p.getName() + Loader.getChatFormat(p, Item.SUFFIX);
	}
	
	public static String customName(Player p) {
		if(TheAPI.getUser(p).exist("DisplayName"))
			return TheAPI.colorize(PlaceholderAPI.setPlaceholders(p, TheAPI.getUser(p).getString("DisplayName")));
		return p.getCustomName()!=null?p.getCustomName():p.getName();
	}
	
	public static void setupName(Player p) {
		if(p==null)return;
		p.setDisplayName(ChatFormatter.displayName(p));
		p.setCustomName(ChatFormatter.customName(p));
	}
	
	public static Object chat(Player p, String message) {
		Object[] format = getChatFormat(p, 1);
		return format!=null?ChatFormat.r(p, format[0], message, (format[0] instanceof Map || format[0] instanceof List) && getStatus(p, (int)format[1], "json"), true):null;
	}
	public static Object[] getChatFormat(Player player, int type) {
		String t = type==0?".name":".chat";
		String g = API.getGroup(player);
		
		//1. PerWorld -> PerUser
		if(!getStatus(player, 1, "enabled"))return null;
		if(Loader.config.exists("ChatFormat.world."+player.getWorld().getName()+".user."+player.getName()+t)) {
			return new Object[] {Loader.config.get("ChatFormat.world."+player.getWorld().getName()+".user."+player.getName()+t),1};
		}
		//2. PerWorld -> PerGroup
		if(!getStatus(player, 2, "enabled"))return null;
		if(Loader.config.exists("ChatFormat.world."+player.getWorld().getName()+".group."+g+t)) {
			return new Object[] {Loader.config.get("ChatFormat.world."+player.getWorld().getName()+".group."+g+t),2};
		}
		//3. PerWorld
		if(!getStatus(player, 3, "enabled"))return null;
		if(Loader.config.exists("ChatFormat.world."+player.getWorld().getName()+".global"+t)) {
			return new Object[] {Loader.config.get("ChatFormat.world."+player.getWorld().getName()+".global"+t),3};
		}
		//4. PerUser
		if(!getStatus(player, 4, "enabled"))return null;
		if(Loader.config.exists("ChatFormat.user."+player.getName()+t)) {
			return new Object[] {Loader.config.get("ChatFormat.user."+player.getName()+t),4};
		}
		//5. PerGroup
		if(!getStatus(player, 5, "enabled"))return null;
		if(Loader.config.exists("ChatFormat.group."+g+t)) {
			return new Object[] {Loader.config.get("ChatFormat.group."+g+t),5};
		}
		//6. Global
		return getStatus(player, 6, "enabled") ? new Object[] {Loader.config.get("ChatFormat.global"+t),6} : null;
	}
	
	public static boolean getStatus(Player player, int subType, String name) {
		String t = ".options."+name;
		switch(subType) {
		case 1:
			if(Loader.config.exists("ChatFormat.world."+player.getWorld().getName()+".user."+player.getName()+t)) {
				return Loader.config.getBoolean("ChatFormat.world."+player.getWorld().getName()+".user."+player.getName()+t);
			}
			if(Loader.config.exists("ChatFormat.world."+player.getWorld().getName()+".global"+t)) {
				return Loader.config.getBoolean("ChatFormat.world."+player.getWorld().getName()+".global"+t);
			}
			if(Loader.config.exists("ChatFormat.user."+player.getName()+t)) {
				return Loader.config.getBoolean("ChatFormat.user."+player.getName()+t);
			}
			break;
		case 3:
			if(Loader.config.exists("ChatFormat.world."+player.getWorld().getName()+".global"+t)) {
				return Loader.config.getBoolean("ChatFormat.world."+player.getWorld().getName()+".global"+t);
			}
			break;
		case 4:
			if(Loader.config.exists("ChatFormat.user."+player.getName()+t)) {
				return Loader.config.getBoolean("ChatFormat.user."+player.getName()+t);
			}
			break;
		case 2:
			String g = API.getGroup(player);
			if(Loader.config.exists("ChatFormat.world."+player.getWorld().getName()+".group."+g+t)) {
				return Loader.config.getBoolean("ChatFormat.world."+player.getWorld().getName()+".group."+g+t);
			}
			if(Loader.config.exists("ChatFormat.group."+g+t)) {
				return Loader.config.getBoolean("ChatFormat.group."+g+t);
			}
			if(Loader.config.exists("ChatFormat.user."+player.getName()+t)) {
				return Loader.config.getBoolean("ChatFormat.user."+player.getName()+t);
			}
			break;
		case 5:
			g = API.getGroup(player);
			if(Loader.config.exists("ChatFormat.group."+g+t)) {
				return Loader.config.getBoolean("ChatFormat.group."+g+t);
			}
			break;
		}
		return Loader.config.getBoolean("ChatFormat.global"+t);
	}

	public static boolean getNotify(Player s) {
		User a = TheAPI.getUser(s);
		if(!a.exist("notify.chat"))return true;
		return a.getBoolean("notify.chat");
	}

	public static void setNotify(Player s, boolean b) {
		TheAPI.getUser(s).setAndSave("notify.chat", b);
	}

	public static String getChatOrientation(Player s) {
		return TheAPI.getUser(s).getString("notify.orient");
	}

	public static void setChatOrientation(Player s, String b) {
		TheAPI.getUser(s).setAndSave("notify.orient", b);
	}
}

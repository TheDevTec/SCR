package me.DevTec.ServerControlReloaded.Utils;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.Events.ChatFormat;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Item;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;

public class ChatFormatter {
	public static String displayName(Player p) {
		String group = Loader.getChatFormat(p, Item.GROUP);
		if (Loader.config.exists("Chat-Groups." + group + ".Name")) {
			String g = PlaceholderAPI.setPlaceholders(p,Loader.config.getString("Chat-Groups." + group + ".Name"));
			g = (String) ChatFormat.r(p, g, null, false);
			return Colors.colorize(g, false, p);
		}
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
		Object format = Loader.config.get("Chat-Groups." + Loader.getChatFormat(p,Item.GROUP) + ".Chat");
		if (format != null) {
			return ChatFormat.r(p, format, message, format instanceof Map || format instanceof List);
		}
		return null;
	}
}

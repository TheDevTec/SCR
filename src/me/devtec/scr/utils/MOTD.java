package me.devtec.scr.utils;

import java.util.List;

import org.bukkit.Bukkit;

import me.devtec.scr.Loader;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class MOTD {

	public static String getUsed() {
		if(Loader.config.getString("serverlist.change_after").equalsIgnoreCase("-1"))
			return Loader.config.getString("serverlist.use");
		else 
			return getChangingUsed();
	}
	//	(START 
	//	- System.currentTimeMillis() / 1000 + TIME <= 0)
	private static String getChangingUsed() {
		long time = StringUtils.timeFromString(Loader.config.getString("serverlist.change_after"));
		long start = Loader.data.getLong("serverlist.start");
		if( (start - System.currentTimeMillis()/1000 + time) <=0) { //if time expired
			List<String> list = Loader.config.getStringList("serverlist.use");
			int last = -1;
			int size = -1;
			//Zda byl někdy použit changing motd
			if(Loader.data.exists("serverlist.last"))
				last = Loader.data.getInt("serverlist.last");
			if(Loader.data.exists("serverlist.size"))
				size = Loader.data.getInt("serverlist.size");
			
			if(size != list.size()) { // Pokud se seznam změnil -> reset
				last = 0;
				size = list.size();
			}
			else // Jinak další v seznamu
				last = last+1;
			
			if( !(list.size()>=last) ) // Pokud seznam má aktuální položku
				size = 0;
			
			Loader.data.set("serverlist.start", System.currentTimeMillis()/1000);
			Loader.data.set("serverlist.last", last);
			Loader.data.set("serverlist.size", list.size());
			Loader.data.set("serverlist.use", list.get(last));
			Loader.data.save();
			return list.get(last);
		}
		return Loader.data.getString("serverlist.use");
	}
	
	// GETTING MOTD things
	
	public static String getIcon(String motd) {
		if(Loader.config.exists("serverlist."+motd+".icon") &&
				!Loader.config.getString("serverlist."+motd+".icon").isEmpty())
			return Loader.config.getString("serverlist."+motd+".icon");
		else 
			return null;
	}
	public static String getMotd(String motd) {
		if(Loader.config.exists("serverlist."+motd+".motd.0") &&
				Loader.config.exists("serverlist."+motd+".motd.1"))
			return Loader.config.exists("serverlist."+motd+".motd.0")+"\n"
				+Loader.config.exists("serverlist."+motd+".motd.1");
		return null;
	}
	public static enum PlayersCountType {
		ONLINE, MAX;
	}
	public static int getPlayers(String motd, PlayersCountType type) {
		int vanished = 0;
		int rOnline = BukkitLoader.getOnlinePlayers().size();
		if(Loader.config.getBoolean("serverlist."+motd+".hide-vanished"))
			vanished = 0; //TODO - vanished
		int online = rOnline-vanished;
		String text = "%online%";
		
		switch (type) {
			case ONLINE:
				text = Loader.config.getString("serverlist."+motd+".online");
				break;
			case MAX:
				text = Loader.config.getString("serverlist."+motd+".max");
				break;
		}
		
		return StringUtils.getInt( PlaceholderAPISupport.replace(
				text.replace("%online%", online+"").replace("%online_real%", rOnline+"")
				.replace("%max_players%", getMaxPlayers()+""), null, false) );
	}
	private static int getMaxPlayers() {
		return Bukkit.getMaxPlayers();
	}
	
	public static List<String> getList(String motd) {
		List<String> list = Loader.config.getStringList("serverlist."+motd+".players.list");
		list.replaceAll(a -> PlaceholderAPISupport.replace(a, null, true));
		return list;
	}
	public static String getVersion(String motd) {
		if(Loader.config.exists("serverlist."+motd+".serverversion") &&
				!Loader.config.getString("serverlist."+motd+".serverversion").isEmpty() )
			return Loader.config.getString("serverlist."+motd+".serverversion");
		return null;
	}
	public static int getProtocol(String motd) {
		if(Loader.config.exists("serverlist."+motd+".protocol"))
			return Loader.config.getInt("serverlist."+motd+".protocol");
		return -1;
	}
}

package me.devtec.servercontrolreloaded.events.functions;

import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import me.devtec.servercontrolreloaded.commands.info.WhoIs;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;

public class CountryBlocker implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void asyncLogin(AsyncPlayerPreLoginEvent e){
		if(e.getLoginResult()!=AsyncPlayerPreLoginEvent.Result.ALLOWED)return;
		try {
			boolean ok = Loader.config.getStringList("CountryBlocker.List").isEmpty();
			if(ok)return;
			Map<String,Object> country = WhoIs.getCountry(e.getAddress().toString().replaceAll("[^0-9.]+", "").replace("..", ""));
			String countryName = ((String)country.getOrDefault("countryCode","UNKNOWN")).toUpperCase();
		for(String a : Loader.config.getStringList("CountryBlocker.List")){
			if(!countryName.equals(a.toUpperCase())) {
				for(String b:Loader.config.getStringList("CountryBlocker.Whitelist")){
					if(e.getName().equalsIgnoreCase(b)){
						ok=true;
						break;
					}else{
						ok=false;
						break;
					}
				}
			}else{
				ok=true;
				break;
			}
		}
		if(!ok) {
			e.setKickMessage(TheAPI.colorize(Loader.config.getString("CountryBlocker.KickMessage")));
			e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
		}
		}catch(Exception err) {}
	}
}
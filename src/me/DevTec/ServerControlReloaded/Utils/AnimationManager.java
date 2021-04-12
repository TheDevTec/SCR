package me.DevTec.ServerControlReloaded.Utils;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AnimationManager {
	Map<String, Animation> a = new HashMap<>();
	
	public void reload() {
		a.clear();
		for(String anim : Loader.anim.getKeys()) {
			Animation s = new Animation(Loader.anim.getStringList(anim+".lines")
					, StringUtils.calculate(Loader.anim.getString(anim+".speed")).longValue());
			a.put(anim, s);
		}
	}
	
	public String requestAnimation(String name) {
		Animation get = a.getOrDefault(name, null);
		if(get==null)return null;
		return get.get();
	}
	
	public String replace(Player player, String where) {
		if(where==null)return null;
		for(Entry<String, Animation> e : a.entrySet()) {
			where=where.replace("%animation-"+e.getKey()+"%", e.getValue().get());
		}
		return PlaceholderAPI.setPlaceholders(player, TabList.replace(where, player, true));
	}

	public String replaceWithoutColors(Player player, String string) {
		if(string==null)return null;
		for(Entry<String, Animation> e : a.entrySet()) {
			string=string.replace("%animation-"+e.getKey()+"%", e.getValue().get());
		}
		return PlaceholderAPI.setPlaceholders(player, TabList.replace(string, player, false));
	}
	
	public void update() {
		for(Animation e : a.values())
			e.update();
	}
}

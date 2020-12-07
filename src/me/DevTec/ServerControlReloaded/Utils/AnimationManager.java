package me.DevTec.ServerControlReloaded.Utils;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.PlaceholderAPI.PlaceholderAPI;
import me.DevTec.TheAPI.Utils.Animation;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.Maps.UnsortedMap;

public class AnimationManager {
	static Map<String, Animation> a = new UnsortedMap<>();
	
	public static void reload() {
		a.clear();
		for(String anim : Loader.anim.getKeys()) {
			Animation s = new Animation(Loader.anim.getStringList(anim+".lines"), StringUtils.calculate(Loader.anim.getString(anim+".speed")).longValue());
			a.put(anim, s);
		}
	}
	
	public static String requestAnimation(String name) {
		Animation get = a.getOrDefault(name, null);
		if(get==null)return null;
		return get.get();
	}
	
	public static String replace(Player player, String where) {
		if(where==null)return null;
		for(Entry<String, Animation> e : a.entrySet()) {
			where=where.replace("%animation-"+e.getKey()+"%", e.getValue().get());
		}
		return PlaceholderAPI.setPlaceholders(player, TabList.replace(where, player, true));
	}
}

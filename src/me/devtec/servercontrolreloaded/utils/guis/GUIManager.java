package me.devtec.servercontrolreloaded.utils.guis;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class GUIManager {
	
	public static Map<String, GUIMaker> makers = new HashMap<>();
	
	public static void open(Player player, String res) {
		GUIMaker maker = makers.get(res);
		if(maker!=null)
			maker.open(player);
	}
}

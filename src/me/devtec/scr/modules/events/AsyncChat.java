package me.devtec.scr.modules.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncChat implements Listener {
	
	public static double radius;
	public static Map<String, List<String>> perWorldGroup;
	public static List<Rule> rules = new ArrayList<>();
	
	/**
	 * TODO
	 * - Radius & per-world chat
	 * - Chat rules (own anti-ad, anti-swear....)
	 * - Chat format
	 * 
	 **/
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player s = event.getPlayer();
		String msg = event.getMessage();
		
	}
	
}

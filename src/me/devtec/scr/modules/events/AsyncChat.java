package me.devtec.scr.modules.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncChat implements Listener {
	
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

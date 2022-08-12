package me.devtec.scr.listeners.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.devtec.scr.commands.message.privatemessage.MessageManager;

public class PrivateMessageListener implements Listener {

	/*
	 * Locking /msg and /helpop
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMessage(AsyncPlayerChatEvent event) {
		if(event.isCancelled()) return;
		Player player = event.getPlayer();
		if(MessageManager.chatLock.containsKey(player.getName())) {
			MessageManager.message(player, null, event.getMessage());
			event.setMessage(null);
			event.setCancelled(true);
			return;
		}
		if(MessageManager.acChatLock.contains(player.getName())) {
			MessageManager.sendhelpop(player, event.getMessage());
			event.setMessage(null);
			event.setCancelled(true);
			return;
		}
	}
	
}

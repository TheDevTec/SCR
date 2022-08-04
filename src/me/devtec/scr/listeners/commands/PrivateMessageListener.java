package me.devtec.scr.listeners.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.devtec.scr.commands.message.privatemessage.MessageManager;

public class PrivateMessageListener implements Listener {

	@EventHandler
	public void onMessage(AsyncPlayerChatEvent event) {
		if(event.isCancelled()) return;
		Player player = event.getPlayer();
		if(MessageManager.chatLock.containsKey(player.getName())) {
			event.setCancelled(true);
			MessageManager.message(player, null, event.getMessage());
			return;
		}
	}
	
}

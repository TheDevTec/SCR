package me.devtec.scr.listeners.additional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.utils.ChatUtils;
import me.devtec.scr.utils.ChatUtils.ChatFormat;

public class ChatListeners implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void chatFormat(AsyncPlayerChatEvent e) {
		if(e.isCancelled()) return;
		Player player = e.getPlayer();
		User u = API.getUser(player);
		
		//CooldownCheck
		
		//ChatFormat
		if(ChatFormat.isEnabled()) {
			String message = e.getMessage();
			
			//ChatLock
			if(ChatUtils.isChatLocked() && 
					!u.isAutorized(Loader.commands.getString("chatlock.permission.bypass"))) {
				e.setCancelled(true);
				if(Loader.translations.exists("chatlock.isLocked"))
					MessageUtils.message(player, "chatlock.isLocked", null, true);
				MessageUtils.message(player, "chatlock.format", Placeholders.c().addPlayer("player", player)
						.add("message", message), true);
			}
			
			//ChatType loading variables
			Iterator<Player> targets = e.getRecipients().iterator();
			String type = Loader.chat.getString("options.type");
			double distance = Loader.chat.getDouble("options.distance");
			
			List<String> worlds = new ArrayList<>();
			if(type.equalsIgnoreCase("PER_WORLD") || type.equalsIgnoreCase("PERWORLD") || type.equalsIgnoreCase("WORLD")) {
				for(String path : Loader.chat.getKeys("options.per_world"))
					if(Loader.chat.getStringList("options.per_world."+path)
							.contains(player.getLocation().getWorld().getName()))
						worlds = Loader.chat.getStringList("options.per_world."+path);
				if(worlds.isEmpty())
					worlds.add(player.getLocation().getWorld().getName());
			}
			
			//ChatType - removing bad players
			while (targets.hasNext()) {
				Player target = targets.next();
				if(target.equals(player)) continue; // sender je i target
				//TODO - ignore
				
				if(target.hasPermission("SCR.Other.ChatTypeBypass")) continue; //TODO - permise do configu
				
				//PER_WORLD type
				if(type.equalsIgnoreCase("PER_WORLD") || type.equalsIgnoreCase("PERWORLD") || type.equalsIgnoreCase("WORLD")) {
					if( !worlds.contains(target.getLocation().getWorld().getName()) ) //If group of worlds contains target world
						target.remove();
					continue;
				}
			
				//DISTANCE
				if( type.equalsIgnoreCase("DISTANCE") ) {
					if(!player.getWorld().equals(target.getWorld())) //If they are in same world
						target.remove();
					else if(target.getLocation().distance(player.getLocation()) > distance)
						target.remove();
					continue;
				}
			}
			
			//TODO - flood, antispam, ...
			
			if(me.devtec.scr.utils.ChatUtils.Notification.isEnabled()) {
				
			}
			
		}
		
		
	}
	
	
}

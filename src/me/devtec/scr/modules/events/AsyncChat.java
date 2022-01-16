package me.devtec.scr.modules.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
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
	 * - Cooldown
	 * + Radius & per-world chat
	 * - Chat prevention settings
	 * - Chat rules
	 * - Chat format
	 * 
	 **/
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if(event.isCancelled())return;
		Player s = event.getPlayer();
		if(isWaiting(s)) {
			event.setCancelled(true);
			return;
		}
		if(radius>0) {
			Iterator<Player> recipients = event.getRecipients().iterator();
			Location loc = s.getLocation();
			while(recipients.hasNext()) {
				Player target = recipients.next();
				if(target.getWorld()!=loc.getWorld() || target.getLocation().distance(loc) > radius)
					recipients.remove();
			}
		}else {
			if(!perWorldGroup.isEmpty()) {
				Iterator<Player> recipients = event.getRecipients().iterator();
				Location loc = s.getLocation();
				while(recipients.hasNext()) {
					Player target = recipients.next();
					if(target.getWorld()!=loc.getWorld())
						recipients.remove();
				}
			}
		}
		String msg = event.getMessage();
	}
	
	// cooldown
	public boolean isWaiting(Player player) {
		return false;
	}
	
}

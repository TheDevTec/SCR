package me.devtec.scr.listeners.fun;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import me.devtec.scr.commands.fun.God;

// 1.7.10 - 1.9
public class AntiDamage implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntityType()==EntityType.PLAYER && God.antiDamage.contains(e.getEntity().getUniqueId())) {
			e.setDamage(0);
			e.setCancelled(true);
		}
	}
}

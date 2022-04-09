package me.devtec.scr.listeners.fun;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.devtec.scr.commands.fun.Fly;

public class AntiFallDamage implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntityType()==EntityType.PLAYER && e.getCause()==DamageCause.FALL && Fly.antiFall.contains(e.getEntity().getUniqueId())) {
			e.setDamage(0);
			e.setCancelled(true);
			Fly.antiFall.remove(e.getEntity().getUniqueId());
		}
	}
}

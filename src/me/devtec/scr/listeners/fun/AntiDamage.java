package me.devtec.scr.listeners.fun;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import me.devtec.scr.api.API;

public class AntiDamage implements Listener {

	@EventHandler
	public void onDamage(EntityTargetEvent e) {
		if (e.getTarget() != null && e.getTarget().getType() == EntityType.PLAYER)
			if (API.getUser((Player) e.getTarget()).god())
				e.setCancelled(true);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntityType() == EntityType.PLAYER && API.getUser((Player) e.getEntity()).god())
			e.setCancelled(true);
	}
}

package me.devtec.servercontrolreloaded.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.devtec.servercontrolreloaded.scr.Loader;

public class EntitySpawn implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntitySpawn(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.NATURAL || e.getSpawnReason() == SpawnReason.SPAWNER
				|| e.getSpawnReason().name().contains("BUILD") || e.getSpawnReason() == SpawnReason.SILVERFISH_BLOCK
				|| e.getSpawnReason() == SpawnReason.BREEDING)
			if (e instanceof Player == false && Loader.mw.getBoolean("WorldsSettings." + e.getEntity().getWorld().getName() + ".NoMobs"))
				e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			String w = e.getEntity().getWorld().getName();
			if (e.getCause() == DamageCause.FALL && !Loader.mw.getBoolean("WorldsSettings." + w + ".DoFallDamage"))
				e.setCancelled(true);
			if (e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK)
				if (!Loader.mw.getBoolean("WorldsSettings." + w + ".DoFireDamage"))
					e.setCancelled(true);
			if (e.getCause() == DamageCause.DROWNING && !Loader.mw.getBoolean("WorldsSettings." + w + ".DoDrownDamage"))
				e.setCancelled(true);
		}
	}
}
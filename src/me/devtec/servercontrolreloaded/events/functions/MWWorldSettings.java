package me.devtec.servercontrolreloaded.events.functions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;

import me.devtec.servercontrolreloaded.scr.Loader;

public class MWWorldSettings implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntitySpawn(CreatureSpawnEvent e) {
		if(e.isCancelled())return;
		if(e instanceof Player == false)
		if (e.getSpawnReason() == SpawnReason.NATURAL || e.getSpawnReason() == SpawnReason.SPAWNER
				|| e.getSpawnReason().name().startsWith("BUILD") || e.getSpawnReason() == SpawnReason.SILVERFISH_BLOCK
				|| e.getSpawnReason() == SpawnReason.BREEDING)
			if (Loader.mw.getBoolean("WorldsSettings." + e.getEntity().getWorld().getName() + ".NoMobs"))
				e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageEvent e) {
		if(e.isCancelled())return;
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalTravel(PortalCreateEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("WorldsSettings." + e.getWorld().getName() + ".CreatePortal"))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalTravel(PlayerPortalEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("WorldsSettings." + e.getFrom().getWorld().getName() + ".PortalTeleport"))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalTravel(EntityPortalEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("WorldsSettings." + e.getFrom().getWorld().getName() + ".PortalTeleport"))
			e.setCancelled(true);
	}
}
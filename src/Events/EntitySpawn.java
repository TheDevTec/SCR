package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ServerControl.Loader;

public class EntitySpawn implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntitySpawn(CreatureSpawnEvent e) {
			if(e.getSpawnReason()==SpawnReason.NATURAL||e.getSpawnReason()==SpawnReason.SPAWNER)
		if(e instanceof Player == false && Loader.mw.getBoolean("WorldsSettings."+e.getLocation().getWorld().getName()+".NoMobs")) {
				e.setCancelled(true);
	}}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageEvent e) {
		if(e instanceof Player && e.getCause()==DamageCause.FALL
				&& !Loader.mw.getBoolean("WorldsSettings."+e.getEntity().getWorld().getName()+".DoFallDamage"))
				e.setCancelled(true);
		if(e instanceof Player && e.getCause()==DamageCause.FIRE
				&& !Loader.mw.getBoolean("WorldsSettings."+e.getEntity().getWorld().getName()+".DoFireDamage"))
				e.setCancelled(true);
		if(e instanceof Player && e.getCause()==DamageCause.DROWNING
				&& !Loader.mw.getBoolean("WorldsSettings."+e.getEntity().getWorld().getName()+".DoDrownDamage"))
				e.setCancelled(true);
	}}
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
		String w = e.getEntity().getWorld().getName();
		if(e.getEntity() instanceof Player) {
		if(e.getCause()==DamageCause.FALL
				&& !Loader.mw.getBoolean("WorldsSettings."+w+".DoFallDamage"))
				e.setCancelled(true);
		if(e.getCause()==DamageCause.FIRE||e.getCause()==DamageCause.FIRE_TICK)
		if(!Loader.mw.getBoolean("WorldsSettings."+w+".DoFireDamage"))
				e.setCancelled(true);
		if(e.getCause()==DamageCause.DROWNING||e.getCause()==DamageCause.DRYOUT
				&& !Loader.mw.getBoolean("WorldsSettings."+w+".DoDrownDamage"))
				e.setCancelled(true);
	}}}
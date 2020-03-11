package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import ServerControl.Loader;

public class EntitySpawn implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntitySpawn(CreatureSpawnEvent e) {
			if(e.getSpawnReason()==SpawnReason.NATURAL||e.getSpawnReason()==SpawnReason.SPAWNER)
		if(e instanceof Player == false && Loader.mw.getBoolean("WorldsSettings."+e.getLocation().getWorld().getName()+".NoMobs")) {
				e.setCancelled(true);
	}}}
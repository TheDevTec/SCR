package Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;

import ServerControl.Loader;

public class CreatePortal implements Listener {	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalCreate(PortalCreateEvent e) {
		if(!Loader.mw.getBoolean("WorldsSettings."+e.getWorld().getName()+".CreatePortal"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalTravel(PlayerPortalEvent e) {
		if (!Loader.mw.getBoolean("WorldsSettings." + e.getFrom().getWorld().getName() + ".PortalTeleport"))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalTravel(EntityPortalEvent e) { // not working for player
		if (!Loader.mw.getBoolean("WorldsSettings." + e.getFrom().getWorld().getName() + ".PortalTeleport"))
			e.setCancelled(true);
	}
}

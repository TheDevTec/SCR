package Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;

import ServerControl.Loader;

public class CreatePortal implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void OnCreatePortal(PortalCreateEvent e) {
		if(Loader.mw.getBoolean("WorldsSettings."+e.getWorld().getName()+".CreatePortal")==false)e.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void OnEntityPortalTravel(EntityPortalEvent e) {
		if(Loader.mw.getBoolean("WorldsSettings."+e.getFrom().getWorld().getName()+".PortalTeleport")==false)e.setCancelled(true);
	}
}

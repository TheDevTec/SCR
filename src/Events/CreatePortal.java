package Events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import ServerControl.Loader;

public class CreatePortal implements Listener {
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEndPortal(BlockPhysicsEvent e) {
		Bukkit.broadcastMessage(e.getChangedType().name());
		if(e.getBlock().getType().name().equals("END_PORTAL")||e.getBlock().getType().name().equals("NETHER_PORTAL"))
		if(!Loader.mw.getBoolean("WorldsSettings."+e.getBlock().getWorld().getName()+".CreatePortal"))e.getBlock().setType(Material.AIR);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalTravel(PlayerPortalEvent e) { //not working for player
		if(!Loader.mw.getBoolean("WorldsSettings."+e.getFrom().getWorld().getName()+".PortalTeleport"))e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalTravel(EntityPortalEvent e) { //not working for player
		if(!Loader.mw.getBoolean("WorldsSettings."+e.getFrom().getWorld().getName()+".PortalTeleport"))e.setCancelled(true);
	}
}

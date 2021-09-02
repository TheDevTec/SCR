package me.devtec.servercontrolreloaded.events.functions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;

import me.devtec.servercontrolreloaded.scr.Loader;

public class MWSettings implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalCreate(PortalCreateEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("settings." + e.getWorld().getName() + ".portals.create"))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalTravel(PlayerPortalEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("settings." + e.getFrom().getWorld().getName() + ".portals.teleport"))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPortalTravel(EntityPortalEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("settings." + e.getFrom().getWorld().getName() + ".portals.teleport"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onItemPickup(EntityPickupItemEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("settings." + e.getEntity().getWorld().getName() + ".modifyWorld.pickupItem"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onItemDrop(EntityDropItemEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("settings." + e.getEntity().getWorld().getName() + ".modifyWorld.dropItem"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLeavesDecay(LeavesDecayEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("settings." + e.getBlock().getWorld().getName() + ".modifyWorld.decayLeaves"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlaceBlock(BlockPlaceEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("settings." + e.getBlock().getWorld().getName() + ".modifyWorld.placeBlock"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(BlockBreakEvent e) {
		if(e.isCancelled())return;
		if (Loader.mw.getBoolean("settings." + e.getBlock().getWorld().getName() + ".disableDrops.blocks")) {
			e.setDropItems(false);
			e.setExpToDrop(0);
		}
		if (!Loader.mw.getBoolean("settings." + e.getBlock().getWorld().getName() + ".modifyWorld.breakBlock"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntity() instanceof Player) {
			if (Loader.mw.getBoolean("settings." + e.getEntity().getWorld().getName() + ".disableDrops.players")) {
				e.getDrops().clear();
				e.setDroppedExp(0);
			}
		}else {
			if (Loader.mw.getBoolean("settings." + e.getEntity().getWorld().getName() + ".disableDrops.entities")) {
				e.getDrops().clear();
				e.setDroppedExp(0);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteractBlock(PlayerInteractEvent e) {
		if(e.isCancelled())return;
		if(e.getClickedBlock()!=null)
		if (!Loader.mw.getBoolean("settings." + e.getClickedBlock().getWorld().getName() + ".modifyWorld.interact.blocks"))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		if(e.isCancelled())return;
		if (!Loader.mw.getBoolean("settings." + e.getRightClicked().getWorld().getName() + ".modifyWorld.interact.entities"))
			e.setCancelled(true);
	}
}
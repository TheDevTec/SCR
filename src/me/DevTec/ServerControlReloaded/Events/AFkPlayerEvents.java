package me.DevTec.ServerControlReloaded.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.punishmentapi.PlayerBanList;
import me.devtec.theapi.punishmentapi.PunishmentAPI;

public class AFkPlayerEvents implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(PlayerInteractEvent e) {
		Loader.getInstance.save(e.getPlayer());
		PlayerBanList d= PunishmentAPI.getBanList(e.getPlayer().getName());
		if (d.isJailed() || d.isTempJailed() || d.isTempIPJailed() || d.isIPJailed())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(PlayerCommandPreprocessEvent e) {
		Loader.getInstance.save(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(AsyncPlayerChatEvent e) {
		Loader.getInstance.save(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(InventoryClickEvent e) {
		Loader.getInstance.save((Player)e.getWhoClicked());
	}
}

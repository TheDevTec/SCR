package me.devtec.servercontrolreloaded.events.functions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.XMaterial;

public class DisableItems implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("SCR.Other.DisallowedItemsBypass")) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				ItemStack mat = p.getItemInHand();
				for (String s : Loader.config.getStringList("Options.Disallowed-Items.Worlds." + p.getWorld().getName())) {
					XMaterial match = XMaterial.matchXMaterial(s.toUpperCase());
					if (match != null && match.isSimilar(mat)) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDispenserEvent(BlockDispenseEvent e) {
		Material mat = e.getItem().getType();
		for (String s : Loader.config.getStringList("Options.Disallowed-Items.Worlds." + e.getBlock().getWorld().getName())) {
			Material match = Material.matchMaterial(s.toUpperCase());
			if (match != null && match==mat)
				e.setCancelled(true);
		}
	}
}

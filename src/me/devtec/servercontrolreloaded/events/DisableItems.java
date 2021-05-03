package me.devtec.servercontrolreloaded.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.setting;

public class DisableItems implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (setting.disable_item) {
			if (!p.hasPermission("SCR.Other.DisallowedItemsBypass")) {
					if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					Material mat = p.getItemInHand().getType();
					for (String s : Loader.config.getStringList("Options.Disallowed-Items.Worlds." + p.getWorld().getName())) {
						Material match = Material.matchMaterial(s.toUpperCase());
						if (match != null && match==mat)
							e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDispenserEvent(BlockDispenseEvent e) {
		if (setting.disable_item) {
			Material mat = e.getItem().getType();
			for (String s : Loader.config.getStringList("Options.Disallowed-Items.Worlds." + e.getBlock().getWorld().getName())) {
				Material match = Material.matchMaterial(s.toUpperCase());
				if (match != null && match==mat)
					e.setCancelled(true);
			}
		}
	}
}

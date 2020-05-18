package Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import ServerControl.Loader;
import Utils.setting;

public class DisableItems implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (setting.disable_item) {
			if (!p.hasPermission("ServerControl.DisableItemsAccess")) {
				@SuppressWarnings("deprecation")
				Material mat = p.getItemInHand().getType();
				if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					for (String s : Loader.config
							.getStringList("Options.Disable-Items.Worlds." + p.getWorld().getName())) {
						if (Material.matchMaterial(s.toUpperCase()) != null) {
							if (mat == Material.matchMaterial(s.toUpperCase())) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDispenserEvent(BlockDispenseEvent e) {
		if (setting.disable_item) {
			for (String s : Loader.config
					.getStringList("Options.Disable-Items.Worlds." + e.getBlock().getWorld().getName())) {
				if (Material.matchMaterial(s.toUpperCase()) != null) {
					if (e.getItem().getType() == Material.matchMaterial(s.toUpperCase())) {
						e.setCancelled(true);
					}
				}
			}
		}
	}
}

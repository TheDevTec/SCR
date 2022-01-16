package me.devtec.scr.modules.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemDrop implements Listener {

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(event.isCancelled())return;
		Player s = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		ItemMeta meta = item.getItemMeta();
		String text = meta.getDisplayName();
		//TODO rules
		if(text==null) {
			event.setCancelled(true);
			return;
		}
		meta.setDisplayName(text);
		item.setItemMeta(meta);
		event.getItemDrop().setItemStack(item);
	}
}

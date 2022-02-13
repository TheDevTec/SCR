package me.devtec.scr.modules.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.devtec.scr.utils.Rule;

public class ItemPickup implements Listener {
	
	public static List<Rule> rules = new ArrayList<>();

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		if(event.isCancelled())return;
		Player s = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();
		ItemMeta meta = item.getItemMeta();
		String text = meta.getDisplayName();
		for(Rule rule : rules) {
			text=rule.apply(text, s);
			if(text==null)break;
		}
		if(text==null) {
			event.setCancelled(true);
			return;
		}
		meta.setDisplayName(text);
		item.setItemMeta(meta);
		event.getItem().setItemStack(item);
	}
}

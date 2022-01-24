package me.devtec.scr.modules.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilPrepare implements Listener {
	
	public static List<Rule> rules = new ArrayList<>();

	private static ItemStack empty = new ItemStack(Material.AIR);
	
	@EventHandler
	public void onAnvil(PrepareAnvilEvent event) {
		if(event.getResult()==null||event.getResult().getType()==Material.AIR)return;
		Player s = (Player) event.getViewers().get(0);
		ItemStack item = event.getResult();
		ItemMeta meta = item.getItemMeta();
		String text = meta.getDisplayName();
		for(Rule rule : rules) {
			text=rule.apply(text, s);
			if(text==null)break;
		}
		if(text==null) {
			event.setResult(empty);
			return;
		}
		meta.setDisplayName(text);
		item.setItemMeta(meta);
	}
}

package me.devtec.servercontrolreloaded.events;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.Rule;
import me.devtec.servercontrolreloaded.utils.setting;

import java.util.ArrayList;
import java.util.List;

/**
 * 18.11. 2020
 * 
 * @author StraikerinaCZ
 *
 */

//TODO REWORK

public class SecurityListenerV4 implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommands(PlayerCommandPreprocessEvent event) {
		String msg = event.getMessage();
		if (!event.getPlayer().hasPermission("SCR.Other.RulesBypass")) {
		for (Rule rule : Loader.rules) {
			if(!Loader.events.getStringList("onCommand.Rules").contains(rule.getName()))continue;
			msg = rule.apply(msg);
			if (msg == null) break;
		}
		if (msg == null) {
			event.setCancelled(true);
			return;
		}
		}
		if (!event.getPlayer().hasPermission("SCR.Other.CommandsBlockerBypass") && setting.cmdblock) {
			for (String cen : Loader.config.getStringList("Options.CommandsBlocker.List")) {
				String mes = msg.toLowerCase();
				if (mes.startsWith("/" + cen.toLowerCase()) || mes.startsWith("/bukkit:" + cen.toLowerCase())
						|| mes.startsWith("/minecraft:" + cen.toLowerCase())) {
						event.setCancelled(true);
				}
			}
			for (String cen : Loader.config.getStringList("Options.CommandsBlocker.PerWorld."+event.getPlayer().getWorld().getName())) {
				String mes = msg.toLowerCase();
				if (mes.startsWith("/" + cen.toLowerCase()) || mes.startsWith("/bukkit:" + cen.toLowerCase())
						|| mes.startsWith("/minecraft:" + cen.toLowerCase())) {
						event.setCancelled(true);
				}
			}
		}
		event.setMessage(msg);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void signCreate(SignChangeEvent e) {
		if (!e.getPlayer().hasPermission("SCR.Other.RulesBypass")) {
		int i = -1;
		for (String msg : e.getLines()) {
			for (Rule rule : Loader.rules) {
				if(!Loader.events.getStringList("onSign.Rules").contains(rule.getName()))continue;
				msg = rule.apply(msg);
				if (msg == null) break;
			}
			if (msg == null) {
				e.setLine(++i, "");
				continue;
			}
			e.setLine(++i, msg);
		}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void BookSave(PlayerEditBookEvent e) {
		if (!e.getPlayer().hasPermission("SCR.Other.RulesBypass")) {
		List<String> lines = new ArrayList<>();
		for (String msg : e.getNewBookMeta().getPages()) {
			for (Rule rule : Loader.rules) {
				if(!Loader.events.getStringList("onBook.Rules").contains(rule.getName()))continue;
				msg = rule.apply(msg);
				if (msg == null) break;
			}
			if (msg == null) {
				lines.add("");
				continue;
			}
			lines.add(msg);
		}
		BookMeta meta = e.getNewBookMeta();
		meta.setPages(lines);
		e.setNewBookMeta(meta);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (!event.getWhoClicked().hasPermission("SCR.Other.RulesBypass")) {
		if (event.getInventory().getType() == InventoryType.ANVIL)
			if (event.getSlotType() == InventoryType.SlotType.RESULT) {
				ItemStack item = event.getCurrentItem();
				if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
					ItemMeta meta = item.getItemMeta();
					String msg = meta.getDisplayName();
					for (Rule rule : Loader.rules) {
						if(!Loader.events.getStringList("onAnvil.Rules").contains(rule.getName()))continue;
						msg = rule.apply(msg);
						if (msg == null) break;
					}
					if (msg == null) {
						event.setCancelled(true);
						return;
					}
					meta.setDisplayName(msg);
					item.setItemMeta(meta);
					event.setCurrentItem(item);
				}
		}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PickupAntiADEvent(PlayerPickupItemEvent event) {
		if (!event.getPlayer().hasPermission("SCR.Other.RulesBypass")) {
		ItemStack item = event.getItem().getItemStack();
		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			ItemMeta meta = item.getItemMeta();
			String msg = meta.getDisplayName();
			for (Rule rule : Loader.rules) {
				if(!Loader.events.getStringList("onPickupItem.Rules").contains(rule.getName()))continue;
				msg = rule.apply(msg);
				if (msg == null) break;
			}
			if (msg == null) {
				event.setCancelled(true);
				return;
			}
			meta.setDisplayName(msg);
			item.setItemMeta(meta);
			event.getItem().setItemStack(item);
		}}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void DropAntiADEvent(PlayerDropItemEvent event) {
		if (!event.getPlayer().hasPermission("SCR.Other.RulesBypass")) {
		ItemStack item = event.getItemDrop().getItemStack();
		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			ItemMeta meta = item.getItemMeta();
			String msg = meta.getDisplayName();
			for (Rule rule : Loader.rules) {
				if(!Loader.events.getStringList("onDropItem.Rules").contains(rule.getName()))continue;
				msg = rule.apply(msg);
				if (msg == null) break;
			}
			if (msg == null) {
				event.setCancelled(true);
				return;
			}
			meta.setDisplayName(msg);
			item.setItemMeta(meta);
			event.getItemDrop().setItemStack(item);
		}}
	}
}
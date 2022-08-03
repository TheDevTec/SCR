package me.devtec.scr.listeners.additional;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.devtec.scr.commands.inventory.menus.EnchantingTable;

public class EnchantingTableListener implements Listener {

	public EnchantingTable table;

	public EnchantingTableListener(EnchantingTable cmd) {
		table = cmd;
	}

	@EventHandler
	public void onPreEnchantTest(PrepareItemEnchantEvent e) {
		if (table.openedInv.contains(e.getEnchanter().getUniqueId()) && e.getOffers() != null) {
			if (e.getOffers()[0] == null)
				e.getOffers()[0] = new EnchantmentOffer(Enchantment.DURABILITY, 1, 1);
			else
				e.getOffers()[0].setCost(1);
			if (e.getOffers()[1] == null)
				e.getOffers()[1] = new EnchantmentOffer(Enchantment.DURABILITY, 2, 15);
			else
				e.getOffers()[1].setCost(15);
			if (e.getOffers()[2] == null)
				e.getOffers()[2] = new EnchantmentOffer(Enchantment.DURABILITY, 3, 30);
			else
				e.getOffers()[2].setCost(30);
		}
	}

	@EventHandler
	public void onCloseInv(InventoryCloseEvent e) {
		table.openedInv.remove(e.getPlayer().getUniqueId());
	}
}

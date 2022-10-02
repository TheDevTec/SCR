package me.devtec.scr.commands.inventory.menus;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.Ref;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.theapi.bukkit.gui.AnvilGUI;
import me.devtec.theapi.bukkit.gui.GUI.ClickType;
import me.devtec.theapi.bukkit.nms.utils.InventoryUtils;
import me.devtec.theapi.bukkit.nms.utils.InventoryUtils.DestinationType;

public class Anvil implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // anvil [player]
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			openAnvil((Player) s);
			msgSec(s, "self");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
		}).argument("-s", (s, structure, args) -> {
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			new AnvilGUI("Anvil", (Player) s).setInsertable(true);
		}).parent().selector(Selector.PLAYER, (s, structure, args) -> {
			Player p = Bukkit.getPlayer(args[0]);
			new AnvilGUI("Anvil", p).setInsertable(true);
			msgSec(s, "other.sender", Placeholders.c().addPlayer("target", p));
			msgSec(p, "other.target", Placeholders.c().addPlayer("player", s));
		}).permission(permission("other")).argument("-s", (s, structure, args) -> {
			Player p = Bukkit.getPlayer(args[0]);
			openAnvil(p);
		}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	public void openAnvil(Player p) {
		new AnvilGUI("Anvil", p) {
			@Override
			public void onClose(Player p) {
				if (getItem(0) != null) {
					p.getInventory().addItem(getItem(0));
					removeItem(0);
				}
				if (getItem(1) != null) {
					p.getInventory().addItem(getItem(1));
					removeItem(1);
				}
			}

			boolean isConflicting(Set<Enchantment> with, Enchantment target) {
				for (Enchantment e : with)
					if (e.conflictsWith(target))
						return true;
				return false;
			}

			@Override
			public boolean onIteractItem(Player player, ItemStack item, ClickType type, int slot, boolean gui) {
				if (gui && slot == 2) {
					ItemStack first = getItem(0);
					if (first == null)
						return false;
					ItemStack second = getItem(1);
					if (!areCompatible(first, second))
						return true;

					ItemStack sum = first.clone();

					boolean modify = false;
					ItemMeta meta = sum.getItemMeta();
					if (second != null) {
						ItemMeta metaS = second.getItemMeta();
						for (Entry<Enchantment, Integer> entry : second.getType() == Material.ENCHANTED_BOOK ? ((EnchantmentStorageMeta) metaS).getStoredEnchants().entrySet()
								: metaS.getEnchants().entrySet()) {
							if (!first.getType().name().endsWith("_HORSE_ARMOR")
									&& (!entry.getKey().canEnchantItem(sum) || sum.getType() != Material.ENCHANTED_BOOK && isConflicting(sum.getEnchantments().keySet(), entry.getKey())))
								continue;
							int enchLevel = sum.getEnchantments().getOrDefault(entry.getKey(), 0) + entry.getValue();
							if (sum.getType() == Material.ENCHANTED_BOOK)
								((EnchantmentStorageMeta) meta).addStoredEnchant(entry.getKey(), enchLevel > entry.getKey().getMaxLevel() ? entry.getKey().getMaxLevel() : enchLevel, true);
							else
								meta.addEnchant(entry.getKey(), enchLevel > entry.getKey().getMaxLevel() ? entry.getKey().getMaxLevel() : enchLevel, true);
							modify = true;
						}
						short damage = (short) (sum.getDurability() - second.getDurability() < 0 ? 0 : sum.getDurability() - second.getDurability());
						if (sum.getDurability() != damage) {
							sum.setDurability(damage);
							modify = true;
						}
					}
					if (!getRenameText().isEmpty() && !getRenameText().equals(meta.getDisplayName())) {
						meta.setDisplayName(getRenameText());
						modify = true;
					}
					sum.setItemMeta(meta);
					if (modify) {
						if (type == ClickType.SHIFT_LEFT_DROP || type == ClickType.SHIFT_LEFT_PICKUP) {
							removeItem(0);
							removeItem(1);
							shift(sum, player.getInventory());
							return true;
						}
						removeItem(0);
						removeItem(1);
						player.setItemOnCursor(sum);
						return true;
					}
				}
				return false;
			}

			private boolean areCompatible(ItemStack first, ItemStack second) {
				if (second == null || second.getType() == Material.AIR)
					return true;
				if (isToolOrArmor(first.getType()) && isToolOrArmor(second.getType()) && first.getType() == second.getType())
					return true;
				if (isToolOrArmor(first.getType()) && second.getType() == Material.ENCHANTED_BOOK || first.getType() == Material.ENCHANTED_BOOK && second.getType() == Material.ENCHANTED_BOOK)
					return true;
				return false;
			}

			private boolean isToolOrArmor(Material type) {
				String name = type.name();
				return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS") || name.equals("ELYTRA") // armor
						|| name.endsWith("_SWORD") || name.endsWith("_PICKAXE") || name.endsWith("_AXE") || name.endsWith("_SHOVEL") || name.endsWith("_SPADE") || name.endsWith("_HOE")
						|| type == Material.SHEARS || type == Material.FISHING_ROD || type == Material.CARROT_ON_A_STICK || name.equals("WARPED_FUNGUS_ON_A_STICK") || type == Material.BOW
						|| name.equals("CROSSBOW") || name.equals("TRIDENT") || name.equals("SHIELD") || name.endsWith("_HORSE_ARMOR");
			}

			private void shift(ItemStack sum, Inventory inventory) {
				inventory.setItem(InventoryUtils.findFirstEmpty(null, null, null, null, DestinationType.PLAYER_INV_CUSTOM_INV, null, getContents(inventory)), sum);
			}

			private ItemStack[] getContents(Inventory inv) {
				if (Ref.isNewerThan(8))
					return inv.getStorageContents();
				return inv.getContents();
			}
		}.setInsertable(true);
	}

	@Override
	public String configSection() {
		return "anvil";
	}

}

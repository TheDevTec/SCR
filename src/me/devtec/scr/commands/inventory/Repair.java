package me.devtec.scr.commands.inventory;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.nms.NBTEdit;

public class Repair  implements ScrCommand {
	
	// repair - hand
	// repair ALL - repair all items in inventory
	// repair [player] - repair players hand
	// repair [player] ALL - all items in inventory

	@SuppressWarnings({ "deprecation"})
	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if( !(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			Player p = (Player) s;
			Material hand = p.getItemInHand().getType();
			if (hand != Material.AIR) {
				if(isTool(p.getItemInHand().getType().name())) {
					NBTEdit edit = new NBTEdit(p.getItemInHand());
					Bukkit.broadcastMessage(edit.getKeys().toString());
					if(edit.hasKey("Damage"))
						edit.remove("Damage");
					if(edit.hasKey("damage"))
					edit.remove("damage");
					p.setItemInHand(BukkitLoader.getNmsProvider().setNBT(p.getItemInHand(), edit));
				}
			msgSec(s, "hand.you");
			} else
				msg(s, "missing.handEmpty");
		})
		.cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
			.argument("-s", (s, structure, args) -> { // repair -s
				if( !(s instanceof Player)) {
					help(s, "usage");
					return;
				}
				Player p = (Player) s;
				Material hand = p.getItemInHand().getType();
				if (hand != Material.AIR) {
					if(isTool(p.getItemInHand().getType().name())) {
						NBTEdit edit = new NBTEdit(p.getItemInHand());
						edit.remove("Damage");
						edit.remove("damage");
						p.setItemInHand(BukkitLoader.getNmsProvider().setNBT(p.getItemInHand(), edit));
					}
				} else
					msg(s, "missing.handEmpty");
			})
		.parent() // repair
			.argument("all", (s, structure, args) -> { // repair ALL
				if( !(s instanceof Player)) {
					help(s, "usage");
					return;
				}
					Player p = (Player) s;
					ItemStack[] items = p.getInventory().getContents();
					int pos = 0;
					for (ItemStack t : items) {
						if (t != null && t.getType()!=Material.AIR)
							if(isTool(t.getType().name())) {
								NBTEdit edit = new NBTEdit(t);
								edit.remove("Damage");
								edit.remove("damage");
								t=BukkitLoader.getNmsProvider().setNBT(t, edit);
							}
						items[pos++]=t;
					}
					p.getInventory().setContents(items);
					msgSec(s, "all.you");
				}, "ALL").permission(permission("all"))
				.argument("-s", (s, structure, args) -> { // repair ALL -s
					if( !(s instanceof Player)) {
						help(s, "usage");
						return;
					}
					Player p = (Player) s;
					ItemStack[] items = p.getInventory().getContents();
					int pos = 0;
					for (ItemStack t : items) {
						if (t != null && t.getType()!=Material.AIR)
							if(isTool(t.getType().name())) {
								NBTEdit edit = new NBTEdit(t);
								edit.remove("Damage");
								edit.remove("damage");
								t=BukkitLoader.getNmsProvider().setNBT(t, edit);
							}
						items[pos++]=t;
					}
					p.getInventory().setContents(items);
				})
			.parent() // repair ALL
		.parent() // repair
			.fallback((s, structure, args) -> {
				offlinePlayer(s, args[0]);
			})
			.selector(Selector.PLAYER, (s, structure, args) -> { // repair [player]
				for(Player p : playerSelectors(s, args[0])) {
					Material hand = p.getItemInHand().getType();
					if (hand != Material.AIR) {
						if(isTool(p.getItemInHand().getType().name())) {
							NBTEdit edit = new NBTEdit(p.getItemInHand());
							edit.remove("Damage");
							edit.remove("damage");
							p.setItemInHand(BukkitLoader.getNmsProvider().setNBT(p.getItemInHand(), edit));
						}
					msgSec(s, "hand.other.sender", Placeholders.c().addPlayer("target", p));
					msgSec(p, "hand.other.receiver", Placeholders.c().addPlayer("player", p));
					} else
						msg(s, "missing.targetHandEmpty");
				}
			}).permission(permission("other"))
				.argument("-s", (s, structure, args) -> { // repair [player] -s
				for(Player p : playerSelectors(s, args[0])) {
					Material hand = p.getItemInHand().getType();
					if (hand != Material.AIR) {
						if(isTool(p.getItemInHand().getType().name())) {
							NBTEdit edit = new NBTEdit(p.getItemInHand());
							edit.remove("Damage");
							edit.remove("damage");
							p.setItemInHand(BukkitLoader.getNmsProvider().setNBT(p.getItemInHand(), edit));
						}
					} else
						msg(s, "missing.targetHandEmpty");
				}
			})
			.parent() // repalr [player]
				.argument("all", (s, structure, args) -> { // repair [player] -s
					for(Player p : playerSelectors(s, args[0])) {
						ItemStack[] items = p.getInventory().getContents();
						int pos = 0;
						for (ItemStack t : items) {
							if (t != null && t.getType()!=Material.AIR)
								if(isTool(t.getType().name())) {
									NBTEdit edit = new NBTEdit(t);
									edit.remove("Damage");
									edit.remove("damage");
									t=BukkitLoader.getNmsProvider().setNBT(t, edit);
								}
							items[pos++]=t;
						}
						p.getInventory().setContents(items);
						msgSec(s, "all.other.sender", Placeholders.c().addPlayer("target", p));
						msgSec(p, "all.other.receiver", Placeholders.c().addPlayer("player", p));
					}
				}, "ALL").permission(permission("all"))
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "repair";
	}
	
	private boolean isTool(String name) {
		return name.endsWith("_PICKAXE")||name.endsWith("_AXE")||name.endsWith("_SPADE")||name.endsWith("_SHOVEL")
				||name.endsWith("_HOE")||name.endsWith("_HELMET")||name.endsWith("_BOOTS")||name.endsWith("_ON_A_STICK")||
		name.endsWith("_LEGGINGS")||name.endsWith("_CHESTPLATE")||name.endsWith("_SWORD")||name.equals("BOW")
		||name.equals("SHEARS")||name.equals("FLINT_AND_STEEL")||name.equals("TRIDENT")||name.equals("ELYTRA")
		||name.equals("CROSSBOW")||name.equals("SHIELD")||name.equals("FISHING_ROD");
	}
}

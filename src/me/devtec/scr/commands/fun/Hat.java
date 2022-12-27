package me.devtec.scr.commands.fun;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Hat implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd

			if (s.getItemInHand().getType() != Material.AIR) {
				// INV free slot checker
				Inventory inv = s.getInventory();
				boolean check = inv.firstEmpty() == -1;
				ItemStack helmet = new ItemStack(Material.AIR);
				if (s.getInventory().getHelmet() != null && check)
					s.getInventory().addItem(s.getInventory().getHelmet());
				if (s.getInventory().getHelmet() != null && !check)
					helmet = s.getInventory().getHelmet();
				s.getInventory().setHelmet(s.getItemInHand());
				s.getInventory().setItemInHand(helmet);
				msgSec(s, "equipped.you", Placeholders.c().add("item", s.getInventory().getHelmet().getType().name()));
			} else
				msg(s, "missing.handEmpty");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).argument("-s", (s, structure, args) -> { // hat -s

			if (s.getItemInHand().getType() != Material.AIR) {
				// INV free slot checker
				Inventory inv = s.getInventory();
				boolean check = inv.firstEmpty() == -1;
				ItemStack helmet = new ItemStack(Material.AIR);
				if (s.getInventory().getHelmet() != null && check)
					s.getInventory().addItem(s.getInventory().getHelmet());
				if (s.getInventory().getHelmet() != null && !check)
					helmet = s.getInventory().getHelmet();
				s.getInventory().setHelmet(s.getItemInHand());
				s.getInventory().setItemInHand(helmet);
			} else
				msg(s, "missing.handEmpty");
		}).parent() // cmd

				.fallback((s, structure, args) -> {
					offlinePlayer(s, args[0]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // hat [player]
					for (Player target : playerSelectors(s, args[0]))
						if (s.getItemInHand().getType() != Material.AIR) {
							// INV free slot checker
							Inventory inv = target.getInventory();
							boolean check = inv.firstEmpty() == -1;
							if (target.getInventory().getHelmet() != null && !check) { // FULL INV
								msgSec(s, "fullInv", Placeholders.c().add("item", s.getInventory().getHelmet().getType().name()).addPlayer("target", target));
								return;
							}
							target.getInventory().addItem(target.getInventory().getHelmet());
							target.getInventory().setHelmet(s.getItemInHand());
							s.getInventory().setItemInHand(new ItemStack(Material.AIR));

							msgSec(s, "equipped.other.sender", Placeholders.c().add("item", s.getInventory().getHelmet().getType().name()).addPlayer("target", target));
							msgSec(target, "equipped.other.target", Placeholders.c().add("item", s.getInventory().getHelmet().getType().name()).addPlayer("player", s));
						} else
							msg(s, "missing.handEmpty");
				}).permission(permission("other")).argument("-s", (s, structure, args) -> { // hat [player] -s
					for (Player target : playerSelectors(s, args[0]))
						if (s.getItemInHand().getType() != Material.AIR) {
							// INV free slot checker
							Inventory inv = target.getInventory();
							boolean check = inv.firstEmpty() == -1;
							if (target.getInventory().getHelmet() != null && !check) { // FULL INV
								msgSec(s, "fullInv", Placeholders.c().add("item", s.getInventory().getHelmet().getType().name()).addPlayer("target", target));
								return;
							}
							target.getInventory().addItem(target.getInventory().getHelmet());
							target.getInventory().setHelmet(s.getItemInHand());
							s.getInventory().setItemInHand(new ItemStack(Material.AIR));
						} else
							msg(s, "missing.handEmpty");

				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "hat";
	}

}

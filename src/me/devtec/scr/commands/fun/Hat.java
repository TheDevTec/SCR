package me.devtec.scr.commands.fun;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Hat implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			switch (setHat(s, s)) {
			case EMPTY_HAND:
				msg(s, "missing.handEmpty");
				break;
			case FULL_INVENTORY:
				msgSec(s, "fullInv");
				break;
			case SUCCESS:
				msgSec(s, "equipped.you", Placeholders.c().add("item", s.getInventory().getHelmet().getType().name()));
				break;
			}
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).argument("-s", (s, structure, args) -> { // hat -s
			switch (setHat(s, s)) {
			case EMPTY_HAND:
				msg(s, "missing.handEmpty");
				break;
			case FULL_INVENTORY:
				msgSec(s, "fullInv");
				break;
			case SUCCESS:
				break;
			}
		}).first() // cmd
				.fallback((s, structure, args) -> {
					offlinePlayer(s, args[0]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // hat [player]
					ItemStack current = s.getEquipment().getItemInMainHand();
					for (Player target : playerSelectors(s, args[0]))
						switch (setHat(target, current)) {
						case EMPTY_HAND:
							msg(s, "missing.handEmpty");
							break;
						case FULL_INVENTORY:
							msgSec(s, "fullInv");
							break;
						case SUCCESS:
							msgSec(s, "equipped.other.sender", Placeholders.c().add("item", current.getType().name()).addPlayer("target", target));
							msgSec(target, "equipped.other.target", Placeholders.c().add("item", current.getType().name()).addPlayer("player", s));
							break;
						}
				}).permission(permission("other")).argument("-s", (s, structure, args) -> { // hat [player] -s
					ItemStack current = s.getEquipment().getItemInMainHand();
					for (Player target : playerSelectors(s, args[0]))
						switch (setHat(target, current)) {
						case EMPTY_HAND:
							msg(s, "missing.handEmpty");
							break;
						case FULL_INVENTORY:
							msgSec(s, "fullInv");
							break;
						case SUCCESS:
							break;
						}
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	public static enum HatAction {
		SUCCESS, FULL_INVENTORY, EMPTY_HAND
	}

	public HatAction setHat(Player from, Player target) {
		ItemStack current = from.getEquipment().getItemInMainHand();
		HatAction result;
		if ((result = setHat(target, current)) == HatAction.SUCCESS)
			from.getEquipment().setItemInMainHand(null);
		return result;
	}

	public HatAction setHat(Player target, ItemStack item) {
		if (item == null || item.getType() == Material.AIR || item.getAmount() <= 0)
			return HatAction.EMPTY_HAND;
		ItemStack current = target.getEquipment().getHelmet();
		if (current != null) {
			if (target.getInventory().firstEmpty() == -1)
				return HatAction.FULL_INVENTORY;
			target.getInventory().addItem(current);
		}
		target.getEquipment().setHelmet(item);
		return HatAction.SUCCESS;
	}

	@Override
	public String configSection() {
		return "hat";
	}

}

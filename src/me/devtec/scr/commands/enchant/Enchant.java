package me.devtec.scr.commands.enchant;

import java.util.ArrayList;
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
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.game.EnchantmentAPI;

public class Enchant implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		List<String> enchants = new ArrayList<>();
		for (EnchantmentAPI ench : EnchantmentAPI.values())
			if (ench.isSupported())
				enchants.add(ench.name().toLowerCase());

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // enchant {player} [enchant] [level]
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> { // enchant {player}
			offlinePlayer(s, args[0]);
			// only for player
		}).callableArgument((s, structure, args) -> enchants, (s, structure, args) -> { // enchant [enchant]
			help(s, "usage");
		}).selector(Selector.INTEGER, (s, structure, args) -> { // enchant [enchant] [level]
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			Player target = (Player) s;
			if (target.getItemInHand().getType() == Material.AIR || target.getItemInHand().getAmount() <= 0) {
				msgSec(s, "emptyHand", Placeholders.c().addPlayer("player", s));
				return;
			}
			msgSec(s, "self", Placeholders.c().addPlayer("player", s).addPlayer("target", target).add("enchantment", args[0].toUpperCase()).add("level", args[1]));
			ItemStack stack = target.getItemInHand();
			if (StringUtils.getInt(args[1]) == 0)
				stack.removeEnchantment(EnchantmentAPI.byName(args[0].toUpperCase()).getEnchantment());
			else
				stack.addUnsafeEnchantment(EnchantmentAPI.byName(args[0].toUpperCase()).getEnchantment(), StringUtils.getInt(args[1]));
			target.setItemInHand(stack);
		}).argument("-s", (s, structure, args) -> {
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			Player target = (Player) s;
			if (target.getItemInHand().getType() == Material.AIR || target.getItemInHand().getAmount() <= 0) {
				msgSec(s, "emptyHand", Placeholders.c().addPlayer("player", s));
				return;
			}
			ItemStack stack = target.getItemInHand();
			if (StringUtils.getInt(args[1]) == 0)
				stack.removeEnchantment(EnchantmentAPI.byName(args[0].toUpperCase()).getEnchantment());
			else
				stack.addUnsafeEnchantment(EnchantmentAPI.byName(args[0].toUpperCase()).getEnchantment(), StringUtils.getInt(args[1]));
			target.setItemInHand(stack);
		}).first() // enchant
				// for all
				.selector(Selector.PLAYER, (s, structure, args) -> { // enchant {player}
					help(s, "usage");
				}).permission(permission("other")).callableArgument((s, structure, args) -> enchants, (s, structure, args) -> { // enchant {player} [enchant]
					help(s, "usage");
				}).selector(Selector.INTEGER, (s, structure, args) -> { // enchant {player} [enchant] [level]
					Player target = Bukkit.getPlayer(args[0]);
					if (target.getItemInHand().getType() == Material.AIR || target.getItemInHand().getAmount() <= 0) {
						msgSec(s, "emptyHand", Placeholders.c().addPlayer("player", s));
						return;
					}
					msgSec(s, "other.sender", Placeholders.c().addPlayer("player", s).addPlayer("target", target).add("enchantment", args[1].toUpperCase()).add("level", args[2]));
					msgSec(target, "other.receiver", Placeholders.c().addPlayer("player", target).addPlayer("target", s).add("enchantment", args[1].toUpperCase()).add("level", args[2]));
					ItemStack stack = target.getItemInHand();
					if (StringUtils.getInt(args[2]) == 0)
						stack.removeEnchantment(EnchantmentAPI.byName(args[1].toUpperCase()).getEnchantment());
					else
						stack.addUnsafeEnchantment(EnchantmentAPI.byName(args[1].toUpperCase()).getEnchantment(), StringUtils.getInt(args[2]));
					target.setItemInHand(stack);
				}).argument("-s", (s, structure, args) -> {
					Player target = Bukkit.getPlayer(args[0]);
					if (target.getItemInHand().getType() == Material.AIR || target.getItemInHand().getAmount() <= 0) {
						msgSec(s, "emptyHand", Placeholders.c().addPlayer("player", s));
						return;
					}
					ItemStack stack = target.getItemInHand();
					if (StringUtils.getInt(args[2]) == 0)
						stack.removeEnchantment(EnchantmentAPI.byName(args[1].toUpperCase()).getEnchantment());
					else
						stack.addUnsafeEnchantment(EnchantmentAPI.byName(args[1].toUpperCase()).getEnchantment(), StringUtils.getInt(args[2]));
					target.setItemInHand(stack);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "enchant";
	}

}

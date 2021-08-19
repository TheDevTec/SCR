package me.devtec.servercontrolreloaded.commands.enchantment;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.EnchantmentAPI;
import me.devtec.theapi.utils.StringUtils;

public class EnchantTable implements CommandExecutor, TabCompleter {
	final List<String> enchs = Lists.newArrayList();

	public EnchantTable() {
		for(EnchantmentAPI a : EnchantmentAPI.values())enchs.add(a.name());
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Enchant", "Enchantment")) {
			if(!CommandsManager.canUse("Enchantment.Enchant", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Enchantment.Enchant", s))));
				return true;
			}
			if (s instanceof Player) {
				Player p = (Player) s;
				if (args.length == 0) {
					Loader.Help(s, "Enchant", "Enchantment");
					return true;
				}
				Material a = p.getItemInHand().getType();
				if (args.length == 1) {
					if (a != Material.AIR) {
						e(p.getItemInHand(), 1, args[0], s);
						return true;
					}
					Loader.sendMessages(s, "Missing.HandEmpty");
					return true;
				}
				if (args.length == 2) {
					if (a != Material.AIR) {
						e(p.getItemInHand(), StringUtils.getInt(args[1]), args[0], s);
						return true;
					}
					Loader.sendMessages(s, "Missing.HandEmpty");
					return true;
				}

			}
			return true;
		}
		Loader.noPerms(s, "Enchant", "Enchantment");
		return true;
	}

	public void e(ItemStack hand, int level, String enechant, CommandSender s) {
		try {
			if (level <= 0) {
				hand.removeEnchantment(TheAPI.getEnchantmentAPI(enechant).getEnchantment());
			} else
				hand.addUnsafeEnchantment(TheAPI.getEnchantmentAPI(enechant).getEnchantment(), level);
			Loader.sendMessages(s, "Enchant.Add", Placeholder.c()
					.add("%enchant%", enechant)
					.add("%level%", String.valueOf(level))
					.add("%item%", hand.getType().name()));
		} catch (Exception error) {
			Loader.sendMessages(s, "Missing.Enchant.NotExist", Placeholder.c()
					.add("%enchant%", enechant));
		}
	}

	public boolean contains(String s, String[] args) {
        return args[0].equalsIgnoreCase(s);
    }
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command a, String ea, String[] args) {
		if (Loader.has(s, "Enchant", "Enchantment") && s instanceof Player) {
			if (args.length == 1)
				return StringUtils.copyPartialMatches(args[0], enchs);
			if (args.length == 2)
				if (EnchantmentAPI.byName(args[0]) != null)
					return Collections.singletonList(EnchantmentAPI.byName(args[0]).getEnchantment().getMaxLevel() + "");
		}
		return Collections.emptyList();
	}
}
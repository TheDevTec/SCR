package me.DevTec.ServerControlReloaded.Commands.Enchantment;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import com.google.common.collect.Lists;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.EnchantmentAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class EnchantTable implements CommandExecutor, TabCompleter {
	List<String> enchs = Lists.newArrayList();

	public EnchantTable() {
		for(EnchantmentAPI a : EnchantmentAPI.values())enchs.add(a.name());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Enchant", "Enchantment")) {
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
			return;
		} catch (Exception error) {
			Loader.sendMessages(s, "Missing.Enchant.NotExist", Placeholder.c()
					.add("%enchant%", enechant));
			return;
		}
	}

	public boolean contains(String s, String[] args) {
		if (args[0].equalsIgnoreCase(s))
			return true;
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command a, String ea, String[] args) {
		List<String> c = new ArrayList<>();
		if (s.hasPermission("ServerControl.Enchant")) {

			if (s instanceof Player) {
				if (args.length == 1) {
					c.addAll(StringUtil.copyPartialMatches(args[0], enchs, new ArrayList<>()));
				}
				if (args.length == 2) {
					if (EnchantmentAPI.byName(args[0]) != null)
						c.add(EnchantmentAPI.byName(args[0]).getEnchantment().getMaxLevel() + "");
				}
			}
		}
		return c;
	}
}
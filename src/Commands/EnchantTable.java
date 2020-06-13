package Commands;

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

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.EnchantmentAPI;
import me.DevTec.TheAPI;

public class EnchantTable implements CommandExecutor, TabCompleter {
	List<String> enchs = Lists.newArrayList();

	public EnchantTable() {
		for(EnchantmentAPI a : EnchantmentAPI.values())enchs.add(a.name());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Enchant")) {
			if (s instanceof Player) {
				Player p = (Player) s;
				if (args.length == 0) {
					Loader.Help(s, "/Enchant <enchant> <level>", "Enchant");
					return true;
				}
				Material a = p.getItemInHand().getType();
				if (args.length == 1) {
					if (a != Material.AIR) {
						e(p.getItemInHand(), 1, args[0], s);
						return true;
					}
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Enchant.HandIsEmpty").replace("%enchant%", args[0])
							.replace("%level%", "0").replace("%item%", a.name()), s);
					return true;
				}
				if (args.length == 2) {
					if (a != Material.AIR) {
						e(p.getItemInHand(), TheAPI.getNumbersAPI(args[1]).getInt(), args[0], s);
						return true;
					}
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Enchant.HandIsEmpty").replace("%enchant%", args[0])
							.replace("%level%", "0").replace("%item%", a.name()), s);
					return true;
				}

			}
			TheAPI.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}

		return true;
	}

	public void e(ItemStack hand, int level, String enechant, CommandSender s) {
		try {
			if (level <= 0) {
				hand.removeEnchantment(TheAPI.getEnchantmentAPI(enechant).getEnchantment());
			} else
				hand.addUnsafeEnchantment(TheAPI.getEnchantmentAPI(enechant).getEnchantment(), level);
			TheAPI.msg(Loader.s("Prefix") + Loader.s("Enchant.Enchanted").replace("%enchant%", enechant)
					.replace("%level%", String.valueOf(level)).replace("%item%", hand.getType().name()), s);
			return;
		} catch (Exception error) {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("Enchant.EnchantNotExist").replace("%enchant%", enechant)
					.replace("%level%", "0").replace("%item%", hand.getType().name()), s);
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
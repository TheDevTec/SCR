package me.devtec.servercontrolreloaded.commands.enchantment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;

public class EnchantTableRemoveAll implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "EnchantRemoveAll", "Enchantment")) {
			if(!CommandsManager.canUse("Enchantment.EnchantRemoveAll", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Enchantment.EnchantRemoveAll", s))));
				return true;
			}
			if (s instanceof Player) {
				if (args.length == 0) {
					Player p = (Player) s;
					Material a = p.getItemInHand().getType();
					if (a != Material.AIR) {
						List<String> enchants = new ArrayList<>();
						if (!p.getItemInHand().getEnchantments().isEmpty()) {
							for (Enchantment ea : p.getItemInHand().getEnchantments().keySet()) {
								enchants.add(ea.getName());
								p.getItemInHand().removeEnchantment(ea);

							}
							Loader.sendMessages(s, "Enchant.Remove.All", Placeholder.c().add("%enchats%", StringUtils.join(enchants, ", ")));
							return true;
						}
						Loader.sendMessages(s, "Missing.Enchant.NoEnchant");
						return true;
					}
					Loader.sendMessages(s, "Missing.HandEmpty");
					return true;
				}
			}
			return true;
		}
		Loader.noPerms(s, "EnchantRemoveAll", "Enchantment");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Arrays.asList();
	}
}

package me.DevTec.ServerControlReloaded.Commands.Enchantment;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;

public class EnchantTableRemoveAll implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "EnchantRemoveAll", "Enchantment")) {
			if (s instanceof Player) {
				if (args.length == 0) {
					Player p = (Player) s;
					Material a = p.getItemInHand().getType();
					if (a != Material.AIR) {
						ArrayList<String> enchants = new ArrayList<String>();
						if (p.getItemInHand().getEnchantments().isEmpty() == false) {

							for (Enchantment ea : p.getItemInHand().getEnchantments().keySet()) {
								enchants.add(ea.getName());
								p.getItemInHand().removeEnchantment(ea);

							}
							Loader.sendMessages(s, "Enchant.Remove.All");
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

}

package Commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class EnchantTableRemoveAll implements CommandExecutor {
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Enchant")) {
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
							TheAPI.msg(Loader.s("Prefix") + Loader.s("Enchant.EnchantsRemoved")
									.replace("%enchants%", StringUtils.join(enchants, ", "))
									.replace("%level%", "none").replace("%item%", a.name()), s);
							return true;
						}

						TheAPI.msg(Loader.s("Prefix") + Loader.s("Enchant.NoEnchants").replace("%enchant%", "none")
								.replace("%level%", "none").replace("%item%", a.name()), s);
						return true;
					}
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Enchant.HandIsEmpty").replace("%enchant%", "none")
							.replace("%level%", "none").replace("%item%", "none"), s);
					return true;
				}
			}
			TheAPI.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}

		return true;
	}

}

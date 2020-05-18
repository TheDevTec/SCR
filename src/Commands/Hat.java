package Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Hat implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Hat")) {
			if (s instanceof Player) {
				Player p = (Player) s;
				if (p.getItemInHand().getType() == Material.AIR) {
					Loader.msg(Loader.s("Hat.HandIsEmpty"), s);
					return true;
				}
				if (args.length == 0) {
					if (p.getInventory().getHelmet() != null)
						p.getInventory().addItem(p.getInventory().getHelmet());
					p.getInventory().setHelmet(p.getEquipment().getItemInMainHand());
					p.getInventory().setItemInHand(new ItemStack(Material.AIR));
					Loader.msg(Loader.s("Hat.Equiped"), s);
					return true;
				}
				if (args.length == 1) {
					Player t = TheAPI.getPlayer(args[0]);
					if (t == null) {
						Loader.msg(Loader.PlayerNotOnline(args[0]), s);
						return true;
					}
					if (t.getInventory().getHelmet() != null)
						t.getInventory().addItem(t.getInventory().getHelmet());
					t.getInventory().setHelmet(p.getEquipment().getItemInMainHand());
					p.getInventory().setItemInHand(new ItemStack(Material.AIR));
					Loader.msg(Loader.s("Hat.EquipedToOther").replace("%target%", t.getName()), s);
					return true;
				}
			}
			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}

}

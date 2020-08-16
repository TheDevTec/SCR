package Commands;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.ItemCreatorAPI;
import me.DevTec.TheAPI;

@SuppressWarnings("deprecation")
public class Skull implements CommandExecutor {

	private Material getMaterial() {
		if (Material.matchMaterial("LEGACY_SKULL_ITEM") == null)
			return Material.matchMaterial("SKULL_ITEM");
		return Material.matchMaterial("LEGACY_SKULL_ITEM");
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Skull")) {
			if (args.length == 0) {
				if (s instanceof Player)
					Loader.Help(s, "/Skull <name>", "Skull");
				else // ready
					Loader.Help(s, "/Skull <name> <player>", "SkullOther");
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					String player = args[0];
					ItemCreatorAPI w = new ItemCreatorAPI(getMaterial());
					w.setOwner(player);
					w.setSkullType(SkullType.PLAYER);
					w.setDisplayName("&6" + player + "'s Head");
					TheAPI.giveItem((Player) s, w.create());
					TheAPI.msg(Loader.s("Skull-Given").replace("%head%", player), s);
					return true;
				}
				Loader.Help(s, "/Skull <name> <player>", "SkullOther");
				return true;
			}
			if (args.length == 2) {
				Player p = TheAPI.getPlayer(args[1]);
				if (p != null) {
					String player = args[0];
					ItemCreatorAPI w = new ItemCreatorAPI(getMaterial());
					w.setOwner(player);
					w.setSkullType(SkullType.PLAYER);
					w.setDisplayName("&6" + player + "'s Head");
					TheAPI.giveItem((Player) s, w.create());
					TheAPI.msg(Loader.s("Skull-GivenToPlayer").replace("%head%", player)
							.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
					return true;
				} else {
					TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
					return true;
				}
			}
		}
		return true;
	}

}

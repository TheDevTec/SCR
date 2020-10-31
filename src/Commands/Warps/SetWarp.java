package Commands.Warps;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.Utils.StringUtils;

public class SetWarp implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "SetWarp", "Warps")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Loader.Help(s, "SetWarp", "Warps");
					return true;
				}
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					if (!Loader.config.exists("Warps." + args[0])) {
						Player p = (Player) s;
						Location local = p.getLocation();
						Loader.config.set("Warps." + args[0], StringUtils.getLocationAsString(local));
						Loader.config.save();
						Loader.sendMessages(s, "Warp.Created.Normal", Placeholder.c()
								.add("%warp%", args[0]));
						return true;
					}
					Loader.sendMessages(s, "Warp.Exists", Placeholder.c()//xDDDD
							.add("%warp%", args[0]));
					return true;
				}
				return true;
			}
			if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("yes")) {
				if (s instanceof Player) {
					if (!Loader.config.exists("Warps." + args[0])) {
						Player p = (Player) s;
						Location local = p.getLocation();
						Loader.config.set("Warps." + args[0] + ".NeedPermission", true);
						Loader.config.set("Warps." + args[0], StringUtils.getLocationAsString(local));
						Loader.config.save();
						Loader.sendMessages(s, "Warp.Created.WithPerms", Placeholder.c()
								.add("%warp%", args[0])
								.add("%permission%", "ServerControl.Warp." + args[0]));
						return true;
					}
					Loader.sendMessages(s, "Warp.Exist", Placeholder.c()
						.add("%warp%", args[0]));
					return true;
				}
				return true;
			}
		}
		Loader.noPerms(s, "SetWarp", "Warps");
		return true;
	}
}
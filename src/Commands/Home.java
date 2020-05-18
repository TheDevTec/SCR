package Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;
import Utils.setting;
import me.Straiker123.TheAPI;
import me.Straiker123.User;

public class Home implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if (s instanceof Player) {
			Player p = (Player) s;
			User d = TheAPI.getUser(p);
			if (API.hasPerm(s, "ServerControl.Home")) {
				if (args.length == 0) {
					if (d.exist("Homes.home")) {
						Location loc = TheAPI.getStringUtils().getLocationFromString(d.getString("Homes.home"));
						API.setBack(p);
						if (loc != null) {
							if (setting.tp_safe)
								TheAPI.getPlayerAPI(p).safeTeleport(loc);
							else
								TheAPI.getPlayerAPI(p).teleport(loc);
							Loader.msg(
									Loader.s("Prefix") + Loader.s("Homes.Teleporting").replace("%player%", p.getName())
											.replace("%playername%", p.getDisplayName()).replace("%home%", "home"),
									s);
							return true;
						}
					}
					API.setBack(p);
					API.teleportPlayer(p, TeleportLocation.SPAWN);
					Loader.msg(Loader.s("Prefix")
							+ Loader.s("Spawn.NoHomesTeleportedToSpawn").replace("%world%", p.getWorld().getName())
									.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()),
							s);
					return true;
				}
				if (args.length == 1) {
					if (d.exist("Homes." + args[0])) {
						Location loc = TheAPI.getStringUtils().getLocationFromString(d.getString("Homes." + args[0]));
						API.setBack(p);
						if (loc != null) {
							if (setting.tp_safe)
								TheAPI.getPlayerAPI(p).safeTeleport(loc);
							else
								TheAPI.getPlayerAPI(p).teleport(loc);
							Loader.msg(
									Loader.s("Prefix") + Loader.s("Homes.Teleporting").replace("%player%", p.getName())
											.replace("%playername%", p.getDisplayName()).replace("%home%", args[0]),
									s);
							return true;
						}
					}
					Loader.msg(Loader.s("Prefix") + Loader.s("Homes.NotExists").replace("%player%", p.getName())
							.replace("%playername%", p.getDisplayName()).replace("%home%", args[0]), s);
					return true;
				}
			}
			return true;
		}
		Loader.msg(Loader.s("Prefix") + Loader.s("ConsoleErrorMessage"), s);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (s instanceof Player) {
			if (args.length == 1) {
				if (s.hasPermission("ServerControl.Home")) {
					try {
						Set<String> homes = TheAPI.getUser(s.getName()).getKeys("Homes");
						if (!homes.isEmpty() && homes != null)
							c.addAll(StringUtil.copyPartialMatches(args[0], homes, new ArrayList<>()));
					} catch (Exception e) {

					}
				}
			}
		}
		return c;
	}
}
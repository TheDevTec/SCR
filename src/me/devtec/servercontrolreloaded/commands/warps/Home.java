package me.devtec.servercontrolreloaded.commands.warps;


import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.API.TeleportLocation;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class Home implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (s instanceof Player) {
			if (Loader.has(s, "Home", "Warps")) {
				if(!CommandsManager.canUse("Warps.Home", s)) {
					Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Warps.Home", s))));
					return true;
				}
				Player p = (Player) s;
				User d = TheAPI.getUser(p);
				if (args.length == 0) {
					if (d.exist("Homes.home")) {
						Position loc = Position.fromString(d.getString("Homes.home"));
						if (loc != null) {
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport((Player)s,false,loc);
							else
								API.teleport((Player)s, loc);
							Loader.sendMessages(s, "Home.Teleporting", Placeholder.c()
									.add("%home%", "home"));
							return true;
						}
						return true;
					}
					if(!d.getKeys("Homes").isEmpty()) {
						String home = (String) d.getKeys("Homes").toArray()[0];
						Position loc2 = Position.fromString(d.getString("Homes." + home)); 
						API.setBack(p);
						if (loc2 != null) {
							if(setting.tp_safe)
								API.safeTeleport((Player)s,false,loc2);
							else
								API.teleport((Player)s, loc2);
							Loader.sendMessages(s, "Home.Teleporting", Placeholder.c()
									.add("%home%", home));
							return true;
						}
						Loader.sendMessages(s, "Home.NotExist", Placeholder.c()
								.add("%home%",home));
						return true;
					}
					API.setBack(p);
					API.teleportPlayer(p, TeleportLocation.SPAWN);
					Loader.sendMessages(s, "Home.TpSpawn");
					return true;
				}
				if (d.getString("Homes." + args[0])!=null) {
					Position loc2 = Position.fromString(d.getString("Homes." + args[0]));
					if (loc2 != null) {
						API.setBack(p);
						if(setting.tp_safe)
							API.safeTeleport((Player)s,false,loc2);
						else
							API.teleport((Player)s, loc2);
						Loader.sendMessages(s, "Home.Teleporting", Placeholder.c()
								.add("%home%", args[0]));
						return true;
					}
				}
				Loader.sendMessages(s, "Home.NotExist", Placeholder.c()
						.add("%home%", args[0]));
				return true;
			}
			Loader.noPerms(s, "Home", "Warps");
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (Loader.has(s, "Home", "Warps") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], TheAPI.getUser(s.getName()).getKeys("Homes"));
		return Collections.emptyList();
	}
}
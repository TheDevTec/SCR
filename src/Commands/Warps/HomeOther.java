package Commands.Warps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class HomeOther implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			if (Loader.has(s, "HomeOther", "Warps")) {
				if (args.length <= 1) {
					Loader.Help(s, "HomeOther", "Warps");
					return true;
				}
				if (args.length == 2) {
					User d = TheAPI.getUser(args[0]);
					if (d.exist("Homes." + args[1])) {
						Position loc = Position.fromString(d.getString("Homes." + args[1]));
						if (loc != null) {
							API.setBack(p);
							if (setting.tp_safe)
								API.safeTeleport((Player)s,loc.toLocation());
							else
								((Player)s).teleport(loc.toLocation());
							Loader.sendMessages(s, "Home.Other.Teleporting", Placeholder.c()
									.add("%home%", args[1])
									.add("%player%", p.getName())
									.add("%playername%", p.getDisplayName()));
							return true;
						}
					}
					Loader.sendMessages(s, "Home.Other.NotExist", Placeholder.c()
							.add("%home%", args[1])
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					return true;
				}
				if (args.length == 3) {
					Player pl = TheAPI.getPlayer(args[2]);
					if (pl == null) {
						Loader.notOnline(s, args[2]);
						return true;
					}
					User d = TheAPI.getUser(args[0]);
					if (d.exist("Homes." + args[1])) {
						Position loc = Position.fromString(d.getString("Homes." + args[1]));
						if (loc != null) {
							API.setBack(pl);
							if (setting.tp_safe)
								API.safeTeleport(pl,loc.toLocation());
							else
								pl.teleport(loc.toLocation());
							Loader.sendMessages(s, "Home.Other.Teleporting", Placeholder.c()
									.add("%home%", args[1])
									.add("%player%", p.getName())
									.add("%playername%", p.getDisplayName()));
							return true;
						}
					}
					Loader.sendMessages(s, "Home.Other.NotExist", Placeholder.c()
							.add("%home%", args[1])
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					return true;
				}
				return true;
			}
			Loader.noPerms(s, "HomeOther", "Warps");
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (s instanceof Player) {
			if (args.length == 1) {
				return null;
			}
			if (args.length == 2) {
				if(TheAPI.getUser(args[0]).getKeys("Homes")!=null)
				c.addAll(StringUtil.copyPartialMatches(args[1], TheAPI.getUser(args[0]).getKeys("Homes"),
						new ArrayList<>()));
				return c;
			}
			if (args.length == 3) {
				return null;
			}
		}
		return c;
	}

}

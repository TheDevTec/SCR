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
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class SetHome implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			if (Loader.has(s, "SetHome", "Warps")) {
				if(!CommandsManager.canUse("Warps.SetHome", s)) {
					Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Warps.SetHome", s))));
					return true;
				}
				if (Loader.vault == null) {
					if (args.length == 0) {
						TheAPI.getUser(p).setAndSave("Homes.home",
								new Position(p.getLocation()).toString());
						Loader.sendMessages(s, "Home.Create", Placeholder.c()
								.add("%home%", "home"));
						return true;
					}
					User d = TheAPI.getUser(p);
					d.setAndSave("Homes." + args[0], new Position(p.getLocation()).toString());
					Loader.sendMessages(s, "Home.Create", Placeholder.c()
							.add("%home%", "home"));
				} else {
					if (args.length == 0) {
						TheAPI.getUser(p).setAndSave("Homes.home",
								new Position(p.getLocation()).toString());
						Loader.sendMessages(s, "Home.Create", Placeholder.c()
								.add("%home%", "home"));
						return true;
					}
					User d = TheAPI.getUser(p);
					if (d.getKeys("Homes").size() >= getLimit(p)) {
						Loader.sendMessages(s, "Home.Limit");
						return true;
					}
					d.setAndSave("Homes." + args[0],
							new Position(p.getLocation()).toString());
					Loader.sendMessages(s, "Home.Create", Placeholder.c()
							.add("%home%", args[0]));
				}
				return true;
			}
			Loader.noPerms(s, "SetHome", "Warps");
			return true;
		}
		return true;
	}
	
	public int getLimit(Player player) {
		String group = API.getGroup(player);
		if (!Loader.config.exists("Homes." + group))group="default";
		return Loader.config.getInt("Homes." + group);
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "SetHome", "Warps") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}
}

package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;

public class God implements CommandExecutor, Listener {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (args.length == 0) {
			if (s instanceof Player) {
				SPlayer p = new SPlayer((Player) s);
				if (API.hasPerm(s, "ServerControl.God")) {
					p.toggleGod(null);
					return true;
				}
				return true;
			}
			Loader.Help(s, "/God <player> <on/off>", "God");
			return true;
		}
		if (args.length == 1) {
			if (Bukkit.getServer().getPlayer(args[0]) == null) {
				Loader.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
			SPlayer target = new SPlayer(Bukkit.getServer().getPlayer(args[0]));
			if (target == s) {
				if (API.hasPerm(s, "ServerControl.God")) {
					target.toggleGod(null);
					return true;
				}
			} else {
				if (API.hasPerm(s, "ServerControl.God.Other")) {
					target.toggleGod(s);
					return true;
				}
			}
			return true;
		}
		if (args.length == 2) {
			if (Bukkit.getServer().getPlayer(args[0]) == null) {
				Loader.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
			SPlayer target = new SPlayer(Bukkit.getServer().getPlayer(args[0]));
			if (s == target.getPlayer()) {
				if (API.hasPerm(s, "ServerControl.God")) {
					if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false")) {
						target.disableGod();
						Loader.msg(Loader.s("Prefix") + Loader.s("God.Disabled").replace("%player%", target.getName())
								.replace("%playername%", target.getDisplayName()), target.getPlayer());
						return true;
					}
					if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
						target.enableGod();
						Loader.msg(Loader.s("Prefix") + Loader.s("God.Enabled").replace("%player%", target.getName())
								.replace("%playername%", target.getDisplayName()), target.getPlayer());
						return true;
					}
				}
				return true;
			} else if (API.hasPerm(s, "ServerControl.God.Other")) {
				if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false")) {
					target.disableGod();
					Loader.msg(Loader.s("Prefix") + Loader.s("God.Disabled").replace("%player%", target.getName())
							.replace("%playername%", target.getDisplayName()), target.getPlayer());
					Loader.msg(Loader.s("Prefix")
							+ Loader.s("God.SpecifiedPlayerGodDisabled").replace("%player%", target.getName())
									.replace("%playername%".toLowerCase(), target.getDisplayName()),
							s);
					return true;
				}
				if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
					target.enableGod();
					Loader.msg(Loader.s("Prefix") + Loader.s("God.Enabled").replace("%player%", target.getName())
							.replace("%playername%", target.getDisplayName()), target.getPlayer());
					Loader.msg(Loader.s("Prefix") + Loader.s("God.SpecifiedPlayerGodEnabled")
							.replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()), s);
					return true;
				}
			}
			return true;
		}
		return false;
	}
}

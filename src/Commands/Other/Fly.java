package Commands.Other;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Scheduler.Tasker;

public class Fly implements CommandExecutor {

	public static HashMap<SPlayer, Integer> task = new HashMap<SPlayer, Integer>();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (API.hasPerm(s, "ServerControl.Fly")) {
				if (s instanceof Player) {
					SPlayer p = API.getSPlayer((Player) s);
					if (task.get(p) != null)
						Tasker.cancelTask(task.get(p));
					p.toggleFly(null);
					return true;
				}
				Loader.Help(s, "/Fly <player> <on/off>", "Fly");
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Usage"), s);
				return true;
			}
			return true;
		}
		if (args.length == 1) {
			if (Bukkit.getServer().getPlayer(args[0]) == null) {
				TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
			SPlayer target = API.getSPlayer(Bukkit.getServer().getPlayer(args[0]));
			if (target.getPlayer() == s) {
				if (API.hasPerm(s, "ServerControl.Fly")) {
					if (task.get(target) != null)
						Tasker.cancelTask(task.get(target));
					target.toggleFly(null);
					return true;
				}
				return true;
			} else {
				if (API.hasPerm(s, "ServerControl.Fly.Other")) {
					if (task.get(target) != null)
						Tasker.cancelTask(task.get(target));
					target.toggleFly(s);
					return true;
				}
				return true;
			}
		}
		if (args.length == 2) {
			if (Bukkit.getServer().getPlayer(args[0]) == null) {
				TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
			SPlayer target = API.getSPlayer(Bukkit.getServer().getPlayer(args[0]));
			if (target.getPlayer() != s) {
				if (API.hasPerm(s, "ServerControl.Fly.Other")) {
					if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false")) {
						if (task.get(target) != null)
							Tasker.cancelTask(task.get(target));
						target.disableFly();
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Disabled").replace("%player%", target.getName())
								.replace("%playername%", target.getDisplayName()), target.getPlayer());
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.SpecifiedPlayerFlyDisabled")
								.replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()),
								s);
						return true;
					}
					if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
						if (task.get(target) != null)
							Tasker.cancelTask(task.get(target));
						target.enableFly();
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Enabled").replace("%player%", target.getName())
								.replace("%playername%", target.getDisplayName()), target.getPlayer());
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.SpecifiedPlayerFlyEnabled")
								.replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()),
								s);
						return true;
					}
					if (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("true")
							&& !args[1].equalsIgnoreCase("off") && !args[1].equalsIgnoreCase("false")) {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Usage"), s);
						return true;
					}
				}
				return true;
			} else {
				if (API.hasPerm(s, "ServerControl.Fly")) {
					if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false")) {
						if (task.get(target) != null)
							Tasker.cancelTask(task.get(target));
						target.disableFly();
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Disabled").replace("%player%", target.getName())
								.replace("%playername%", target.getDisplayName()), target.getPlayer());
						return true;
					}
					if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
						if (task.get(target) != null)
							Tasker.cancelTask(task.get(target));
						target.enableFly();
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Enabled").replace("%player%", target.getName())
								.replace("%playername%", target.getDisplayName()), target.getPlayer());
						return true;
					}
					if (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("true")
							&& !args[1].equalsIgnoreCase("off") && !args[1].equalsIgnoreCase("false")) {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Usage"), s);
						return true;
					}
				}
				return true;
			}
		}
		return false;
	}
}
package Commands.Other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import ServerControl.SPlayer;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Scheduler.Scheduler;
import me.DevTec.TheAPI.Utils.NMS.NMSAPI;

public class Fly implements CommandExecutor, TabCompleter {
	public static HashMap<SPlayer, Integer> task = new HashMap<SPlayer, Integer>();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (Loader.has(s, "Fly", "Other")) {
				if (s instanceof Player) {
					SPlayer p = API.getSPlayer((Player) s);
					if (task.get(p) != null)
						Scheduler.cancelTask(task.get(p));
					p.toggleFly(null);
					return true;
				}
				Loader.Help(s, "Fly", "Other");
				return true;
			}
			Loader.noPerms(s, "Fly", "Other");
			return true;
		}
		SPlayer target = null;
		if (args.length == 1) {
			if(s instanceof Player) {
			if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false")) {
				target = API.getSPlayer((Player)s);
				if (task.get(target) != null)
					Scheduler.cancelTask(task.get(target));
				target.disableFly();
				Loader.sendMessages(s, "Fly.Disabled.You");
				return true;
			}
			if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {
				target = API.getSPlayer((Player)s);
				if (task.get(target) != null)
					Scheduler.cancelTask(task.get(target));
				target.enableFly();
				Loader.sendMessages(s, "Fly.Enabled.You");
				return true;
			}}
			if (TheAPI.getPlayer(args[0]) == null) {
				Loader.notOnline(s, args[0]);
				return true;
			}
			target = API.getSPlayer(TheAPI.getPlayer(args[0]));
			if (target.getPlayer() == s) {
				if (Loader.has(s, "Fly", "Other")) {
					if (task.get(target) != null)
						Scheduler.cancelTask(task.get(target));
					target.toggleFly(null);
					return true;
				}
				Loader.noPerms(s, "Fly", "Other");
				return true;
			} else {
				if (Loader.has(s, "Fly", "Other", "Other")) {
					if (task.get(target) != null)
						Scheduler.cancelTask(task.get(target));
					target.toggleFly(s);
					return true;
				}
				Loader.noPerms(s, "Fly", "Other", "Other");
				return true;
			}
		}
		if (TheAPI.getPlayer(args[0]) == null) {
			Loader.notOnline(s, args[0]);
			return true;
		}
		target = API.getSPlayer(TheAPI.getPlayer(args[0]));
		if (args.length == 2) {
			if (target.getPlayer() != s) {
				if (Loader.has(s, "Fly", "Other", "Other")) {
					if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false")) {
						if (task.get(target) != null)
							Scheduler.cancelTask(task.get(target));
						target.disableFly();
						Loader.sendMessages(s, "Fly.Disabled.Other.Sender", Placeholder.c().replace("%player%", target.getName())
								.replace("%playername%", target.getDisplayName()));
						Loader.sendMessages(target.getPlayer(), "Fly.Disabled.Other.Receiver", Placeholder.c().replace("%player%", s.getName())
								.replace("%playername%", s.getName()));
						return true;
					}
					if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
						if (task.get(target) != null)
							Scheduler.cancelTask(task.get(target));
						target.enableFly();
						Loader.sendMessages(s, "Fly.Enabled.Other.Sender", Placeholder.c().replace("%player%", target.getName())
								.replace("%playername%", target.getDisplayName()));
						Loader.sendMessages(target.getPlayer(), "Fly.Enabled.Other.Receiver", Placeholder.c().replace("%player%", s.getName())
								.replace("%playername%", s.getName()));
						return true;
					}
					Loader.Help(s, "Fly", "Other");
					return true;
				}
				Loader.noPerms(s, "Fly", "Other", "Other");
				return true;
			} else {
				if (Loader.has(s, "Fly", "Other")) {
					if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false")) {
						if (task.get(target) != null)
							Scheduler.cancelTask(task.get(target));
						target.disableFly();
						Loader.sendMessages(s, "Fly.Disabled.You");
						return true;
					}
					if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
						if (task.get(target) != null)
							Scheduler.cancelTask(task.get(target));
						target.enableFly();
						Loader.sendMessages(s, "Fly.Enabled.You");
						return true;
					}
					Loader.Help(s, "Fly", "Other");
					return true;
				}
				Loader.noPerms(s, "Fly", "Other");
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String a, String[] args) {
		List<String> c = new ArrayList<>();
		if(args.length==1) {
			if(Loader.has(s, "Fly", "Other")) {
				List<String> list = Arrays.asList("On","Off");
				if(Loader.has(s, "Fly", "Other", "Other"))
				list.addAll(NMSAPI.getOnlinePlayersNames());
				else list.add(s.getName());
				c.addAll(StringUtil.copyPartialMatches(args[0], list, new ArrayList<>()));
		}}
		if(args.length==2)
			if(Loader.has(s, "Fly", "Other"))
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("On","Off"), new ArrayList<>()));
		return c;
	}
}
package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.Events.LoginEvent;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Scheduler.Scheduler;
import me.DevTec.TheAPI.Scheduler.Tasker;

public class Vanish implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
	private static HashMap<String, Integer> task = new HashMap<>();
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Vanish", "Other")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					if (!TheAPI.hasVanish(p.getName())) {
						TheAPI.setVanish(p.getName(), "ServerControl.Vanish", true);
						LoginEvent.moveInTab(p);
						Loader.sendMessages(s, "Vanish.Enabled.You");
						if(setting.vanish_action)
							task.put(p.getName(), new Tasker() {
								@Override
								public void run() {
									if(!TheAPI.hasVanish(p.getName()) || !p.isOnline()) {
										cancel();
										return;
									}
									TheAPI.sendActionBar(p, Loader.getTranslation("Vanish.Active").toString());
								}
							}.runRepeating(0, 20));
						return true;
					}
					if(task.containsKey(s.getName())) {
						Scheduler.cancelTask(task.get(s.getName()));
						task.remove(s.getName());
						TheAPI.sendActionBar(p, "");
					}
					TheAPI.setVanish(p.getName(), Loader.cmds.getString("Other.Vanish.Permission"), false);
					LoginEvent.moveInTab(p);
					Loader.sendMessages(s, "Vanish.Disabled.You");
					return true;
				}
				Loader.Help(s, "Vanish", "Other");
				return true;
			}
			if (Loader.has(s, "Vanish", "Other", "Other")) {
			Player t = TheAPI.getPlayer(args[0]);
			if (t != null) {
				if (!TheAPI.hasVanish(t.getName())) {
					TheAPI.setVanish(t.getName(), "ServerControl.Vanish", true);
					LoginEvent.moveInTab(t);
					Loader.sendMessages(s, "Vanish.Enabled.Other.Sender", Placeholder.c().add("%player%", t.getName()).add("%playername%", t.getDisplayName()));
					Loader.sendMessages(s, "Vanish.Enabled.Other.Receiver", Placeholder.c().add("%player%", s.getName()).add("%playername%", s.getName()));
					if(setting.vanish_action)
						task.put(t.getName(),new Tasker() {
							@Override
							public void run() {
								if(!TheAPI.hasVanish(t.getName()) || !t.isOnline()) {
									cancel();
									return;
								}
								TheAPI.sendActionBar(t, Loader.getTranslation("Vanish.Active").toString());
							}
						}.runRepeating(0, 20));
					return true;
				}
				if(task.containsKey(t.getName())) {
					Scheduler.cancelTask(task.get(t.getName()));
					task.remove(t.getName());
					TheAPI.sendActionBar(t, "");
				}
				TheAPI.setVanish(t.getName(), "ServerControl.Vanish", false);
				LoginEvent.moveInTab(t);
				Loader.sendMessages(s, "Vanish.Disabled.Other.Sender", Placeholder.c().add("%player%", t.getName()).add("%playername%", t.getDisplayName()));
				Loader.sendMessages(s, "Vanish.Disabled.Other.Receiver", Placeholder.c().add("%player%", s.getName()).add("%playername%", s.getName()));
				return true;
			}
			Loader.notOnline(s, args[0]);
			return true;
			}
			Loader.noPerms(s, "Vanish", "Other", "Other");
			return true;
		}
		Loader.noPerms(s, "Vanish", "Other");
		return true;
	}
}
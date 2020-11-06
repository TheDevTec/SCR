package me.DevTec.ServerControlReloaded.Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.Events.LoginEvent;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Scheduler.Tasker;

public class Vanish implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Vanish", "Other")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					if (!TheAPI.isVanished(p)) {
						TheAPI.vanish(p, "ServerControl.Vanish", true);
						LoginEvent.moveInTab(p);
						Loader.sendMessages(s, "Vanish.Enabled.You");
						if(setting.vanish_action)
						new Tasker() {
							@Override
							public void run() {
								if(!TheAPI.isVanished(p)) {
									cancel();
									return;
								}
								TheAPI.sendActionBar(p, Loader.getTranslation("Vanish.Active").toString());
							}
						}.runRepeating(0, 20);
						return true;
					}
					TheAPI.vanish(p, "ServerControl.Vanish", false);
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
				if (!TheAPI.isVanished(t)) {
					TheAPI.vanish(t, "ServerControl.Vanish", true);
					LoginEvent.moveInTab(t);
					Loader.sendMessages(s, "Vanish.Enabled.Other.Sender", Placeholder.c().add("%player%", t.getName()).add("%playername%", t.getDisplayName()));
					Loader.sendMessages(s, "Vanish.Enabled.Other.Receiver", Placeholder.c().add("%player%", s.getName()).add("%playername%", s.getName()));
					if(setting.vanish_action)
					new Tasker() {
						@Override
						public void run() {
							if(!TheAPI.isVanished(t)) {
								cancel();
								return;
							}
							TheAPI.sendActionBar(t, Loader.getTranslation("Vanish.Active").toString());
						}
					}.runRepeating(0, 20);
					return true;
				}
				TheAPI.vanish(t, "ServerControl.Vanish", false);
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
package me.devtec.servercontrolreloaded.commands.info;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

public class Maintenance implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
			if (Loader.has(s, "Maintenance", "Info")) {
				if(!CommandsManager.canUse("Info.Maintenance", s)) {
					Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Info.Maintenance", s))));
					return true;
				}
				if (setting.lock_server) {
					Loader.config.set("Options.Maintenance.Enabled", false);
					Loader.config.save();
					setting.lock_server = false;
					Loader.sendMessages(s, "Maintenance.Disabled");
					return true;
				}

				Loader.config.set("Options.Maintenance.Enabled", true);
				Loader.config.save();
				setting.lock_server = true;
				Loader.sendMessages(s, "Maintenance.Enabled");
				return true;
			}
			Loader.noPerms(s, "Maintenance", "Info");
			return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Arrays.asList();
	}
}

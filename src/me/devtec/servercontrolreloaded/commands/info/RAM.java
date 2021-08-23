package me.devtec.servercontrolreloaded.commands.info;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.apis.MemoryAPI;
import me.devtec.theapi.utils.StringUtils;

public class RAM implements CommandExecutor, TabCompleter {
	private boolean clearing;
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Memory", "Info")) {
			if(!CommandsManager.canUse("Info.Memory", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Info.Memory", s))));
				return true;
			}
			if (args.length == 0) {
				if (setting.ram) {
					Loader.sendMessages(s, "Memory.Info", Placeholder.c()
							.add("%free_ram%", StringUtils.fixedFormatDouble(MemoryAPI.getFreeMemory(true)))
							.add("%max_ram%", StringUtils.fixedFormatDouble(MemoryAPI.getMaxMemory()))
							.add("%used_ram%", StringUtils.fixedFormatDouble(MemoryAPI.getUsedMemory(true))));
					return true;
				}
				Loader.sendMessages(s, "Memory.Info", Placeholder.c()
						.add("%free_ram%", StringUtils.fixedFormatDouble(MemoryAPI.getFreeMemory(false)))
						.add("%max_ram%", StringUtils.fixedFormatDouble(MemoryAPI.getMaxMemory()))
						.add("%used_ram%", StringUtils.fixedFormatDouble(MemoryAPI.getUsedMemory(false))));
				return true;
			}
			if (!clearing) {
				clearing = true;
				Loader.sendMessages(s, "Memory.Clear");
				Loader.sendMessages(s, "Memory.Cleared", Placeholder.c()
						.add("%amount%", MemoryAPI.clearMemory()));
				clearing = false;
				return true;
			}
			Loader.sendMessages(s, "Memory.AlreadyClearing");
			return true;
		}
		Loader.noPerms(s, "Memory", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Collections.emptyList();
	}
}
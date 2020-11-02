package me.DevTec.ServerControlReloaded.Commands.Info;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.DevTec.TheAPI.APIs.MemoryAPI;

public class RAM implements CommandExecutor {
	private boolean clearing;
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Memory", "Info")) {
			if (args.length == 0) {
				if (setting.ram) {
					Loader.sendMessages(s, "Memory.Info", Placeholder.c()
							.add("%free_ram%", MemoryAPI.getFreeMemory(true) + "")
							.add("%max_ram%", MemoryAPI.getMaxMemory() + "")
							.add("%used_ram%", MemoryAPI.getUsedMemory(true) + ""));
					return true;
				}
				Loader.sendMessages(s, "Memory.Info", Placeholder.c()
						.add("%free_ram%", MemoryAPI.getFreeMemory(false) + "")
						.add("%max_ram%", MemoryAPI.getMaxMemory() + "")
						.add("%used_ram%", MemoryAPI.getUsedMemory(false) + ""));
				return true;
			}
			if (clearing == false) {
				clearing = true;
				Loader.sendMessages(s, "Memory.Clearing");
				Loader.sendMessages(s, "Memory.Cleared", Placeholder.c()
						.add("%amount%", MemoryAPI.clearMemory() + ""));
				clearing = false;
				return true;
			}
			Loader.sendMessages(s, "Memory.AlreadyClearing");
			return true;
		}
		Loader.noPerms(s, "Memory", "Info");
		return true;
	}
}
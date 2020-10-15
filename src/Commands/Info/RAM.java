package Commands.Info;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.MemoryAPI;

public class RAM implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (Loader.has(s, "RAM", "Info")) {
			if (args.length == 0) {
				Loader.sendMessages(s, "Memory.MemoryLine");
				TheAPI.msg("", s);
				if (setting.ram) {
					List<String> normal = Loader.trans.getStringList("RAM.Info.Normal");
					for (String ss : normal) {
						
						Loader.sendMessages(s, "Memory.Info", Placeholder.c()
								.add("%free_ram%", MemoryAPI.getFreeMemory(false) + "")
								.add("%max_ram%", MemoryAPI.getMaxMemory() + "")
								.add("%used_ram%", MemoryAPI.getUsedMemory(false) + ""));
					}
					return true;
				}
				List<String> normal = Loader.trans.getStringList("RAM.Info.Percent");
				for (String sss : normal) {
					Loader.sendMessages(s, "Memory.Info", Placeholder.c()
							.add("%free_ram%", MemoryAPI.getFreeMemory(false) + "")
							.add("%max_ram%", MemoryAPI.getMaxMemory() + "")
							.add("%used_ram%", MemoryAPI.getUsedMemory(false) + ""));

				}
				return true;
			}
			if (ServerControl.clearing == false) {
				ServerControl.clearing = true;
				Loader.sendMessages(s, "Memory.Clearing");
				Loader.sendMessages(s, "Memory.Cleared", Placeholder.c()
						.add("%amount%", MemoryAPI.clearMemory() + ""));
				ServerControl.clearing = false;
				return true;
			}
			Loader.sendMessages(s, "Memory.AlreadyClearing");
			return true;
		}
		Loader.noPerms(s, "RAM", "Info");
		return true;
	}
}
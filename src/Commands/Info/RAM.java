package Commands.Info;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.MemoryAPI;

public class RAM implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (API.hasPerm(s, "ServerControl.RAM")) {
			if (args.length == 0) {
				TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bRAM &e-----------------", s);
				TheAPI.msg("", s);
				if (setting.ram) {
					List<String> normal = Loader.trans.getStringList("RAM.Info.Normal");
					for (String ss : normal) {

						TheAPI.msg(
								Loader.s("Prefix")
										+ ss.replace("%free_ram%", MemoryAPI.getFreeMemory(false) + "")
												.replace("%max_ram%", MemoryAPI.getMaxMemory() + "")
												.replace("%used_ram%", MemoryAPI.getUsedMemory(false) + ""),
								s);
					}
					return true;
				}
				List<String> normal = Loader.trans.getStringList("RAM.Info.Percent");
				for (String sss : normal) {
					TheAPI.msg(Loader.s("Prefix")
							+ sss.replace("%used_ram%", MemoryAPI.getUsedMemory(true) + "")
									.replace("%max_ram%", MemoryAPI.getMaxMemory() + "")
									.replace("%free_ram%", MemoryAPI.getFreeMemory(true) + ""),
							s);

				}
				return true;
			}
			if (ServerControl.clearing == false) {
				ServerControl.clearing = true;
				TheAPI.msg(Loader.s("Prefix") + Loader.s("RAM.Clearing"), s);
				TheAPI.msg(Loader.s("Prefix")
						+ Loader.s("RAM.Cleared").replace("%cleared%", MemoryAPI.clearMemory() + ""), s);
				ServerControl.clearing = false;
				return true;
			}
			TheAPI.msg(Loader.s("Prefix") + Loader.s("RAM.AlreadyClearing"), s);
			return true;
		}
		return true;
	}
}
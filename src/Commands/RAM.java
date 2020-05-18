package Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import Utils.setting;
import me.Straiker123.TheAPI;

public class RAM implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (API.hasPerm(s, "ServerControl.RAM")) {
			if (args.length == 0) {
				Loader.msg(Loader.s("Prefix") + "&e----------------- &bRAM &e-----------------", s);
				Loader.msg("", s);
				if (setting.ram) {
					List<String> normal = Loader.TranslationsFile.getStringList("RAM.Info.Normal");
					for (String ss : normal) {

						Loader.msg(
								Loader.s("Prefix")
										+ ss.replace("%free_ram%", TheAPI.getMemoryAPI().getFreeMemory(false) + "")
												.replace("%max_ram%", TheAPI.getMemoryAPI().getMaxMemory() + "")
												.replace("%used_ram%", TheAPI.getMemoryAPI().getUsedMemory(false) + ""),
								s);
					}
					return true;
				}
				List<String> normal = Loader.TranslationsFile.getStringList("RAM.Info.Percent");
				for (String sss : normal) {
					Loader.msg(Loader.s("Prefix")
							+ sss.replace("%used_ram%", TheAPI.getMemoryAPI().getUsedMemory(true) + "")
									.replace("%max_ram%", TheAPI.getMemoryAPI().getMaxMemory() + "")
									.replace("%free_ram%", TheAPI.getMemoryAPI().getFreeMemory(true) + ""),
							s);

				}
				return true;
			}
			if (ServerControl.clearing == false) {
				ServerControl.clearing = true;
				Loader.msg(Loader.s("Prefix") + Loader.s("RAM.Clearing"), s);
				Loader.msg(Loader.s("Prefix")
						+ Loader.s("RAM.Cleared").replace("%cleared%", TheAPI.getMemoryAPI().clearMemory() + ""), s);
				ServerControl.clearing = false;
				return true;
			}
			Loader.msg(Loader.s("Prefix") + Loader.s("RAM.AlreadyClearing"), s);
			return true;
		}
		return true;
	}
}
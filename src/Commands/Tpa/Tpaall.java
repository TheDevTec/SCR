package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Tpaall implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpaAll", "TpSystem")) {
			if (s instanceof Player) {
				List<String> list = new ArrayList<String>();
				for (Player d : TheAPI.getOnlinePlayers()) {
					if (d == s)
						continue;
					String p = d.getName();
					if (!RequestMap.has(p, s.getName())) {
						if (!TheAPI.getUser(p).getBoolean("TpBlock." + s.getName())
								&& !TheAPI.getUser(p).getBoolean("TpBlock-Global")) {
							list.add(p);
							RequestMap.add((Player)s, p, 1);
						}
					}
				}
				Loader.sendMessages(s, "TpSystem.TpaAll", Placeholder.c().replace("%list%",
							list.isEmpty()?"none":StringUtils.join(list, ", ")).replace("%amount%",
									list.size()+""));
				return true;
			}
			return true;
		}
		return true;
	}
}

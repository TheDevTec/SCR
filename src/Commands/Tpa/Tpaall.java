package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Tpaall implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Tpaall")) {
			if (s instanceof Player) {
				ArrayList<String> list = new ArrayList<String>();
				for (Player d : TheAPI.getOnlinePlayers()) {
					if (d == s)
						continue;
					String p = d.getName();
					if (!RequestMap.containsRequest(p, s.getName())) {
						if (!TheAPI.getUser(p).getBoolean("TpBlock." + s.getName())
								&& !TheAPI.getUser(p).getBoolean("TpBlock-Global")) {
							list.add(p);
							RequestMap.addRequest(s.getName(), p, RequestMap.Type.TPAHERE);
						}
					}
				}
				if (!list.isEmpty())
					Loader.msg(Loader.s("Prefix") + Loader.s("TpaSystem.Tpaall").replace("%players%",
							TheAPI.getStringUtils().join(list, ", ")), s);
				else
					Loader.msg(Loader.s("Prefix") + Loader.s("TpaSystem.Tpaall").replace("%players%", "---"), s);
				return true;
			}
			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		return new ArrayList<>();
	}
}

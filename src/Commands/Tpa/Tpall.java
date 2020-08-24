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
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class Tpall implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Tpall")) {
			if (s instanceof Player) {
				ArrayList<String> list = new ArrayList<String>();
				for (Player p : TheAPI.getOnlinePlayers()) {
					if (p == s)
						continue;
					User d = TheAPI.getUser(p);
					if (!d.getBoolean("TpBlock." + s.getName()) && !d.getBoolean("TpBlock-Global")
							|| d.getBoolean("TpBlock." + s.getName()) && !d.getBoolean("TpBlock-Global")
									&& s.hasPermission("ServerControl.Tpall.Blocked")
							|| d.getBoolean("TpBlock." + s.getName()) && d.getBoolean("TpBlock-Global")
									&& s.hasPermission("ServerControl.Tpall.Blocked")
							|| !d.getBoolean("TpBlock." + s.getName()) && d.getBoolean("TpBlock-Global")
									&& s.hasPermission("ServerControl.Tpall.Blocked")) {
						list.add(p.getName());
						p.teleport(((Player) s));
					}
				}
				TheAPI.msg(Loader.s("Prefix")
						+ Loader.s("TpaSystem.Tpall").replace("%players%", StringUtils.join(list, ", ")),
						s);
				return true;
			}
			TheAPI.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		return c;
	}
}
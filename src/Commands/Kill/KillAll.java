package Commands.Kill;

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

public class KillAll implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "KillAll", "Kill")) {
			List<String> pl = new ArrayList<String>();
			for (Player p : TheAPI.getOnlinePlayers()) {
				boolean i = p.isDead();
				p.setHealth(0);
				if (p.isDead() && !i) {
					pl.add(p.getName());
				}
			}
			Loader.sendMessages(s, "Kill.KilledMore", Placeholder.c()
					.add("%list%", pl.isEmpty()?"none":StringUtils.join(pl, ","))
					.replace("%amount%", pl.size()+""));
			return true;
		}
		return true;
	}
}

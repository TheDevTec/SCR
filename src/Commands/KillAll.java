package Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class KillAll implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.KillAll")) {
			int amount = 0;
			List<String> pl = new ArrayList<String>();
			for (Player p : TheAPI.getOnlinePlayers()) {
				boolean i = p.isDead();
				p.setHealth(0);
				if (p.isDead() && !i) {
					pl.add(p.getName());
					++amount;
				}
			}
			TheAPI.msg(Loader.s("Kill.KilledAll").replace("%amount%", amount + "").replace("%players%",
					TheAPI.getStringUtils().join(pl, ",")), s);
			return true;
		}
		return true;
	}
}

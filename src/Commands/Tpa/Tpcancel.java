package Commands.Tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class Tpcancel implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (s.hasPermission("ServerControl.TpCancel")) {
			if (s instanceof Player) {
				Player a = RequestMap.getFirst(s.getName());
				if (a==null) {
					Loader.sendMessages(s, "TpSystem.NoRequest");
					return true;
				}
				int type = TheAPI.getUser(s.getName()).getInt("teleport."+a.getName()+".a");
				Loader.sendMessages(s, "TpSystem.Cancel."+(type==0?"Tpa":"Tpahere")+".Sender", Placeholder.c()
						.replace("%player%", a.getName()).replace("%playername%", a.getDisplayName()));
				Loader.sendMessages(a, "TpSystem.Cancel."+(type==0?"Tpa":"Tpahere")+".Receiver", Placeholder.c()
						.replace("%player%", s.getName()).replace("%playername%", ((Player)s).getDisplayName()));
				return true;
			}
			return true;
		}
		return true;
	}

}

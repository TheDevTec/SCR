package Commands.Other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import me.DevTec.TheAPI.TheAPI;

public class Heal implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (!API.hasPerm(s, "ServerControl.Heal"))
			return true;
		if (args.length == 0) {
			if (s instanceof Player == false) {
				Loader.Help(s, "/Heal <player>", "Heal");
				return true;
			}
			Player p = (Player) s;
			API.getSPlayer(p).heal();
			TheAPI.msg(Loader.s("Prefix") + Loader.s("Heal.Healed").replace("%player%", p.getName())
					.replace("%playername%", p.getDisplayName()), s);
			return true;
		}
		if (args.length == 1) {
			if (args[0].equals("*")) {
				Repeat.a(s, "heal *");
				return true;
			}
			Player target = Bukkit.getServer().getPlayer(args[0]);
			if (target == null) {
				TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
			if (target == s) {
				Player p = (Player) s;
				API.getSPlayer(p).heal();
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Heal.Healed").replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName()), s);
				return true;
			}
			if (API.hasPerm(s, "ServerControl.Heal.Other")) {
				API.getSPlayer(target).heal();
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Heal.Healed").replace("%player%", target.getName())
						.replace("%playername%", target.getDisplayName()), target);
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Heal.SpecifyPlayerHealed")
						.replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()), s);
				return true;
			}
			return true;
		}

		return false;
	}
}

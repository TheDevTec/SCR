package Commands.Time;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class PDay implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				if (Loader.has(s, "PlayerDay", "Time")) {
					((Player) s).setPlayerTime(1000, true);
					TheAPI.msg(Loader.s("Prefix")
							+ Loader.s("Time.Day").replace("%world%", ((Player) s).getLocation().getWorld().getName()),
							s); //TODO - chybí v transaltion "Time.PDay"
					return true;
				}
				Loader.noPerms(s, "PlayerDay", "Time");
				return true;
			}
			Loader.Help(s, "/Day <world>", "Time");
			return true;
		}
		if (args.length == 1) {
			if (Loader.has(s, "PlayerDay", "Time", "Other")) {
				if (TheAPI.getPlayer(args[0]) != null) {
					TheAPI.getPlayer(args[0]).setPlayerTime(1000, true);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Time.Day").replace("%world%", args[0]), s); //TODO - chybí v transaltion "Time.PDay"
					return true;
				}
				Loader.notOnline(s, args[0]);
				return true;
			}
			Loader.noPerms(s, "PlayerDay", "Time", "Other");
			return true;
		}
		return false;
	}

}

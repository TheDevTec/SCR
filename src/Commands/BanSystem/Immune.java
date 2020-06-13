package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class Immune implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String label, String[] args) {
		if (API.hasPerm(s, "ServerControl.Immune")) {
			if (args.length == 0) {
				if (!(s instanceof Player)) {
					Loader.Help(s, "/Immune <player> <true/false>", "BanSystem.Immune");
					return true;
				}
				Player p = (Player) s;
				boolean im = TheAPI.getUser(p).getBoolean("Immune");
				TheAPI.getUser(p).setAndSave("Immune", !im);
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune." + (im ? "Disable" : "Enabled")), p);
				return true;
			}
			if (args.length == 1) {
				if (API.hasPerm(s, "ServerControl.Immune.Other")) {
					Player p = (Player) s;
					boolean im = TheAPI.getUser(args[0]).getBoolean("Immune");
					TheAPI.getUser(args[0]).setAndSave("Immune", !im);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Immune." + (im ? "Disable" : "Enabled")), p);
					TheAPI.msg(
							Loader.s("Prefix")
									+ Loader.s("Immune." + (im ? "Off" : "On") + "Other").replace("%target%", args[0]),
							p);
					return true;
				}
				return true;
			}
			return true;
		}
		return true;
	}
}

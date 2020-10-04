package Commands.Gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class GamemodeSP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(TheAPI.isOlderThan(8)) {
			TheAPI.msg("&cUnsupported GameMode type", s);
			return true;
		}
		if (API.hasPerm(s, "ServerControl.Gamemode.Spectator")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					p.setGameMode(GameMode.SPECTATOR);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Gamemode.Changed").replace("%gamemode%", "Spectator"), s);
					return true;
				}
				Loader.Help(s, "/Gmsp <player>", "Gamemode");
				return true;
			}
			if (args.length == 1) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null) {
					p.setGameMode(GameMode.SPECTATOR);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Gamemode.ChangedOther").replace("%gamemode%", "Spectator")
							.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
					return true;
				}
				TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
		}
		return true;
	}
}
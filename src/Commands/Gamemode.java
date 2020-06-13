package Commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class Gamemode implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if (args.length == 0) {
			if (API.hasPerm(s, "ServerControl.Gamemode")) {
				Loader.Help(s, "/GameMode <s|c|a|sp> <player>", "Gamemode");
				return true;
			}
			return true;
		}
		String gamemode = null;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0"))
				gamemode = "Survival";
			else

			if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1"))
				gamemode = "Creative";
			else

			if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2"))
				gamemode = "Adventure";
			else

			if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectator")
					|| args[0].equalsIgnoreCase("3"))
				gamemode = "Spectator";
			else {

				gamemode = null;

			}
			if (s instanceof Player) {
				if (API.hasPerm(s, "ServerControl.Gamemode." + gamemode)) {
					if (gamemode != null) {
						((Player) s).setGameMode(GameMode.valueOf(gamemode.toUpperCase()));
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Gamemode.Changed").replace("%gamemode%", gamemode),
								s);
						return true;
					}
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Gamemode.Invalid").replace("%gamemode%", args[0]), s);
					return true;
				}
				return true;
			}
			if (gamemode != null) {

				Loader.Help(s, "/GameMode " + args[0] + " <player>", "Gamemode");
				return true;
			}
			TheAPI.msg(Loader.s("Prefix") + Loader.s("Gamemode.Invalid").replace("%gamemode%", args[0]), s);
			return true;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0"))
				gamemode = "Survival";
			else

			if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1"))
				gamemode = "Creative";
			else

			if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2"))
				gamemode = "Adventure";
			else

			if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectator")
					|| args[0].equalsIgnoreCase("3"))
				gamemode = "Spectator";
			else {

				gamemode = null;

			}
			if (API.hasPerm(s, "ServerControl.Gamemode." + gamemode)) {
				Player p = TheAPI.getPlayer(args[1]);
				if (gamemode != null) {
					if (p != null) {
						p.setGameMode(GameMode.valueOf(gamemode.toUpperCase()));
						TheAPI.msg(
								Loader.s("Prefix") + Loader.s("Gamemode.ChangedOther").replace("%gamemode%", gamemode)
										.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()),
								s);
						return true;
					}
					TheAPI.msg(Loader.PlayerNotOnline(args[0]), s);
					return true;
				}
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Gamemode.Invalid").replace("%gamemode%", args[0]), s);
				return true;
			}
			return true;
		}

		return false;
	}
}
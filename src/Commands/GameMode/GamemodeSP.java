package Commands.GameMode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class GamemodeSP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(TheAPI.isOlderThan(8)) {
			TheAPI.msg("&cUnsupported GameMode type", s);
			return true;
		}
		if (Loader.has(s, "GameModeSpectator", "GameMode")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					p.setGameMode(GameMode.SPECTATOR);
					Loader.sendMessages(p, "GameMode.Your.Spectator");
					return true;
				}
				Loader.Help(s, "GameModeSpectator", "GameMode");
				return true;
			}
			if (args.length == 1) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null) {
					p.setGameMode(GameMode.SPECTATOR);
					Loader.sendMessages(p, "GameMode.Other.Spectator.Receiver");
					
					Loader.sendMessages(s, "GameMode.Other.Spectator.Sender", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));	
					return true;
				}
				Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
						.add("%player%", args[0]));
				return true;
			}
		}
		Loader.noPerms(s, "GameModeSpectator", "GameMode");
		return true;
	}
}
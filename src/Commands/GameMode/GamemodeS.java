package Commands.GameMode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class GamemodeS implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "GameModeSurvival", "GameMode")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					p.setGameMode(GameMode.SURVIVAL);
					Loader.sendMessages(p, "GameMode.Your.Survival");
					return true;
				}
				Loader.Help(s, "GameModeSurvival", "GameMode");
				return true;
			}
			if (args.length == 1) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null) {
					p.setGameMode(GameMode.SURVIVAL);
					Loader.sendMessages(p, "GameMode.Other.Survival.Receiver");
					
					Loader.sendMessages(s, "GameMode.Other.Survival.Sender", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					return true;
				}
				Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
						.add("%player%", args[0]));
				return true;
			}
		}
		Loader.noPerms(s, "GameModeSurvival", "GameMode");
		return true;
	}
}

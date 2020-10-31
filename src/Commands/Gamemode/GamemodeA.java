package Commands.Gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class GamemodeA implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Gamemode", "Gamemode")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					p.setGameMode(GameMode.ADVENTURE);
					Loader.sendMessages(p, "Gamemode.Your.Adventure");
					return true;
				}
				Loader.Help(s, "/Gma <player>", "Gamemode");
				return true;
			}
			if (args.length == 1) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null) {
					p.setGameMode(GameMode.ADVENTURE);
					Loader.sendMessages(p, "Gamemode.Other.Adventure.Receiver");
					
					Loader.sendMessages(s, "Gamemode.Other.Adventure.Sender", Placeholder.c()
							.add("%player%", p.getName())
							.add("%playername%", p.getDisplayName()));
					return true;
				}
				Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c()
						.add("%player%", args[0]));
				return true;
			}
		}
		Loader.noPerms(s, "Gamemode", "Gamemode");
		return true;
	}
}
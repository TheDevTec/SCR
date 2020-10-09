package Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import ServerControl.SPlayer;
import me.DevTec.TheAPI.TheAPI;

public class TempFly implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
			if (args.length == 0) {
				Loader.Help(s, "TempFly", "Other");
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					if (Loader.has(s, "TempFly", "Other")) {
						API.getSPlayer(TheAPI.getPlayer(s.getName()))
								.enableTempFly(StringUtils.getTimeFromString(args[0]));
						return true;
					}
					return true;
				}
				Loader.Help(s, "TempFly", "Other");
				return true;
			}
			if (args.length == 2) {
				Player player = TheAPI.getPlayer(args[0]);
				if (player == null) {
					Loader.notOnline(s,args[0]);
					return true;
				}
				SPlayer t = API.getSPlayer(player);
				long sec = StringUtils.getTimeFromString(args[1]);
				if (t.getPlayer() == s) {
					if (Loader.has(s, "TempFly", "Other")) {
						t.enableTempFly(sec);
						return true;
					}
					return true;
				}
				if (Loader.has(s, "TempFly", "Other", "Other")) {
					Loader.sendMessages(s, "Fly.Temp.Enabled.Other.Sender", Placeholder.c().replace("%player%", player.getName())
							.replace("%playername%", player.getDisplayName()).replace("%time%", StringUtils.setTimeToString(sec)));
					Loader.sendMessages(player, "Fly.Temp.Enabled.Other.Receiver", Placeholder.c().replace("%player%", s.getName())
							.replace("%playername%", s.getName()).replace("%time%", StringUtils.setTimeToString(sec)));
					t.enableTempFly(sec);
					return true;
				}
				return true;
			}
		return true;
	}
}

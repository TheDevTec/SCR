package Commands.Nickname;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class NickReset implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if (Loader.has(s, "Nickname", "Nickname")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					TheAPI.getUser(s.getName()).setAndSave("DisplayName", null);
					if(TheAPI.getPlayerOrNull(s.getName())!=null)
						TheAPI.getPlayerOrNull(s.getName()).setCustomName(null);
					Loader.sendMessages(s, "Nickname.Reset", Placeholder.c()
							.add("%player%", s.getName())
							.add("%playername%", ((Player) s).getDisplayName()));
					return true;
				}
				Loader.Help(s, "/NickReset <player>", "NickReset");
				return true;
			}
			String a = args[0];
			if (!TheAPI.existsUser(a)) {
				Loader.sendMessages(s, "Missing.Player.NotExist", Placeholder.c()
						.add("%player%", a)
						.add("%playername%", a));
				return true;
			}
			TheAPI.getUser(s.getName()).setAndSave("DisplayName", null);
			if(TheAPI.getPlayerOrNull(a)!=null)
				TheAPI.getPlayerOrNull(a).setCustomName(null);
			Loader.sendMessages(s, "Nickname.Reset", Placeholder.c()
					.add("%player%", a)
					.add("%playername%", a));
			return true;
		}
		return true;

	}
}

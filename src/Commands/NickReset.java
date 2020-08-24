package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class NickReset implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if (API.hasPerm(s, "ServerControl.Nickname")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					TheAPI.getUser(s.getName()).setAndSave("DisplayName", null);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("NicknameReseted"), s);
					return true;
				}
				Loader.Help(s, "/NickReset <player>", "NickReset");
				return true;
			}
			String a = args[0];
			if (!TheAPI.existsUser(a)) {
				TheAPI.msg(Loader.PlayerNotEx(a), s);
				return true;
			}
			TheAPI.getUser(s.getName()).setAndSave("DisplayName", null);
			TheAPI.msg(Loader.s("Prefix")
					+ Loader.s("NicknameResetedOther").replace("%player%", a).replace("%playername%", a), s);
			return true;
		}
		return true;

	}
}

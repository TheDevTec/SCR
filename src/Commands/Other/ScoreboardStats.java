package Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;

public class ScoreboardStats implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerCotrol.Scoreboard")) {
			TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bScoreboard Reload &e-----------------", s);
			TheAPI.msg("", s);
			Utils.ScoreboardStats.removeScoreboard();
			Loader.sb.reload();
			if (setting.sb) {
				for (Player p : TheAPI.getOnlinePlayers())
					Utils.ScoreboardStats.createScoreboard(p);
			}
			TheAPI.msg(Loader.s("Prefix") + Loader.s("ConfigReloaded"), s);
			return true;
		}
		return true;
	}

}

package Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;

public class ScoreboardStats implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "ScoreBoard", "Other")) {
			TheAPI.msg(setting.prefix+"Reloading Scoreboard..", s);
			TheAPI.msg("", s);
			Utils.ScoreboardStats.removeScoreboard();
			Loader.sb.reload();
			if (setting.sb) {
				for (Player p : TheAPI.getOnlinePlayers())
					Utils.ScoreboardStats.createScoreboard(p);
			}
			TheAPI.msg(setting.prefix+"Scoreboard reloaded", s);
			return true;
		}
		Loader.noPerms(s, "ScoreBoard", "Other");
		return true;
	}

}

package Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

public class ScoreboardStats implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s,"ServerCotrol.Scoreboard")) {
			Loader.msg(Loader.s("Prefix")+ ChatColor.YELLOW+"----------------- "+ChatColor.DARK_AQUA+"Scoreboard Reload"+ChatColor.YELLOW+" -----------------",s);
			    Loader.msg("",s);
				Configs.ScoreboardLoading();
				Utils.ScoreboardStats.removeScoreboard();
				if(Loader.scFile.getBoolean("Scoreboard-Enabled")==true) {
				for(Player p : Bukkit.getOnlinePlayers())
					Utils.ScoreboardStats.createScoreboard(p);}
				Loader.msg(Loader.s("Prefix")+Loader.s("ConfigReloaded"),s);
				return true;
		}
		return true;
	}

}

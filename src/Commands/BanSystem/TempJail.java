package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;

public class TempJail implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Jail")) {
			if(args.length==0) {
				Loader.Help(s, "/TempJail <player> <time> <reason>", "BanSystem.Jail");
				return true;
			}
			
			
			
		}
		return true;
	}

}

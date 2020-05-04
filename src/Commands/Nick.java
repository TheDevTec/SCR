package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Nick implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(s instanceof Player) {
			if(API.hasPerm(s, "ServerControl.Nickname")){
			if(args.length==0) {
				Loader.Help(s, "/Nick <nickname>", "Nick");
				return true;
			}
			String msg = TheAPI.buildString(args);
			TheAPI.getUser(s.getName()).setAndSave("DisplayName", msg);
			 Loader.msg(Loader.s("Prefix")+Loader.s("NicknameChanged").replace("%nick%", msg).replace("%nickname%", msg), s);
			return true;
		}return true;
		}
		Loader.msg(Loader.s("ConsoleErrorMessage"), s);
		return true;
	}

}

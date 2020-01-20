package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

public class Nick implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(s instanceof Player) {
			if(API.hasPerm(s, "ServerControl.Nick")){
			if(args.length==0) {
				Loader.Help(s, "/Nick <nickname>", "Nick");
				return true;
			}
			String msg = "";
			for(int i = 0; i<args.length; ++i) {
				msg=msg+args[i]+" ";
			}
			msg=msg.substring(0,msg.length()-1);
			Loader.me.set("Players."+s.getName()+".DisplayName", msg);
			 Configs.chatme.save();
			 Loader.msg(Loader.s("Prefix")+Loader.s("NicknameChanged").replace("%nick%", msg).replace("%nickname%", msg), s);
			return true;
		}return true;
		}
		Loader.msg(Loader.s("ConsoleErrorMessage"), s);
		return true;
	}

}

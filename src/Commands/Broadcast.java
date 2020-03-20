package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import Commands.BanSystem.BanSystem;
import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Broadcast implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Broadcast")){
			if(args.length==0) { 
				Loader.Help(s, "/Broadcast <message>", "Broadcast");
				return true;
			}
			if(args.length>=1) {
				String msg = TheAPI.buildString(args);
				TheAPI.broadcastMessage(Loader.config.getString("Format.Broadcast").replace("%sender%", BanSystem.getName(s.getName()))
						.replace("%message%", msg));
				return true;
			}}
		return true;
	}

}

package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;

public class Sudo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Sudo")) {
		if(args.length==0) {
			Loader.Help(s, "/Sudo <player> <arguments>", "Sudo");
			return true;
		}
		if(args.length==1) {
			Player target = Bukkit.getPlayer(args[0]);
			if(target !=null) {
			Loader.Help(s, "/Sudo "+target.getName()+" <arguments>", "Sudo");
			return true;
		}
			Loader.Help(s, "/Sudo <player> <arguments>", "Sudo");
			return true;
		}
		if(args.length>=2) {
			Player target = Bukkit.getPlayer(args[0]);
			if(target !=null) {
			if(args[1].startsWith("/")) {
				String command = "";
				for (int i = 0; i < args.length; i++) {
					command = command + args[i]+" ";
				}
				command=command.replaceFirst(args[0]+" /", "");
				TheAPI.sudo(target, SudoType.COMMAND, command);
				String st = API.replacePlayerName(Loader.s("Sudo.SendCommand"),target);
				Loader.msg(Loader.s("Prefix")+st.replace("%command%", command.substring(0,command.length()-1)), s);
				return true;
			}else {
				String message = "";
				for (int i = 0; i < args.length; i++) {
					message = message + args[i]+" ";
				}
				message=message.replaceFirst(args[0]+" ", "");
				TheAPI.sudo(target, SudoType.CHAT, message);
				String st = API.replacePlayerName(Loader.s("Sudo.SendMessage"),target);
				Loader.msg(Loader.s("Prefix")+st.replace("%message%", message.substring(0,message.length()-1)), s);
				return true;
			}
			}
			Loader.msg(Loader.PlayerNotOnline(args[0]),s);
			return true;
		}
	}return true;

	}
}

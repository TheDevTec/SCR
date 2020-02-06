package Commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;

public class Mail implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String label, String[] args) {
		
			if(args.length == 0) {
				if(API.hasPerm(s, "ServerControl.Mail.Send")) Loader.Help(s, "/Mail send <player> <text>", "Mail.Send");
				if(API.hasPerm(s, "ServerControl.Mail.Read")) Loader.Help(s, "/Mail clear", "Mail.Clear");
				if(API.hasPerm(s, "ServerControl.Mail.Read")) Loader.Help(s, "/Mail read", "Mail.Read");
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("Send")&&API.hasPerm(s, "ServerControl.Mail.Send")) {
					Loader.Help(s, "/Mail send <player> <text>", "Mail.Send");
					return true;
					}
				if(args[0].equalsIgnoreCase("Read")&&API.hasPerm(s, "ServerControl.Mail.Read")) {
					
					return true;
				}
				if(args[0].equalsIgnoreCase("Clear")&&API.hasPerm(s, "ServerControl.Mail.Read")) {
					
					return true;
				}
			}
			if(args.length > 2 && args[0].equalsIgnoreCase("Send")&&API.hasPerm(s, "ServerControl.Mail.Send")) {
				if(Loader.me.getString("Players."+args[1])==null) {
            		Loader.msg(Loader.PlayerNotEx(args[1]),s);
            		return true;
            	}
				String msg = "";
				for (int i = 3; i < args.length; ++i) {
                msg = String.valueOf(msg) + args[i] + " ";
				}
				msg=msg.substring(0,msg.length()-1);
				//List<String> mails = Loader.me.getStringList("Players."+args[1]+"Mails");
				
			}
		return true;
	}

}

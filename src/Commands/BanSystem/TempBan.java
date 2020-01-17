package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class TempBan implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.TempBan")) {
		if(args.length==0) {
			Loader.Help(s, "/TempBan <player> <time> <reason>", "BanSystem.TempBan");
			return true;
		}
		if(args.length==1) {
				if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "TempBan").replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempBan.Reason");
				TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
				TheAPI.getPunishmentAPI().setTempBan(args[0], msg,TheAPI.getTimeConventorAPI().getTimeFromString(Loader.config.getString("BanSystem.TempBan.Time")));
				return true;
			
		}
		if(args.length==2) {
				if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "TempBan").replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempBan.Reason");
				TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
				TheAPI.getPunishmentAPI().setTempBan(args[0], msg,TheAPI.getTimeConventorAPI().getTimeFromString(args[1]));
				return true;
		}
		if(args.length>=3) {
				if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "TempBan").replace("%target%", args[0]), s);
					return true;
				}
				//String msg = TheAPI.buildString(args);
				//msg=msg.replaceFirst(" "+args[0]+" "+args[1],"");
				/*String msg = "";
				  for (int i = 2; i < args.length; ++i) {
                    msg = String.valueOf(msg) + args[i] + " ";
                }
                msg=msg.substring(0,msg.length()-1);*/
                String msg = BanSystem.BuildString(2, 1, args);
			TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
			TheAPI.getPunishmentAPI().setTempBan(args[0], msg,TheAPI.getTimeConventorAPI().getTimeFromString(args[1]));
			return true;
		}}
		return true;
	}
}

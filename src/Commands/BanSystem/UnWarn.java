package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class UnWarn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.unWarn")) {
			if(args.length==0) {
				Loader.Help(s, "/unWarn <player>", "BanSystem.unWarn");
				return true;
			}
			if(args.length==1) {
				if(Loader.me.getString("Players."+args[0])==null) {
					Loader.msg(Loader.PlayerNotEx(args[0]), s);
					return true;
				}
				
				if(Loader.ban.getString("Warn."+args[0])==null) {
					Loader.msg(Loader.s("BanSystem.NotWarned").replace("%player%", args[0])
							.replace("%playername%", BanSystem.getName(args[0])), s);
					return true;
				}

				Loader.ban.set("Wan."+args[0]+".Amount", Loader.ban.getInt("Wan."+args[0]+".Amount")-1);
				Loader.ban.set("Wan."+args[0]+".WarnLater.Wait", false);
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.unWarned").replace("%player%", args[0])
						.replace("%playername%", BanSystem.getName(args[0])), s);
				TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.UnWarn")
						.replace("%operator%", s.getName()).replace("%player%", args[0])
						.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.UnWarn");
				return true;
			}
		}
		return true;
	}

}

package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import Commands.BanSystem.BanSystem;
import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

public class TpaBlock implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.TpaBlock")) {
			if(s instanceof Player) {
			if(args.length==0) {
				if(Loader.me.getBoolean("Players."+s.getName()+".TpBlock-Global")==false) {
					Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpaBlock.Blocked-Global")
					.replace("%player%", "All").replace("%playername%", "All"), s);
					Loader.me.set("Players."+s.getName()+".TpBlock-Global", true);
					Configs.chatme.save();return true;
				}else {
					Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpaBlock.UnBlocked-Global")
					.replace("%player%", "All").replace("%playername%", "All"), s);
					Loader.me.set("Players."+s.getName()+".TpBlock-Global", null);
					Configs.chatme.save();return true;
				}
			}
			if(args.length==1) {
				String p = Loader.me.getString("Players."+args[0]);
				if(p!=null) {
					if(s.getName()!=args[0]) {
					if(Loader.me.getBoolean("Players."+s.getName()+".TpBlock."+args[0])==false) {
					Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpaBlock.Blocked")
					.replace("%player%", args[0]).replace("%playername%", BanSystem.getName(args[0])), s);
					Configs.chatme.save();Loader.me.set("Players."+s.getName()+".TpBlock."+args[0], true);
					return true;
					}else {
						Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpaBlock.UnBlocked")
						.replace("%player%", args[0]).replace("%playername%", BanSystem.getName(args[0])), s);
						Configs.chatme.save();Loader.me.set("Players."+s.getName()+".TpBlock."+args[0], null);
						return true;
					}
					}
					Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.CantBlockSelf")
					.replace("%playername%", ((Player) s).getDisplayName())
					.replace("%player%", s.getName()), s);
				return true;	
				}
				Loader.msg(Loader.PlayerNotEx(args[0]),s);
				return true;
			}
			return true;
			}
			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
			
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
			if(args.length==1)
			return null;
		return c;
	}
}

package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

public class NickReset implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		
			if(API.hasPerm(s, "ServerControl.NickReset")){
			if(args.length==0) {
				if(s instanceof Player) {
				Loader.me.set("Players."+s.getName()+".DisplayName", null);
				 Configs.chatme.save();Loader.msg(Loader.s("Prefix")+Loader.s("NicknameReseted"), s);
				return true;
				}
				Loader.Help(s, "/NickReset <player>", "NickReset");
				return true;
			}
		String a = args[0];
		if(Loader.me.getString("Players."+a)==null) {
			Loader.msg(Loader.PlayerNotEx(a), s);
		return true;
		}
		Loader.me.set("Players."+a+".DisplayName", null);
		 Configs.chatme.save();Loader.msg(Loader.s("Prefix")+Loader.s("NicknameResetedOther").replace("%player%", a).replace("%playername%", a), s);
		return true;
	}return true;
	
}}

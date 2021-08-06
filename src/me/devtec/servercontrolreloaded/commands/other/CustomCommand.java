package me.devtec.servercontrolreloaded.commands.other;

import java.util.Collection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class CustomCommand implements CommandExecutor {
	
	String d;
	public CustomCommand(String c) {
		d=c;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.customCmds.getString(d+".Permission")==null||Loader.customCmds.getString(d+".Permission").trim().equals("")||s.hasPermission(Loader.customCmds.getString(d+".Permission"))) {
			if(!CommandsManager.canUse("CustomCommand."+d, s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("CustomCommand."+d, s))));
		    	return true;
			}
			if(args.length==0 && s instanceof Player) {
			Object o = Loader.customCmds.get(d+".Messages");
			if(o!=null) {
			if(o instanceof Collection) {
				for(Object d : (Collection<?>)o)
					TheAPI.msg(TabList.replace(d+"", (Player)s, false), s);
			}else
				if(!(o+"").isEmpty())
			TheAPI.msg(TabList.replace(o+"", (Player)s, false), s);
			}
			o = Loader.customCmds.get(d+".Commands");
			if(o!=null) {
			if(o instanceof Collection) {
				for(Object d : (Collection<?>)o)
					TheAPI.sudoConsole(TabList.replace(d+"", (Player)s, true));
			}else
				if(!(o+"").isEmpty())
			TheAPI.sudoConsole(TabList.replace(o+"", (Player)s, true));
			}
			return true;
			}
			if(s.hasPermission("scr.customcommand.other")) {
				Player target = TheAPI.getPlayer(args[0]);
				if(target==null) {
					Loader.notOnline(s, args[0]);
					return true;
				}
				Object o = Loader.customCmds.get(d+".Messages");
				if(o!=null) {
				if(o instanceof Collection) {
					for(Object d : (Collection<?>)o)
						TheAPI.msg(TabList.replace(d+"", target, false), s);
				}else
					if(!(o+"").isEmpty())
				TheAPI.msg(TabList.replace(o+"", target, false), s);
				}
				o = Loader.customCmds.get(d+".Commands");
				if(o!=null) {
				if(o instanceof Collection) {
					for(Object d : (Collection<?>)o)
						TheAPI.sudoConsole(TabList.replace(d+"", target, true));
				}else
					if(!(o+"").isEmpty())
				TheAPI.sudoConsole(TabList.replace(o+"", target, true));
				}
			}
			return true;
		}
		Loader.sendMessages(s, "NoPerms", Placeholder.c().add("%permission%", Loader.customCmds.getString(d+".Permission")));
		return true;
	}

}

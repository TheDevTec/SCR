package me.devtec.servercontrolreloaded.commands.message;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;

public class SocialSpy implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "SocialSpy", "Message")) {
			if(TheAPI.getUser((Player)s).getBoolean("socialspy"))
				Loader.sendMessages(s, "SocialSpy.Off");
			else
				Loader.sendMessages(s, "SocialSpy.On");
			TheAPI.getUser((Player)s).setSave("socialspy", !TheAPI.getUser((Player)s).getBoolean("socialspy"));
			return true;
		}
		Loader.noPerms(s, "SocialSpy", "Message");
		return true;
	}
	
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Arrays.asList();
	}
}

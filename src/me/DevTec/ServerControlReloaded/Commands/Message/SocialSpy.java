package me.DevTec.ServerControlReloaded.Commands.Message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;

public class SocialSpy implements CommandExecutor {

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

}

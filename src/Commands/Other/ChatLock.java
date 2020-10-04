package Commands.Other;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;

public class ChatLock implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (API.hasPerm(s, "ServerControl.ChatLock")) {
			if (!setting.lock_chat) {
				TheAPI.broadcastMessage(Loader.s("Prefix") + Loader.s("ChatLock.ChatIsLocked"));
				Loader.config.set("Options.ChatLock", true);
				setting.lock_chat = true;
				return true;
			}
			TheAPI.broadcastMessage(Loader.s("Prefix") + Loader.s("ChatLock.ChatIsUnlocked"));
			Loader.config.set("Options.ChatLock", false);
			setting.lock_chat = false;
			return true;
		}
		return true;
	}
}

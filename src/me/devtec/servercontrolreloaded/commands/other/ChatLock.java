package me.devtec.servercontrolreloaded.commands.other;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.utils.StringUtils;

public class ChatLock implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "ChatLock", "Other")) {
			if(!CommandsManager.canUse("Other.ChatLock", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.ChatLock", s))));
				return true;
			}
			if (!setting.lock_chat) {
				Loader.sendBroadcasts(s, "ChatLock.Lock");
				Loader.config.set("Options.ChatLock", true);
				Loader.config.save();
				setting.lock_chat = true;
				return true;
			}
			Loader.sendBroadcasts(s, "ChatLock.Unlock");
			Loader.config.set("Options.ChatLock", false);
			Loader.config.save();
			setting.lock_chat = false;
			return true;
		}
		Loader.noPerms(s, "ChatLock", "Other");
		return true;
	}
}

package me.devtec.servercontrolreloaded.commands.message;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.utils.StringUtils;

public class Sudo implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Sudo", "Message")) {
			if(!CommandsManager.canUse("Message.Sudo", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Message.Sudo", s))));
				return true;
			}
			if (args.length <= 1) {
				Loader.Help(s, "Sudo", "Message");
				return true;
			}
			Player target = TheAPI.getPlayer(args[0]);
			if (target != null) {
				String msg = StringUtils.buildString(1, args);
				if (msg.startsWith("/")) {
					msg = msg.replaceFirst("/", "");
					TheAPI.sudo(target, SudoType.COMMAND, msg);
					Loader.sendMessages(s, "Sudo.Command", Placeholder.c().add("%command%", msg).add("%player%", target.getName())
							.add("%playername%", target.getDisplayName()));
					return true;
				}
				TheAPI.sudo(target, SudoType.CHAT, msg);
				Loader.sendMessages(s, "Sudo.Message", Placeholder.c().add("%message%", msg).add("%player%", target.getName())
						.add("%playername%", target.getDisplayName()));
				return true;
			}
			Loader.notOnline(s, args[0]);
			return true;
		}
		Loader.noPerms(s, "Sudo", "Message");
		return true;
	}
	
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Sudo", "Message"))
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		return Collections.emptyList();
	}
}

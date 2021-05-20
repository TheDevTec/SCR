package me.devtec.servercontrolreloaded.commands.message;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
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
import me.devtec.theapi.utils.StringUtils;

public class ClearChat implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (s instanceof Player == false) {
			if (args.length == 0) {
				for (Player online : TheAPI.getOnlinePlayers())
					if (!Loader.has(online, "ClearChat", "Message", "Bypass"))
						for (int i = 0; i < 250; i++) {
							TheAPI.msg("", online);
						}
				Loader.sendBroadcasts(s, "ClearChat.Cleared.Console");
				return true;
			}
			else {
			Player target = TheAPI.getPlayer(args[0]);
			Bukkit.broadcastMessage(args[0]+" ; "+target);
			if (target == null) {
				Loader.notOnline(s, args[0]);
				return true;
			}
			if (!Loader.has(target, "ClearChat", "Message", "Bypass")) {
				for (int i = 0; i < 250; i++) {
					TheAPI.msg("", target);
				}
				Loader.sendMessages(s, "ClearChat.Specific", Placeholder.c().add("%player%", target.getName()));
				Loader.sendMessages(target, "ClearChat.Cleared.Console");
				return true;
			}
			Loader.sendMessages(s, "ClearChat.SpecificChatHaveBypass", Placeholder.c().add("%player%", target.getName()));
			return true;
			}
		}
		if (Loader.has(s, "ClearChat", "Message")) {
			if(!CommandsManager.canUse("Message.ClearChat", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Message.ClearChat", s))));
				return true;
			}
			Player p = (Player) s;
			if (args.length == 0) {
				for (Player online : TheAPI.getOnlinePlayers()) {
					if (!Loader.has(online, "ClearChat", "Message", "Bypass"))
						for (int i = 0; i < 250; i++) {
							TheAPI.msg("", online);
						}
				}
				Loader.sendBroadcasts(p, "ClearChat.Cleared.Player", Placeholder.c().add("%player%", p.getName()));
				return true; 
			}
			Player target = TheAPI.getPlayer(args[0]);
				if (target == null) {
					Loader.notOnline(s, args[0]);
					return true;
				}
				if (target == s) {
					Loader.sendMessages(p, "ClearChat.NoClearOwnChat", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				if (!Loader.has(target, "ClearChat", "Message", "Bypass")) {
					for (int i = 0; i < 250; i++) {
						TheAPI.msg("", target);
					}
					Loader.sendMessages(s, "ClearChat.Specific", Placeholder.c().add("%player%", target.getName()));
					Loader.sendMessages(target, "ClearChat.Cleared.Player", Placeholder.c().add("%player%", p.getName()));
					return true;
				}
				Loader.sendMessages(s, "ClearChat.SpecificChatHaveBypass", Placeholder.c().add("%player%", target.getName()));
				return true;
		}
		Loader.noPerms(s, "ClearChat", "Message");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "ClearChat", "Message"))
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

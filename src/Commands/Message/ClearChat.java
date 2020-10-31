package Commands.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;

public class ClearChat implements CommandExecutor {

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
		if (s instanceof Player == true) {
			Player p = (Player) s;
			if (args.length == 0) {
				if (Loader.has(s, "ClearChat", "Message")) {
					for (Player online : TheAPI.getOnlinePlayers()) {
						if (!Loader.has(online, "ClearChat", "Message", "Bypass"))
							for (int i = 0; i < 250; i++) {
								TheAPI.msg("", online);
							}
					}
					Loader.sendBroadcasts(p, "ClearChat.Cleared.Player", Placeholder.c().add("%player%", p.getName()));
					return true; 
				}
				Loader.noPerms(s, "ClearChat", "Message");
				return true;
			}
			if (Loader.has(s, "ClearChat", "Message")) {
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
		return true;
	}
}
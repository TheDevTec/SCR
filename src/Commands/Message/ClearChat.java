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
				for (int i = 0; i < 250; i++) {
					for (Player online : TheAPI.getOnlinePlayers()) {
						if (!online.hasPermission("ServerControl.ClearChat.Bypass")) {
							TheAPI.msg("", online);
						}
					}
				}
				Loader.sendBroadcasts(s, "ClearChat.Cleared.Console");
			}
			if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == s) {
					Loader.sendMessages(s, "ClearChat.NoClearOwnChat", Placeholder.c().add("%player%", args[0]));
				}
				if (target != s) {
					if (target == null) {
						Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c().add("%player%", target.getName()).add("%playername%", target.getName()));
					}
					if (target != null) {
						if (!target.hasPermission("ServerControl.ClearChat.Bypass")) {
							for (int i = 0; i < 250; i++) {
								TheAPI.msg("", target);
							}
							Loader.sendMessages(s, "ClearChat.Specific", Placeholder.c().add("%player%", target.getName()));
							Loader.sendMessages(target, "ClearChat.Cleared.Console");
						} else if (target.hasPermission("ServerControl.ClearChat.Bypass")) {
							Loader.sendMessages(s, "ClearChat.SpecificChatHaveBypass", Placeholder.c().add("%player%", target.getName()));
						}
					}
				}
			}
		}
		if (s instanceof Player == true) {
			Player p = (Player) s;
			if (args.length == 0) {
				if (Loader.has(s, "ClearChat", "Message")) {
					for (int i = 0; i < 250; i++) {
						for (Player online : TheAPI.getOnlinePlayers()) {

							if (!online.hasPermission("ServerControl.ClearChat.Bypass")) {

								TheAPI.msg("", online);
							}
						}
					}
					Loader.sendBroadcasts(p, "ClarChat.Cleared.Player", Placeholder.c().add("%player%", p.getName()));
				}
				return true;
			}
			if (args.length == 1) {

				if (Loader.has(s, "ClearChat", "Message")) {
					Player target = Bukkit.getServer().getPlayer(args[0]);
					if (target == s) {
								Loader.sendMessages(p, "ClearChat.NoClearOwnChat", Placeholder.c().add("%player%", args[0]));
						return true;
					}
					if (target != s) {
						if (target == null) {
							Loader.sendMessages(s, "Missing.Player.Offline", Placeholder.c().add("%player%", target.getName()).add("%playername%", target.getName()));
							return true;
						}
						if (target != null) {
							if (!target.hasPermission("ServerControl.ClearChat.Bypass")) {
								for (int i = 0; i < 250; i++) {
									TheAPI.msg("", target);
								}
								Loader.sendMessages(s, "ClearChat.Specific", Placeholder.c().add("%player%", target.getName()));
								Loader.sendMessages(target, "ClearChat.Cleared.Player", Placeholder.c().add("%player%", p.getName()));
								return true;
							} else if (target.hasPermission("ServerControl.ClearChat.Bypass")) {
								Loader.sendMessages(s, "ClearChat.SpecificChatHaveBypass", Placeholder.c().add("%player%", target.getName()));
								return true;
							}
						}
					}
				}
				return true;
			}
		}
		return true;
	}
}
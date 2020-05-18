package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class ClearChat implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (s instanceof Player == false) {
			if (args.length == 0) {
				for (int i = 0; i < 250; i++) {
					for (Player online : TheAPI.getOnlinePlayers()) {
						if (!online.hasPermission("ServerControl.ClearChat.Bypass")) {
							Loader.msg("", online);
						}
					}
				}
				TheAPI.broadcastMessage(Loader.s("Prefix") + Loader.s("ClearChat.ByConsole"));
			}
			if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == s) {
					Loader.msg(Loader.s("Prefix") + Loader.s("ClearChat.NoClearOwnChat").replace("%player%", args[0]),
							s);
				}
				if (target != s) {
					if (target == null) {
						Loader.msg(Loader.PlayerNotOnline(args[0]), s);
					}
					if (target != null) {
						if (!target.hasPermission("ServerControl.ClearChat.Bypass")) {
							for (int i = 0; i < 250; i++) {
								Loader.msg("", target);
							}
							Loader.msg(Loader.s("ClearChat.SpecifedChatCleared").replace("%player%", target.getName()),
									s);
							Loader.msg(Loader.s("Prefix") + Loader.s("ClearChat.ByConsole"), target);
						} else if (target.hasPermission("ServerControl.ClearChat.Bypass")) {
							Loader.msg(
									Loader.s("ClearChat.SpecifedChatHaveBypass").replace("%player%", target.getName()),
									s);
						}
					}
				}
			}
		}
		if (s instanceof Player == true) {
			Player p = (Player) s;
			if (args.length == 0) {
				if (API.hasPerm(s, "ServerControl.ClearChat")) {
					for (int i = 0; i < 250; i++) {
						for (Player online : TheAPI.getOnlinePlayers()) {

							if (!online.hasPermission("ServerControl.ClearChat.Bypass")) {

								Loader.msg("", online);
							}
						}
					}
					TheAPI.broadcastMessage(Loader.s("Prefix") + Loader.s("ClearChat.ByPlayer")
							.replace("%playername%", p.getDisplayName()).replace("%player%", p.getName()));
				}
				return true;
			}
			if (args.length == 1) {

				if (API.hasPerm(s, "ServerControl.ClearChat")) {
					Player target = Bukkit.getServer().getPlayer(args[0]);
					if (target == s) {
						Loader.msg(
								Loader.s("Prefix") + Loader.s("ClearChat.NoClearOwnChat").replace("%player%", args[0]),
								s);
						return true;
					}
					if (target != s) {
						if (target == null) {
							Loader.msg(Loader.PlayerNotOnline(args[0]), s);
							return true;
						}
						if (target != null) {
							if (!target.hasPermission("ServerControl.ClearChat.Bypass")) {
								for (int i = 0; i < 250; i++) {
									Loader.msg("", target);
								}
								Loader.msg(
										Loader.s("ClearChat.SpecifedChatCleared").replace("%player%", target.getName())
												.replace("%playername%", target.getDisplayName()),
										s);
								Loader.msg(Loader.s("Prefix") + Loader.s("ClearChat.ByPlayer")
										.replace("%playername%", p.getDisplayName()).replace("%player%", p.getName()),
										target);
								return true;
							} else if (target.hasPermission("ServerControl.ClearChat.Bypass")) {
								Loader.msg(Loader.s("ClearChat.SpecifedChatHaveBypass")
										.replace("%player%", target.getName())
										.replace("%playername%", target.getDisplayName()), s);
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
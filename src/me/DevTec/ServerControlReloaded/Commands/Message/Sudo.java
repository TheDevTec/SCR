package me.DevTec.ServerControlReloaded.Commands.Message;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.utils.StringUtils;

public class Sudo implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Sudo", "Message")) {
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
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}

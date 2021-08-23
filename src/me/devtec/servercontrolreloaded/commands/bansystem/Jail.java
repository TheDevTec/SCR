
package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.utils.StringUtils;

public class Jail implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Jail", "BanSystem")) {
			if(!CommandsManager.canUse("BanSystem.Jail", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("BanSystem.Jail", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "Jail", "BanSystem");
				return true;
			}
			if (args.length == 1) {
				if (!PunishmentAPI.getjails().isEmpty()) {
					if (TheAPI.getUser(args[0]).getBoolean("Immune")
							|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
						return true;
					}
					PunishmentAPI.jail(args[0], Loader.config.getString("BanSystem.Jail.Text").replace("%reason%", Loader.config.getString("BanSystem.Jail.Reason")));
					Loader.sendMessages(s, "BanSystem.Jail.Sender", Placeholder.c().replace("%operator%", s.getName())
							.replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.Jail.Reason")));
					Loader.sendBroadcasts(s, "BanSystem.Jail.Admins", Placeholder.c().replace("%operator%", s.getName())
							.replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.Jail.Reason")));
					return true;
				}
				Loader.sendMessages(s, "Jail.Empty");
				return true;
			}
			if (!PunishmentAPI.getjails().isEmpty()) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				String msg = StringUtils.buildString(1, args);
				if(msg.endsWith("-s")||msg.endsWith("- s")) {
					msg = msg.endsWith("- s")?msg.substring(0, msg.length()-3):msg.substring(0, msg.length()-2);
					PunishmentAPI.jail(args[0], Loader.config.getString("BanSystem.Jail.Text").replace("%reason%", msg));
					Loader.sendMessages(s, "BanSystem.Jail.Sender", Placeholder.c().replace("%operator%", s.getName())
							.replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]"));
					Loader.sendBroadcasts(s, "BanSystem.Jail.Admins", Placeholder.c().replace("%operator%", s.getName())
							.replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]"));
					return true;
				}
				PunishmentAPI.jail(args[0], Loader.config.getString("BanSystem.Jail.Text").replace("%reason%", msg));
				Loader.sendMessages(s, "BanSystem.Jail.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%player%", args[0]).replace("%reason%", msg));
				Loader.sendBroadcasts(s, "BanSystem.Jail.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%player%", args[0]).replace("%reason%", msg));
				return true;
			}
			Loader.sendMessages(s, "Jail.Empty");
			return true;
		}
		Loader.noPerms(s, "Jail", "BanSystem");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "Jail", "BanSystem"))
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		return Collections.emptyList();
	}
}

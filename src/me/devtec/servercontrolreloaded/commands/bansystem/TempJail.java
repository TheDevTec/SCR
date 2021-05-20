package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Arrays;
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

public class TempJail implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "TempJail", "BanSystem")) {
			if(args.length==2) {
				try {
					if(args[1].substring(args[1].length()-1, args[1].length()).matches("[0-9]"))
						return Arrays.asList(args[1]+"s",args[1]+"m",args[1]+"h",args[1]+"d",args[1]+"w",args[1]+"mo");
				}catch(Exception e) {
					return Arrays.asList("15m","2h","2h30m","6h","12h","7d");
				}
			}
			return StringUtils.copyPartialMatches(args[args.length-1], API.getPlayerNames(s));
		}
		return Arrays.asList();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		if (Loader.has(s, "TempJail", "BanSystem")) {
			if(!CommandsManager.canUse("BanSystem.TempJail", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("BanSystem.TempJail", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "TempJail", "BanSystem");
				return true;
			}
			if (args.length == 1) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempjail(args[0], Loader.config.getString("BanSystem.TempJail.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempJail.Reason")), StringUtils.getTimeFromString(Loader.config.getString("BanSystem.TempJail.Time")));
				Loader.sendMessages(s, "BanSystem.TempJail.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempJail.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(Loader.config.getString("BanSystem.TempJail.Time")))));
				Loader.sendBroadcasts(s, "BanSystem.TempJail.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempJail.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(Loader.config.getString("BanSystem.TempJail.Time")))));
				return true;
			}
			if (args.length == 2) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")
						|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
					return true;
				}
				PunishmentAPI.tempjail(args[0], Loader.config.getString("BanSystem.TempJail.Text").replace("%reason%",
						Loader.config.getString("BanSystem.TempJail.Reason")), StringUtils.getTimeFromString(args[1]));
				Loader.sendMessages(s, "BanSystem.TempJail.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempJail.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
				Loader.sendBroadcasts(s, "BanSystem.TempJail.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", Loader.config.getString("BanSystem.TempJail.Reason")).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
				return true;
			}
			if (TheAPI.getUser(args[0]).getBoolean("Immune")
					|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
				Loader.sendMessages(s, "Immune.NoPunish", Placeholder.c().add("%player%", args[0]));
				return true;
			}
			String msg = StringUtils.buildString(2, args);
			if(msg.endsWith("-s")||msg.endsWith("- s")) {
				msg = msg.endsWith("- s")?msg.substring(0, msg.length()-3):msg.substring(0, msg.length()-2);
				PunishmentAPI.tempjail(args[0], Loader.config.getString("BanSystem.TempJail.Text").replace("%reason%",msg), StringUtils.timeFromString(args[1]));
				Loader.sendMessages(s, "BanSystem.TempJail.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
				Loader.sendBroadcasts(s, "BanSystem.TempJail.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg+" &f[Silent]").replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))), "servercontrol.silent");
				return true;
			}
			PunishmentAPI.tempjail(args[0], Loader.config.getString("BanSystem.TempJail.Text").replace("%reason%",msg), StringUtils.timeFromString(args[1]));
			Loader.sendMessages(s, "BanSystem.TempJail.Sender", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
			Loader.sendBroadcasts(s, "BanSystem.TempJail.Admins", Placeholder.c().replace("%operator%", s.getName())
					.replace("%playername%", args[0]).replace("%player%", args[0]).replace("%reason%", msg).replace("%time%", StringUtils.timeToString(StringUtils.timeFromString(args[1]))));
			return true;
		}
		Loader.noPerms(s, "TempJail", "BanSystem");
		return true;
	}
}

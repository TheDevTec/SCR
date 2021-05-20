package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.punishmentapi.BanList;
import me.devtec.theapi.punishmentapi.PlayerBanList;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.utils.StringUtils;

public class UnMute implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "UnMute", "BanSystem")) {
			List<String> jail = BanList.getMuted();
			jail.addAll(BanList.getTempMuted());
			return StringUtils.copyPartialMatches(args[0], jail);
		}
		return Arrays.asList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "UnMute", "BanSystem")) {
			if(!CommandsManager.canUse("BanSystem.UnMute", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("BanSystem.UnMute", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "UnMute", "BanSystem");
				return true;
			}
			PlayerBanList p = PunishmentAPI.getBanList(args[0]);
			if (p.isMuted() || p.isTempMuted()) {
				PunishmentAPI.unmute(args[0]);
				Loader.sendMessages(s, "BanSystem.UnMute.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				Loader.sendBroadcasts(s, "BanSystem.UnMute.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				return true;
			}
			Loader.sendMessages(s, "BanSystem.Not.Muted", Placeholder.c().replace("%ip%", args[0]).replace("%player%", args[0]));
			return true;
		}
		Loader.noPerms(s, "UnMute", "BanSystem");
		return true;
	}

}

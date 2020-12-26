package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class TempFly implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "TempFly", "Other")) {
			if (args.length == 0) {
				Loader.Help(s, "TempFly", "Other");
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					API.getSPlayer(TheAPI.getPlayer(s.getName()))
							.enableTempFly(StringUtils.getTimeFromString(args[0]));
					return true;
				}
				Loader.Help(s, "TempFly", "Other");
				return true;
			}
			Player player = TheAPI.getPlayer(args[0]);
			if (player == null) {
				Loader.notOnline(s,args[0]);
				return true;
			}
			SPlayer t = API.getSPlayer(player);
			long sec = StringUtils.getTimeFromString(args[1]);
			if (t.getPlayer() == s) {
				t.enableTempFly(sec);
				return true;
			}
			if (Loader.has(s, "TempFly", "Other", "Other")) {
				Loader.sendMessages(s, "Fly.Temp.Enabled.Other.Sender", Placeholder.c().replace("%player%", player.getName())
						.replace("%playername%", player.getDisplayName()).replace("%time%", StringUtils.setTimeToString(sec)));
				Loader.sendMessages(player, "Fly.Temp.Enabled.Other.Receiver", Placeholder.c().replace("%player%", s.getName())
						.replace("%playername%", s.getName()).replace("%time%", StringUtils.setTimeToString(sec)));
				t.enableTempFly(sec);
				return true;
			}
			Loader.noPerms(s, "TempFly", "Other", "Other");
			return true;
		}
		Loader.noPerms(s, "TempFly", "Other");
		return true;
	}
}

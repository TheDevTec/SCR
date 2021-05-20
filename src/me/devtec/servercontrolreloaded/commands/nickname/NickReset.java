package me.devtec.servercontrolreloaded.commands.nickname;

import java.util.Arrays;
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
import me.devtec.theapi.utils.StringUtils;

public class NickReset implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "NicknameReset", "Nickname")) {
			if(!CommandsManager.canUse("Nickname.NicknameReset", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Nickname.NicknameReset", s))));
				return true;
			}
			if (args.length == 0) {
				if (s instanceof Player) {
					TheAPI.getUser(s.getName()).setAndSave("DisplayName", null);
					if(TheAPI.getPlayerOrNull(s.getName())!=null)
						TheAPI.getPlayerOrNull(s.getName()).setCustomName(null);
					Loader.sendMessages(s, "Nickname.Reset", Placeholder.c()
							.add("%player%", s.getName())
							.add("%playername%", ((Player) s).getDisplayName()));
					return true;
				}
				Loader.Help(s, "NicknameReset", "Nickname");
				return true;
			}
			String a = args[0];
			if (!TheAPI.existsUser(a)) {
				Loader.notExist(s, a);
				return true;
			}
			TheAPI.getUser(s.getName()).setAndSave("DisplayName", null);
			if(TheAPI.getPlayerOrNull(a)!=null)
				TheAPI.getPlayerOrNull(a).setCustomName(null);
			Loader.sendMessages(s, "Nickname.Reset", Placeholder.c()
					.add("%player%", a)
					.add("%playername%", a));
			return true;
		}
		Loader.noPerms(s, "NicknameReset", "Nickname");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "NicknameReset", "Nickname") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

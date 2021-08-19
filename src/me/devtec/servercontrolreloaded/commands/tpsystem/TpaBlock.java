package me.devtec.servercontrolreloaded.commands.tpsystem;

import java.util.Arrays;
import java.util.Collections;
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

public class TpaBlock implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "TpToggle", "TpSystem") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpToggle", "TpSystem")) {
			if(!CommandsManager.canUse("TpSystem.TpToggle", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("TpSystem.TpToggle", s))));
				return true;
			}
			if (s instanceof Player) {
				if (args.length == 0) {
					boolean state = TheAPI.getUser((Player)s).getBoolean("TpBlock-Global");
					Loader.sendMessages(s, "TpSystem.Block.Global."+(state?"Off":"On"));
					TheAPI.getUser((Player)s).setAndSave("TpBlock-Global", !state);
					return true;
				}
				if (args.length == 1) {
					if (TheAPI.existsUser(args[0])) {
						if (s.getName().equals(args[0])) {
							boolean state = TheAPI.getUser((Player)s).getBoolean("TpBlock." + args[0]);
							Loader.sendMessages(s, "TpSystem.Block."+(state?"Remove":"Add"));
							TheAPI.getUser((Player)s).setAndSave("TpBlock." + args[0], !state);
							return true;
						}
						Loader.sendMessages(s, "TpSystem.Block.BlockSelf");
						return true;
					}
					Loader.notExist(s,args[0]);
					return true;
				}
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "TpToggle", "TpSystem");
		return true;
	}
}

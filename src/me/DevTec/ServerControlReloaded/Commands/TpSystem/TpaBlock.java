package me.DevTec.ServerControlReloaded.Commands.TpSystem;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class TpaBlock implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if (Loader.has(s, "TpaBlock", "TpSystem") && args.length == 1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "TpaBlock", "TpSystem")) {
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
		Loader.noPerms(s, "TpaBlock", "TpSystem");
		return true;
	}
}

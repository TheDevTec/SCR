package me.DevTec.ServerControlReloaded.Commands.BanSystem;

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

public class Immune implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String label, String[] args) {
		if (Loader.has(s, "Immune", "BanSystem")) {
			if (args.length == 0) {
				if (!(s instanceof Player)) {
					Loader.Help(s, "Immune", "BanSystem");
					return true;
				}
				Player p = (Player) s;
				boolean im = TheAPI.getUser(p).getBoolean("Immune");
				TheAPI.getUser(p).setAndSave("Immune", !im);
				Loader.sendMessages(s, "Immune." + (im ? "Disabled.You" : "Enabled.You"));
				return true;
			}
			if (Loader.has(s, "Immune", "BanSystem", "Other")) {
				boolean im = TheAPI.getUser(args[0]).getBoolean("Immune");
				TheAPI.getUser(args[0]).setAndSave("Immune", !im);
				String aw = "Immune." + (im ? "Disabled.Other." : "Enabled.Other.");
				Loader.sendMessages(s, aw+"Sender");
				if(TheAPI.getPlayerOrNull(args[0])!=null)
				Loader.sendMessages(TheAPI.getPlayerOrNull(args[0]), aw+"Receiver");
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "Immune", "BanSystem");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Immune", "BanSystem") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}
}

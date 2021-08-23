package me.devtec.servercontrolreloaded.commands.other;

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
import me.devtec.servercontrolreloaded.utils.HDBSupport;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.utils.StringUtils;

public class Skull implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s,"Skull","Other") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Skull", "Other")) {
			if(!CommandsManager.canUse("Other.Skull", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Skull", s))));
				return true;
			}
			if (args.length == 0) {
				if(s instanceof Player) {
					TheAPI.giveItem((Player) s, ItemCreatorAPI.createHead(1, "&7" + s.getName() + "'s Head", s.getName()));
					Loader.sendMessages(s, "Give.Skull.You", Placeholder.c().replace("%head%", s.getName()));
					return true;
				}
				Loader.Help(s, "Skull", "Other");
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player) {
					if(args[0].toLowerCase().startsWith("hdb:"))
						TheAPI.giveItem((Player) s, HDBSupport.parse(args[0]));
					else
					if(args[0].startsWith("https://")||args[0].startsWith("http://"))
						TheAPI.giveItem((Player) s, ItemCreatorAPI.createHeadByWeb(1, "&7Head from website", args[0]));
					else
					if(args[0].length()>16)
						TheAPI.giveItem((Player) s, ItemCreatorAPI.createHeadByValues(1, "&7Head from values", args[0]));
					else
					TheAPI.giveItem((Player) s, ItemCreatorAPI.createHead(1, "&7" + args[0] + "'s Head", args[0]));
					Loader.sendMessages(s, "Give.Skull.You", Placeholder.c().replace("%head%", args[0]));
					return true;
				}
				Loader.Help(s, "Skull", "Other");
				return true;
			}
			if (args.length == 2) {
				Player p = TheAPI.getPlayer(args[1]);
				if (p != null) {
					if(args[0].toLowerCase().startsWith("hdb:"))
						TheAPI.giveItem(p, HDBSupport.parse(args[0]));
					else
					if(args[0].startsWith("https://")||args[0].startsWith("http://"))
						TheAPI.giveItem(p, ItemCreatorAPI.createHeadByWeb(1, "&7Head from website", args[0]));
					else
					if(args[0].length()>16)
						TheAPI.giveItem(p, ItemCreatorAPI.createHeadByValues(1, "&7Head from values", args[0]));
					else
					TheAPI.giveItem(p, ItemCreatorAPI.createHead(1, "&7" + args[0] + "'s Head", args[0]));
					Loader.sendMessages(s, "Give.Skull.Other.Sender", Placeholder.c().replace("%head%", args[0]));
					Loader.sendMessages(p, "Give.Skull.Other.Receiver", Placeholder.c().replace("%head%", args[0]));
					return true;
				}
				Loader.notOnline(s, args[1]);
				return true;
			}
		}
		Loader.noPerms(s, "Skull", "Other");
		return true;
	}

}

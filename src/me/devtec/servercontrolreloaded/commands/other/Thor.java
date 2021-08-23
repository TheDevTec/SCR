package me.devtec.servercontrolreloaded.commands.other;

import java.util.Collections;
import java.util.List;

import org.bukkit.block.Block;
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
import me.devtec.theapi.blocksapi.BlocksAPI;
import me.devtec.theapi.utils.StringUtils;

public class Thor implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Thor", "Other") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Thor","Other")) {
			if(!CommandsManager.canUse("Other.Thor", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Thor", s))));
				return true;
			}
			if (args.length == 0) {
				if(s instanceof Player) {
				Player p2 = (Player) s;
				Block b = BlocksAPI.getLookingBlock(p2, 100);
				b.getWorld().strikeLightning(b.getLocation());
				Loader.sendMessages(s, "Thor.Block");
				return true;
				}
				Loader.Help(s, "Thor", "Other");
				return true;
			}
			Player p = TheAPI.getPlayer(args[0]);
			if (p != null) {
				p.getWorld().strikeLightning(p.getLocation());
				Loader.sendMessages(s, "Thor.Player", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName()));
				return true;
			}
			Loader.notOnline(s, args[0]);
			return true;
		}
		Loader.noPerms(s, "Thor", "Other");
		return true;
	}

}

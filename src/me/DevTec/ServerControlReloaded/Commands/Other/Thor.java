package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.blocksapi.BlocksAPI;

public class Thor implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Thor","Other")) {
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

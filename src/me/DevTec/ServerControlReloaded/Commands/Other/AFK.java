package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.Repeat;
import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class AFK implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (Loader.has(s, "AFK", "Other")) {
				if (s instanceof Player) {
					SPlayer p = API.getSPlayer((Player) s);
					if (p.isAFK()) {
						p.setAFK(false);
						if (!API.hasVanish((Player)s))
							Loader.sendBroadcasts(p.getPlayer(), "AFK.End");
					} else {
						p.setAFK(true);
					}
					return true;
				}
				Loader.Help(s, "AFK", "Other");
				return true;
			}
			Loader.noPerms(s, "AFK", "Other");
			return true;
		}
		if (args.length >= 1) {
			if (Loader.has(s, "AFK", "Other", "Other")) {
				if (args[0].equals("*")) {
					Repeat.a(s, "AFK *");
					return true;
				}
				Player player = TheAPI.getPlayer(args[0]);
				if(player!=null) {
					SPlayer p = API.getSPlayer(player);
					if (p.isAFK()) {
						Loader.sendMessages(s, "AFK.Command.Other.End");
						Loader.sendMessages(p.getPlayer(), "AFK.Command.End");
						p.setAFK(false);
						if (!API.hasVanish(player))
							Loader.sendBroadcasts(p.getPlayer(), "AFK.End");
					} else {
						Loader.sendMessages(s, "AFK.Command.Other.Start");
						Loader.sendMessages(p.getPlayer(), "AFK.Command.Start");
						p.setAFK(true);
					}
					return true;
				}
			}
			if(Loader.has(s, "AFK", "Other")) {
				if (Loader.has(s, "AFK", "Other", "Reason")) {
					if (s instanceof Player) {
						SPlayer p = API.getSPlayer((Player) s);
						if (p.isAFK()) {
							p.setAFK(false);
							if (!API.hasVanish((Player)s))
								Loader.sendBroadcasts(p.getPlayer(), "AFK.End");
						} else {
							p.setAFK(true, StringUtils.buildString(args));
						}
						return true;
					}
					Loader.Help(s, "AFK", "Other");
					return true;
				}
				Loader.noPerms(s, "AFK", "Other", "Reason");
				return true;
				
			}
			Loader.noPerms(s, "AFK", "Other");
			return true;
		}
		return true;
	}
}

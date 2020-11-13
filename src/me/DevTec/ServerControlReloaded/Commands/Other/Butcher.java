package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.BlocksAPI.BlocksAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Butcher implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	public static int butcher(World a, Location w, int radius, EntityType type) {
		if (radius == 0) {
			int killed = 0;
			for (Entity e : a.getEntities()) {
				if (e instanceof Player == false) {
					if(type==null||e.getType()==type) {
					++killed;
					e.remove();
				}
				}
			}
			return killed;
		} else {
			int killed = 0;
			if (radius > 100000)
				radius = 100000;
			for (Entity e : BlocksAPI.getNearbyEntities(w, radius)) {
				if (e instanceof Player == false) {
					if(type==null||e.getType()==type) {
					++killed;
					e.remove();
					}
				}
			}
			return killed;
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Butcher", "Other")) {
			if (args.length == 0) {
				Loader.Help(s, "Butcher", "Other");
				return true;
			}
			if (args.length == 1) {
				if (s instanceof Player == false) {
					World w = Bukkit.getWorld(args[0]);
					if (w == null) {
						Loader.sendMessages(s, "Missing.World", Placeholder.c().add("%world%", args[0]));
						return true;
					}
					Loader.sendMessages(s, "Butcher.Killed", Placeholder.c().add("%amount%", butcher(w, null, 0,null) + ""));
					return true;
				}
				World w = Bukkit.getWorld(args[0]);
				if (w != null) {
					Loader.sendMessages(s, "Butcher.Killed", Placeholder.c().add("%amount%", butcher(w,
							null, 0,null) + ""));
					return true;
				}
				Loader.sendMessages(s, "Butcher.Killed", Placeholder.c().add("%amount%", butcher(((Player) s).getWorld(),
						((Player) s).getLocation(), StringUtils.getInt(args[0]),null) + ""));
				return true;
			}
			if (s instanceof Player == false) {
				World w = Bukkit.getWorld(args[0]);
				if (w == null) {
					Loader.sendMessages(s, "Missing.World", Placeholder.c().add("%world%", args[0]));
					return true;
				}
				Loader.sendMessages(s, "Butcher.Killed", Placeholder.c().add("%amount%", butcher(w, null, 0, EntityType.valueOf(args[1].toUpperCase())) + ""));
				return true;
			}
			World w = Bukkit.getWorld(args[0]);
			if (w != null) {
				Loader.sendMessages(s, "Butcher.Killed", Placeholder.c().add("%amount%", butcher(w,
						null, 0,EntityType.valueOf(args[1].toUpperCase())) + ""));
				return true;
			}
			Loader.sendMessages(s, "Butcher.Killed", Placeholder.c().add("%amount%", butcher(((Player) s).getWorld(),
					((Player) s).getLocation(), StringUtils.getInt(args[0]), EntityType.valueOf(args[1].toUpperCase())) + ""));
			return true;
		}
		Loader.noPerms(s, "Butcher", "Other");
		return true;
	}
}

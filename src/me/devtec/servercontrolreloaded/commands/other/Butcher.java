package me.devtec.servercontrolreloaded.commands.other;

import java.util.ArrayList;
import java.util.Collections;
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

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.blocksapi.BlocksAPI;
import me.devtec.theapi.utils.StringUtils;

public class Butcher implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Butcher", "Other")) {
			if(args.length==1) {
				if (s instanceof Player) {
					List<String> names = new ArrayList<>();
					names.add("<radius>");
					for(World w : Bukkit.getWorlds())
						names.add(w.getName());
					return StringUtils.copyPartialMatches(args[0], names);
				}
				List<String> names = new ArrayList<>();
				for(World w : Bukkit.getWorlds())
					names.add(w.getName());
				return StringUtils.copyPartialMatches(args[0], names);
			}
			if(args.length==2) {
				List<String> names = new ArrayList<>();
				for(EntityType w : EntityType.values())
					names.add(w.name());
				return StringUtils.copyPartialMatches(args[1], names);
			}
		}
		return Collections.emptyList();
	}

	public static int butcher(World a, Location w, int radius, EntityType type) {
		int killed = 0;
		if (radius == 0) {
			for (Entity e : a.getEntities()) {
				if (!(e instanceof Player)) {
					if(type==null||e.getType()==type) {
					++killed;
					e.remove();
				}
				}
			}
		} else {
			if (radius > 256)
				radius = 256;
			for (Entity e : BlocksAPI.getNearbyEntities(w, radius)) {
				if (!(e instanceof Player)) {
					if(type==null||e.getType()==type) {
					++killed;
					e.remove();
					}
				}
			}
		}
		return killed;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Butcher", "Other")) {
			if(!CommandsManager.canUse("Other.Butcher", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Butcher", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "Butcher", "Other");
				return true;
			}
			if (args.length == 1) {
				if (!(s instanceof Player)) {
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
			if (!(s instanceof Player)) {
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

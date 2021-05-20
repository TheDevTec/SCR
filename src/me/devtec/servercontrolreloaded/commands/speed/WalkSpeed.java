package me.devtec.servercontrolreloaded.commands.speed;

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

public class WalkSpeed implements CommandExecutor, TabCompleter {

	public void speed(CommandSender s) {
		if (s instanceof Player) {
			Loader.Help(s, "WalkSpeed", "Speed");
		}
		if (s instanceof Player == false) {
			Loader.Help(s, "WalkSpeed", "Speed");
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "WalkSpeed", "Speed")) {
		if(!CommandsManager.canUse("Speed.WalkSpeed", s)) {
			Loader.sendMessages(s, "Cooldowns.Commands");
			return true;
		}
		if (args.length == 0) {
			speed(s);
			return true;
		}
		if (args.length == 1) {
			if (s instanceof Player == false) {
				speed(s);
				return true;
			} else {
				if (Loader.has(s, "WalkSpeed", "Speed")) {
					double flightmodifier = StringUtils.getDouble(args[0]);
					if (flightmodifier > 10.0)
						flightmodifier = 10.0;
					if (flightmodifier < -10.0)
						flightmodifier = -10.0;
					((Player) s).setWalkSpeed((float) flightmodifier / 10);
					TheAPI.getUser(s.getName()).setAndSave("WalkSpeed", flightmodifier / 10);
					Loader.sendMessages(s, "Speed.Walk.You", Placeholder.c().add("%speed%", String.valueOf(flightmodifier)));
					return true;
				}
				Loader.noPerms(s, "WalkSpeed", "Speed");
				return true;
			}
		}
			if (Loader.has(s, "WalkSpeed", "Speed", "Other")) {
				Player target = TheAPI.getPlayer(args[0]);
				if (target != null) {
					double flightmodifier = StringUtils.getDouble(args[1]);
					if (flightmodifier > 10.0)
						flightmodifier = 10.0;
					if (flightmodifier < -10.0)
						flightmodifier = -10.0;
					target.setWalkSpeed((float) flightmodifier / 10);
					TheAPI.getUser(target).setAndSave("WalkSpeed", flightmodifier / 10);
					Loader.sendMessages(s, "Speed.Walk.Other.Sender", Placeholder.c().add("%speed%", String.valueOf(flightmodifier))
							.add("%player%", target.getName()).add("%playername%", target.getDisplayName()));

					Loader.sendMessages(target, "Speed.Walk.Other.Receiver", Placeholder.c().add("%speed%", String.valueOf(flightmodifier)));
					return true;
				}
				Loader.notOnline(s, args[0]);
				return true;
			}
			Loader.noPerms(s, "WalkSpeed", "Speed", "Other");
			return true;
		}
		Loader.noPerms(s, "WalkSpeed", "Speed");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "WalkSpeed", "Speed")) {
			if (args.length == 1)
				return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
			if (args.length == 2)
				return StringUtils.copyPartialMatches(args[1], Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
		}
		return Arrays.asList();
	}
}
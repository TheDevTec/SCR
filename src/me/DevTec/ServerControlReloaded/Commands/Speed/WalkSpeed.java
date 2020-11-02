package me.DevTec.ServerControlReloaded.Commands.Speed;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class WalkSpeed implements CommandExecutor {

	public void speed(CommandSender s) {
		if (s instanceof Player) {
			Loader.Help(s, "/WalkSpeed <number>", "WalkSpeed");
		}
		if (s instanceof Player == false) {
			Loader.Help(s, "/WalkSpeed <player> <number>", "WalkSpeed");
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (args.length == 0) {
			if (Loader.has(s, "WalkSpeed", "Speed")) {
				speed(s);
				return true;
			}
			Loader.noPerms(s, "WalkSpeed", "Speed");
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
		if (args.length == 2) {
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
		return false;
	}
}
package me.devtec.servercontrolreloaded.commands.bansystem;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.API.TeleportLocation;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.scr.events.BanlistUnjailEvent;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.servercontrolreloaded.utils.setting.DeathTp;
import me.devtec.servercontrolreloaded.utils.punishment.SPunishmentAPI;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.Punishment;
import me.devtec.theapi.punishmentapi.Punishment.PunishmentType;
import me.devtec.theapi.utils.StringUtils;

public class UnJail implements CommandExecutor, TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "UnJail", "BanSystem")) {
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		}
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "UnJail", "BanSystem")) {
			if(!CommandsManager.canUse("BanSystem.UnJail", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("BanSystem.UnJail", s))));
				return true;
			}
			if (args.length == 0) {
				Loader.Help(s, "UnJail", "BanSystem");
				return true;
			}
			Punishment push = TheAPI.getPunishmentAPI().getPunishments(args[0]).stream().filter(a -> a.getType()==PunishmentType.JAIL).findFirst().orElse(TheAPI.getPunishmentAPI().getPunishmentsIP(args[0]).stream().filter(a -> a.getType()==PunishmentType.JAIL).findFirst().orElse(null));
			if (push!=null) {
				//teleport to player's "spawn"
				if(push.isIP()) {
					for(String nick : Accounts.findPlayersOnIP(push.getUser())) {
						Player p = TheAPI.getPlayerOrNull(nick);
						if(p!=null) {
							if (setting.deathspawnbol) {
								if (setting.deathspawn == DeathTp.HOME)
									API.teleport(p, API.getTeleportLocation(p, TeleportLocation.HOME));
								else if (setting.deathspawn == DeathTp.BED)
									API.teleport(p, API.getTeleportLocation(p, TeleportLocation.BED));
								else if (setting.deathspawn == DeathTp.SPAWN) {
									API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
									Loader.sendMessages(p, "Spawn.Teleport.You");
								}
							}else
								API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
						}else {
							List<String> home = SPunishmentAPI.data.getStringList("tp-home");
							home.add(nick);
							SPunishmentAPI.data.set("tp-home", home);
							SPunishmentAPI.data.save();
						}
					}
				}
				Player p = TheAPI.getPlayerOrNull(push.getUser());
				if(p!=null) {
					if (setting.deathspawnbol) {
						if (setting.deathspawn == DeathTp.HOME)
							API.teleport(p, API.getTeleportLocation(p, TeleportLocation.HOME));
						else if (setting.deathspawn == DeathTp.BED)
							API.teleport(p, API.getTeleportLocation(p, TeleportLocation.BED));
						else if (setting.deathspawn == DeathTp.SPAWN) {
							API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
							Loader.sendMessages(p, "Spawn.Teleport.You");
						}
					}else
						API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
				}else {
					List<String> home = SPunishmentAPI.data.getStringList("tp-home");
					SPunishmentAPI.data.set("tp-home", home);
					home.add(push.getUser());
					SPunishmentAPI.data.save();
				}
				TheAPI.callEvent(new BanlistUnjailEvent(push));
				push.remove();
				if (TheAPI.getPlayer(args[0]) != null)
					API.teleportPlayer(TheAPI.getPlayer(args[0]), TeleportLocation.SPAWN);
				else TheAPI.getUser(args[0]).setAndSave("request-spawn", 1);
				Loader.sendMessages(s, "BanSystem.UnJail.Sender", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				Loader.sendBroadcasts(s, "BanSystem.UnJail.Admins", Placeholder.c().replace("%operator%", s.getName())
						.replace("%playername%", args[0]).replace("%player%", args[0]));
				return true;
			}
			Loader.sendMessages(s, "BanSystem.Not.Arrested", Placeholder.c().replace("%playername%", args[0]).replace("%player%", args[0]));
			return true;
		}
		Loader.noPerms(s, "UnJail", "BanSystem");
		return true;
	}
}
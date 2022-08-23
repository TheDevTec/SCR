package me.devtec.scr.commands.teleport.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.tpsystem.TpSystem;
import me.devtec.shared.API;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Home implements ScrCommand {

	@Override
	public void init(List<String> cmds) { // home [home/player] [home] {player}
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (!(s instanceof Player)) {
				help(s, "admin_usage");
				return;
			}
			StringBuilder homeNames = new StringBuilder();
			Set<String> homes = HomeManager.homesOf(((Player) s).getUniqueId());
			for (String home : homes) {
				if (homeNames.length() != 0)
					homeNames.append(Loader.translations.getString(configSection() + ".list_split"));
				homeNames.append(home);
			}
			msgSec(s, "list", Placeholders.c().add("homes", homeNames).add("amount", homes.size()).add("limit", HomeManager.getLimit((Player) s)));
		}).cooldownDetection((s, structure, args) -> inCooldown(s)) //cd
		.permission(permission("cmd")) // perm
		.fallback((s, structure, args) -> {
			msgSec(s, "notFound", Placeholders.c().add("home", args[0]));
		})
		// /cmd [home]
		.callableArgument((s, structure, args) -> s instanceof Player ? new ArrayList<>(HomeManager.homesOf(((Player) s).getUniqueId())) : Collections.emptyList(), (s, structure, args) -> { // cmd [home]
			
			if (!(s instanceof Player)) {
				help(s, "admin_usage");
				return;
			}

			HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
			TpSystem.teleport((Player)s, home.location().toLocation());
			//((Player) s).teleport(home.location().toLocation());
			msgSec(s, "self", Placeholders.c().add("home", home.name()));
		}).priority(1)
			.argument("-s", (s, structure, args) -> { // cmd [home] -s
				if (!(s instanceof Player)) { // must be player
					help(s, "admin_usage");
					return;
				}
	
				HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
				TpSystem.teleport((Player)s, home.location().toLocation());
				//((Player) s).teleport(home.location().toLocation());
			})
			.parent() // cmd [home]
		.parent() // cmd
		.fallback((s, structure, args) -> {
					offlinePlayer(s, args[0]);
		})
		.selector(Selector.PLAYER, (s, structure, args) -> { // cmd [player]
			StringBuilder homeNames = new StringBuilder();
			Set<String> homes = HomeManager.homesOf(Bukkit.getPlayer(me.devtec.scr.api.API.getRealName(args[0])).getUniqueId());
			for (String home : homes) {
				if (homeNames.length() != 0)
					homeNames.append(Loader.translations.getString(configSection() + ".list_split"));
				homeNames.append(home);
			}
			msgSec(s, "listOther", Placeholders.c().addPlayer("player", Bukkit.getPlayer(args[0])).add("homes", homeNames).add("amount", homes.size()) );
		}).priority(2)
		.permission(permission("admin"))
			// cmd [player] [homes]
			.callableArgument((s, structure, args) -> new ArrayList<>(HomeManager.homesOf(Bukkit.getPlayer(me.devtec.scr.api.API.getRealName(args[0])).getUniqueId())), (s, structure, args) -> {
				// [home]
				if (!(s instanceof Player)) {
					help(s, "admin_usage");
					return;
				}

				HomeHolder home = HomeManager.find(Bukkit.getPlayer(me.devtec.scr.api.API.getRealName(args[0])).getUniqueId(), args[1]);
				//((Player) s).teleport(home.location().toLocation());
				TpSystem.teleport((Player)s, home.location().toLocation());
				msgSec(s, "self", Placeholders.c().add("home", home.name()));
			})
				.argument("-s", (s, structure, args) -> { // cmd [player] [home] -s
					if (!(s instanceof Player)) {
						help(s, "admin_usage");
						return;
					}
	
					HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[1]);
					//((Player) s).teleport(home.location().toLocation());
					TpSystem.teleport((Player)s, home.location().toLocation());
				})
			.parent() // cmd [player] [home]

			.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [player] [home] [target]
				HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(me.devtec.scr.api.API.getRealName(args[0])).getFile().getName().substring(0,
						API.getUser(me.devtec.scr.api.API.getRealName(args[0])).getFile().getName().length() - 4)), args[1]);
				for (Player p : playerSelectors(s, args[2])) {
					TpSystem.teleport(p, home.location().toLocation());
					//p.teleport(home.location().toLocation());
					msgSec(s, "other-otherHouse.sender", Placeholders.c().addPlayer("target", Bukkit.getPlayer(args[0])).add("home", home.name()).addPlayer("player", p));
					msgSec(p, "other-otherHouse.target", Placeholders.c().addPlayer("target", Bukkit.getPlayer(args[0])).add("home", home.name()).addPlayer("player", s));
				}
			}).permission(permission("tp_other"))
				.argument("-s", (s, structure, args) -> { // cmd [player] [home] -s
					HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(args[0]).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length() - 4)), args[1]);
					for (Player p : playerSelectors(s, args[2]))
						TpSystem.teleport(p, home.location().toLocation());
						//p.teleport(home.location().toLocation());
				})
				.parent() // cmd [player] [home] [target]
			.parent() // cmd [player] [home]
		.parent() // cmd [player]
		.parent() // cmd
		.argument(null, (s, structure, args) -> { // cmd [playerName/uuid]
			StringBuilder homeNames = new StringBuilder();
			Set<String> homes = API.getUser(args[0]).getKeys("home");
			for (String home : homes) {
				if (homeNames.length() != 0)
					homeNames.append(Loader.translations.getString(configSection() + ".list_split"));
				homeNames.append(home);
			}
			msgSec(s, "listOther", Placeholders.c().add("player", args[0])
					.add("homes", homeNames).add("amount", homes.size()));
		})
		// cmd [uuid] [homes]
			.callableArgument((s, structure, args) -> new ArrayList<>(API.getUser(me.devtec.scr.api.API.getPlayerName(args[0])).getKeys("home")), (s, structure, args) -> { // cmd [player] [home]
				if (!(s instanceof Player)) { // must be player
					help(s, "admin_usage");
					return;
				}

				HomeHolder home = HomeManager.find(
						UUID.fromString(API.getUser(me.devtec.scr.api.API.getRealName(args[0])).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length() - 4)), args[1]);
				TpSystem.teleport((Player)s, home.location().toLocation());
				((Player) s).teleport(home.location().toLocation());
				msgSec(s, "self-otherHouse", Placeholders.c().add("player", me.devtec.scr.api.API.getPlayerName(args[0])).add("home", home.name()));
			})
				.argument("-s", (s, structure, args) -> { // cmd [player] [home] -s
					if (!(s instanceof Player)) { // must be player
						help(s, "admin_usage");
						return;
					}

					HomeHolder home = HomeManager.find(
							UUID.fromString(API.getUser(me.devtec.scr.api.API.getPlayerName(args[0])).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length() - 4)),
							args[1]);
					TpSystem.teleport((Player)s, home.location().toLocation());
					//((Player) s).teleport(home.location().toLocation());
				})
			.parent() // cmd [player] [home]
			.fallback((s, structure, args) -> {
				offlinePlayer(s, args[2]);
			})
			.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd  [player] [home] [player]
				HomeHolder home = HomeManager.find(
						UUID.fromString(API.getUser(me.devtec.scr.api.API.getPlayerName(args[0])).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length() - 4)),
						args[1]);
				for (Player p : playerSelectors(s, args[2])) {
					TpSystem.teleport(p, home.location().toLocation());
					//p.teleport(home.location().toLocation());
					msgSec(s, "other-otherHouse.sender", Placeholders.c().add("target", me.devtec.scr.api.API.getPlayerName(args[0])).add("home", home.name()).addPlayer("player", p));
					msgSec(p, "other-otherHouse.target", Placeholders.c().add("target", me.devtec.scr.api.API.getPlayerName(args[0])).add("home", home.name()).addPlayer("player", s));
				}
			}).permission(permission("tp_other"))
				.argument("-s", (s, structure, args) -> { // cmd [player] [home] [player] -s
					HomeHolder home = HomeManager.find(
							UUID.fromString(API.getUser(me.devtec.scr.api.API.getPlayerName(args[0])).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length() - 4)),
							args[1]);
					for (Player p : playerSelectors(s, args[2]))
						TpSystem.teleport(p, home.location().toLocation());
						//p.teleport(home.location().toLocation());
				}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure();
	}

	@Override
	public String configSection() {
		return "home";
	}

}

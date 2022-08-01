package me.devtec.scr.commands.teleport.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.API;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Home implements ScrCommand {

	@Override
	public void init(int cd, List<String> cmds) { // home [home/player] [home] {player}
		cooldownMap.put(CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
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
			msgSec(s, "list", Placeholders.c().add("homes", homeNames).add("amount", homes.size()));
		}).permission(permission("cmd")).fallback((s, structure, args) -> {
			msgSec(s, "notFound", Placeholders.c().add("home", args[0]));
		}).callableArgument((s, structure, args) -> new ArrayList<>(HomeManager.homesOf(((Player) s).getUniqueId())), (s, structure, args) -> { // cmd [home]
			if (!(s instanceof Player)) {
				help(s, "admin_usage");
				return;
			}

			HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
			((Player) s).teleport(home.location().toLocation());
			msgSec(s, "self", Placeholders.c().add("home", home.name()));
		}).fallback((s, structure, args) -> {
			offlinePlayer(s, args[1]);
		}).argument("-s", (s, structure, args) -> { // cmd [home] -s
			if (!(s instanceof Player)) { // must be player
				help(s, "admin_usage");
				return;
			}

			HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
			((Player) s).teleport(home.location().toLocation());
		}).first() // cmd
				.fallback((s, structure, args) -> {
					offlinePlayer(s, args[0]);
				}).selector(Selector.PLAYER, (s, structure, args) -> { // cmd [player]
					StringBuilder homeNames = new StringBuilder();
					Set<String> homes = HomeManager.homesOf(Bukkit.getPlayer(args[0]).getUniqueId());
					for (String home : homes) {
						if (homeNames.length() != 0)
							homeNames.append(Loader.translations.getString(configSection() + ".list_split"));
						homeNames.append(home);
					}
					msgSec(s, "listOther", Placeholders.c().add("player", Bukkit.getPlayer(args[0]).getName()).add("homes", homeNames).add("amount", homes.size()));
				}).permission(permission("admin")).callableArgument((s, structure, args) -> new ArrayList<>(HomeManager.homesOf(Bukkit.getPlayer(args[0]).getUniqueId())), (s, structure, args) -> { // cmd
																																																	// [player]
																																																	// [home]
					if (!(s instanceof Player)) {
						help(s, "usage");
						return;
					}

					HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
					((Player) s).teleport(home.location().toLocation());
					msgSec(s, "self", Placeholders.c().add("home", home.name()));
				}).argument("-s", (s, structure, args) -> { // cmd [player] [home] -s
					if (!(s instanceof Player)) {
						help(s, "admin_usage");
						return;
					}

					HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
					((Player) s).teleport(home.location().toLocation());
				}).first()

				.argument(null, (s, structure, args) -> { // cmd [playerName/uuid]
					StringBuilder homeNames = new StringBuilder();
					Set<String> homes = API.getUser(args[0]).getKeys("home");
					for (String home : homes) {
						if (homeNames.length() != 0)
							homeNames.append(Loader.translations.getString(configSection() + ".list_split"));
						homeNames.append(home);
					}
					msgSec(s, "listOther", Placeholders.c().add("player", args[0]).add("homes", homeNames).add("amount", homes.size()));
				}).fallback((s, structure, args) -> {
					offlinePlayer(s, args[0]);
				}).callableArgument((s, structure, args) -> new ArrayList<>(API.getUser(args[0]).getKeys("home")), (s, structure, args) -> { // cmd [player] [home]
					if (!(s instanceof Player)) { // must be player
						help(s, "admin_usage");
						return;
					}

					HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(args[0]).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length() - 4)), args[0]);
					((Player) s).teleport(home.location().toLocation());
					msgSec(s, "self-otherHouse", Placeholders.c().add("player", args[0]).add("home", home.name()));
				}).argument("-s", (s, structure, args) -> { // cmd [player] [home] -s
					if (!(s instanceof Player)) { // must be player
						help(s, "admin_usage");
						return;
					}

					HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(args[0]).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length() - 4)), args[0]);
					((Player) s).teleport(home.location().toLocation());
				}).parent() // cmd [player] [home]

				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [player]
					HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(args[0]).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length() - 4)), args[0]);
					for (Player p : playerSelectors(s, args[2])) {
						p.teleport(home.location().toLocation());
						msgSec(s, "other-otherHouse.sender", Placeholders.c().add("player", args[0]).add("home", home.name()).add("target", p.getName()));
						msgSec(p, "other-otherHouse.target", Placeholders.c().add("player", args[0]).add("home", home.name()).add("target", s.getName()));
					}
				}).permission(permission("tp_other")).argument("-s", (s, structure, args) -> { // cmd [player] [home] -s
					HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(args[0]).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length() - 4)), args[0]);
					for (Player p : playerSelectors(s, args[2]))
						p.teleport(home.location().toLocation());
				}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure(), new CooldownHolder(this, cd));
	}

	@Override
	public String configSection() {
		return "home";
	}

}

package me.devtec.scr.commands.teleport.home;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.API;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Home implements ScrCommand {
	
	@Override
	public void init(List<String> cmds) { //home [name/player] [name] {player}
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if(!(s instanceof Player)) {  //must be player
				help(s, 0);
				return;
			}
			StringBuilder homeNames = new StringBuilder();
			for(String home : HomeManager.homesOf(((Player) s).getUniqueId())) {
				if(homeNames.length()!=0)
					homeNames.append(Loader.translations.getString(configSection()+".list_split"));
				homeNames.append(home);
			}
			msgConfig(s, configSection()+".list", homeNames);
		}).permission("scr."+configSection())
			.fallback((s, structure, args) -> {
				msgConfig(s, configSection()+".notFound", args[0]);
			})
			.callableArgument((s, structure, args) -> {
				return new ArrayList<>(HomeManager.homesOf(((Player) s).getUniqueId()));
				}, (s, structure, args) -> { // cmd [home]
				if(!(s instanceof Player)) { //must be player
					help(s, 0);
					return;
				}
				
				HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
				((Player)s).teleport(home.location().toLocation());
				msgConfig(s, configSection()+".self", home.name());
				}).fallback((s, structure, args) -> {
					msgConfig(s, "offlinePlayer", args[1]);
				})
				.argument("-s", (s, structure, args) -> { // cmd [home] -s
					if(!(s instanceof Player)) { //must be player
						help(s, 0);
						return;
					}

					HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
					((Player)s).teleport(home.location().toLocation());
				})
				.parent() //cmd [home]
			.parent() //cmd
			.fallback((s, structure, args) -> {
				msgConfig(s, "offlinePlayer", args[0]);
			})
			.selector(Selector.PLAYER, (s, structure, args) -> { // cmd [player]
				help(s, 0);
				}).permission("scr."+configSection()+".other")
			.callableArgument((s, structure, args) -> {
				return new ArrayList<>(HomeManager.homesOf(Bukkit.getPlayer(args[0]).getUniqueId()));
				}, (s, structure, args) -> { // cmd [player] [home]
				if(!(s instanceof Player)) { //must be player
					help(s, 0);
					return;
				}
				
				HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
				((Player)s).teleport(home.location().toLocation());
				msgConfig(s, configSection()+".self", home.name());
				})
				.argument("-s", (s, structure, args) -> { // cmd [player] [home] -s
					if(!(s instanceof Player)) { //must be player
						help(s, 0);
						return;
					}

					HomeHolder home = HomeManager.find(((Player) s).getUniqueId(), args[0]);
					((Player)s).teleport(home.location().toLocation());
				})
				.parent() // cmd [player] [home]
				.parent() // cmd [player]
				.parent() // cmd

				.argument(null, (s, structure, args) -> { // cmd [playerName/uuid]
					help(s, 0);
					}).fallback((s, structure, args) -> {
						msgConfig(s, "offlinePlayer", args[0]);
					})
					.callableArgument((s, structure, args) -> {
						return new ArrayList<>(API.getUser(args[0]).getKeys("home"));
						}, (s, structure, args) -> { // cmd [player] [home]
							if(!(s instanceof Player)) { //must be player
								help(s, 0);
								return;
							}
							
							HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(args[0]).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length()-4)), args[0]);
							((Player)s).teleport(home.location().toLocation());
							msgConfig(s, configSection()+".self-otherHouse", home.name(), args[0]);
						})
						.argument("-s", (s, structure, args) -> { // cmd [player] [home] -s
							if(!(s instanceof Player)) { //must be player
								help(s, 0);
								return;
							}
							
							HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(args[0]).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length()-4)), args[0]);
							((Player)s).teleport(home.location().toLocation());
						})
						.parent() // cmd [player] [home]

						.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [player]
							HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(args[0]).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length()-4)), args[0]);
							for(Player p : playerSelectors(s, args[2])) {
								p.teleport(home.location().toLocation());
								msgConfig(s, configSection()+".other-otherHouse.sender", home.name(), args[0], p.getName());
								msgConfig(s, configSection()+".other-otherHouse.target", home.name(), args[0], p.getName());
							}
							}).permission("scr."+configSection()+".other-otherHouse")
							.argument("-s", (s, structure, args) -> { // cmd [player] [home] -s
								HomeHolder home = HomeManager.find(UUID.fromString(API.getUser(args[0]).getFile().getName().substring(0, API.getUser(args[0]).getFile().getName().length()-4)), args[0]);
								for(Player p : playerSelectors(s, args[2])) {
									p.teleport(home.location().toLocation());
								}
							})
				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "home";
	}
	
}

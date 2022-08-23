package me.devtec.scr.commands.server_managment;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.StringBuilders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.API;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;

public class ConfigCommand implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		// cmd <configname | user>
		// cmd config <PATH> [Get | Set | Remove? | Add]
		// Get - return value
		// Set set new value
		// Remove - too dangerous? yes...
		// Add - Adds to stringlist (or create new stringlist)
		
		//cmd user <player | uuid> [Get | Set | Remove | Add]
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		
			.argument("user", (s, structure, args) -> { // cmd user
				help(s, "user");
			})
				.selector(Selector.PLAYER, (s, structure, args) -> { // cmd user [ONLINE PLAYER]
					help(s, "user");
				})
					.argument("Set", (s, structure, args) -> { // cmd user [player] set
						help(s, "user");
					})
						// cmd user [ONLINE] Set [path]
						.callableArgument((s, structure, args) -> new ArrayList<>(API.getUser(args[1]).getKeys(true)), (s, structure, args) -> {
							help(s, "user");
						})
							.argument(null, (s, structure, args) -> { // cmd user player set path [value]
								for(Player p : playerSelectors(s, args[1])) {
									Config user = API.getUser(p.getName());
									String path = args[3];
									String value = StringUtils.buildString(4, args);
									if(value == null || value.equalsIgnoreCase("null")) {
										user.remove(path);
									} else {
										user.set(path, value);
									}
									user.save();
									msgSec(s, "user.set", Placeholders.c().addPlayer("player", p).add("value", value)
											.add("path", path));
								}
							})
							.parent() // cmd user player set path
						.parent() // cmd user player set
					.parent() // cmd user player
					
					.argument("Remove", (s, structure, args) -> { // cmd user [Player] Remove
						help(s, "user");
					})
						// cmd user [Player] Remove [path]
						.callableArgument((s, structure, args) -> new ArrayList<>(API.getUser(args[1]).getKeys(true)), (s, structure, args) -> {
							for(Player p : playerSelectors(s, args[1])) {
								Config user = API.getUser(p.getName());
								String path = args[3];
								user.remove(path);
								user.save();
								msgSec(s, "user.remove", Placeholders.c().addPlayer("player", p).add("path", path));
							}
						})
						.parent() // cmd user player remove
					.parent() // cmd user player
					
					.argument("Get", (s, structure, args) -> { // cmd user [Player] Get
						help(s, "user");
					})
						// cmd user [Player] Get [path]
						.callableArgument((s, structure, args) -> new ArrayList<>(API.getUser(args[1]).getKeys(true)), (s, structure, args) -> {
							for(Player p : playerSelectors(s, args[1])) {
								Config user = API.getUser(p.getName());
								String path = args[3];
								msgSec(s, "user.get", Placeholders.c().addPlayer("player", p).add("path", path)
										.add("value", String.valueOf(user.get(path))) );
							}
						})
						.parent() // cmd user player Get
					.parent() // cmd user player
						

		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}
	
	@Override
	public String configSection() {
		return "config";
	}

}
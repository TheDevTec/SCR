package me.devtec.scr.commands.fun;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;

public class Feed implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // feed
			if(s instanceof Player) {
				Player p = (Player) s;
				p.setFoodLevel(20);
				msgSec(s, "you");
			}
			else 
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
			.argument("-s", (s, structure, args) -> { // feed -s
				if(s instanceof Player) {
					Player p = (Player) s;
					p.setFoodLevel(20);
				}
				else 
					help(s, "usage");
			})
		.parent() // feed
		
			.fallback((s, structure, args) -> { // feed {level}
				msg(s, "missing.number", Placeholders.c().replace("number", args[0]));
			})
			.selector(Selector.INTEGER, (s, structure, args) -> { // feed {level}
				if(s instanceof Player) {
					Player p = (Player) s;
					int level = StringUtils.getInt(args[0]);
					if(level < 0)
						level = 0;
					if(level > 20)
						level = 20;
					p.setFoodLevel(level);
					msgSec(s, "you");
				}
				else 
					help(s, "usage");
			}, (s, structure, args) -> {
				List<String> list = new ArrayList<>();
				for (int i = 0 ; i<=20 ; i++)
					list.add(""+i);
				return list;
			}).permission(permission("food_level"))
				.argument("-s", (s, structure, args) -> { // feed {level} -s
					if(s instanceof Player) {
						Player p = (Player) s;
						int level = StringUtils.getInt(args[0]);
						if(level < 0)
							level = 0;
						if(level > 20)
							level = 20;
						p.setFoodLevel(level);
					}
					else 
						help(s, "usage");
				})
			.parent() // feed {level}
		.parent() // feed
			
			.fallback((s, structure, args) -> { // feed [player]
				offlinePlayer(s, args[0]);
			})
			.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // feed [player]
				for(Player p : playerSelectors(s, args[0])) {
					p.setFoodLevel(20);
					msgSec(s, "other.sender", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
					msgSec(p, "other.receiver", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
				}
			}).permission(permission("other"))
				.argument("-s", (s, structure, args) -> { // feed [player] -s
					for(Player p : playerSelectors(s, args[0])) {
						p.setFoodLevel(20);
						}
				})
			.parent()  // feed [player]
			.fallback((s, structure, args) -> { // feed [player] {level}
				msg(s, "missing.number", Placeholders.c().replace("number", args[0]));
			})
			.selector(Selector.INTEGER, (s, structure, args) -> { // feed [player] {level}
				for(Player p : playerSelectors(s, args[0])) {
					int level = StringUtils.getInt(args[1]);
					if(level < 0)
						level = 0;
					if(level > 20)
						level = 20;
					p.setFoodLevel(level);
					msgSec(s, "other.sender", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
					msgSec(p, "other.receiver", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
				}
			}, (s, structure, args) -> {
				List<String> list = new ArrayList<>();
				for (int i = 0 ; i<=20 ; i++)
					list.add(""+i);
				return list;
			}).permission(permission("food_level"))
				.argument("-s", (s, structure, args) -> { // feed [player] {level} -s
					for(Player p : playerSelectors(s, args[0])) {
						int level = StringUtils.getInt(args[1]);
						if(level < 0)
							level = 0;
						if(level > 20)
							level = 20;
						p.setFoodLevel(level);
					}
				})
				
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}
	
	@Override
	public String configSection() {
		return "feed";
	}
	
}

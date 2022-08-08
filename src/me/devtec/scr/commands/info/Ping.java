package me.devtec.scr.commands.info;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class Ping implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if(s instanceof Player)
				msgSec(s, "self", Placeholders.c()
					.add("ping", ""+pingPlayer( (Player)s )));
			else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // ping
		.fallback((s, structure, args) -> { // /ping [player]
			offlinePlayer(s, args[0]);
		})
		.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // ping [player]
			for(Player p : playerSelectors(s, args[0])) {
				msgSec(s, "other", Placeholders.c()
					.add("ping", ""+pingPlayer(p)).addPlayer("target", p) );
			}
		})
		.permission(permission("other")) // ping [player]
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "ping";
	}

	private static String getColoredPing(Player p) {
		int s = getPlayerPing(p);
		if (s >= 500)
			return StringUtils.colorize("&c" + s);
		if (s >= 200)
			return StringUtils.colorize("&e" + s);
		if (s >= 0)
			return StringUtils.colorize("&a" + s);
		return StringUtils.colorize("&4" + s);
	}

	public static String pingPlayer(Player who) {
		//if (tab.getBoolean("Colored-Ping")) //TODO - add?
			return getColoredPing(who);
		//return String.valueOf(getPlayerPing(who));
	}
	
	private static int getPlayerPing(Player p) {
		try {
			return BukkitLoader.getNmsProvider().getPing(p);
		} catch (Exception e) {
			return -1;
		} 
	}
	
}

package me.devtec.scr.commands.fun;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.listeners.fun.AntiDamage;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class God implements ScrCommand {

	private static boolean enabled;

	@Override
	public void init(List<String> cmds) {
		enabled = true;
		Loader.plugin.getLogger().info("[God] Using AntiDamage listener");
		Loader.registerListener(new AntiDamage());

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (s instanceof Player) {
				Player p = (Player) s;
				apply(p, isInvulnerable(p));

				String status = isInvulnerable(p) ? "enabled" : "disabled";
				msgSec(s, "" + status);
			} else
				help(s, "admin_usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
		}).permission(permission("cmd")).argument("-s", (s, structure, args) -> { // cmd -s
			if (s instanceof Player) {
				Player p = (Player) s;
				apply(p, isInvulnerable(p));
			} else
				help(s, "admin_usage");
		}).parent() // cmd
				.selector(Selector.BOOLEAN, (s, structure, args) -> { // cmd [boolean]
					if (s instanceof Player) {
						Player p = (Player) s;
						apply(p, !Boolean.parseBoolean(args[0]));

						String status = Boolean.parseBoolean(args[0]) ? "enabled" : "disabled";
						msgSec(s, "" + status);
					} else
						help(s, "admin_usage");
				}).argument("-s", (s, structure, args) -> { // cmd [boolean] -s
					if (s instanceof Player) {
						Player p = (Player) s;
						apply(p, !Boolean.parseBoolean(args[0]));
					} else
						help(s, "admin_usage");
				}).parent() // cmd [boolean]
				.parent() // cmd
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [entity_selector]
					for (Player p : playerSelectors(s, args[0])) {
						apply(p, isInvulnerable(p));

						String status = isInvulnerable(p) ? "enabled" : "disabled";
						msgSec(s, "other." + status + ".sender", Placeholders.c().addPlayer("target", p));
						msgSec(p, "other." + status + ".target", Placeholders.c().addPlayer("player", s));
					}
				}).permission(permission("other")).fallback((s, structure, args) -> {
					help(s, "admin_usage");
				}).argument("-s", (s, structure, args) -> { // cmd [entity_selector] -s
					for (Player p : playerSelectors(s, args[0]))
						apply(p, isInvulnerable(p));
				}).parent() // cmd [entity_selector]
				.selector(Selector.BOOLEAN, (s, structure, args) -> { // cmd [entity_selector] [boolean]
					for (Player p : playerSelectors(s, args[0])) {
						apply(p, !Boolean.parseBoolean(args[1]));

						String status = Boolean.parseBoolean(args[1]) ? "enabled" : "disabled";
						msgSec(s, "other." + status + ".sender", Placeholders.c().addPlayer("target", p));
						msgSec(p, "other." + status + ".target", Placeholders.c().addPlayer("player", s));
					}
				}).argument("-s", (s, structure, args) -> { // cmd [entity_selector] [boolean] -s
					for (Player p : playerSelectors(s, args[0]))
						apply(p, !Boolean.parseBoolean(args[1]));
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	public static boolean isInvulnerable(Player p) {
		return API.getUser(p).god();
	}

	public static void apply(Player p, boolean status) {
		if (status)
			API.getUser(p).god(false);
		else
			API.getUser(p).god(true);
	}

	@Override
	public String configSection() {
		return "god";
	}

	public static boolean isEnabled() {
		return enabled;
	}

}

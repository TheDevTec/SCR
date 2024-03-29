package me.devtec.scr.commands.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.listeners.fun.AntiFallDamage;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.scheduler.Tasker;

public class Fly implements ScrCommand {

	public static List<UUID> antiFall;

	@Override
	public void init(List<String> cmds) {
		antiFall = new ArrayList<>();

		Loader.plugin.getLogger().info("[Fly] Using AntiFallDamage listener");
		Loader.registerListener(new AntiFallDamage());

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (s instanceof Player) {
				Player p = (Player) s;
				apply(p, p.getAllowFlight());

				String status = p.getAllowFlight() ? "enabled" : "disabled";
				msgSec(s, "" + status);
			} else
				help(s, "admin_usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> { // /fly [player]
			offlinePlayer(s, args[0]);
		}).argument("-s", (s, structure, args) -> { // cmd -s
			if (s instanceof Player) {
				Player p = (Player) s;
				apply(p, p.getAllowFlight());
			} else
				help(s, "admin_usage");
		}).parent() // cmd <-
				.selector(Selector.BOOLEAN, (s, structure, args) -> { // cmd [boolean]
					if (s instanceof Player) {
						Player p = (Player) s;
						apply(p, !Boolean.parseBoolean(args[0]));

						String status = Boolean.parseBoolean(args[0]) ? "enabled" : "disabled";
						msgSec(s, status);
					} else
						help(s, "admin_usage");
				}).argument("-s", (s, structure, args) -> { // cmd [boolean] -s
					if (s instanceof Player) {
						Player p = (Player) s;
						apply(p, !Boolean.parseBoolean(args[0]));
					} else
						help(s, "admin_usage");
				}).parent() // cmd [boolean] <-
				.parent() // cmd <-
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [entity_selector]
					for (Player p : playerSelectors(s, args[0])) {
						apply(p, p.getAllowFlight());

						String status = p.getAllowFlight() ? "enabled" : "disabled";
						msgSec(s, "other." + status + ".sender", Placeholders.c().addPlayer("target", p));
						msgSec(p, "other." + status + ".target", Placeholders.c().addPlayer("player", s));
					}
				}).permission(permission("other")).fallback((s, structure, args) -> {
					help(s, "admin_usage");
				}).argument("-s", (s, structure, args) -> { // cmd [entity_selector] -s
					for (Player p : playerSelectors(s, args[0]))
						apply(p, p.getAllowFlight());
				}).parent() // cmd [entity_selector]
				.selector(Selector.BOOLEAN, (s, structure, args) -> { // cmd [entity_selector] [boolean]
					for (Player p : playerSelectors(s, args[0])) {
						apply(p, !Boolean.parseBoolean(args[1]));

						String status = Boolean.parseBoolean(args[1]) ? "enabled" : "disabled";
						msgSec(s, "other." + status + ".sender", Placeholders.c().addPlayer("target", p));
						msgSec(p, "other." + status + ".target", Placeholders.c().addPlayer("player", s));
					}
				}).argument("-s", (s, structure, args) -> { // cmd [boolean] -s
					for (Player p : playerSelectors(s, args[0]))
						apply(p, !Boolean.parseBoolean(args[1]));
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	// status == false -- turning fly ON
	public static void apply(Player p, boolean status) {
		if (status) {
			p.setFlying(false);
			p.setAllowFlight(false);
			API.getUser(p).fly(false);
			if (antiFall != null && !p.isOnGround()) {
				antiFall.add(p.getUniqueId());
				new Tasker() {
					@Override
					public void run() {
						if (!antiFall.contains(p.getUniqueId())) {
							cancel();
							return;
						}
						if (!p.isOnline()) {
							antiFall.remove(p.getUniqueId());
							cancel();
							return;
						}
						if (p.isOnGround()) {
							antiFall.remove(p.getUniqueId());
							cancel();
						}
					}
				}.runRepeating(10, 10); // half-tick time
			}
		} else {
			p.setAllowFlight(true);
			if (antiFall != null)
				antiFall.remove(p.getUniqueId());
			API.getUser(p).fly(true);
			if (!p.isOnGround())
				p.setFlying(true);
		}
	}

	@Override
	public String configSection() {
		return "fly";
	}

	public static boolean isEnabled() {
		return antiFall != null;
	}

}

package me.devtec.scr.commands.teleport.warp;

import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.dataholder.DataType;
import me.devtec.theapi.bukkit.game.Position;

public class Warp implements ScrCommand {
	public static Config storedWarps = new Config("plugins/SCR/warps.yml");

	@Override
	public void init(List<String> cmds) {
		int count = 0;
		for (String key : storedWarps.getKeys("warp")) {
			++count;
			WarpManager.create(storedWarps.getAs("warp." + key + ".owner", UUID.class), key, storedWarps.getAs("warp." + key + ".location", Position.class), storedWarps.getAs("warp." + key + ".icon", Material.class), storedWarps.getString("warp." + key + ".permission"),
					storedWarps.getDouble("warp." + key + ".cost"));
		}
		Loader.plugin.getLogger().info("[Warp] Registered " + count + " warp" + (count != 1 ? "s" : ""));

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			StringBuilder warpNames = new StringBuilder();
			for (String warp : WarpManager.availableWarpNames(s)) {
				if (warpNames.length() != 0)
					warpNames.append(Loader.translations.getString(configSection() + ".list_split"));
				warpNames.append(warp);
			}
			msgSec(s, "list", Placeholders.c().add("warps", warpNames));
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")).fallback((s, structure, args) -> {
			msgSec(s, "notFound", Placeholders.c().add("warp", args[0]));
		}).callableArgument((s, structure, args) -> WarpManager.availableWarpNames(s), (s, structure, args) -> { // cmd [warp]
			if (!(s instanceof Player)) { // must be player
				help(s, "usage");
				return;
			}

			WarpHolder warp = WarpManager.find(args[0]);
			int teleportResult;
			if ((teleportResult = warp.canTeleport((Player) s)) != 0) {
				String reason = teleportResult == 1 ? "perms" : "money";
				msgSec(s, "cannot_teleport." + reason, Placeholders.c().add("warp", warp.name()));
				return;
			}
			((Player) s).teleport(warp.location().toLocation());
			msgSec(s, "self", Placeholders.c().add("warp", warp.name()));
		}).fallback((s, structure, args) -> {
			offlinePlayer(s, args[1]);
		}).argument("-s", (s, structure, args) -> { // cmd [warp] -s
			if (!(s instanceof Player)) { // must be player
				help(s, "usage");
				return;
			}

			WarpHolder warp = WarpManager.find(args[0]);
			int teleportResult;
			if ((teleportResult = warp.canTeleport((Player) s)) != 0) {
				String reason = teleportResult == 1 ? "perms" : "money";
				msgSec(s, "cannot_teleport." + reason, Placeholders.c().add("warp", warp.name()));
				return;
			}
			((Player) s).teleport(warp.location().toLocation());
		}).parent() // cmd [warp]
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [warp] [entity_selector]
					WarpHolder warp = WarpManager.find(args[0]);
					for (Player p : playerSelectors(s, args[1])) {
						int teleportResult;
						if ((teleportResult = warp.canTeleport(p)) != 0) {
							String reason = teleportResult == 1 ? "perms" : "money";
							msgSec(s, "cannot_teleport." + reason, Placeholders.c().add("warp", warp.name()));
							return;
						}
						p.teleport(warp.location().toLocation());
						msgSec(s, "other.sender", Placeholders.c().add("warp", warp.name()).add("target", p.getName()));
						msgSec(p, "other.target", Placeholders.c().add("warp", warp.name()).add("target", s.getName()));
					}
				}).permission(permission("other"))
				.argument("-s", (s, structure, args) -> { // cmd [warp] [entity_selector] -s
					WarpHolder warp = WarpManager.find(args[0]);
					for (Player p : playerSelectors(s, args[1])) {
						int teleportResult;
						if ((teleportResult = warp.canTeleport(p)) != 0) {
							String reason = teleportResult == 1 ? "perms" : "money";
							msgSec(s, "cannot_teleport." + reason, Placeholders.c().add("warp", warp.name()));
							return;
						}
						p.teleport(warp.location().toLocation());
					}
				}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure();
	}

	@Override
	public void disabling() {
		for (WarpHolder warp : WarpManager.registered_warps)
			storedWarps.set("warp." + warp.name() + ".owner", warp.owner()).set("warp." + warp.name() + ".location", warp.location()).set("warp." + warp.name() + ".icon", warp.icon()).set("warp." + warp.name() + ".permission", warp.permission()).set("warp." + warp.name() + ".cost", warp.cost());
		storedWarps.save(DataType.YAML);
		WarpManager.registered_warps.clear();
	}

	@Override
	public String configSection() {
		return "warp";
	}

}

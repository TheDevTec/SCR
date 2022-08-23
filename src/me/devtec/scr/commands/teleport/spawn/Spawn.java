package me.devtec.scr.commands.teleport.spawn;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.teleport.warp.Warp;
import me.devtec.scr.commands.tpsystem.TpSystem;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.dataholder.DataType;
import me.devtec.theapi.bukkit.game.Position;

public class Spawn implements ScrCommand {
	protected static Position spawn;

	@Override
	public void init(List<String> cmds) {
		spawn = Warp.storedWarps.getAs("spawn", Position.class);
		if (spawn == null)
			spawn = new Position(Bukkit.getWorlds().get(0).getSpawnLocation());

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (s instanceof Player) {
				TpSystem.teleport((Player)s, spawn.toLocation());
				//((Player) s).teleport(spawn.toLocation());
				msgSec(s, "self");
			} else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")).argument("-s", (s, structure, args) -> { // cmd -s
			if (s instanceof Player)
				TpSystem.teleport((Player)s, spawn.toLocation());
				//((Player) s).teleport(spawn.toLocation());
			else
				help(s, "usage");
		}).parent().selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [entity_selector]
			Location loc = spawn.toLocation();
			for (Player p : playerSelectors(s, args[0])) {
				//p.teleport(loc);
				TpSystem.teleport(p, loc);
				msgSec(s, "other.sender", Placeholders.c().addPlayer("target", p));
				msgSec(p, "other.target", Placeholders.c().addPlayer("player", s));
			}
		}).permission(permission("other")).argument("-s", (s, structure, args) -> { // cmd [entity_selector] -s
			Location loc = spawn.toLocation();
			for (Player p : playerSelectors(s, args[0]))
				TpSystem.teleport(p, loc);
				//p.teleport(loc);
		}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure();
	}

	@Override
	public void disabling() {
		Warp.storedWarps.set("spawn", spawn);
		Warp.storedWarps.save(DataType.YAML);
	}

	@Override
	public String configSection() {
		return "spawn";
	}

}

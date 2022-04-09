package me.devtec.scr.commands.teleport.warp;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Warp implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			List<WarpHolder> warps = WarpManager.availableWarps(s);
			StringBuilder warpNames = new StringBuilder();
			for(WarpHolder warp : warps) {
				if(warpNames.length()!=0)
					warpNames.append(Loader.translations.getString(configSection()+".list_split"));
				warpNames.append(warp.name());
			}
			msgConfig(s, configSection()+".list", warpNames);
		}).argument(null, (s, structure, args) -> { // cmd [any string]
			if(!(s instanceof Player)) { //must be player
				help(s, 0);
				return;
			}
			
			WarpHolder warp = WarpManager.find(args[0]);
			if(warp == null) {
				msgConfig(s, "warp.notFound", args[0]);
				return;
			}
			int teleportResult;
			if((teleportResult=warp.canTeleport((Player)s)) != 0) {
				String reason = teleportResult == 1 ? "perms" : "money";
				msgConfig(s, "warp.cannot_teleport."+reason, args[0]);
				return;
			}
			((Player)s).teleport(warp.location());
			msgConfig(s, configSection()+".warp.self", warp.name());
		}).argument("-s", (s, structure, args) -> { // cmd [any string] -s
			if(!(s instanceof Player)) { //must be player
				help(s, 0);
				return;
			}
			
			WarpHolder warp = WarpManager.find(args[0]);
			if(warp == null) {
				msgConfig(s, "warp.notFound", args[0]);
				return;
			}
			int teleportResult;
			if((teleportResult=warp.canTeleport((Player)s)) != 0) {
				String reason = teleportResult == 1 ? "perms" : "money";
				msgConfig(s, "warp.cannot_teleport."+reason, args[0]);
				return;
			}
			((Player)s).teleport(warp.location());
		}).parent()
		.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [any string] [entity_selector]
			WarpHolder warp = WarpManager.find(args[0]);
			if(warp == null) {
				msgConfig(s, "warp.notFound", args[0]);
				return;
			}
			for(Player p : playerSelectors(s, args[0])) {
				int teleportResult;
				if((teleportResult=warp.canTeleport(p)) != 0) {
					String reason = teleportResult == 1 ? "perms" : "money";
					msgConfig(s, "warp.cannot_teleport."+reason, args[0]);
					return;
				}
				p.teleport(warp.location());
				msgConfig(s, configSection()+".warp.other.sender", warp.name(), p.getName());
				msgConfig(p, configSection()+".warp.other.target", warp.name(), p.getName());
			}
		}).argument("-s", (s, structure, args) -> { // cmd [any string] [entity_selector] -s
			WarpHolder warp = WarpManager.find(args[0]);
			if(warp == null) {
				msgConfig(s, "warp.notFound", args[0]);
				return;
			}
			for(Player p : playerSelectors(s, args[0])) {
				int teleportResult;
				if((teleportResult=warp.canTeleport(p)) != 0) {
					String reason = teleportResult == 1 ? "perms" : "money";
					msgConfig(s, "warp.cannot_teleport."+reason, args[0]);
					return;
				}
				p.teleport(warp.location());
			}
		}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "warp";
	}
	
}

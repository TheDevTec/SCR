package me.devtec.scr.commands.teleport.warp;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;

public class DeleteWarp implements ScrCommand {

	@Override
	public void init(int cd, List<String> cmds) {
		cooldownMap.put(CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, "usage");
		}).permission(permission("cmd")).fallback((s, structure, args) -> {
			msgSec(s, "not_exists", Placeholders.c().add("warp", args[0]));
		}).callableArgument((s, structure, args) -> WarpManager.availableWarpNames(s), (s, structure, args) -> { // cmd [warp]
			WarpHolder warp = WarpManager.find(args[0]);
			WarpManager.delete(warp.name());
			msgSec(s, "deleted", Placeholders.c().add("warp", warp.name()));
		}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure(), new CooldownHolder(this, cd));
	}

	@Override
	public String configSection() {
		return "delwarp";
	}

}

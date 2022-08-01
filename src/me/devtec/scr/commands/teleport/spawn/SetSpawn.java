package me.devtec.scr.commands.teleport.spawn;

import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;
import me.devtec.shared.utility.StringUtils.FormatType;
import me.devtec.theapi.bukkit.game.Position;

public class SetSpawn implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			Spawn.spawn = new Position(s);
			msg(s, configSection(),
					Placeholders.c().add("world", Spawn.spawn.getWorldName()).add("x", StringUtils.formatDouble(FormatType.NORMAL, Spawn.spawn.getX()))
							.add("y", StringUtils.formatDouble(FormatType.NORMAL, Spawn.spawn.getY())).add("z", StringUtils.formatDouble(FormatType.NORMAL, Spawn.spawn.getZ()))
							.add("yaw", StringUtils.formatDouble(FormatType.NORMAL, Spawn.spawn.getYaw())).add("pitch", StringUtils.formatDouble(FormatType.NORMAL, Spawn.spawn.getPitch())));
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure();
	}

	@Override
	public String configSection() {
		return "setspawn";
	}

}

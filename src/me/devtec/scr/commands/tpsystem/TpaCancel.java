package me.devtec.scr.commands.tpsystem;

import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.tpsystem.requests.TeleportRequest;
import me.devtec.shared.commands.structures.CommandStructure;

public class TpaCancel implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> {
			TeleportRequest req = API.getUser(s).getSendTpReq();
			if (req != null)
				req.cancel();
			else
				msg(s, "teleportreq.norequest");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tpacancel";
	}
}
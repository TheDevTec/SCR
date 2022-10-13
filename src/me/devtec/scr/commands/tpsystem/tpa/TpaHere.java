package me.devtec.scr.commands.tpsystem.tpa;

import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.tpsystem.requests.TpahereRequest;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class TpaHere implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> {
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.fallback((s, structure, args) -> { // /tpahere [player]
					offlinePlayer(s, args[0]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // /tpahere [player]
					for (Player p : playerSelectors(s, args[0])) {
						if (API.getUser(p).getTpReqOf(API.getUser(s)) != null) {
							msg(s, "teleportreq.tpahere.already-send", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
							continue;
						}
						TpahereRequest req = new TpahereRequest(API.getUser(s), API.getUser(p));
						API.getUser(s).addSendTpReq(req);
						API.getUser(p).addTpReq(req);
						msg(s, "teleportreq.tpahere.send.sender", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
						msg(p, "teleportreq.tpahere.send.receiver", Placeholders.c().addPlayer("target", s).addPlayer("player", p));
					}
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tpahere";
	}
}
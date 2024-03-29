package me.devtec.scr.commands.tpsystem.tpa;

import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
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
					Collection<? extends Player> players = playerSelectors(s, args[0]);
					User whoSending = API.getUser(s);
					for (Player p : players) {
						if (p.getUniqueId().equals(s.getUniqueId())) {
							if (players.size() == 1)
								msg(s, "teleportreq.send-request-to-self", Placeholders.c().addPlayer("player", p));
							continue;
						}
						User target = API.getUser(p);
						if (target.getTpReqOf(whoSending) != null) {
							if (players.size() == 1)
								msg(s, "teleportreq.tpa.already-send", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
							continue;
						}
						TpahereRequest req = new TpahereRequest(whoSending, target);
						whoSending.addSendTpReq(req);
						target.addTpReq(req);
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
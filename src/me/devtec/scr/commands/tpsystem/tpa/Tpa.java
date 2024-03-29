package me.devtec.scr.commands.tpsystem.tpa;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.tpsystem.requests.TpaRequest;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Tpa implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> {
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.selector(Selector.PLAYER, (s, structure, args) -> {
					Player p = Bukkit.getPlayer(args[0]);
					if (p.getUniqueId().equals(s.getUniqueId())) {
						msg(s, "teleportreq.send-request-to-self", Placeholders.c().addPlayer("player", p));
						return;
					}
					User whoSending = API.getUser(s);
					User target = API.getUser(p);
					if (target.getTpReqOf(whoSending) != null) {
						msg(s, "teleportreq.tpa.already-send", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
						return;
					}
					TpaRequest req = new TpaRequest(whoSending, target);
					whoSending.addSendTpReq(req);
					target.addTpReq(req);
					msg(s, "teleportreq.tpa.send.sender", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
					msg(p, "teleportreq.tpa.send.receiver", Placeholders.c().addPlayer("target", s).addPlayer("player", p));
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tpa";
	}
}
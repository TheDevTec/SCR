package me.devtec.scr.commands.tpsystem.tpa;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
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
					TpaRequest req = new TpaRequest(API.getUser(s), API.getUser(p));
					API.getUser(s).addSendTpReq(req);
					API.getUser(p).addTpReq(req);
					msgSec(s, "sender", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
					msgSec(s, "receiver", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tpa";
	}
}
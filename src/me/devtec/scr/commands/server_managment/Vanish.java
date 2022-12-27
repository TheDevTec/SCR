package me.devtec.scr.commands.server_managment;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.listeners.additional.PlayerJoin;
import me.devtec.scr.listeners.additional.PlayerQuit;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class Vanish implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (s instanceof Player)
				toggle(s, (Player) s);
			else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.selector(Selector.BOOLEAN, (s, structure, args) -> {
					if (s instanceof Player)
						apply(s, (Player) s, StringUtils.getBoolean(args[0]));
					else
						help(s, "usage");
				}).first().selector(Selector.PLAYER, (s, structure, args) -> {
					toggle(s, Bukkit.getPlayer(args[0]));
				}).selector(Selector.BOOLEAN, (s, structure, args) -> {
					apply(s, Bukkit.getPlayer(args[0]), StringUtils.getBoolean(args[1]));
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	private void toggle(CommandSender sender, Player s) {
		apply(sender, s, !isVanished(s));
	}

	private void apply(CommandSender sender, Player s, boolean status) {
		API.getUser(s).setVanished(status);
		if (!status) {
			if (PlayerJoin.config.getBoolean("enabled"))
				MessageUtils.msgConfig(s, "broadcast", PlayerJoin.config, Placeholders.c().addPlayer("player", s), whoCannotSee(s));
		} else if (PlayerQuit.config.getBoolean("enabled"))
			MessageUtils.msgConfig(s, "broadcast", PlayerQuit.config, Placeholders.c().addPlayer("player", s), whoCannotSee(s));

		if (sender.equals(s))
			msgSec(s, status ? "self.enabled" : "self.disabled");
		else {
			msgSec(sender, status ? "other.enabled.sender" : "other.disabled.sender", Placeholders.c().addPlayer("target", s));
			msgSec(s, status ? "other.enabled.receiver" : "other.disabled.receiver", Placeholders.c().addPlayer("target", sender));
		}
	}

	private CommandSender[] whoCannotSee(Player s) {
		List<CommandSender> players = new ArrayList<>();
		for (Player player : BukkitLoader.getOnlinePlayers())
			if (!player.hasPermission("scr.command.vanish"))
				players.add(player);
		return players.toArray(new CommandSender[0]);
	}

	private boolean isVanished(Player s) {
		return API.isVanished(s);
	}

	@Override
	public String configSection() {
		return "vanish";
	}
}

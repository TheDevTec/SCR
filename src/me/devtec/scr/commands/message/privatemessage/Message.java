package me.devtec.scr.commands.message.privatemessage;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.listeners.commands.PrivateMessageListener;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;

public class Message implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		Loader.plugin.getLogger().info("[PrivateMessage] Using message listener");
		Loader.registerListener(new PrivateMessageListener());

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (MessageManager.chatLock.containsKey(s.getName()))
				MessageManager.chatLock(s, null);
			else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.fallback((s, structure, args) -> { // /pm [player]
					offlinePlayer(s, args[0]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // /pm [player]
					for (Player p : playerSelectors(s, args[0]))
						MessageManager.message(s, p, null);
				}).priority(1).argument(null, (s, structure, args) -> { // /pm [player] [message]
					for (Player p : playerSelectors(s, args[0]))
						MessageManager.message(s, p, StringUtils.buildString(1, args));

				}).parent() // pm [player]
				.parent()// cmd
				.argument("CONSOLE", (s, structure, args) -> { // /pm [console]
					MessageManager.message(s, Bukkit.getConsoleSender(), null);
				}).priority(2).argument(null, -1, (s, structure, args) -> { // /pm [console] [message]
					MessageManager.message(s, Bukkit.getConsoleSender(), StringUtils.buildString(1, args));

				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "privateMessage";
	}

}

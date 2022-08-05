package me.devtec.scr.commands.message.privatemessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.shared.dataholder.Config;
import me.devtec.theapi.bukkit.BukkitLoader;

public class MessageManager {

	// IGNORE
	public static void ignore(CommandSender sender, Player target) {
		User s = API.getUser(sender);
		if (s.isIgnoring(target.getName())) {
			s.removeIgnore(target.getName());
			MessageUtils.message(sender, "ignore.remove", Placeholders.c().addPlayer("target", target));
		} else {
			s.addIgnore(target.getName());
			MessageUtils.message(sender, "ignore.add", Placeholders.c().addPlayer("target", target));
		}
	}

	// SocialSpy
	public static void socialSpy(CommandSender sender, String target, boolean message) { // enabling or disabling
		Config c = me.devtec.shared.API.getUser(target);
		if (c.getBoolean("privateMessage.socialspy")) {
			c.set("privateMessage.socialspy", false);
			if (message)
				if (sender.getName().equalsIgnoreCase(target))
					MessageUtils.message(sender, "socialSpy.self.disabled", null);
				else
					MessageUtils.message(sender, "socialSpy.other.sender.disabled", Placeholders.c().add("target", target));
		} else {
			c.set("privateMessage.socialspy", true);
			if (message)
				if (sender.getName().equalsIgnoreCase(target))
					MessageUtils.message(sender, "socialSpy.self.enabled", null);
				else
					MessageUtils.message(sender, "socialSpy.other.sender.enabled", Placeholders.c().add("target", target));
		}
		c.save();
	}

	public static void socialSpy(CommandSender sender, CommandSender target, boolean message) { // enabling or disabling
		Config c = me.devtec.shared.API.getUser(target.getName());
		if (c.getBoolean("privateMessage.socialspy")) {
			c.set("privateMessage.socialspy", false);
			if (message)
				if (sender.getName().equalsIgnoreCase(target.getName()))
					MessageUtils.message(sender, "socialSpy.self.disabled", null);
				else {
					MessageUtils.message(sender, "socialSpy.other.sender.disabled", Placeholders.c().addPlayer("target", target));
					MessageUtils.message(target, "socialSpy.other.target.disabled", Placeholders.c().addPlayer("player", sender));
				}
		} else {
			c.set("privateMessage.socialspy", true);
			if (message)
				if (sender.getName().equalsIgnoreCase(target.getName()))
					MessageUtils.message(sender, "socialSpy.self.enabled", null);
				else {
					MessageUtils.message(sender, "socialSpy.other.sender.enabled", Placeholders.c().add("target", target));
					MessageUtils.message(target, "socialSpy.other.target.enabled", Placeholders.c().addPlayer("player", sender));
				}
		}
		c.save();
	}

	// MESSAGE

	public static Map<String, String> replyList = new HashMap<>(); // sender | target
	public static Map<String, String> chatLock = new HashMap<>(); // sender | target

	/*
	 * message(sender, target, null) - locking chat on target message(sender, null,
	 * message) - reply message(sender, target, message) - normal message (+adding
	 * target to reply)
	 */
	public static void message(CommandSender sender, CommandSender target, String message) { // Main thing
		if (sender != null && target != null && message == null) { // lock
			chatLock(sender, target);
			return;
		}
		if (sender != null && target == null && message != null) { // reply
			reply(sender, message);
			addTarget(target, sender);
		}
		if (sender != null && target != null && message != null) { // msg player message....
			addTarget(sender, target);
			addTarget(target, sender);
			sendMessage(sender, target, message);
		}

	}

	private static void addTarget(CommandSender sender, CommandSender target) { // reply, msg Player -> locking player to reply
		if (target == null || replyList.containsKey(sender instanceof Player ? sender.getName() : "console"))
			replyList.remove(sender instanceof Player ? sender.getName() : "console");
		replyList.put(sender instanceof Player ? sender.getName() : "console", target instanceof Player ? target.getName() : "console");
	}

	public static void chatLock(CommandSender sender, CommandSender target) { // locking chat to send message to player
		if (chatLock.containsKey(sender.getName()) || target == null) { // removing lock
			chatLock.remove(sender.getName());
			addTarget(sender, null); // removing
			MessageUtils.message(sender, "privateMessage.chatlock.disabled", Placeholders.c().addPlayer("target", target));
		} else { // adding lock
			chatLock.put(sender.getName(), target.getName());
			addTarget(sender, target);
			MessageUtils.message(sender, "privateMessage.chatlock.enabled", Placeholders.c().addPlayer("target", target));
		}
	}

	private static void sendMessage(CommandSender sender, CommandSender target, String message) {
		// if(target instanceof Player) {
		MessageUtils.message(sender, "privateMessage.formats.sender",
				Placeholders.c().addPlayer("player", sender).addPlayer("from", sender).addPlayer("to", target).addPlayer("target", target).add("message", message));
		MessageUtils.message(target, "privateMessage.formats.target",
				Placeholders.c().addPlayer("player", sender).addPlayer("from", sender).addPlayer("to", target).addPlayer("target", target).add("message", message));
		// }
		/*
		 * else { //COSNOLE MessageUtils.message(sender,
		 * "privateMessage.formats.sender", Placeholders.c().addPlayer("player", sender)
		 * .addPlayer("from", sender).add("to", "CONSOLE") .add("target",
		 * "CONSOLE").add("message", message) ); MessageUtils.message(target,
		 * "privateMessage.formats.target", Placeholders.c().addPlayer("player", sender)
		 * .addPlayer("from", sender).add("to", "CONSOLE") .add("target",
		 * "CONSOLE").add("message", message) ); }
		 */
		socialSpyMessage(sender, target, message);
	}

	private static void reply(CommandSender sender, String message) {
		String t = replyList.get(sender instanceof Player ? sender.getName() : "console");
		CommandSender target;

		if (t == null) { // offline
			MessageUtils.message(sender, "privateMessage.noreply", Placeholders.c().add("message", message));
			return;
		}

		if (t.equalsIgnoreCase("console"))
			target = Bukkit.getConsoleSender();
		else
			target = API.getPlayer(replyList.get(sender instanceof Player ? sender.getName() : "console"));

		if (target == null) { // offline
			MessageUtils.message(sender, "privateMessage.noreply", Placeholders.c().add("message", message));
			return;
		}

		// if(target instanceof Player) {
		MessageUtils.message(sender, "privateMessage.formats.sender",
				Placeholders.c().addPlayer("player", sender).addPlayer("from", sender).addPlayer("to", target).addPlayer("target", target).add("message", message));
		MessageUtils.message(target, "privateMessage.formats.target",
				Placeholders.c().addPlayer("player", sender).addPlayer("from", sender).addPlayer("to", target).addPlayer("target", target).add("message", message));
		// }
		/*
		 * else { //COSNOLE MessageUtils.message(sender,
		 * "privateMessage.formats.sender", Placeholders.c().addPlayer("player", sender)
		 * .addPlayer("from", sender).add("to", "CONSOLE") .add("target",
		 * "CONSOLE").add("message", message) ); MessageUtils.message(target,
		 * "privateMessage.formats.target", Placeholders.c().addPlayer("player", sender)
		 * .addPlayer("from", sender).add("to", "CONSOLE") .add("target",
		 * "CONSOLE").add("message", message) );
		 * 
		 * }
		 */
		socialSpyMessage(sender, target, message);

	}

	private static void socialSpyMessage(CommandSender sender, CommandSender target, String message) {
		MessageUtils.message(Bukkit.getConsoleSender(), "privateMessage.formats.socialspy", Placeholders.c().addPlayer("player", sender).addPlayer("target", target).add("message", message));

		for (Player p : BukkitLoader.getOnlinePlayers())
			// privateMessage.ignorelist.
			if (p != sender && p != target && me.devtec.shared.API.getUser(p.getName()).getBoolean("privateMessage.socialspy"))
				MessageUtils.message(p, "privateMessage.formats.socialspy", Placeholders.c().addPlayer("player", sender).addPlayer("target", target).add("message", message));
	}

	// HELPOP PART:

	public static List<String> acChatLock = new ArrayList<>(); // sender

	public static void acChatLock(CommandSender sender) { // locking chat to send helpops
		if (acChatLock.contains(sender.getName())) { // removing lock
			acChatLock.remove(sender.getName());
			MessageUtils.message(sender, "helpop.chatlock.disabled", null);
		} else { // adding lock
			acChatLock.add(sender.getName());
			MessageUtils.message(sender, "helpop.chatlock.enabled", null);
		}
	}

	public static void sendhelpop(CommandSender sender, String message) {
		MessageUtils.message(Bukkit.getConsoleSender(), "helpop.formats.reciever", Placeholders.c().addPlayer("player", sender).add("message", message));

		for (Player p : BukkitLoader.getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(sender.getName())) {
				MessageUtils.message(p, "helpop.formats.sender", Placeholders.c().addPlayer("player", sender).add("message", message));
				continue;
			}
			if (p.hasPermission(Loader.commands.getString("helpop.permission.see")))
				MessageUtils.message(p, "helpop.formats.reciever", Placeholders.c().addPlayer("player", sender).add("message", message));
		}
	}
}

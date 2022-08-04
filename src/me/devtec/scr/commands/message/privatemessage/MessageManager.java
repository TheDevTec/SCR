package me.devtec.scr.commands.message.privatemessage;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.shared.dataholder.Config;
import me.devtec.theapi.bukkit.BukkitLoader;

public class MessageManager {

	
	//IGNORE
	public static void ignore(CommandSender sender, Player target) {
		User s = API.getUser(sender);
		if(s.isIgnoring( target.getName() )) {
			s.removeIgnore(target.getName());
			MessageUtils.message(sender, "ignore.remove", Placeholders.c().addPlayer("target", target));
		}
		else {
			s.addIgnore(target.getName());
			MessageUtils.message(sender, "ignore.add", Placeholders.c().addPlayer("target", target));
		}
	}
	//SocialSpy
	public static void socialSpy(CommandSender sender, String target, boolean message) { //enabling or disabling
		Config c = me.devtec.shared.API.getUser(target);
		if(c.getBoolean("privateMessage.socialspy")) {
			c.set("privateMessage.socialspy", false);
			if(message) {
				if(sender.getName().equalsIgnoreCase(target))
					MessageUtils.message(sender, "socialSpy.self.disabled", null);
				else {
					MessageUtils.message(sender, "socialSpy.other.sender.disabled", 
							Placeholders.c().add("target", target));
				}
			}
		}else {
			c.set("privateMessage.socialspy", true);
			if(message) {
				if(sender.getName().equalsIgnoreCase(target))
					MessageUtils.message(sender, "socialSpy.self.enabled", null);
				else {
					MessageUtils.message(sender, "socialSpy.other.sender.enabled", 
							Placeholders.c().add("target", target));
				}
			}
		}
		c.save();
	}
	public static void socialSpy(CommandSender sender, CommandSender target, boolean message) { //enabling or disabling
		Config c = me.devtec.shared.API.getUser(target.getName());
		if(c.getBoolean("privateMessage.socialspy")) {
			c.set("privateMessage.socialspy", false);
			if(message) {
				if(sender.getName().equalsIgnoreCase(target.getName()))
					MessageUtils.message(sender, "socialSpy.self.disabled", null);
				else {
					MessageUtils.message(sender, "socialSpy.other.sender.disabled", 
							Placeholders.c().addPlayer("target", target));
					MessageUtils.message(target, "socialSpy.other.target.disabled", 
							Placeholders.c().addPlayer("player", sender));
				}
			}
		}else {
			c.set("privateMessage.socialspy", true);
			if(message) {
				if(sender.getName().equalsIgnoreCase(target.getName()))
					MessageUtils.message(sender, "socialSpy.self.enabled", null);
				else {
					MessageUtils.message(sender, "socialSpy.other.sender.enabled", 
							Placeholders.c().add("target", target));
					MessageUtils.message(target, "socialSpy.other.target.enabled", 
							Placeholders.c().addPlayer("player", sender));
				}
			}
		}
		c.save();
	}
	
	//MESSAGE

	public static Map<String, String> replyList = new HashMap<>(); // sender | target
	public static Map<String, String> chatLock = new HashMap<>(); // sender | target
	
	/*
	 * message(sender, target, null) - locking chat on target
	 * message(sender, null, message) - reply
	 * message(sender, target, message) - normal message (+adding target to reply)
	 */
	public static void message(CommandSender sender, Player target, String message) { //Main thing
		if(sender !=null && target!=null && message==null) { //lock
			chatLock(sender, target);
			return;
		}
		if(sender!=null && target==null && message!=null) { //reply
			reply(sender, message);
		}
		if(sender!=null && target!=null && message!=null) { //msg player message....
			addTarget(sender, target);
			sendMessage(sender, target, message);
		}
		
	}
	private static void addTarget(CommandSender sender, Player target) { // reply, msg Player -> locking player to reply
		if(target==null || replyList.containsKey(sender.getName()))
			replyList.remove(sender.getName());
		replyList.put(sender.getName(), target.getName());
	}
	
	public static void chatLock(CommandSender sender, Player target) { // locking chat to send message to player
		if(chatLock.containsKey(sender.getName()) || target == null) { // removing lock
			chatLock.remove(sender.getName());
			addTarget(sender, null); //removing
			MessageUtils.message(sender, "privateMessage.chatlock.disabled", Placeholders.c().addPlayer("target", target));
		} else { //adding lock
			chatLock.put(sender.getName(), target.getName());
			addTarget(sender, target);
			MessageUtils.message(sender, "privateMessage.chatlock.enabled", null);
		}
	}
	
	private static void sendMessage(CommandSender sender, Player target, String message) {
		
		MessageUtils.message(sender, "privateMessage.formats.sender", Placeholders.c().addPlayer("player", sender)
				.addPlayer("target", target).add("message", message) );
		MessageUtils.message(sender, "privateMessage.formats.target", Placeholders.c().addPlayer("player", sender)
				.addPlayer("target", target).add("message", message) );
		socialSpyMessage(sender, target, message);
	}
	
	private static void reply(CommandSender sender, String message) {
		Player target = API.getPlayer(replyList.get(sender.getName()));
		
		if(target == null) { // offline
			MessageUtils.message(sender, "privateMessage.noreply", Placeholders.c().add("target", target).add("message", message) );
			return;
		}
		
		MessageUtils.message(sender, "privateMessage.formats.sender", Placeholders.c().addPlayer("player", sender)
				.addPlayer("target", target).add("message", message) );
		MessageUtils.message(sender, "privateMessage.formats.target", Placeholders.c().addPlayer("player", sender)
				.addPlayer("target", target).add("message", message) );
		socialSpyMessage(sender, target, message);
		
	}
	
	private static void socialSpyMessage(CommandSender sender, Player target, String message) {
		MessageUtils.message(Bukkit.getConsoleSender(), "privateMessage.formats.socialspy",
			Placeholders.c().addPlayer("player", sender).addPlayer("target", target).add("message", message) );
	
		for(Player p : BukkitLoader.getOnlinePlayers()) {
			//privateMessage.ignorelist.
			if(p!=sender && p!= target &&
					me.devtec.shared.API.getUser(p.getName()).getBoolean("privateMessage.socialspy") )
				MessageUtils.message(p, "privateMessage.formats.socialspy",
						Placeholders.c().addPlayer("player", sender).addPlayer("target", target).add("message", message) );
			continue;
		}
	}
}

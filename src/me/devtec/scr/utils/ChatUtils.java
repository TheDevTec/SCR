package me.devtec.scr.utils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.message.Sudo;
import me.devtec.scr.commands.message.Sudo.SudoType;
import me.devtec.scr.functions.Tablist;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.nms.NmsProvider.TitleAction;

public class ChatUtils {

	// LOCK
	public static void lockChat() { // TODO
		if (Loader.data.getBoolean("chatlock", false))
			Loader.data.set("chatlock", false);
		else
			Loader.data.set("chatlock", true);
		Loader.data.save();
	}

	public static boolean isChatLocked() {
		return Loader.data.getBoolean("chatlock");
	}

	// CHATFORMAT
	public static class ChatFormat {

		private static Config c = Loader.chat;

		public static boolean isEnabled() {
			return Loader.chat.getBoolean("format.enabled");
		}

		/*
		 * format: global: group: <group> user: <user> world: <world>: repeat...
		 */
		public static String getPath(Player p) {
			String world = p.getLocation().getWorld().getName();

			if (c.exists("format.world." + world)) { // World formats:
				if (c.exists("format.world." + world + ".user." + p.getName()))
					return "format.world." + world + ".user." + p.getName();
				if (c.exists("format.world." + world + ".group." + Tablist.getVaultGroup(p)))
					return "format.world." + world + ".group." + Tablist.getVaultGroup(p);
				if (c.exists("format.world." + world + ".global"))
					return "format.world." + world + ".global";
			}

			if (c.exists("format.user." + p.getName()))
				return "format.user." + p.getName();
			if (c.exists("format.group." + Tablist.getVaultGroup(p)))
				return "format.group." + Tablist.getVaultGroup(p);
			if (c.exists("format.global")) {
			}

			return "format.global";
		}
	}

	// NOTIFICATION
	public static class Notification {

		public static boolean isEnabled() {
			return Loader.chat.getBoolean("chatNotification.enabled");
		}
		
		public static String notificationReplace(Player pinger, String msg, Set<Player> targets) {
			for (Player player : targets)
				if (//!player.getUniqueId().equals(pinger.getUniqueId()) && 
						msg.contains(player.getName())) {
					boolean endsWithName = msg.endsWith(player.getName());
					
					String notificationColor = Loader.chat.exists("chatNotification.color")?
							Loader.chat.getString("chatNotification.color"):"§c";
					
					notify(pinger, player);

					String[] split = Pattern.compile(player.getName(), Pattern.CASE_INSENSITIVE).split(msg);
					if (split.length == 0) // Just Player
						return notificationColor + player.getName();
					
					String lastColors = StringUtils.getLastColors(split[0]);
					if (lastColors.isEmpty())
						lastColors = "§f";
					else {
						char[] chars = lastColors.toCharArray();
						lastColors = "";
						for (char c : chars)
							lastColors += "§" + c;
					}
					StringBuilder builder = new StringBuilder(split[0]);
					for (int i = 1; i < split.length; ++i) {
						builder.append(notificationColor).append(player.getName()).append(lastColors).append(split[i]);
						lastColors = StringUtils.getLastColors(lastColors + split[i]);
						if (lastColors.isEmpty())
							lastColors = "&f";
						else {
							char[] chars = lastColors.toCharArray();
							lastColors = "";
							for (char c : chars)
								lastColors += "§" + c;
						}
					}
					if (endsWithName)
						builder.append(notificationColor).append(player.getName());
					msg = builder.toString();
				}
			return msg;
		}
		
		private static void notify(Player pinger, Player target) {
			Sound sound = Loader.chat.exists("chatNotification.color")?
					Sound.valueOf(Loader.chat.getString("chatNotification.sound")):null;
			
			if(sound != null) //Sound
				target.playSound(target.getLocation(), sound, 5, 5);
			//Title & SubTitle
			if(Loader.chat.exists("chatNotification.title") || Loader.chat.exists("chatNotification.subtitle")) {
				String title = Loader.chat.exists("chatNotification.title")?Loader.chat.getString("chatNotification.title"):"";
				String subtitle = Loader.chat.exists("chatNotification.subtitle")?Loader.chat.getString("chatNotification.subtitle"):"";
				sendTitle(pinger, MessageUtils.placeholder(target, title, Placeholders.c()
						.addPlayer("pinger", pinger).addPlayer("player", pinger)
						.addPlayer("pinged", target).addPlayer("target", target) ),
						MessageUtils.placeholder(target, subtitle, Placeholders.c()
								.addPlayer("pinger", pinger).addPlayer("player", pinger)
								.addPlayer("pinged", target).addPlayer("target", target) ));
			}
			//ActionBar
			if(Loader.chat.exists("chatNotification.actionbar")) {
				sendActionBar(pinger, MessageUtils.placeholder(target, Loader.chat.getString("chatNotification.actionbar"), Placeholders.c()
						.addPlayer("pinger", pinger).addPlayer("player", pinger)
						.addPlayer("pinged", target).addPlayer("target", target) ));
			}
			//CMDS
			for(String cmd : Loader.chat.getStringList("chatNotification.commands")) {
				Sudo.sudoConsole(SudoType.COMMAND,  MessageUtils.placeholder(target, cmd, Placeholders.c()
						.addPlayer("pinger", pinger).addPlayer("player", pinger)
						.addPlayer("pinged", target).addPlayer("target", target) ));
			}
			//MSGS
			if(Loader.chat.exists("chatNotification.messages"))
				MessageUtils.msgConfig(target, "chatNotification.messages", Loader.chat, Placeholders.c()
						.addPlayer("pinger", pinger).addPlayer("player", pinger)
						.addPlayer("pinged", target).addPlayer("target", target));
		}
		
		public static void sendTitle(Player p, String firstLine, String nextLine) {
			if(p == null && firstLine == null && nextLine == null)
				return;
			if(firstLine.isEmpty() && nextLine.isEmpty() )
				return;
			if((firstLine == null && nextLine!=null) || (firstLine.isEmpty() && !nextLine.isEmpty()) ) firstLine=""; // Just in case...
			if((firstLine != null && nextLine==null) || !firstLine.isEmpty() && nextLine.isEmpty()) nextLine="";
			BukkitLoader.getPacketHandler().send(p, BukkitLoader.getNmsProvider().packetTitle(TitleAction.TITLE, StringUtils.colorize(firstLine)));
			BukkitLoader.getPacketHandler().send(p, BukkitLoader.getNmsProvider().packetTitle(TitleAction.SUBTITLE, StringUtils.colorize(nextLine)));
		}
		public static void sendActionBar(Player p, String text) {
			if(text == null)
				return;
			if(text.isEmpty())
				return;
			BukkitLoader.getPacketHandler().send(p, BukkitLoader.getNmsProvider().packetTitle(
					TitleAction.ACTIONBAR, StringUtils.colorize(text), 10, 20, 10));
		}
	}

	public static class Colors {
		private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

		public static String remove(String string) {
			if (string != null)
				string = string.replace("§", "&");
			return string;
		}

		private static final Pattern fixedSplit = Pattern.compile("(#[A-Fa-f0-9]{6}|[&§][Xx]([&§][A-Fa-f0-9]){6}|[&§][A-Fa-f0-9UuXx])");

		public static String colorize(String b, boolean sign, CommandSender dr) {
			String p = sign ? "sign" : "chat";
			if (Loader.config.getString("options.colors." + p + ".rainbow") != null && !Loader.config.getString("options.colors." + p + ".rainbow").equals("") && b.toLowerCase().contains("&u")
					&& dr.hasPermission(Loader.config.getString("options.colors." + p + ".rainbow"))) {
				StringBuilder d = new StringBuilder(b.length());
				String[] split = fixedSplit.split(b);
				// atempt to add colors to split
				Matcher m = fixedSplit.matcher(b);
				int id = 1;
				while (m.find())
					try {
						split[id] = m.group(1) + split[id++];
					} catch (Exception err) {
					}
				// colors
				for (String ff : split) {
					String lower = ff.toLowerCase();
					if (lower.contains("§u") || lower.contains("&u"))
						ff = StringUtils.colorize(ff.replaceAll("[§&][Uu]", ""));
					d.append(ff);
				}
				b = d.toString();
			}
			if (Ref.isNewerThan(15)) {
				if (Loader.config.getString("options.colors." + p + ".gradient") != null && !Loader.config.getString("options.colors." + p + ".gradient").equals("")
						&& dr.hasPermission(Loader.config.getString("options.colors." + p + ".gradient")))
					b = StringUtils.gradient(b);
				if (b.contains("#") || b.contains("&x"))
					if (Loader.config.getString("options.colors." + p + ".hex") != null && !Loader.config.getString("options.colors." + p + ".hex").equals("")
							&& dr.hasPermission(Loader.config.getString("options.colors." + p + ".hex"))) {
						b = b.replaceAll("&[xX]", "§x");
						Matcher match = pattern.matcher(b);
						while (match.find()) {
							String color = match.group();
							StringBuilder magic = new StringBuilder("§x");
							char[] c = color.substring(1).toCharArray();
							for (char value : c)
								magic.append("§" + Character.toLowerCase(value));
							b = b.replace(color, magic.toString());
						}
					}
			}
			if (Loader.config.getString("options.colors." + p + ".color") != null && !Loader.config.getString("options.colors." + p + ".color").equals("")
					&& dr.hasPermission(Loader.config.getString("options.colors." + p + ".color"))) {
				for (int i = 0; i < 10; ++i)
					b = b.replace("&" + i, "§" + i);
				b = b.replaceAll("&([aAbBcCdDeEfF])", "§$1");
			}
			if (dr.hasPermission(Loader.config.getString("options.colors." + p + ".format")))
				b = b.replaceAll("&([oOlLmMnNrR])", "§$1");
			if (dr.hasPermission(Loader.config.getString("options.colors." + p + ".magic")))
				b = b.replaceAll("&[kK]", "§k");
			return b;
		}
	}
}

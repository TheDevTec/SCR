package me.DevTec.ServerControlReloaded.Commands.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;

import java.util.Objects;

public class PrivateMessageManager {
	private static String s;
	public static void sendMessage(CommandSender who, CommandSender to, String message) {
		if(who instanceof Player) {
			if(to instanceof Player) {
				TheAPI.msg(Loader.config.getString("Format.SocialSpy")
				.replace("%player%", who.getName()).replace("%playername%", ((Player)who).getDisplayName()+"")
				.replace("%customname%", ((Player)who).getCustomName()+"")
				.replace("%target%", to.getName()).replace("%targetname%", ((Player)to).getDisplayName()+"")
				.replace("%targetcustomname%", ((Player)to).getCustomName()+"").replace("%message%", message), TheAPI.getConsole());
				for(Player ps : TheAPI.getOnlinePlayers())
					if(ps!=who && ps!=to && TheAPI.getUser(ps).getBoolean("socialspy"))
						TheAPI.msg(Loader.config.getString("Format.SocialSpy")
						.replace("%player%", who.getName()).replace("%playername%", ((Player)who).getDisplayName())
						.replace("%customname%", Objects.requireNonNull(((Player) who).getDisplayName()))
						.replace("%target%", to.getName()).replace("%targetname%", ((Player)to).getDisplayName())
						.replace("%message%", message), ps);
			}else {
				TheAPI.msg(Loader.config.getString("Format.SocialSpy")
				.replace("%player%", who.getName()).replace("%playername%", ((Player)who).getDisplayName()+"")
				.replace("%customname%", ((Player)who).getCustomName()+"")
				.replace("%target%", to.getName()).replace("%targetname%", to.getName())
				.replace("%targetcustomname%", to.getName()).replace("%message%", message), TheAPI.getConsole());
				for(Player ps : TheAPI.getOnlinePlayers())
					if(ps!=who && ps!=to && TheAPI.getUser(ps).getBoolean("socialspy"))
						TheAPI.msg(Loader.config.getString("Format.SocialSpy")
						.replace("%player%", who.getName()).replace("%playername%", ((Player)who).getDisplayName()+"")
						.replace("%customname%", ((Player)who).getCustomName()+"")
						.replace("%target%", to.getName()).replace("%targetname%", to.getName())
						.replace("%targetcustomname%", to.getName()).replace("%message%", message), ps);
			}
		}else {
			if(to instanceof Player) {
				TheAPI.msg(Loader.config.getString("Format.SocialSpy")
				.replace("%player%", who.getName()).replace("%playername%", who.getName())
				.replace("%customname%", who.getName())
				.replace("%target%", to.getName()).replace("%targetname%", ((Player)to).getDisplayName()+"")
				.replace("%targetcustomname%", ((Player)to).getCustomName()+"").replace("%message%", message), TheAPI.getConsole());
				for(Player ps : TheAPI.getOnlinePlayers())
					if(ps!=who && ps!=to && TheAPI.getUser(ps).getBoolean("socialspy"))
						TheAPI.msg(Loader.config.getString("Format.SocialSpy")
						.replace("%player%", to.getName()).replace("%playername%", who.getName())
						.replace("%customname%", who.getName())
						.replace("%target%", to.getName()).replace("%targetname%", ((Player)to).getDisplayName()+"")
						.replace("%targetcustomname%", ((Player)to).getCustomName()+"").replace("%message%", message), ps);
			}else {
				if(who != to && to!=TheAPI.getConsole() && who!=TheAPI.getConsole())
				TheAPI.msg(Loader.config.getString("Format.SocialSpy")
				.replace("%player%", who.getName()).replace("%playername%", who.getName())
				.replace("%customname%", who.getName())
				.replace("%target%", to.getName()).replace("%targetname%", to.getName())
				.replace("%targetcustomname%", to.getName()).replace("%message%", message), TheAPI.getConsole());
				for(Player ps : TheAPI.getOnlinePlayers())
					if(ps!=who && ps!=to && TheAPI.getUser(ps).getBoolean("socialspy"))
						TheAPI.msg(Loader.config.getString("Format.SocialSpy")
						.replace("%player%", who.getName()).replace("%playername%", who.getName())
						.replace("%customname%", who.getName())
						.replace("%target%", to.getName()).replace("%targetname%", to.getName())
						.replace("%targetcustomname%", to.getName()).replace("%message%", message), ps);
			}
		}
		TheAPI.msg(Loader.config.getString("Format.PrivateMessageTo")
				.replace("%from%", who.getName()).replace("%to%", to.getName()).replace("%message%", message),who);
		TheAPI.msg(Loader.config.getString("Format.PrivateMessageFrom")
				.replace("%from%", who.getName()).replace("%to%", to.getName()).replace("%message%", message),to);
	}
	
	public static void reply(CommandSender who, String message) {
		String tot = getReply(who);
		if(tot==null) {
			Loader.sendMessages(who, "PrivateMessage.NoReply");
			return;
		}
		Player to = TheAPI.getPlayerOrNull(tot);
		if(to!=null || tot.equalsIgnoreCase("console"))
			sendMessage(who, to==null?Bukkit.getConsoleSender():to, message);
		else
			Loader.notOnline(who, tot);
	}
	
	public static void setReply(CommandSender who, String to) {
		if(who==null)return;
		if(who instanceof Player)
			API.getSPlayer((Player)who).reply=to;
		else s = to;
	}

	public static String getReply(CommandSender who) {
		if(who instanceof Player)
			return API.getSPlayer((Player)who).reply;
		return s;
	}

	public static void setChatLock(Player s, boolean lock) {
		API.getSPlayer(s).lock=lock;
	}

	public static boolean hasChatLock(Player s) {
		return API.getSPlayer(s).lock;
	}

	public static void setLockType(Player s, String lock) {
		API.getSPlayer(s).type=lock;
	}

	public static String getLockType(Player s) {
		return API.getSPlayer(s).type;
	}
}

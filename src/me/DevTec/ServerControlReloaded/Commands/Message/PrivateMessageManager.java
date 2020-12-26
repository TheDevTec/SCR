package me.DevTec.ServerControlReloaded.Commands.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;

public class PrivateMessageManager {
	private static String s;
	public static void sendMessage(CommandSender who, CommandSender to, String message) {
		TheAPI.msg(Loader.config.getString("Format.PrivateMessageTo")
				.replace("%from%", who.getName()).replace("%to%", to.getName()).replace("%message%", message),who);
		TheAPI.msg(Loader.config.getString("Format.PrivateMessageFrom")
				.replace("%from%", who.getName()).replace("%to%", to.getName()).replace("%message%", message),to);
	}
	
	public static void reply(CommandSender who, String message) {
		String to = getReply(who);
		if(to==null) {
			Loader.sendMessages(who, "PrivateMessage.NoReply");
			return;
		}
		if(TheAPI.getPlayerOrNull(to)!=null || to.equalsIgnoreCase("console"))
		sendMessage(who, to.equalsIgnoreCase("console")?TheAPI.getConsole():TheAPI.getPlayerOrNull(to), message);
		else
			Loader.notOnline(who, to);
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

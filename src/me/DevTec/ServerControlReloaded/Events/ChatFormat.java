package me.DevTec.ServerControlReloaded.Events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Item;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.Colors;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsGUI;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PlaceholderAPI.PlaceholderAPI;
import me.DevTec.TheAPI.Utils.DataKeeper.User;


public class ChatFormat implements Listener {
	static Loader plugin = Loader.getInstance;

	public static String r(Player p, String s, String msg) {
		s=s.replace("&u", "&<U>");
		s = TabList.replace(s, p, false);
		if (msg != null)
			s=s.replace("%message%", r(msg.replace("&u", "&<UU>"), p));
		s=s.replace("&<U>", "&u");
		if(p.hasPermission(Loader.config.getString("Options.Colors.Chat.Permission.Rainbow")))
			s=TheAPI.colorize(s.replace("&<UU>", "&u"));
		s=s.replace("&<UU>", "&u");
		return s;
	}

	public static String r(String msg, CommandSender p) {
		if (setting.color_chat)
			return Colors.colorize(msg, false, p);
		else
			return msg;
	}

	@EventHandler
	public void set(PlayerChatEvent e) {
		Player p = e.getPlayer();
		Loader.setupChatFormat(p);
		if (TheAPI.getCooldownAPI(p.getName()).getStart("world-create") != -1) {
			e.setCancelled(true);
			if (e.getMessage().toLowerCase().equals("cancel")) {
				User d = TheAPI.getUser(p);
				TheAPI.getCooldownAPI(p.getName()).removeCooldown("world-create");
				d.setAndSave("MultiWorlds-Create", null);
				d.setAndSave("MultiWorlds-Generator", null);
				TheAPI.sendTitle(p,"", "&6Cancelled");
				return;
			}
			if (TheAPI.getCooldownAPI(p.getName()).expired("world-create")) {
				TheAPI.getCooldownAPI(p.getName()).removeCooldown("world-create");
				MultiWorldsGUI.openInvCreate(p);
			} else {
				TheAPI.getCooldownAPI(p.getName()).removeCooldown("world-create");
				TheAPI.getUser(p).setAndSave("MultiWorlds-Create", Colors.remove(e.getMessage()));
				MultiWorldsGUI.openInvCreate(p);
			}
		}
		String msg = r(e.getMessage(), p);
		e.setMessage(msg);
		if (setting.lock_chat && !p.hasPermission("ServerControl.ChatLock")) {
			e.setCancelled(true);
			Loader.sendMessages(p, "ChatLock.IsLocked");
			Loader.sendBroadcasts(p, "ChatLock.Message", Placeholder.c().add("%player%", p.getName())
					.add("%playername%", p.getDisplayName()).add("%message%", e.getMessage()), "ServerControl.ChatLock.Notify");
			return;
		}
		if (Loader.config.getBoolean("Chat-Groups-Enabled")) {
			String format = PlaceholderAPI.setPlaceholders(p, Loader.config.getString("Chat-Groups." + Loader.get(p,Item.GROUP) + ".Chat"));
			if (format != null) {
				format=(r(p, format, msg));
				e.setFormat(format.replace("%", "%%"));
			}
		}

	}
}
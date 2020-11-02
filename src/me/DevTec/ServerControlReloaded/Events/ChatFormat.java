package me.DevTec.ServerControlReloaded.Events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

@SuppressWarnings("deprecation")
public class ChatFormat implements Listener {
	static Loader plugin = Loader.getInstance;
	String m;

	public static String r(Player p, String s, String msg, boolean a) {
		s = TabList.replace(s, p);
		if (msg != null)
			s = s.replace("%message%", msg);
		return s;
	}

	public static String r(String msg, CommandSender p) {
		if (setting.color_chat)
			return Colors.colorize(msg, false, p);
		else
			return msg;
	}

	@EventHandler(priority = EventPriority.LOW)
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
		msg = msg.replace("%", "%%");
		e.setMessage(msg);
		if (setting.lock_chat) {
			if (!p.hasPermission("ServerControl.ChatLock")) {
				e.setCancelled(true);
				Loader.sendMessages(p, "ChatLock.IsLocked");
				Loader.sendBroadcasts(p, "ChatLock.Message", Placeholder.c().add("%player%", p.getName())
						.add("%playername%", p.getDisplayName()).add("%message%", e.getMessage()), "ServerControl.ChatLock.Notify");
			}
		}
		if (Loader.config.getBoolean("Chat-Groups-Enabled") == true) {
			if (Loader.config.getString("Chat-Groups." + Loader.get(p,Item.GROUP) + ".Chat") != null) {
				m = Loader.config.getString("Chat-Groups." +Loader.get(p,Item.GROUP) + ".Chat");

				String format = PlaceholderAPI.setPlaceholders(p,
						Loader.config.getString("Chat-Groups." + Loader.get(p,Item.GROUP) + ".Chat"));
				if (format != null)
					e.setFormat(r(p, TheAPI.colorize(format), e.getMessage(), true));
			}
		}

	}
}
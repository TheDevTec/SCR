package me.devtec.scr.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.api.API;
import me.devtec.scr.utils.MOTD;
import me.devtec.scr.utils.MOTD.PlayersCountType;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.events.Event;
import me.devtec.shared.events.EventListener;
import me.devtec.theapi.bukkit.events.ServerListPingEvent;
import me.devtec.theapi.bukkit.events.ServerListPingEvent.PlayerProfile;

public class ServerList implements EventListener {

	@Override
	public void listen(Event event) {
		if (event instanceof ServerListPingEvent) {
			ServerListPingEvent e = (ServerListPingEvent) event;

			if (!Loader.config.getBoolean("serverlist.enabled"))
				return;

			String motd_name = MOTD.getUsed();

			// MOTD
			if (MOTD.getMotd(motd_name) != null)
				e.setMotd(PlaceholderAPISupport.replace(MOTD.getMotd(motd_name), null, false, null));

			// ONLINE
			if (Loader.config.exists("serverlist." + motd_name + ".players.online"))
				e.setOnlinePlayers(MOTD.getPlayers(motd_name, PlayersCountType.ONLINE));

			// MAXIMAL
			if (Loader.config.exists("serverlist." + motd_name + ".players.max"))
				e.setMaxPlayers(MOTD.getPlayers(motd_name, PlayersCountType.MAX));

			// PLAYERS
			if (Loader.config.exists("serverlist." + motd_name + ".players.list") && !Loader.config.getStringList("serverlist." + motd_name + ".players.list").isEmpty()) {
				// LIST
				List<String> list = MOTD.getList(motd_name);
				List<PlayerProfile> profiles = new ArrayList<>(list.size());
				for (String s : list)
					profiles.add(new PlayerProfile(s));
				e.setPlayersText(profiles);
			} else {
				// CUSTOM PLAYER NAMES
				boolean hide = Loader.config.getBoolean("serverlist." + motd_name + ".players.hide-vanished");
				Iterator<PlayerProfile> p = e.getPlayersText().iterator();
				while (p.hasNext()) {
					PlayerProfile profile = p.next();
					Player player = Bukkit.getPlayer(profile.getUUID());
					if (player != null && hide && API.isVanished(player))
						p.remove();
					else
						profile.setName(PlaceholderAPISupport.replace(
								Loader.config.getString("serverlist." + motd_name + ".players.playername-format"), player, true, null));
				}
			}

			// ICON
			if (MOTD.getIcon(motd_name) != null)
				e.setFalvicon(MOTD.getIcon(motd_name));

			// SERVER VERSION
			if (MOTD.getVersion(motd_name) != null)
				e.setVersion(MOTD.getVersion(motd_name));

			// PROTOCOL
			if (MOTD.getProtocol(motd_name) != -1)
				e.setProtocol(MOTD.getProtocol(motd_name));

		}
	}
}

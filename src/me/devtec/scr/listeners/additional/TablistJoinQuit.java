package me.devtec.scr.listeners.additional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.devtec.scr.functions.Tablist;
import me.devtec.scr.functions.Tablist.TabSettings;
import me.devtec.shared.Ref;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.tablist.NameTagAPI;

public class TablistJoinQuit implements Listener {

	Tablist tablist;

	public TablistJoinQuit(Tablist tablist) {
		this.tablist = tablist;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		TabSettings set = tablist.getSettingsOf(e.getPlayer());
		BukkitLoader.getPacketHandler().send(e.getPlayer(), Tablist.createObjectivePacket(0, "ping", e.getPlayer().getName(), Tablist.showType(set.yellowNumberDisplay)));
		Object packet = BukkitLoader.getNmsProvider().packetScoreboardDisplayObjective(0, null);
		Ref.set(packet, "b", "ping");
		BukkitLoader.getPacketHandler().send(e.getPlayer(), packet);
		for (NameTagAPI a : tablist.tags.values())
			if (a.getPlayer().isOnline())
				a.send(e.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		tablist.fullyUnregister(e.getPlayer());
	}
}

package me.DevTec.ServerControlReloaded.Events;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import net.lapismc.afkplus.api.AFKStartEvent;
import net.lapismc.afkplus.api.AFKStopEvent;
import net.lapismc.afkplus.playerdata.AFKPlusPlayer;

public class AFKPlus implements Listener {
	public static HashMap<String, AFKPlusPlayer> AFKPlus = new HashMap<>();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(AFKStartEvent e) {
		if (!AFKPlus.containsKey(e.getPlayer().getName()))
			AFKPlus.put(e.getPlayer().getName(), e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(AFKStopEvent e) {
		if (AFKPlus.containsKey(e.getPlayer().getName()))
			AFKPlus.remove(e.getPlayer().getName());
	}
}

package me.DevTec.ServerControlReloaded.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.TheAPI.PunishmentAPI.PlayerBanList;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;


public class AFkPlayerEvents implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMessage(PlayerChatEvent e) { //Chatting in chat
		SPlayer p = API.getSPlayer(e.getPlayer());
		if (p.isAFK() && !p.hasVanish())
			Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
		Loader.getInstance.save(p);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMessage(PlayerCommandPreprocessEvent e) { //Sending commands
		if (!e.getMessage().toLowerCase().startsWith("/afk") && !e.getMessage().toLowerCase().startsWith("/away")) {
			SPlayer p = API.getSPlayer(e.getPlayer());
			if (p.isAFK() && !p.hasVanish())
				Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
			Loader.getInstance.save(p);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlaceBlock(BlockPlaceEvent e) { //Placing blocks
		SPlayer p = API.getSPlayer(e.getPlayer());
		if (p.isAFK() && !p.hasVanish())
			Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
		Loader.getInstance.save(p);
		PlayerBanList d= PunishmentAPI.getBanList(e.getPlayer().getName());
		if (d.isJailed()
				|| d.isTempJailed()
				|| d.isTempIPJailed()
				|| d.isIPJailed())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCaughtFish(PlayerFishEvent e) { //Fishing
		SPlayer p = API.getSPlayer(e.getPlayer());
		if (p.isAFK() && !p.hasVanish())
			Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
		Loader.getInstance.save(API.getSPlayer(e.getPlayer()));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(BlockBreakEvent e) { //Breaking blocks
		SPlayer p = API.getSPlayer(e.getPlayer());
		if (p.isAFK() && !p.hasVanish())
			Loader.sendBroadcasts(e.getPlayer(), "AFK.End");
		Loader.getInstance.save(p);
		PlayerBanList d= PunishmentAPI.getBanList(e.getPlayer().getName());
		if (d.isJailed()
				|| d.isTempJailed()
				|| d.isTempIPJailed()
				|| d.isIPJailed())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent e) { //Like invetory games
		SPlayer p = API.getSPlayer((Player) e.getWhoClicked());
		if (p.isAFK() && !p.hasVanish())
			Loader.sendBroadcasts((Player)e.getWhoClicked(), "AFK.End");
		Loader.getInstance.save(p);
		PlayerBanList d= PunishmentAPI.getBanList(e.getWhoClicked().getName());
		if (d.isJailed()
				|| d.isTempJailed()
				|| d.isTempIPJailed()
				|| d.isIPJailed())
			e.setCancelled(true);
	}
}

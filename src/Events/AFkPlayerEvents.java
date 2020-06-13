package Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

@SuppressWarnings("deprecation")
public class AFkPlayerEvents implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent e) {
		if (Math.abs(e.getFrom().getBlockX() - e.getTo().getBlockX()) > 0
				|| Math.abs(e.getFrom().getBlockZ() - e.getTo().getBlockZ()) > 0
				|| Math.abs(e.getFrom().getBlockY() - e.getTo().getBlockY()) > 0) {
			if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
				TheAPI.broadcastMessage(
						Loader.s("Prefix") + Loader.s("AFK.NoLongerAFK").replace("%player%", e.getPlayer().getName())
								.replace("%playername%", e.getPlayer().getDisplayName()));
			if (Loader.afk.containsKey(e.getPlayer().getName()))
				Loader.afk.get(e.getPlayer().getName()).save();
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMessage(PlayerChatEvent e) {
		if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
			TheAPI.broadcastMessage(
					Loader.s("Prefix") + Loader.s("AFK.NoLongerAFK").replace("%player%", e.getPlayer().getName())
							.replace("%playername%", e.getPlayer().getDisplayName()));
		if (Loader.afk.containsKey(e.getPlayer().getName()))
			Loader.afk.get(e.getPlayer().getName()).save();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMessage(PlayerCommandPreprocessEvent e) {
		if (!e.getMessage().toLowerCase().startsWith("/afk") && !e.getMessage().toLowerCase().startsWith("/away")) {
			if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
				TheAPI.broadcastMessage(
						Loader.s("Prefix") + Loader.s("AFK.NoLongerAFK").replace("%player%", e.getPlayer().getName())
								.replace("%playername%", e.getPlayer().getDisplayName()));
			if (Loader.afk.containsKey(e.getPlayer().getName()))
				Loader.afk.get(e.getPlayer().getName()).save();
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlaceBlock(BlockPlaceEvent e) {
		if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
			TheAPI.broadcastMessage(
					Loader.s("Prefix") + Loader.s("AFK.NoLongerAFK").replace("%player%", e.getPlayer().getName())
							.replace("%playername%", e.getPlayer().getDisplayName()));
		if (Loader.afk.containsKey(e.getPlayer().getName()))
			Loader.afk.get(e.getPlayer().getName()).save();
		if (TheAPI.getPunishmentAPI().getBanList(e.getPlayer().getName()).isJailed()
				|| TheAPI.getPunishmentAPI().getBanList(e.getPlayer().getName()).isTempJailed())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCaughtFish(PlayerFishEvent e) {
		if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
			TheAPI.broadcastMessage(
					Loader.s("Prefix") + Loader.s("AFK.NoLongerAFK").replace("%player%", e.getPlayer().getName())
							.replace("%playername%", e.getPlayer().getDisplayName()));
		if (Loader.afk.containsKey(e.getPlayer().getName()))
			Loader.afk.get(e.getPlayer().getName()).save();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(BlockBreakEvent e) {
		if (API.getSPlayer(e.getPlayer()).isAFK() && !API.getSPlayer(e.getPlayer()).hasVanish())
			TheAPI.broadcastMessage(
					Loader.s("Prefix") + Loader.s("AFK.NoLongerAFK").replace("%player%", e.getPlayer().getName())
							.replace("%playername%", e.getPlayer().getDisplayName()));
		if (Loader.afk.containsKey(e.getPlayer().getName()))
			Loader.afk.get(e.getPlayer().getName()).save();
		if (TheAPI.getPunishmentAPI().getBanList(e.getPlayer().getName()).isJailed()
				|| TheAPI.getPunishmentAPI().getBanList(e.getPlayer().getName()).isTempJailed())
			e.setCancelled(true);
	}
}

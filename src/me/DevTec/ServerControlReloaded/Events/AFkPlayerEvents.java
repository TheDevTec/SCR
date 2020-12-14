package me.DevTec.ServerControlReloaded.Events;

import java.util.Collection;
import java.util.Set;

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
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.DataKeeper.Collections.UnsortedSet;


public class AFkPlayerEvents implements Listener {
	static Set<Player> p = new UnsortedSet<>();
	static {
		new Tasker() {
			public void run() {
				for(Player s : AFkPlayerEvents.p) {
				SPlayer p = API.getSPlayer(s);
				if (p.isAFK() && !p.hasVanish())
					Loader.sendBroadcasts(s, "AFK.End");
				Loader.getInstance.save(p);
				}
				p.clear();
			}
		}.runRepeating(0, 2);
	}
	
	private void c(Player p) {
		if(!AFkPlayerEvents.p.contains(p))
			AFkPlayerEvents.p.add(p);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMessage(PlayerChatEvent e) { //Chatting in chat
		c(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMessage(PlayerCommandPreprocessEvent e) { //Sending commands
		if(Loader.cmds.getBoolean("Other.AFK.Enabled")) {
			if (!e.getMessage().toLowerCase().startsWith("/"+Loader.cmds.getString("Other.AFK.Name").toLowerCase())) {
				boolean c = false;
				Object o = Loader.cmds.get("Other.AFK.Aliases");
				if(o!=null) {
					if(o instanceof Collection) {
						for(String s : Loader.cmds.getStringList("Other.AFK.Aliases")) {
						c=e.getMessage().toLowerCase().startsWith("/"+s.toLowerCase());
						if(c)break;
						}
					}
					c=e.getMessage().toLowerCase().startsWith("/"+Loader.cmds.getString("Other.AFK.Aliases").toLowerCase());
				}
				if(!c)
				c(e.getPlayer());
			}
		}else {
			c(e.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlaceBlock(BlockPlaceEvent e) { //Placing blocks
		c(e.getPlayer());
		PlayerBanList d= PunishmentAPI.getBanList(e.getPlayer().getName());
		if(d==null)return;
		if (d.isJailed()
				|| d.isTempJailed()
				|| d.isTempIPJailed()
				|| d.isIPJailed())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCaughtFish(PlayerFishEvent e) { //Fishing
		c(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(BlockBreakEvent e) { //Breaking blocks
		c(e.getPlayer());
		PlayerBanList d= PunishmentAPI.getBanList(e.getPlayer().getName());
		if(d==null)return;
		if (d.isJailed()
				|| d.isTempJailed()
				|| d.isTempIPJailed()
				|| d.isIPJailed())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent e) { //Like invetory games
		c((Player)e.getWhoClicked());
		PlayerBanList d= PunishmentAPI.getBanList(e.getWhoClicked().getName());
		if(d==null)return;
		if (d.isJailed()
				|| d.isTempJailed()
				|| d.isTempIPJailed()
				|| d.isIPJailed())
			e.setCancelled(true);
	}
}

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
import ServerControl.SPlayer;
import Utils.AFK;

@SuppressWarnings("deprecation")
public class AFkPlayerEvents implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent e) {
         if(Math.abs(e.getFrom().getBlockX() - e.getTo().getBlockX()) > 0 || Math.abs(e.getFrom().getBlockZ() - e.getTo().getBlockZ()) > 0 
        		 || Math.abs(e.getFrom().getBlockY() - e.getTo().getBlockY()) > 0) {
     		if(!AFK.wait(e.getPlayer())) {
				if(AFK.isAFK(e.getPlayer())) {
					new SPlayer(e.getPlayer()).setAFK(false);
		    		 }
			   AFK.save(e.getPlayer());
         }}
      }

	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onPlayerMessage(PlayerChatEvent e) {
				if(AFK.isAFK(e.getPlayer())) {
					new SPlayer(e.getPlayer()).setAFK(false);
					}
			   AFK.save(e.getPlayer());
	   }
	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onPlayerMessage(PlayerCommandPreprocessEvent e) {
		   if(!e.getMessage().toLowerCase().startsWith("/afk")&&!e.getMessage().toLowerCase().startsWith("/away")) {
			   if(AFK.isAFK(e.getPlayer())) {
					new SPlayer(e.getPlayer()).setAFK(false);
					}
			   AFK.save(e.getPlayer());
		      }
	   }

	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onPlaceBlock(BlockPlaceEvent e) {
				if(AFK.isAFK(e.getPlayer())) {
					new SPlayer(e.getPlayer()).setAFK(false);
					}
			   AFK.save(e.getPlayer());
				if(API.getBanSystemAPI().hasJail(e.getPlayer()))e.setCancelled(true);
	   }
	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onCaughtFish(PlayerFishEvent e) {
				if(AFK.isAFK(e.getPlayer())) {
					new SPlayer(e.getPlayer()).setAFK(false);
					}
			   AFK.save(e.getPlayer());
	   }

	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onBreakBlock(BlockBreakEvent e) {
				if(AFK.isAFK(e.getPlayer())) {
					new SPlayer(e.getPlayer()).setAFK(false);
					}
			   AFK.save(e.getPlayer());
				if(API.getBanSystemAPI().hasJail(e.getPlayer()))e.setCancelled(true);
	   }
}

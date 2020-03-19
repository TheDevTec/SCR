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

@SuppressWarnings("deprecation")
public class AFkPlayerEvents implements Listener {
	 
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent e) {
         if(Math.abs(e.getFrom().getBlockX() - e.getTo().getBlockX()) > 0 || Math.abs(e.getFrom().getBlockZ() - e.getTo().getBlockZ()) > 0 
        		 || Math.abs(e.getFrom().getBlockY() - e.getTo().getBlockY()) > 0) {
        	 Loader.afk.get(e.getPlayer().getName()).save();
         }}
      

	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onPlayerMessage(PlayerChatEvent e) {
		   Loader.afk.get(e.getPlayer().getName()).save();
	   }
	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onPlayerMessage(PlayerCommandPreprocessEvent e) {
		   if(!e.getMessage().toLowerCase().startsWith("/afk") && !e.getMessage().toLowerCase().startsWith("/away"))
		   Loader.afk.get(e.getPlayer().getName()).save();
		      
	   }

	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onPlaceBlock(BlockPlaceEvent e) {
		   Loader.afk.get(e.getPlayer().getName()).save();
			if(API.getBanSystemAPI().hasJail(e.getPlayer()))e.setCancelled(true);
	   }
	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onCaughtFish(PlayerFishEvent e) {
		   Loader.afk.get(e.getPlayer().getName()).save();
	   }

	   @EventHandler(priority = EventPriority.LOWEST)
	   public void onBreakBlock(BlockBreakEvent e) {
		   Loader.afk.get(e.getPlayer().getName()).save();
			if(API.getBanSystemAPI().hasJail(e.getPlayer()))e.setCancelled(true);
	   }
}

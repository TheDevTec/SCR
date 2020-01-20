package Events;

import java.io.BufferedWriter;
import java.io.FileWriter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.apache.commons.lang.StringUtils;

import ServerControl.API;
import ServerControl.Loader;
import ServerControlEvents.PlayerAdvertisementEvent;
import Utils.Configs;
import Utils.setting;
import me.Straiker123.TheAPI;

@SuppressWarnings("deprecation")
public class SecurityListenerAntiAD implements Listener {
public Loader plugin=Loader.getInstance;
     
     public String getMatches(String where) {
    	 String list = null;
    	 if(API.getAdvertisementMatches(where).isEmpty()==false)list=StringUtils.join(API.getAdvertisementMatches(where), ", ");
    	 return list;
     }

	@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(PlayerChatEvent event) {
		Player p = event.getPlayer();
        String message = event.getMessage().toLowerCase();
	    	
        if(setting.ad_chat) {
    	if(!p.hasPermission("ServerControl.Advertisement")) {
        if(API.getAdvertisement(message)) {
        	PlayerAdvertisementEvent ed = new PlayerAdvertisementEvent(event.getPlayer(),event.getMessage());
    		Bukkit.getPluginManager().callEvent(ed);
    		if(!ed.isCancelled()) {
        		chatLog(getMatches(message), p);
     	    	 sendBroadcast(p,getMatches(message),AdType.Other);
                event.setCancelled(true);
                }}}}}
	@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommands(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        if(setting.ad_cmd) {
    	if(!p.hasPermission("ServerControl.Advertisement")) {
        if(API.getAdvertisement(message)) {
        	PlayerAdvertisementEvent ed = new PlayerAdvertisementEvent(event.getPlayer(),event.getMessage());
    		Bukkit.getPluginManager().callEvent(ed);
    		if(!ed.isCancelled()) {
        		chatLog(getMatches(message), p);
	      	    	 sendBroadcast(p,getMatches(message),AdType.Other);
                event.setCancelled(true);
        				}}}}}
    @EventHandler(priority = EventPriority.LOWEST)
    public void signCreate(SignChangeEvent e){
        Player p = e.getPlayer();
        if(setting.ad_sign) {
    	if(!p.hasPermission("ServerControl.Advertisement")) {
     for(String line: e.getLines()) {
    	 
    	 if(API.getAdvertisement(line)) {
         	PlayerAdvertisementEvent ed = new PlayerAdvertisementEvent(e.getPlayer(),line);
     		Bukkit.getPluginManager().callEvent(ed);
     		if(!ed.isCancelled()) {
        		chatLog(getMatches(line), p);
     	    	 sendBroadcast(p,getMatches(line),AdType.Other);
    		 e.setCancelled(true);
    		 e.getBlock().breakNaturally();
    	}}}}}}
	@EventHandler(priority = EventPriority.LOWEST)
    public void BookSave(PlayerEditBookEvent e){
    	Player p = e.getPlayer();
        if(setting.ad_book) {
    	if(!p.hasPermission("ServerControl.Advertisement")) {
    		String page = "";
     for(String line: e.getNewBookMeta().getPages()) {
    	 page=page+" "+line;
     }
     page=page.replace(" ", "");
    	 if(API.getAdvertisement(page)) {
          	PlayerAdvertisementEvent ed = new PlayerAdvertisementEvent(e.getPlayer(),page);
      		Bukkit.getPluginManager().callEvent(ed);
      		if(!ed.isCancelled()) {
        		chatLog(getMatches(page), p);
     	    	 sendBroadcast(p,getMatches(page),AdType.Other);
    		 e.setCancelled(true);
    	}}}}}
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClickEvent(InventoryClickEvent event) {
    	Player p = (Player)event.getWhoClicked();
        if(setting.ad_anvil) {
       	 if (event.getInventory().getType() == InventoryType.ANVIL) {
             if(event.getSlotType() == InventoryType.SlotType.RESULT) {
             	if(!p.hasPermission("ServerControl.Advertisement")) {
             		String displayName = "";
             		if(event.getCurrentItem().getItemMeta().hasDisplayName())
               	  displayName = event.getCurrentItem().getItemMeta().getDisplayName();
            	 if(API.getAdvertisement(displayName)) {
                   	PlayerAdvertisementEvent ed = new PlayerAdvertisementEvent(p,displayName);
              		Bukkit.getPluginManager().callEvent(ed);
              		if(!ed.isCancelled()) {
                		chatLog(getMatches(displayName), p);
   	      	    	 sendBroadcast(p,getMatches(displayName),AdType.Other);
           		 event.setCancelled(true);
           		}}}}}}}
	@EventHandler(priority = EventPriority.LOWEST)
    public void PickupAntiADEvent(PlayerPickupItemEvent event) {
    	Player p = (Player)event.getPlayer();
    	String displayName = event.getItem().getItemStack().getItemMeta().getDisplayName();
        if(setting.ad_itempick) {
    	if(!p.hasPermission("ServerControl.Advertisement")) {
        	if (displayName != null) {
    		if(API.getAdvertisement(displayName)) {
             	PlayerAdvertisementEvent ed = new PlayerAdvertisementEvent(p,displayName);
         		Bukkit.getPluginManager().callEvent(ed);
         		if(!ed.isCancelled()) {
            		chatLog(getMatches(displayName), p);
	      	    	 sendBroadcast(p,getMatches(displayName),AdType.PickUpItem);
	            event.setCancelled(true);
     			event.getItem().remove();
         }}}}}}
	@EventHandler(priority = EventPriority.LOWEST)
    public void DropAntiADEvent(PlayerDropItemEvent event) {
    	Player p = (Player)event.getPlayer();
    	String displayName = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();
        if(setting.ad_itemdrop) {
    	if(!p.hasPermission("ServerControl.Advertisement")) {
        	if (displayName != null) {
    		if(API.getAdvertisement(displayName)) {
             	PlayerAdvertisementEvent ed = new PlayerAdvertisementEvent(p,displayName);
         		Bukkit.getPluginManager().callEvent(ed);
         		if(!ed.isCancelled()) {
            		chatLog(getMatches(displayName), p);
	      	    	 sendBroadcast(p,getMatches(displayName),AdType.DropItem);
     			event.getItemDrop().remove();
                }}}}}}
	public static enum AdType{
		DropItem,
		PickUpItem,
		Other
	}
	public void sendBroadcast(Player p, String message, AdType type) {
		String AD = "Advertisement";
		switch(type) {
		case DropItem:
			AD="DropItem";
					TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessageAdvertisement"+AD)
          .replace("%playername%", p.getDisplayName())
          		.replace("%player%", p.getName())
          		.replace("%message%", message), "ServerControl.Advertisement");
		  break;
		case PickUpItem:
			AD="PickupItem";
			TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessageAdvertisement"+AD)
	          .replace("%playername%", p.getDisplayName())
	          		.replace("%player%", p.getName())
	          		.replace("%message%", message), "ServerControl.Advertisement");
			  break;
		case Other:
			TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessageAdvertisement")
	          .replace("%playername%", p.getDisplayName())
	          		.replace("%player%", p.getName())
	          		.replace("%message%", message), "ServerControl.Advertisement");
			  break;
		}
		if(Loader.config.getBoolean("TasksOnSend.Advertisement.Broadcast")) {
      for(String ad:  Loader.TranslationsFile.getStringList("Security.TryingSendAdvertisement")) {
    	  TheAPI.broadcastMessage(ad
      			.replace("%playername%", p.getDisplayName())
      			.replace("%player%", p.getName())
      			.replace("%prefix%", Loader.s("Prefix")));
   		}}
      if(Loader.config.getBoolean("TasksOnSend.Advertisement.Use-Commands")) {
  		    	for(String cmds: Loader.config.getStringList("TasksOnSend.Advertisement.Commands")) {
	        	plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), TheAPI.colorize(cmds.replace("%player%", p.getName()))); 
  		    	}}}

    public void chatLog(String str, Player p) {
    	String playername = p.getName();
    	String start = str;
    try {
			FileWriter fw = new FileWriter(Configs.chat.getFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			String writingFormat = Loader.config.getString("WritingFormat.Advertisement");
			bw.write(writingFormat.replace("%player%", playername).replace("%message%", start));
			bw.newLine();
			fw.flush();
			bw.close();
			}catch(Exception ev) {
			ev.printStackTrace();
			}}}
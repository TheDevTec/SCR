
package Events;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.Configs;
import Utils.ScoreboardStats;
import Utils.TabList;
import me.Straiker123.TheAPI;

public class OnPlayerJoin implements Listener {
public Loader plugin=Loader.getInstance;
	private String replaceAll(String s, Player p) {
	    SimpleDateFormat format_date_time = new SimpleDateFormat(Loader.config.getString("Format.DateWithTime"));
	    SimpleDateFormat format_time = new SimpleDateFormat(Loader.config.getString("Format.Time"));
	    SimpleDateFormat format_date = new SimpleDateFormat(Loader.config.getString("Format.Date"));
		return s.replace("%players_max%", TheAPI.getCountingAPI().getMaxPlayers()+"")
		  .replace("%players_online%", TheAPI.getCountingAPI().getOnlinePlayers().size()+"")
		  .replace("%player%", p.getDisplayName()) 
		  .replace("%playername%", p.getDisplayName()) 
		  .replace("%prefix%", Loader.s("Prefix")) 
		  .replace("%web%", Loader.s("OnJoin.Web")) 
		  .replace("%time%",format_time.format(new Date()))
		  .replace("%date%",format_date.format(new Date()))
		  .replace("%date-time%",format_date_time.format(new Date()))
		  .replace("%server_support%", plugin.ver())
		  .replace("%version%", "V"+plugin.getDescription().getVersion())
		  .replace("%server_time%", format_time.format(new Date()))
		  .replace("%server_name%", API.getServerName())
		  .replace("%server_ip%", API.getServerIP()+":"+API.getServerPort());
	}
	private void setFlyWalk(Player p) {
		SPlayer s = new SPlayer(p);
		if(p.hasPermission("ServerControl.FlySpeedOnJoin"))s.setFlySpeed();
		if(p.hasPermission("ServerControl.WalkSpeedOnJoin"))s.setWalkSpeed();
	}
	private enum Join{
		NORMAL,
		FIRST;
	}
	private void broadcast(Player p, PlayerJoinEvent event, boolean b) {
		if(Loader.config.getBoolean("OnJoin.CustomJoinMessage")==true){
			event.setJoinMessage("");
			if(b)
			if(API.getPlugin("Essentials")) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						TheAPI.broadcastMessage(replaceAll(Loader.s("OnJoin.Join"),p));
					}}, 11);
			}else {
				TheAPI.broadcastMessage(replaceAll(Loader.s("OnJoin.Join"),p));
			}
		}
		Join type = null;
		if(!p.hasPlayedBefore())type=Join.FIRST;
		else type=Join.NORMAL;
		switch(type) {
		case NORMAL:
			  if(Loader.config.getBoolean("OnJoin.Messages")==true){
					if(API.getPlugin("Essentials")) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
    				public void run() {
		    	for(String ss: Loader.TranslationsFile.getStringList("OnJoin.Messages")) {
		    		Loader.msg(replaceAll(ss,p),p);
		    	}}},11);
			}else {
		    	for(String ss: Loader.TranslationsFile.getStringList("OnJoin.Messages")) {
		    		Loader.msg(replaceAll(ss,p),p);
	    	}}}
			break;
		case FIRST:
			Loader.me.set("Players."+p.getName()+".FirstJoin", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
			Configs.chatme.save();if(Loader.config.getBoolean("FirstJoin.Enabled")==true){
				if(API.getPlugin("Essentials")) {
		  		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		  				public void run() { 
		  					for(String ss: Loader.TranslationsFile.getStringList("OnJoin.FirstJoin.Messages")) {
											  Loader.msg(replaceAll(ss,p),p);
		  				}
		  					TheAPI.broadcastMessage(replaceAll(Loader.s("OnJoin.FirstJoin.BroadCast"),p));
										  if(Loader.config.getBoolean("FirstJoin.PerformCommands.Enabled")==true) {
									  			for(String cmds: Loader.config.getStringList("FirstJoin.PerformCommands.Commands")) {
									  			Bukkit.dispatchCommand(plugin.getServer().getConsoleSender(), TheAPI.colorize(replaceAll(cmds,p)));
									  			}}
									  		if(Loader.config.getBoolean("FirstJoin.Kit-Enabled")==true && Loader.config.getString("FirstJoin.Kit")!=null)
									  			API.giveKit(p.getName(),Loader.config.getString("FirstJoin.Kit"),false,false); 
									  		Location loc;
											float x_head;
											float z_head;
											World world;
											if(Loader.config.getString("Spawn")!=null) {
												 x_head = Loader.config.getInt("Spawn.X_Pos_Head");
												 z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
												 world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
												 loc = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y") ,Loader.config.getDouble("Spawn.Z"), x_head, z_head);
													}else {
														world = Bukkit.getWorlds().get(0);
														 loc = Bukkit.getWorlds().get(0).getSpawnLocation();
													}
													if(world != null && loc!=null) {
														plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
															public void run() {
														TheAPI.getPlayerAPI(p).teleport(loc);
															}}, 0);
													}else {
														Loader.warn("Can't get global spawn location !");
													}
			}},11);
      		}else {
      		for(String ss: Loader.TranslationsFile.getStringList("OnJoin.FirstJoin.Messages")) {
      			Loader.msg(replaceAll(ss,p),p);
      		}
      		TheAPI.broadcastMessage(replaceAll(Loader.s("OnJoin.FirstJoin.BroadCast"),p));
			if(Loader.config.getBoolean("FirstJoin.Kit-Enabled")==true && Loader.config.getString("FirstJoin.Kit")!=null)
			API.giveKit(p.getName(),Loader.config.getString("FirstJoin.Kit"),false,false); 
			if(Loader.config.getBoolean("FirstJoin.PerformCommands.Enabled")==true) {
			for(String cmds: Loader.config.getStringList("FirstJoin.PerformCommands.Commands")) {
			Bukkit.dispatchCommand(plugin.getServer().getConsoleSender(), TheAPI.colorize(replaceAll(cmds,p)));
			}}
			Location loc;
			float x_head;
			float z_head;
			World world;
			if(Loader.config.getString("Spawn")!=null) {
				 x_head = Loader.config.getInt("Spawn.X_Pos_Head");
				 z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
				 world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
				 loc = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y") ,Loader.config.getDouble("Spawn.Z"), x_head, z_head);
					}else {
						world = Bukkit.getWorlds().get(0);
						 loc = Bukkit.getWorlds().get(0).getSpawnLocation();
					}
					if(world != null) {
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
						p.teleport(loc);
							}}, 0);
					}else {
						Loader.warn("Can't get global spawn location !");
					}
      		}}
			break;
		}
	}
	private void sound(Player p) {
		if(Loader.config.getString("Sounds").equalsIgnoreCase("1")
				||Loader.config.getString("Sounds").equalsIgnoreCase("2")
				||Loader.config.getString("Sounds").equalsIgnoreCase("3")
				||Loader.config.getString("Sounds").equalsIgnoreCase("4")
				||Loader.config.getString("Sounds").equalsIgnoreCase("5")
				||Loader.config.getBoolean("Sounds")){
				Sound chime;
		    	Sound harp;
		    	Sound pling;
		    	Sound guitar;
		    	Sound snare;
		    	Sound xylophone;
		    	Sound bass;
		    	Sound portal= TheAPI.getSoundAPI().getByName("PORTAL");
		    	if(TheAPI.getServerVersion().equals("v1_8_R3")) {
		    		bass=TheAPI.getSoundAPI().getByName("NOTE_BASS");
		    		pling=TheAPI.getSoundAPI().getByName("NOTE_PLING");
		    		chime = TheAPI.getSoundAPI().getByName("NOTE_BASS"); 
		    		harp = TheAPI.getSoundAPI().getByName("BLOCK_NOTE_HARP"); 
		    		guitar = TheAPI.getSoundAPI().getByName("NOTE_BASS_GUITAR"); 
		    		snare= TheAPI.getSoundAPI().getByName("NOTE_SNARE");
		    		xylophone =TheAPI.getSoundAPI().getByName("NOTE_PIANO");
		    	}else {
		    	try {
		    		bass=TheAPI.getSoundAPI().getByName("NOTE_BASS");
		    		snare= TheAPI.getSoundAPI().getByName("NOTE_SNARE");
		    		pling=TheAPI.getSoundAPI().getByName("NOTE_PLING");
		    		chime =TheAPI.getSoundAPI().getByName("NOTE_CHIME"); 
		    		harp = TheAPI.getSoundAPI().getByName("NOTE_HARP"); 
		    		guitar = TheAPI.getSoundAPI().getByName("NOTE_GUITAR"); 
		    		xylophone = TheAPI.getSoundAPI().getByName("NOTE_XYLOPHONE");
		    	} catch(Exception e) {
		    		bass=TheAPI.getSoundAPI().getByName("NOTE_BLOCK_BASS");
		    		snare= TheAPI.getSoundAPI().getByName("NOTE_BLOCK_SNARE");
		    		pling=TheAPI.getSoundAPI().getByName("NOTE_BLOCK_PLING");
		    		chime = TheAPI.getSoundAPI().getByName("NOTE_BLOCK_CHIME");
		    		harp = TheAPI.getSoundAPI().getByName("NOTE_BLOCK_HARP"); 
		    		xylophone = TheAPI.getSoundAPI().getByName("NOTE_BLOCK_XYLOPHONE");
		    		guitar =TheAPI.getSoundAPI().getByName("NOTE_BLOCK_GUITAR");
		    	}}
			    if(Loader.config.getString("Sounds").equalsIgnoreCase("1")){
			    	p.playSound(p.getLocation(), chime, 1, 1);
			    	p.playSound(p.getLocation(), harp, 1, 1);
			    }
			    	if(Loader.config.getString("Sounds").equalsIgnoreCase("2")){
			    		p.playSound(p.getLocation(), pling, 1, 1);
			    		p.playSound(p.getLocation(), harp, 1, 1);
			    		p.playSound(p.getLocation(), guitar, 1, 1);
			    		p.playSound(p.getLocation(), snare, 1, 1);
			    	}
			    	if(Loader.config.getString("Sounds").equalsIgnoreCase("3")){
			    		p.playSound(p.getLocation(), snare, 1, 1);
				    	p.playSound(p.getLocation(), xylophone, 1, 1);
				    	p.playSound(p.getLocation(), chime, 1, 1);
			    	}
			    	if(Loader.config.getString("Sounds").equalsIgnoreCase("4")){
			    		p.playSound(p.getLocation(), pling, 1, 1);
			    		p.playSound(p.getLocation(), bass, 1, 1);
			    	}
			    	if(Loader.config.getString("Sounds").equalsIgnoreCase("5")){
			    		p.playSound(p.getLocation(), portal, 1, 1);
			    	}}

		if(!Loader.config.getBoolean("Sounds")){
			
		}else {
			    		plugin.SoundsChecker();
			    	}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerJoinEvent(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(p.getName().equals("Straiker123") && !p.hasPlayedBefore()) {
			TheAPI.broadcastMessage("&8********************************");
			TheAPI.broadcastMessage("&0[&4Creator of ServerControlReloaded&0] &cStraiker123 &ajoined to the game.");
			TheAPI.broadcastMessage("&8********************************");
		broadcast(p,event, false);
		}else
		if(p.getName().equals("Houska02") && !p.hasPlayedBefore()) {
			TheAPI.broadcastMessage("&8********************************");
			TheAPI.broadcastMessage("&0[&4Owner of ServerControlReloaded&0] &cHouska02 &ajoined to the game.");
			TheAPI.broadcastMessage("&8********************************");
		broadcast(p,event, false);
		}else
			broadcast(p,event, true);
				if(Loader.econ!=null)
					if(Loader.econ.createPlayerAccount(p))Loader.getInstance.EconomyLog("Creating economy account for player "+p.getName());
				if(Loader.scFile.getBoolean("Scoreboard-Enabled"))
					ScoreboardStats.createScoreboard(p);

		if(Loader.tab.getBoolean("Tab-Enabled"))
			TabList.setTab(p);

		SPlayer s = new SPlayer(p);
		if(s.hasPermission("servercontrol.flyonjoin") && s.hasPermission("servercontrol.fly") && s.hasFlyEnabled())s.enableFly();
		if(s.hasPermission("servercontrol.godonjoin") && s.hasPermission("servercontrol.god") && s.hasGodEnabled())s.enableGod();
		
	    Loader.me.set("Players."+p.getName()+".Joins", Loader.me.getInt("Players."+p.getName()+".Joins") + 1);
		Loader.me.set("Players."+p.getName()+".JoinTime",System.currentTimeMillis()/1000);
		Configs.chatme.save();
		if(Loader.config.getBoolean("OnJoin.SpawnTeleport")) {
			Location loc;
			float x_head;
			float z_head;
			World world;
			if(Loader.config.getString("Spawn")!=null) {
				 x_head = Loader.config.getInt("Spawn.X_Pos_Head");
				 z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
				 world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
				 loc = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y") ,Loader.config.getDouble("Spawn.Z"), x_head, z_head);
					}else {
						world = Bukkit.getWorlds().get(0);
						 loc = Bukkit.getWorlds().get(0).getSpawnLocation();
					}
					if(world != null) {
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
						p.teleport(loc);
							}}, 0);
					}else {
						Loader.warn("Can't get global spawn location !");
					}
		}
		if(API.getBanSystemAPI().hasJail(p))
			p.teleport((Location) Loader.config.get("Jails."+Loader.me.getString("Players."+p.getName()+".Jail.Location")));
		
		
		setFlyWalk(p);
		sound(p);
}}
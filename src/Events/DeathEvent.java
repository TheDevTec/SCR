package Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.Configs;
public class DeathEvent implements Listener {

	public Loader plugin=Loader.getInstance;

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerDeath(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			Loader.me.set("Players."+e.getEntity().getKiller().getName()+".Kills", Loader.me.getInt("Players."+e.getEntity().getKiller().getName()+".Kills") + 1);
		}
		Player p = e.getEntity().getPlayer();
		API.setBack(p);
		if(p.hasPermission("ServerControl.KeepInv")) {
		e.setKeepInventory(true);
		e.getDrops().clear();
		}
		if(p.hasPermission("ServerControl.KeepExp")) {
		e.setKeepLevel(true);
		e.setDroppedExp(0);
		}
		Loader.me.set("Players."+p.getName()+".Deaths", Loader.me.getInt("Players."+p.getName()+".Deaths") + 1);
		Configs.chatme.save();
	}
	Location loc;
	@EventHandler(priority = EventPriority.LOWEST)
	public void Respawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();

		if(API.getBanSystemAPI().hasJail(p)) {

			Bukkit.getScheduler().scheduleSyncDelayedTask(Loader.getInstance, new Runnable() {
				public void run() {
					p.teleport((Location) Loader.config.get("Jails."+Loader.me.getString("Players."+p.getName()+".Jail.Location")));
				}}, 1);
	}
		if(Loader.me.getString("Players."+p.getName()+".Homes.home")!=null&&Loader.config.getBoolean("OnDeathTeleportToHome")==true) {
			World w = Bukkit.getWorld(Loader.me.getString("Players."+p.getName()+".Homes.home.World"));
			double x = Loader.me.getDouble("Players."+p.getName()+".Homes.home.X");
			double y = Loader.me.getDouble("Players."+p.getName()+".Homes.home.Y");
			double z = Loader.me.getDouble("Players."+p.getName()+".Homes.home.Z");
			float pitch = Loader.me.getInt("Players."+p.getName()+".Homes.home.Pitch");
			float yaw = Loader.me.getInt("Players."+p.getName()+".Homes.home.Yaw");
			Location loc = new Location(w,x,y,z,yaw,pitch);
			API.setBack(p);
			if(w!=null) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(Loader.getInstance, new Runnable() {
				public void run() {
					p.teleport(loc);
						}}, 1);
			}else {
				
			}
		}else {
		float x_head;
		float z_head;
		World world = null;
		if(Loader.config.getString("Spawn")!=null) {
			 x_head = Loader.config.getInt("Spawn.X_Pos_Head");
			 z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
			 world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
			 loc = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y") ,Loader.config.getDouble("Spawn.Z"), x_head, z_head);
				}
				if(Loader.config.getString("Spawn")==null) {
					world = Bukkit.getWorlds().get(0);
					 loc = Bukkit.getWorlds().get(0).getSpawnLocation();
				}
				if(world != null) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(Loader.getInstance, new Runnable() {
						public void run() {
					p.teleport(loc);
						}}, 1);
				}else {
					Loader.warn("Can't get global spawn location !");
				}}
		SPlayer a = new SPlayer(p);
		if(a.hasPermission("servercontrol.fly") && a.hasFlyEnabled())
		a.enableFly();
		if(a.hasPermission("servercontrol.god") && a.hasGodEnabled())
		a.enableGod();
		
	}}

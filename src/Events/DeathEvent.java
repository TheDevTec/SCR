package Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import ServerControl.API.TeleportLocation;
import Utils.Configs;
import Utils.setting;
import Utils.setting.DeathTp;
import me.Straiker123.TheAPI;
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

	@EventHandler(priority = EventPriority.LOWEST)
	public void Respawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();

		if(API.getBanSystemAPI().hasJail(p)) {

			Bukkit.getScheduler().scheduleSyncDelayedTask(Loader.getInstance, new Runnable() {
				public void run() {
					if(setting.tp_safe)
						TheAPI.getPlayerAPI(p).safeTeleport((Location) Loader.config.get("Jails."+Loader.me.getString("Players."+p.getName()+".Jail.Location")));
					else
						TheAPI.getPlayerAPI(p).teleport((Location) Loader.config.get("Jails."+Loader.me.getString("Players."+p.getName()+".Jail.Location")));
				}}, 1);
	}else {
		if(setting.deathspawn == DeathTp.Home)
			API.teleportPlayer(p, TeleportLocation.HOME);
		if(setting.deathspawn == DeathTp.Bed)
			API.teleportPlayer(p, TeleportLocation.BED);
		if(setting.deathspawn == DeathTp.Spawn) {
			API.teleportPlayer(p, TeleportLocation.SPAWN);
		Loader.msg(Loader.s("Spawn.TeleportedToSpawn")
				.replace("%world%", ((Player)p).getWorld().getName())
				.replace("%player%", p.getName())
				.replace("%playername%", ((Player)p).getDisplayName())
				, p);
		}
		SPlayer a = new SPlayer(p);
		if(a.hasPermission("servercontrol.fly") && a.hasFlyEnabled())
		a.enableFly();
		if(a.hasPermission("servercontrol.god") && a.hasGodEnabled())
		a.enableGod();
	}
	}}

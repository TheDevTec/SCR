package Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import ServerControl.API.TeleportLocation;
import Utils.Configs;
import Utils.TabList;
import Utils.setting;
import Utils.setting.DeathTp;
public class DeathEvent implements Listener {

	public Loader plugin=Loader.getInstance;

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerDeath(PlayerAdvancementDoneEvent e) {
		TabList.setNameTag(e.getPlayer());
	}
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
		if(API.getBanSystemAPI().hasJail(p))
		e.setRespawnLocation((Location) Loader.config.get("Jails."+Loader.me.getString("Players."+p.getName()+".Jail.Location")));
		else
			if(setting.deathspawn == DeathTp.HOME)
				e.setRespawnLocation(API.getTeleportLocation(p, TeleportLocation.HOME));
			else
			if(setting.deathspawn == DeathTp.BED)
				e.setRespawnLocation(API.getTeleportLocation(p, TeleportLocation.BED));
			else
			if(setting.deathspawn == DeathTp.SPAWN) {
				e.setRespawnLocation(API.getTeleportLocation(p, TeleportLocation.SPAWN));
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
	}

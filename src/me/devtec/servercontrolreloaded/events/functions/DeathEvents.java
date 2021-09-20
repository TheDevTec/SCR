package me.devtec.servercontrolreloaded.events.functions;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.API.TeleportLocation;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.servercontrolreloaded.utils.setting.DeathTp;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.Punishment;
import me.devtec.theapi.punishmentapi.Punishment.PunishmentType;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.datakeeper.User;

public class DeathEvents implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerDeath(PlayerDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			User s = TheAPI.getUser(e.getEntity().getKiller());
			s.setAndSave("Kills", s.getInt("Kills") + 1);
		}
		Player p = e.getEntity();
		API.setBack(p);
		if (p.hasPermission("SCR.Other.KeepInv")) {
			e.setKeepInventory(true);
			e.getDrops().clear();
		}
		if (p.hasPermission("SCR.Other.KeepExp")) {
			e.setKeepLevel(true);
			e.setDroppedExp(0);
		}
		User s = TheAPI.getUser(p);
		s.setAndSave("Deaths", s.getInt("Deaths") + 1);
		Object o = Loader.events.get("onDeath.Messages");
		if(o!=null) {
			if(o instanceof Collection) {
			for(Object fa : (Collection<?>)o) {
				if(fa!=null)
				TheAPI.msg(JoinQuitEvents.replaceAll(fa+"",p),p);
			}}else
				if(!(""+o).isEmpty())
					TheAPI.msg(JoinQuitEvents.replaceAll(""+o, p),p);
		}

		new Tasker() {
			public void run() {
		Object o = Loader.events.get("onDeath.Commands");
		if(o!=null) {
			if(o instanceof Collection) {
			for(Object fa : (Collection<?>)o) {
				if(fa!=null)
					TheAPI.sudoConsole(TheAPI.colorize(JoinQuitEvents.replaceAll(""+fa, p)));
			}}else
				if(!(""+o).isEmpty())
					TheAPI.sudoConsole(TheAPI.colorize(JoinQuitEvents.replaceAll(""+o, p)));
		}}
		}.runTaskSync();
		o = Loader.events.get("onDeath.Broadcast");
		if(o!=null) {
			if(o instanceof Collection) {
			for(Object fa : (Collection<?>)o) {
				if(fa!=null)
				TheAPI.bcMsg(JoinQuitEvents.replaceAll(fa+"",p));
			}}else
				if(!(""+o).isEmpty())
					TheAPI.bcMsg(JoinQuitEvents.replaceAll(""+o, p));
		}
		
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Respawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		try {
			Punishment banlist = TheAPI.getPunishmentAPI().getPunishments(p.getName()).stream().filter(a -> a.getType()==PunishmentType.JAIL).findFirst().orElse(null);
			if (banlist!=null)
				e.setRespawnLocation(Position.fromString(banlist.getValue("position").toString()).toLocation());
			else if (setting.deathspawnbol) {
				if (setting.deathspawn == DeathTp.HOME)
					e.setRespawnLocation(API.getTeleportLocation(p, TeleportLocation.HOME));
				else if (setting.deathspawn == DeathTp.BED)
					e.setRespawnLocation(API.getTeleportLocation(p, TeleportLocation.BED));
				else if (setting.deathspawn == DeathTp.SPAWN) {
					e.setRespawnLocation(API.getTeleportLocation(p, TeleportLocation.SPAWN));
					Loader.sendMessages(p, "Spawn.Teleport.You");
				}
			}
		}catch(Exception eere) {}
	}
}

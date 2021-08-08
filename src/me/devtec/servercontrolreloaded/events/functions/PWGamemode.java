package me.devtec.servercontrolreloaded.events.functions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import me.devtec.servercontrolreloaded.commands.other.Vanish;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.MultiWorldsUtils;
import me.devtec.servercontrolreloaded.utils.Portal;
import me.devtec.servercontrolreloaded.utils.SPlayer;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.worldsapi.voidGenerator;
import me.devtec.theapi.worldsapi.voidGenerator_1_8;

public class PWGamemode implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLoadWorld(WorldUnloadEvent e) {
		//UNLOAD PORTALS
		Portal.unload(e.getWorld());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLoadWorld(WorldLoadEvent e) {
		String gen = Loader.mw.getString("WorldsSettings." + e.getWorld().getName() + ".Generator");
		if(gen==null) { //lookup for gen
			if(e.getWorld().getGenerator() instanceof voidGenerator || e.getWorld().getGenerator() instanceof voidGenerator_1_8) {
				gen="THE_VOID";
			}else
				if(e.getWorld().getEnvironment()==Environment.THE_END)
					gen="THE_END";
			else
				if(e.getWorld().getEnvironment()==Environment.NETHER)
					gen="NETHER";
			else
				if(e.getWorld().getEnvironment()==Environment.NORMAL && e.getWorld().getWorldType()==WorldType.FLAT)
					gen="FLAT";
				else gen="DEFAULT";
		}
		MultiWorldsUtils.defaultSet(e.getWorld(), gen);
		//LOAD PORTALS
		Portal.load(e.getWorld());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnPlayerWorldChangeEvent(PlayerTeleportEvent e) {
		if(e.isCancelled())return;
		if(e.getFrom().getWorld()!=e.getTo().getWorld()) { //prepare gamemode
			SPlayer a = API.getSPlayer(e.getPlayer());
			new Tasker() {
				public void run() {
					if (a.hasFlyEnabled(false))
						a.enableFly();
					if (a.hasTempFlyEnabled())
						a.enableTempFly();
					if (a.hasGodEnabled())
						a.enableGod();
				}
			}.runLaterSync(1);
			Ref.set(Ref.get(Ref.player(e.getPlayer()), TheAPI.isNewerThan(16)?"d":"playerInteractManager"), "b", MultiWorldsUtils.getGamemodeNMS(e.getTo().getWorld()));
		}
	}

	@EventHandler
	public void onSpawn(PlayerSpawnLocationEvent e) {
		Ref.set(Ref.get(Ref.player(e.getPlayer()), TheAPI.isNewerThan(16)?"d":"playerInteractManager"), "b", MultiWorldsUtils.getGamemodeNMS(e.getSpawnLocation().getWorld()));
	}
	
	List<String> moving = new ArrayList<>();
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChangeGamamode(PlayerGameModeChangeEvent e) {
		if(e.isCancelled())return;
		if(TheAPI.isNewerThan(7) && e.getNewGameMode()==GameMode.SPECTATOR) {
			if(!moving.contains(e.getPlayer().getName())) {
				moving.add(e.getPlayer().getName());
				new Tasker() {
					public void run() {
						Vanish.moveInTab(e.getPlayer(), 1, API.hasVanish(e.getPlayer()));
						moving.remove(e.getPlayer().getName());
					}
				}.runLater(1);
			}
		}
	}
}
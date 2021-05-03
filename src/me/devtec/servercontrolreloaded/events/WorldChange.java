package me.devtec.servercontrolreloaded.events;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.MultiWorldsUtils;
import me.devtec.servercontrolreloaded.utils.SPlayer;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.worldsapi.voidGenerator;
import me.devtec.theapi.worldsapi.voidGenerator_1_8;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldChange implements Listener {

	Map<String, Integer> sleepTask = new HashMap<>();
	Map<String, List<Player>> perWorldSleep = new HashMap<>();
	Constructor<?> c = Ref.constructor(Ref.nms("PacketPlayOutUpdateTime"), long.class, long.class, boolean.class);
	Method setTime = Ref.method(Ref.nms("WorldServer"), "setDayTime", long.class);
	
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
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(PlayerBedEnterEvent e) {
		if (setting.singeplayersleep && canEvent(e)) {
			World w = e.getBed().getWorld();
			Object f = Ref.world(w);
			boolean time = Boolean.parseBoolean(w.getGameRuleValue("DO_DAYLIGHT_CYCLE"));
			List<Player> s = perWorldSleep.getOrDefault(w.getName(), new ArrayList<>());
			s.add(e.getPlayer());
			perWorldSleep.put(w.getName(), s);
			if(!sleepTask.containsKey(w.getName())) {
				sleepTask.put(w.getName(), new Tasker() {
					long start = w.getTime();
					boolean doNight = false;
					
					public void run() {
						for(Player s : perWorldSleep.get(w.getName())) {
							int old = (int) Ref.get(Ref.player(s), "sleepTicks");
							if(old >= 98)
								Ref.set(Ref.player(s), "sleepTicks", 98);
						}
						
						if(start >= 24000) {
							start=0;
							doNight=true;
							Object data = Ref.get(Ref.world(w),"worldData");
							Ref.set(data, "raining", false);
							Ref.set(data, "thundering", false);
							w.setWeatherDuration(0);
						}
						if(doNight && start >= 500) {
							Object data = Ref.get(Ref.world(w),"worldData");
							Ref.set(data, "raining", false);
							Ref.set(data, "thundering", false);
							w.setWeatherDuration(0);
							cancel();
						}
						Ref.invoke(f,setTime, (long)Ref.invoke(f,"getDayTime")+ (start - (long)Ref.invoke(f,"getDayTime")));
						for (Player p : w.getPlayers())
							Ref.sendPacket(p, Ref.newInstance(c, w.getTime(), p.getPlayerTime(), time));
						start+=50;
					}
				}.runRepeating(0, 1));
			}
		}
	}
	
	private boolean canEvent(PlayerBedEnterEvent e) {
		try {
			return e.getBedEnterResult()==BedEnterResult.OK;
		}catch(Exception | NoSuchFieldError | NoSuchMethodError er) {
			return true;
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(PlayerBedLeaveEvent e) {
		if (setting.singeplayersleep) { //remove cache and stop task
			if(perWorldSleep.containsKey(e.getBed().getWorld().getName())) {
			perWorldSleep.get(e.getBed().getWorld().getName()).remove(e.getPlayer());
			if(perWorldSleep.get(e.getBed().getWorld().getName()).isEmpty()) {
				perWorldSleep.remove(e.getBed().getWorld().getName());
			}
			if(sleepTask.containsKey(e.getBed().getWorld().getName())) {
				Scheduler.cancelTask(sleepTask.get(e.getBed().getWorld().getName()));
				sleepTask.remove(e.getBed().getWorld().getName());
			}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnPlayerWorldChangeEvent(PlayerChangedWorldEvent e) {
		SPlayer a = API.getSPlayer(e.getPlayer());
		a.createEconomyAccount();
		if (a.hasFlyEnabled())
			a.enableFly();
		if (a.hasTempFlyEnabled())
			a.enableTempFly();
		if (a.hasGodEnabled())
			a.enableGod();
		a.setGamamode();

	}

	Map<String, Integer> task = new HashMap<>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChangeGamamode(PlayerGameModeChangeEvent e) {
		if(TheAPI.isNewerThan(7) && e.getNewGameMode()==GameMode.SPECTATOR) {
			if(!task.containsKey(e.getPlayer().getName()))
			task.put(e.getPlayer().getName(), new Tasker() {
				public void run() {
					LoginEvent.moveInTab(e.getPlayer(), 1, API.hasVanish(e.getPlayer()));
					task.remove(e.getPlayer().getName());
				}
			}.runLater(1));
		}
		SPlayer a = API.getSPlayer(e.getPlayer());
		if (a.hasFlyEnabled())
			a.enableFly();
		if (a.hasTempFlyEnabled())
			a.enableTempFly();
		if (a.hasGodEnabled())
			a.enableGod();
	}
}
package me.DevTec.ServerControlReloaded.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.reflections.Ref;

public class WorldChange implements Listener {
	
	Map<String, Integer> sleepTask = new HashMap<>();
	List<Player> sleeping = new ArrayList<>();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(PlayerBedEnterEvent e) {
		if (setting.singeplayersleep && e.getBedEnterResult()==BedEnterResult.OK) {
			World w = e.getBed().getWorld();
			if(!sleepTask.containsKey(w.getName())) {
				sleepTask.put(w.getName(), new Tasker() {
					
					long start = w.getTime();
					boolean doNight = !w.isThundering() || start < 2000 && start > 300;
					public void run() {
						for(Player s : sleeping) {
							int old = (int) Ref.get(Ref.player(s), "sleepTicks");
							if(old >= 98) {
								Ref.set(Ref.player(s), "sleepTicks", 98);
							}
						}
						if(start+50 > 24000) {
							start=0;
							doNight=true;
						}
						if(start > 1000) {
							if(doNight) {
								cancel();
								return;
							}
						}
						
						w.setTime(start);
						start=start+50;
					}
				}.runRepeatingSync(0, 1));
			}
			sleeping.add(e.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(PlayerBedLeaveEvent e) {
		if (setting.singeplayersleep) {
			sleeping.remove(e.getPlayer());
			if(sleeping.isEmpty())
			if(sleepTask.containsKey(e.getBed().getWorld().getName())) {
				Scheduler.cancelTask(sleepTask.get(e.getBed().getWorld().getName()));
				sleepTask.remove(e.getBed().getWorld().getName());
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

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChangeGamamode(PlayerGameModeChangeEvent e) {
		SPlayer a = API.getSPlayer(e.getPlayer());
		new Tasker() {
			public void run() {
				if (a.hasFlyEnabled())
					a.enableFly();
				if (a.hasTempFlyEnabled())
					a.enableTempFly();
				if (a.hasGodEnabled())
					a.enableGod();
			}
		}.runTaskSync();
	}
}
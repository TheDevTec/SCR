package me.devtec.servercontrolreloaded.events.functions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.reflections.Ref;

public class SinglePlayerSleep implements Listener {

	final Map<String, Integer> sleepTask = new HashMap<>();
	final Map<String, List<Player>> perWorldSleep = new HashMap<>();
	final Constructor<?> c = Ref.constructor(Ref.nmsOrOld("network.protocol.game.PacketPlayOutUpdateTime","PacketPlayOutUpdateTime"), long.class, long.class, boolean.class);
	static Method setTime = Ref.method(Ref.nmsOrOld("server.level.WorldServer","WorldServer"), "setDayTime", long.class);
	final Field sleepTicks = Ref.field(Ref.nmsOrOld("world.entity.player.EntityHuman", "EntityHuman"), TheAPI.isNewerThan(17)?"cq":TheAPI.isNewerThan(16)?"cp":"sleepTicks");
	static {
		if(setTime==null)
			setTime=Ref.method(Ref.nmsOrOld("server.level.WorldDataServer","WorldDataServer"),"f",int.class);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(PlayerBedEnterEvent e) {
		if(e.isCancelled())return;
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
							int old = (int) Ref.get(Ref.player(s), sleepTicks);
							if(old >= 98)
								Ref.set(Ref.player(s),  sleepTicks, 98);
						}
						if(start >= 24000) {
							start=0;
							doNight=true;
							Object data = Ref.get(f,TheAPI.isNewerThan(17)?"N":TheAPI.isNewerThan(16)?"x":"worldData");
							Ref.set(data, TheAPI.isNewerThan(16)?"t":"raining", false);
							Ref.set(data, TheAPI.isNewerThan(17)?"v":TheAPI.isNewerThan(16)?"w":"thundering", false);
							w.setWeatherDuration(0);
						}
						if(doNight && start >= 500) {
							Object data = Ref.get(f,TheAPI.isNewerThan(17)?"N":TheAPI.isNewerThan(16)?"x":"worldData");
							Ref.set(data, TheAPI.isNewerThan(16)?"t":"raining", false);
							Ref.set(data, TheAPI.isNewerThan(17)?"v":TheAPI.isNewerThan(16)?"w":"thundering", false);
							w.setWeatherDuration(0);
							cancel();
						}
						if(TheAPI.isNewerThan(17)) {
							Object data = Ref.get(f,"N");
							Ref.invoke(data,setTime, (int)((long)Ref.get(data,"m")+ (start - (long)Ref.get(data,"m"))));
						}else
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
		if(e.isCancelled())return;
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
}

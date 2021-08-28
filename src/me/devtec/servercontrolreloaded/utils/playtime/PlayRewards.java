package me.devtec.servercontrolreloaded.utils.playtime;

import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.playtime.PlayTimeUtils.PlayTimeType;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.nms.NMSAPI;

public class PlayRewards {

	final String name;
	PlayTimeType type;
	final boolean repeatable;
	final long time;
	
	int task, checktas;
	
	public PlayRewards(String reward) {
		this.name=reward;
		
		try {
			type = PlayTimeType.valueOf(Loader.rewards.getString("PlayTime."+reward+".Type") );
		} catch (Exception e) {
			type=null;
		}
		
		repeatable = Loader.rewards.getBoolean("PlayTime."+reward+".Repeatable");
		time= StringUtils.getTimeFromString(Loader.rewards.getString("PlayTime."+reward+".Time"));
	}
	
	public String getName() {
		return name;
	}
	public PlayTimeType getType() {
		return type;
	}
	public boolean isRepeatable() {
		return repeatable;
	}
	public long getTime() {
		return time;
	}
	public String getTimeFormated() {
		return StringUtils.setTimeToString(time);
	}
	
	public World getWorld() {
		if( !Loader.rewards.exists("PlayTime."+name+".World") || ( type!= PlayTimeType.WORLD &&type!= PlayTimeType.WORLDGAMEMODE ))
			return null;
		World w = Bukkit.getWorld( Loader.rewards.getString("PlayTime."+name+".World"));
		return w;
	}
	public GameMode getGameMode() {
		if( !Loader.rewards.exists("PlayTime."+name+".Gamemode") || ( type!= PlayTimeType.GAMEMODE && type!= PlayTimeType.WORLDGAMEMODE))
			return null;
		GameMode mode;
		try {
			mode = GameMode.valueOf( Loader.rewards.getString("PlayTime."+name+".Gamemode"));
		} catch (Exception e) {
			return null;
		}
		return mode;
	}

	public boolean isEnabled() {
		return Loader.rewards.getBoolean("PlayTime."+name+".Enabled");
	}
	public boolean isValid() {
		if(type==null) return false;
		if(name==null || name.isEmpty()) return false;
		if(time<0) return false;
		if(type== PlayTimeType.GAMEMODE || type== PlayTimeType.WORLDGAMEMODE) {
			if(!Loader.rewards.exists("PlayTime."+name+".Gamemode"))
				return false;
		}
		if(type== PlayTimeType.WORLD || type== PlayTimeType.WORLDGAMEMODE) {
			return Loader.rewards.exists("PlayTime." + name + ".World");
		}
		return true;
		
	}
	
	public int getPeriod() {
		if(Loader.rewards.exists("PlayTime."+name+".Period"))
			return Loader.rewards.getInt("PlayTime."+name+".Period");
		return 20;
	}
	
	public final HashMap<Player, Long> players = new HashMap<>();
	
	public void start() {
		if(!isValid()) {
			TheAPI.getConsole().sendMessage("&4ERROR: Reward &c"+getName()+" &6is not valid! Try and repair this reward in Rewards.yml");
			return;
		}
		if(isRepeatable()) { //Repeatable 
			task = new Tasker() {
				public void run() {
					for(Player p : TheAPI.getOnlinePlayers()) {
						if(API.isAFK(p) && !Loader.config.getBoolean("Options.PlayTime.UseAfkTime") )
							continue;
						long online = 0;
						if(players.containsKey(p) ) {
							online=players.get(p);
							players.remove(p);
						}
						if(type == PlayTimeType.GAMEMODE || type== PlayTimeType.WORLDGAMEMODE) {
							GameMode pmode = p.getGameMode();
							if(pmode!=getGameMode())
								continue;
						}
						if(type== PlayTimeType.WORLD || type== PlayTimeType.WORLDGAMEMODE) {
							if(p.getWorld()!=getWorld())
								continue;
						}
						online=online+1;
						if(online>=getTime()) {
							process(p);
							if(players.containsKey(p))
								players.remove(p);
                        }
						else {
							players.put(p, online);
                        }
                        continue;
                    }
					for(Player p : Collections.unmodifiableSet(players.keySet())) {
						if(p==null||!p.isOnline())
							players.remove(p);
					}
					
				}
			}.runRepeating(0, getPeriod());

		}else { //NotRepeatable
			task = new Tasker() {
				public void run() {
					
					for(Player p : TheAPI.getOnlinePlayers()) {						
						if(hasFinished(p))
							continue;
						long playtime = PlayTimeUtils.playtime(p, getType(), getGameMode(), getWorld());
						if(playtime >= getTime()) {
							process(p);
							User u = TheAPI.getUser(p);
							u.set("Statistics.Rewards."+getName()+".Finished", true);
							u.set("Statistics.Rewards."+getName()+".When", System.currentTimeMillis());
							u.save();
						}
					}
				}
			}.runRepeating(0, getPeriod());
			
		}
		
	}
	/*
	 * Statistics:
	 *   Rewards:
	 *     <reward>:
	 *       Finished: true
	 *       When: datum
	 */
	public void stop() {
		Scheduler.cancelTask(task);
	}
	
	private void process(Player p) {
		NMSAPI.postToMainThread(() -> {
			for(String cmd : Loader.rewards.getStringList("PlayTime."+name+".Commands"))
				TheAPI.sudoConsole(cmd.replace("%player%", p.getName()));
		});
		for(String msg : Loader.rewards.getStringList("PlayTime."+name+".Messages"))
			TheAPI.msg(msg.replace("%time%", getTimeFormated()).replace("%player%", p.getName()) , p);
	}
	
	public boolean hasFinished(Player p) {
		User u = TheAPI.getUser(p);
		if(u.exist("Statistics.Rewards."+getName()+".Finished"))
			return u.getBoolean("Statistics.Rewards."+getName()+".Finished");
		return false;
	}
}

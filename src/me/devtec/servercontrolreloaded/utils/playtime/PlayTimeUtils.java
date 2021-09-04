package me.devtec.servercontrolreloaded.utils.playtime;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.sortedmap.RankingAPI;
import me.devtec.theapi.sortedmap.SortedMap.ComparableObject;

public class PlayTimeUtils {
	
	public static final HashMap<String, Integer> playtop = new HashMap<>(); //Player UUID || PlayTime
	public static final RankingAPI<String, Integer> ranks = new RankingAPI<>(playtop);
	
	static Statistic st;
	
	static {
		try {
			st=Statistic.valueOf("PLAY_ONE_MINUTE");
		}catch(Exception|NoSuchFieldError er) {
			st=Statistic.valueOf("PLAY_ONE_TICK");
		}
	}
	

	public enum PlayTimeType{
		GLOBAL,
		GAMEMODE,
		WORLD,
		WORLDGAMEMODE
    }
	
	public static long playtime(Player s) {
		if(s==null)return -1;
		if( !Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
			return s.getStatistic(st)/20;
		else
			return API.getSPlayer(s).getPlayTime("PlayTime");
	}
	public static int playtime(String player) {
		OfflinePlayer s = Bukkit.getOfflinePlayer(player);
		if(s==null)return -1;
		if(!Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
			return s.getStatistic(st)/20;
		else
			return API.getSPlayer(s).getPlayTime("PlayTime");
	}
	
	public static long playtime(Player s, String path) {
		if(s==null)return -1;
		if( !Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
			return s.getStatistic(st)/20;
		else
			return API.getSPlayer(s).getPlayTime(path);
	}
	public static long playtime(String player, String path) {
		OfflinePlayer s = Bukkit.getOfflinePlayer(player);
		if(s==null)return -1;
		if( !Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
			return s.getStatistic(st)/20;
		else
			return API.getSPlayer(s).getPlayTime(path);
	}
	
	public static long playtime(Player s, PlayTimeType type, GameMode mode, World world) {
		if(s==null)return -1;
		if( !Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
			return s.getStatistic(st)/20;
		else{
			switch (type) {
			case GLOBAL:
				return API.getSPlayer(s).getPlayTime("PlayTime");
			case GAMEMODE:
				return API.getSPlayer(s).getPlayTime(mode+".PlayTime");
			case WORLD:
				return API.getSPlayer(s).getPlayTime(world.getName()+".PlayTime");
			case WORLDGAMEMODE:
				return API.getSPlayer(s).getPlayTime(mode+"."+world.getName()+".PlayTime");
			}
		}
		return playtime(s);
	}
	public static long playtime(String player, PlayTimeType type, GameMode mode, World world) {
		OfflinePlayer s = Bukkit.getOfflinePlayer(player);
		if(s==null)return -1;
		if( !Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
			return s.getStatistic(st)/20;
		else{
			switch (type) {
			case GLOBAL:
				return API.getSPlayer(s).getPlayTime("PlayTime");
			case GAMEMODE:
				return API.getSPlayer(s).getPlayTime(mode+".PlayTime");
			case WORLD:
				return API.getSPlayer(s).getPlayTime(world.getName()+".PlayTime");
			case WORLDGAMEMODE:
				return API.getSPlayer(s).getPlayTime(mode+"."+world.getName()+".PlayTime");
			}
		}
		return playtime(player);
		}
	
	public static long playtime(Player s, GameMode mode, World world) {
		if(s==null)return -1;
		if( !Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
			return s.getStatistic(st)/20;
		else{
			if(mode!=null && world!=null)
				return API.getSPlayer(s).getPlayTime(mode+"."+world.getName()+".PlayTime");
			if(mode!=null)
				return API.getSPlayer(s).getPlayTime(mode+".PlayTime");
			if(world!=null)
				return API.getSPlayer(s).getPlayTime(world.getName()+".PlayTime");
			}
		return playtime(s);
	}
	public static long playtime(String player, GameMode mode, World world) {
		OfflinePlayer s = Bukkit.getOfflinePlayer(player);
		if(s==null)return -1;
		if(!Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
			return s.getStatistic(st)/20;
		else{
			if(mode!=null && world!=null)
				return API.getSPlayer(s).getPlayTime(mode+"."+world.getName()+".PlayTime");
			if(mode!=null)
				return API.getSPlayer(s).getPlayTime(mode+".PlayTime");
			if(world!=null)
				return API.getSPlayer(s).getPlayTime(world.getName()+".PlayTime");
			}
		return playtime(player);
	}
	
	public static boolean task = false;
	
	public static ComparableObject<String, Integer> getTop(int pos) {
		if(ranks == null || ranks.isEmpty())
			return null;
		ComparableObject<String, Integer> player = ranks.get(pos);
		return player;
	}
	
	public static final HashMap<String, PlayRewards> playrewards = new HashMap<>();
	public static void loadRewards() {
		for(String reward : Loader.rewards.getKeys("PlayTime")) {
			PlayRewards rew = new PlayRewards(reward);
			if(rew.isValid()) {
				if(!rew.isEnabled())
					continue;
				
				rew.start();
				playrewards.put(reward, rew);
				continue;
			}
			TheAPI.getConsole().sendMessage("&4ERROR: Playtime reward &c"+reward+" &6is not valid! Fix this playtime reward in Rewards.yml");
		}
		if(!playrewards.isEmpty()) {
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &fLoaded &6"+playrewards.size()+" &fPlayTime Rewards", TheAPI.getConsole());
		}
	}

	public static void unloadRewards() {
		for(PlayRewards reward : playrewards.values())
			reward.stop();
	}
}

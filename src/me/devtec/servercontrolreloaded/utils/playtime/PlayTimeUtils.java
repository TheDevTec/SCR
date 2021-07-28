package me.devtec.servercontrolreloaded.utils.playtime;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.Tasks;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.sortedmap.RankingAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class PlayTimeUtils {
	
	public static HashMap<UUID, Long> playtop = new HashMap<>(); //Player UUID || PlayTime
	public static RankingAPI<UUID, Long> ranks = new RankingAPI<>(playtop);
	
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
		WORLDGAMEMODE;
	}
	
	public static long playtime(Player s) {
		if(s==null)return -1;
		if( !Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
			return s.getStatistic(st)/20;
		else
			return API.getSPlayer(s).getPlayTime("PlayTime");
	}
	public static long playtime(String player) {
		OfflinePlayer s = Bukkit.getOfflinePlayer(player);
		if(s==null)return -1;
		if( !Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime"))
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
	
	public static boolean task = false;
	
	public static String getTop(int pos) {
		if(playtop == null || playtop.size()==0) {
			Tasks.playTime();
		}
		if(ranks.getObject(pos)==null)
			return "-";
		UUID uuid = ranks.getObject(pos);
		String player = LoaderClass.cache.lookupNameById(uuid);
		
		if(player==null)
			return "-";
		return Loader.trans.getString("PlayTime.PlayTop.Top").replace("%position%", pos+ "")
				.replace("%player%", player).replace("%playername%", player)
				.replace("%playtime%", StringUtils.timeToString( playtime(player) ));
	}

	public static HashMap<String, PlayRewards> playrewards = new HashMap<>();
	public static void loadRewards() {
		for(String reward : Loader.rewards.getKeys("PlayTime")) {
			PlayRewards rew = new PlayRewards(reward);
			if(rew.isValid()) {
				rew.start();
				playrewards.put(reward, rew);
				continue;
			}
			else {
				TheAPI.getConsole().sendMessage("&4ERROR: Reward &c"+reward+" &6is not valid! Try and repair this reward in Rewards.yml");
			}
			continue;
		}
	}

	public static void unloadRewards() {
		for(PlayRewards reward : playrewards.values())
			reward.stop();
	}
}

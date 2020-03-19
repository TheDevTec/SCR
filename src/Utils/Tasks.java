package Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import ServerControl.Loader;
import ServerControl.SPlayer;
import me.Straiker123.TheAPI;

public class Tasks {
	public Tasks() {
		a = Loader.getInstance;
	}
	static Loader a;
	static List<Integer> tasks = new ArrayList<Integer>();
	static int tests;
	static HashMap<String, String> ss = new HashMap<String, String>();
	public static ArrayList<String> players = new ArrayList<String>();
	 static HashMap<String, Scoreboard> l = new HashMap<String, Scoreboard>();
	public static void load() {
		if(setting.am)
		automessage();
		if(setting.vip)
		vipslot();
		
		 if(setting.tab)
		tab();
		 
		if(setting.save)
		savetask();
		
		if(setting.sb) 
		scoreboard();
		
		other();
		tempfly();
	}
	public static void reload() {
		for(Integer t : tasks)
		Bukkit.getScheduler().cancelTask(t);
		tests=0;
		players.clear();
		ss.clear();
		l.clear();
		tasks.clear();
		load();
	}
	private static void tempfly() {
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			if(Loader.me.getString("TempFly") !=null && Loader.me.getStringList("TempFly").isEmpty()==false)
			for(String p:Loader.me.getStringList("TempFly")) {
				Player s = TheAPI.getPlayer(p);
				long start = Loader.me.getLong("Players."+p+".TempFly.Start");
				int end = Loader.me.getInt("Players."+p+".TempFly.Time");
				long timeout = start/1000 - System.currentTimeMillis()/1000 + end;
				if(timeout <= 0) {
					if(s!=null) {
					TheAPI.sendActionBar(s, "&cTempFly ended");
					new SPlayer(s).disableFly();
					List<String> list = Loader.me.getStringList("TempFly");
					list.remove(p);
					Loader.me.set("TempFly",list);
					Loader.me.set("Players."+p+".TempFly",null);
					Configs.chatme.save();
					}
				}
				if(timeout == 5 || timeout == 4 || timeout == 3 || timeout == 2 || timeout == 1 
						|| timeout == 15 || timeout == 10 || timeout == 30) {
					if(s!=null)
					TheAPI.sendActionBar(s, "&6TempFly ends in &c"+TheAPI.getStringUtils().setTimeToString(timeout));
					}
			}
		}},20,20));
	}
	
	private static void scoreboard() {
		int r = Loader.scFile.getInt("RefleshTick");
		if(r <= 0)r=1;
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			for(Player p:TheAPI.getOnlinePlayers())
				ScoreboardStats.createScoreboard(p);
		
	}},20,r));
	}
	private static void savetask() {
		if(Loader.mw.getInt("SavingTask.Delay") < 600){
			Loader.mw.set("SavingTask.Delay", 600);
			Configs.mw.save();
		}
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){
			int now = 0;
			public void run(){
			List<World> w = Bukkit.getWorlds();
			if(w.size()-1 <= now) now = 0;
			try {
				if(!Loader.mw.getBoolean("WorldsSettings."+w.get(now).getName()+".AutoSave"))
				w.get(now).save();
			}catch(Exception err) {
				//out
			}
				++now;
			}}, 20, 20*Loader.mw.getInt("SavingTask.Delay")));
	}
	public static void regPlayer(Player p) {
			if(!ss.containsKey(p.getName())) {
				String uuid = p.getUniqueId().toString();
				uuid = uuid.substring(0, 5);
				String pname = p.getName();
			 if (pname.length() > 5) {
				 pname = pname.substring(0, 5);
	            }
    		ss.put(p.getName(), uuid+pname);
			}
	}
	private static void other() {
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			for(Player p : TheAPI.getOnlinePlayers())
			Loader.setupChatFormat(p);
		    if(setting.motd) {
		    	
				if(!setting.lock_server ||setting.lock_server && !setting.motd_maintenance)
					TheAPI.setServerMotd(Loader.config.getString("Options.ServerList.MOTD.Text.Normal").replace("%next%", "\n").replace("%line%", "\n"));
				else
					TheAPI.setServerMotd(Loader.config.getString("Options.ServerList.MOTD.Text.Maintenance").replace("%next%", "\n").replace("%line%", "\n"));
		}}}, 20, 40));
	}
	
	private static void tab() {
		for(Player p:TheAPI.getOnlinePlayers()) {
		regPlayer(p);
		}
		int r = Loader.tab.getInt("NameTag-RefleshTick");
		if(r <= 0)r=1;
	tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			TabList.setFooterHeader();
		}},20,Loader.tab.getInt("RefleshTick")));
	tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
		TabList.setNameTag();
		}},20,r));
	}
	private static void vipslot() {
		if(Loader.config.getBoolean("Options.VIPSlots.AddSlots"))
	    TheAPI.setMaxPlayers(Bukkit.getMaxPlayers() + Loader.config.getInt("Options.VIPSlots.SlotsToAdd"));
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
	    	   	 for(Player online:TheAPI.getOnlinePlayers()) {
	    	   	 if(!players.contains(online.getName()) &&!online.hasPermission("ServerControl.JoinFullServer"))
	    	   		 players.add(online.getName());
	    	   	 if(players.contains(online.getName()) && online.hasPermission("ServerControl.JoinFullServer"))
	    	   		 players.remove(online.getName());
	    	   	 }}}, 20, 200));
	}
	
	private static void automessage() {
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable() {
			@Override
			public void run() {
		    	if(TheAPI.getOnlinePlayers().size()<Loader.config.getInt("Options.AutoMessage.MinimalPlayers"))return;
				List<Object> l = new ArrayList<Object>();
				for(String s : Loader.config.getStringList("Options.AutoMessage.Messages"))l.add(s);
			  		if(setting.am_random) {
			  				TheAPI.broadcastMessage(TheAPI.getRandomFromList(l).toString()
			     			 			.replace("%used_ram%", TheAPI.getMemoryAPI().getUsedMemory(false)+"")
			    			 			.replace("%free_ram%",TheAPI.getMemoryAPI().getFreeMemory(false)+"")
			     			 			.replace("%max_ram%",TheAPI.getMemoryAPI().getMaxMemory()+"")
			     			 			.replace("%online%", String.valueOf(TheAPI.getOnlinePlayers().size()))
			     			 			.replace("%max_players%", String.valueOf(Bukkit.getMaxPlayers()))
			     			 			.replace("%time%", new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
			     			 			.replace("%date%", new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()))
			     			 			.replace("%prefix%", Loader.s("Prefix")));
			  		}else {
			  			if(l.size() <= tests) {
							tests = 0;
			  		}
						TheAPI.broadcastMessage(l.get(tests).toString()
						 			.replace("%used_ram%", TheAPI.getMemoryAPI().getUsedMemory(false)+"")
						 			.replace("%free_ram%",TheAPI.getMemoryAPI().getFreeMemory(false)+"")
						 			.replace("%max_ram%",TheAPI.getMemoryAPI().getMaxMemory()+"")
			 			 			.replace("%online%", String.valueOf(TheAPI.getOnlinePlayers().size()))
			 			 			.replace("%max_players%", String.valueOf(Bukkit.getMaxPlayers()))
			 			 			.replace("%time%", new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
			 			 			.replace("%date%", new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()))
			 			 			.replace("%prefix%", Loader.s("Prefix")));
						tests = tests+ 1;
				  }
			}
			
		}, 20, 20*TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("Options.AutoMessage.Interval"))));
	}
}

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
import me.Straiker123.ScoreboardAPI;
import me.Straiker123.TheAPI;

public class Tasks {
	public Tasks() {
		a = Loader.getInstance;
	}
	static Loader a;
	static List<Integer> tasks = new ArrayList<Integer>();
	static int tests;
	static HashMap<Player, String> ss = new HashMap<Player, String>();
	public static ArrayList<Player> players = new ArrayList<Player>();
	 static HashMap<Player, Scoreboard> l = new HashMap<Player, Scoreboard>();
	 static HashMap<Player, ScoreboardAPI> setup = new HashMap<Player, ScoreboardAPI>();
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
		setup.clear();
		tasks.clear();
		load();
	}
	private static void setup(Player p) {
		setup.remove(p);
		if(setting.sb_world && 
				 Loader.scFile.getString("PerWorld."+p.getWorld().getName()+".Name")!=null &&
				 Loader.scFile.getString("PerWorld."+p.getWorld().getName()+".Lines")!=null)
			 l.remove(p);
			 if(!l.containsKey(p)) {
				 l.put(p, p.getServer().getScoreboardManager().getNewScoreboard());
				 p.setScoreboard(l.get(p));
			 }
			 Scoreboard f = l.get(p);
			 if(p.getScoreboard()==f||p.getScoreboard().getObjectives().isEmpty()) {
				 ScoreboardAPI a = TheAPI.getScoreboardAPI(p,f);
				 
		String getName = "Name";
		 String getLine = "Lines";
		 if(setting.sb_world && 
				 Loader.scFile.getString("PerWorld."+p.getWorld().getName()+".Name")!=null &&
				 Loader.scFile.getString("PerWorld."+p.getWorld().getName()+".Lines")!=null) {
			 getName="PerWorld."+p.getWorld().getName()+".Name";
			 getLine="PerWorld."+p.getWorld().getName()+".Lines";
		 }
		a.setTitle(Loader.scFile.getString(getName));
		List<String> list = Loader.scFile.getStringList(getLine);
	    list = TheAPI.getPlaceholderAPI().setPlaceholders(p,list);
	    int test= list.size();
		for(String ss: list) {
			--test;
			a.addLine(TabList.replace(ss, p),test);
		}
		setup.put(p, a);
		}
	}
	private static void tempfly() {
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			if(Loader.me.getString("TempFly") !=null && Loader.me.getStringList("TempFly").isEmpty()==false)
			for(String p:Loader.me.getStringList("TempFly")) {
				Player s = Bukkit.getPlayer(p);
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
					TheAPI.sendActionBar(s, "&6TempFly ends in &c"+TheAPI.getTimeConventorAPI().setTimeToString(timeout));
					}
			}
		}},20,20));
	}
	
	private static void scoreboard() {
		int r = Loader.scFile.getInt("RefleshTick");
		if(r <= 0)r=1;
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			for(Player p:Bukkit.getOnlinePlayers())
				setup(p);
		
	}},20,r-1));
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			for(Player p:Bukkit.getOnlinePlayers())
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
			if(w.size()-1 >= now) now = 0;
			try {
				if(!Loader.mw.getBoolean("WorldsSettings."+w.get(now).getName()+".AutoSave"))
				w.get(now).save();
			}catch(Exception err) {
				//out
			}
				++now;
			}}, 20, 20*Loader.mw.getInt("SavingTask.Delay")));
	}
	private static void regPlayers() {
		ss.clear();
		for(Player p:Bukkit.getOnlinePlayers()) {
			if(!ss.containsKey(p)) {
				String uuid = p.getUniqueId().toString();
				uuid = uuid.substring(0, 5);
				String pname = p.getName();
			 if (pname.length() > 5) {
				 pname = pname.substring(0, 5);
	            }
    		ss.put(p, uuid+pname);
			}
		}
	}
	private static void other() {
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
		    if(setting.motd) {
				if(!setting.lock_server ||setting.lock_server && !setting.motd_maintenance)
					TheAPI.setServerMotd(Loader.config.getString("Options.ServerList.MOTD.Text.Normal").replace("%next%", "\n").replace("%line%", "\n"));
				else
					TheAPI.setServerMotd(Loader.config.getString("Options.ServerList.MOTD.Text.Maintenance").replace("%next%", "\n").replace("%line%", "\n"));
		}
	   	 for(Player p:Bukkit.getOnlinePlayers()) {
	   		 if(AFK.isAFK(p)) {
	   				 if(setting.afk_kick && AFK.getAFKTime(p)>=Loader.config.getInt("AFK.Kick.Time")
	   						 && !p.hasPermission("ServerControl.AFK.Bypass")) {
	   					Loader.me.set("Players."+p.getName()+".AFK-Manual",null);
	   					Loader.me.set("Players."+p.getName()+".AFK-Broadcast",null);
	   					Configs.chatme.save();
	   					AFK.time.remove(p);
	   				 p.kickPlayer(TheAPI.colorize(Loader.config.getString("AFK.Kick.Message")));
	   			 }else
	   				 if(AFK.broadcast(p))Loader.getInstance.afk(p, false);
	   		 }
	   	 }}}, 20, 40));
	}
	
	private static void tab() {
		int r = Loader.tab.getInt("NameTag-RefleshTick");
		if(r <= 0)r=1;
	tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			TabList.setFooterHeader();
		}},20,Loader.tab.getInt("RefleshTick")));
	tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
		regPlayers();
		}},20,r-1));
	tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
		TabList.setNameTag();
		}},20,r));
	}
	private static void vipslot() {
		if(Loader.config.getBoolean("Options.VIPSlots.AddSlots"))
	    TheAPI.setMaxPlayers(Bukkit.getMaxPlayers() + Loader.config.getInt("Options.VIPSlots.SlotsToAdd"));
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
	    	   	 for(Player online:Bukkit.getOnlinePlayers()) {
	    	   	 if(!players.contains(online) &&!online.hasPermission("ServerControl.JoinFullServer"))
	    	   		 players.add(online);
	    	   	 if(players.contains(online) && online.hasPermission("ServerControl.JoinFullServer"))
	    	   		 players.remove(online);
	    	   	 }}}, 20, 200));
	}
	
	private static void automessage() {
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable() {
			@Override
			public void run() {
		    	if(Bukkit.getOnlinePlayers().size()<Loader.config.getInt("Options.AutoMessage.MinimalPlayers"))return;
				List<Object> l = new ArrayList<Object>();
				for(String s : Loader.config.getStringList("Options.AutoMessage.Messages"))l.add(s);
			  		if(setting.am_random) {
			  				TheAPI.broadcastMessage(TheAPI.getRandomFromList(l).toString()
			     			 			.replace("%used_ram%", TheAPI.getMemoryAPI().getUsedMemory(false)+"")
			    			 			.replace("%free_ram%",TheAPI.getMemoryAPI().getFreeMemory(false)+"")
			     			 			.replace("%max_ram%",TheAPI.getMemoryAPI().getMaxMemory()+"")
			     			 			.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
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
			 			 			.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
			 			 			.replace("%max_players%", String.valueOf(Bukkit.getMaxPlayers()))
			 			 			.replace("%time%", new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
			 			 			.replace("%date%", new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()))
			 			 			.replace("%prefix%", Loader.s("Prefix")));
						tests = tests+ 1;
				  }
			}
			
		}, 20, 20*TheAPI.getTimeConventorAPI().getTimeFromString(Loader.config.getString("Options.AutoMessage.Interval"))));
	}
}

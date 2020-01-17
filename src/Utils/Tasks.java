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
import me.Straiker123.ScoreboardAPI;
import me.Straiker123.TheAPI;

public class Tasks {
	static Loader a = Loader.getInstance;
	static List<Integer> tasks = new ArrayList<Integer>();
	static int tests;
	static HashMap<Player, String> ss = new HashMap<Player, String>();
	public static ArrayList<Player> players = new ArrayList<Player>();
	 static HashMap<Player, Scoreboard> l = new HashMap<Player, Scoreboard>();
	 static HashMap<Player, ScoreboardAPI> setup = new HashMap<Player, ScoreboardAPI>();
	public static void load() {
		if(Loader.config.getBoolean("AutoMessage.Enabled"))
		automessage();
		if(Loader.config.getBoolean("VIPSlots.Enabled")==true)
		vipslot();
		 if(Loader.tab.getBoolean("Tab-Enabled")==true)
		tab();
		if(Loader.mw.getBoolean("SavingTask.Enabled"))
		savetask();
		if(Loader.scFile.getBoolean("Scoreboard-Enabled")==true)
		scoreboard();
		other();
	}
	public static void reload() {
		for(Integer t : tasks)
		Bukkit.getScheduler().cancelTask(t);
		tests=0;
		players.clear();
		load();
	}
	private static void setup(Player p) {
		setup.remove(p);
		if(Loader.scFile.getBoolean("Scoreboard-PerWorld") && 
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
		 if(Loader.scFile.getBoolean("Scoreboard-PerWorld") && 
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
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			for(World w: Bukkit.getWorlds()) {
				if(Loader.mw.getBoolean("WorldsSettings."+w.getName()+".AutoSave"))
				w.save();
			}}}, 20, 20*Loader.mw.getInt("SavingTask.Delay")));
	}
	private static void regPlayers() {
		ss.clear();
		for(Player p:Bukkit.getOnlinePlayers()) {
			if(!ss.containsKey(p)) {
				String uuid = p.getUniqueId().toString();
				 if (uuid.length() > 6) {
					 uuid = uuid.substring(0, 6);
		            }
				String pname = p.getName();
			 if (pname.length() > 6) {
				 pname = pname.substring(0, 6);
	            }
    		ss.put(p, uuid+pname);
			}
		}
	}
	private static void other() {
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
		    if(Loader.config.getBoolean("ServerMOTD.Enabled")==true) {
				if(Loader.config.getBoolean("MaintenanceMode.Enabled")==false) {
					TheAPI.setServerMotd(Loader.config.getString("ServerMOTD.Normal.MOTD").replace("%next%", "\n").replace("%line%", "\n"));
				}else
					TheAPI.setServerMotd(Loader.config.getString("ServerMOTD.Maintenance.MOTD").replace("%next%", "\n").replace("%line%", "\n"));
		}
	   	 for(Player p:Bukkit.getOnlinePlayers()) {
	   		 if(AFK.isAFK(p)) {
	   				 if(AFK.getAFKTime(p)>=Loader.config.getInt("AFK.Kick.Time") &&Loader.config.getBoolean("AFK.Kick.Enabled")==true 
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
	    TheAPI.setMaxPlayers(Bukkit.getMaxPlayers() + Loader.config.getInt("VIPSlots.MaxSlots"));
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
		    	if(Bukkit.getOnlinePlayers().size()<Loader.config.getInt("AutoMessage.MinimalPlayers"))return;
				List<Object> l = new ArrayList<Object>();
				for(String s : Loader.config.getStringList("AutoMessage.Messages"))l.add(s);
			  		if(Loader.config.getBoolean("AutoMessage.Random")==true) {
			  				TheAPI.broadcastMessage(TheAPI.getRandomFromList(l).toString()
			     			 			.replace("%used_ram%", TheAPI.getMemoryAPI().getUsedMemory(false)+"")
			    			 			.replace("%free_ram%",TheAPI.getMemoryAPI().getFreeMemory(false)+"")
			     			 			.replace("%max_ram%",TheAPI.getMemoryAPI().getMaxMemory()+"")
			     			 			.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
			     			 			.replace("%max_players%", String.valueOf(Bukkit.getMaxPlayers()))
			     			 			.replace("%time%", new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
			     			 			.replace("%date%", new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()))
			     			 			.replace("%prefix%", Loader.s("Prefix")));
			  		}
			  		if(Loader.config.getBoolean("AutoMessage.Random")==false) {
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
			
		}, 20, 20*Loader.config.getInt("AutoMessage.Delay")));
	}
}

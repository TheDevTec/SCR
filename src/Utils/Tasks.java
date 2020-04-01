package Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import Commands.Mail;
import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import ServerControl.API.TeleportLocation;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;

public class Tasks {
	public Tasks() {
		a = Loader.getInstance;
	}
	public static List<String> players = new ArrayList<String>();
	static List<Integer> tasks = new ArrayList<Integer>();
	static HashMap<String, String> ss = new HashMap<String, String>();
	static Loader a;
	static int tests;
	public static void load() {
		joined = new ArrayList<Player>();
		ss = new HashMap<String, String>();
		players = new ArrayList<String>();
		playedBefore = new ArrayList<Player>();
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
			if(w.size()-1 == now) now = 0;
			try {
				if(!Loader.mw.getBoolean("WorldsSettings."+w.get(now).getName()+".AutoSave"))
				w.get(now).save();
			}catch(Exception err) {}
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

	public static List<Player> joined = new ArrayList<Player>(),playedBefore = new ArrayList<Player>()
			,realJoin = new ArrayList<Player>()
			,quit = new ArrayList<Player>();
	public static List<String> ignore = new ArrayList<String>();
	private static void other() {
		FileConfiguration f=Loader.config,c=Loader.me;
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
			for(Player p : TheAPI.getOnlinePlayers())
			Loader.setupChatFormat(p);
		    if(setting.motd) {
		    	if(!setting.lock_server ||setting.lock_server && !setting.motd_maintenance)
					TheAPI.setServerMotd(f.getString("Options.ServerList.MOTD.Text.Normal").replace("%next%", "\n").replace("%line%", "\n"));
				else
					TheAPI.setServerMotd(f.getString("Options.ServerList.MOTD.Text.Maintenance").replace("%next%", "\n").replace("%line%", "\n"));
		}
		    try {
		    if(!joined.isEmpty() && joined != null)
		    for(Player p : joined) {
				if(TheAPI.getPlayer(p.getName())==null) {
					ignore.add(p.getName());
					return;
				}
				realJoin.add(p);
					regPlayer(p);
					AFKV2 v = new AFKV2(p.getName());
					Loader.afk.put(p.getName(), v);
					v.start();
					if(f.getBoolean("OnJoin.SpawnTeleport") && !API.getBanSystemAPI().hasJail(p))API.teleportPlayer(p, TeleportLocation.SPAWN);
					if(API.getBanSystemAPI().hasJail(p))
						if(setting.tp_safe)
						TheAPI.getPlayerAPI(p).safeTeleport(TheAPI.getStringUtils().getLocationFromString(f.getString("Jails."+c.getString("Players."+p.getName()+".Jail.Location"))));
						else
							TheAPI.getPlayerAPI(p).teleport(TheAPI.getStringUtils().getLocationFromString(f.getString("Jails."+c.getString("Players."+p.getName()+".Jail.Location"))));
					if(!Mail.getMails(p.getName()).isEmpty())
						Loader.msg(Loader.s("Prefix")+Loader.s("Mail.Notification")
								.replace("%number%", ""+c.getStringList("Players."+p.getName()+".Mails").size()), p);
					if(Loader.SoundsChecker())
						p.playSound(p.getLocation(), TheAPI.getSoundAPI().getByName(f.getString("Options.Sounds.Sound")), 1, 1);
					c.set("Players."+p.getName()+".Joins", c.getInt("Players."+p.getName()+".Joins") + 1);
					c.set("Players."+p.getName()+".JoinTime",System.currentTimeMillis()/1000);
					Configs.chatme.save();
			if(setting.join_msg){
					if(!TheAPI.isVanished(p))
					TheAPI.broadcastMessage(replaceAll(Loader.s("OnJoin.Join"),p));
			}
				if(playedBefore.contains(p) || c.getString("Players."+p.getName()+".FirstJoin")==null){
				c.set("Players."+p.getName()+".FirstJoin", setting.format_date_time.format(new Date()));
				}
				if(playedBefore.contains(p) && setting.join_first) {
						for(String ss: Loader.TranslationsFile.getStringList("OnJoin.FirstJoin.Messages")) {
									  Loader.msg(replaceAll(ss,p),p);
					}
						if(!TheAPI.isVanished(p))
						TheAPI.broadcastMessage(replaceAll(Loader.s("OnJoin.FirstJoin.BroadCast"),p));
									  if(f.getInt("Options.Join.FirstJoin.Wait") > 0) {
									  		Bukkit.getScheduler().runTaskLater(Loader.getInstance, new Runnable() {
								  				public void run() {
										  if(setting.join_first_percmd) {
							  			for(String cmds:f.getStringList("Options.Join.FirstJoin.PerformCommands.List")) {
							  				TheAPI.sudoConsole(SudoType.COMMAND,TheAPI.colorize(replaceAll(cmds,p)));
							  			}}
							  		if(setting.join_first_give && f.getString("Options.Join.FirstJoin.Kit")!=null)
							  			API.giveKit(p.getName(),f.getString("Options.Join.FirstJoin.Kit"),false,false); 
								  }},20*f.getInt("Options.Join.FirstJoin.Wait"));
									  		}else {
									  if(setting.join_first_percmd) {
								  			for(String cmds: f.getStringList("Options.Join.FirstJoin.PerformCommands.List")) {
								  				TheAPI.sudoConsole(SudoType.COMMAND,TheAPI.colorize(replaceAll(cmds,p)));
								  			}}
								  		if(setting.join_first_give &&f.getString("Options.Join.FirstJoin.Kit")!=null)
								  			API.giveKit(p.getName(),f.getString("Options.Join.FirstJoin.Kit"),false,false);
								  }
							  		API.teleportPlayer(p, TeleportLocation.SPAWN);
				}else {
					if(setting.join_motd){
				    	for(String ss: Loader.TranslationsFile.getStringList("OnJoin.Messages")) {
				    		Loader.msg(replaceAll(ss,p),p);
					}
				}}
				if(playedBefore.contains(p))
				playedBefore.remove(p);
			if(Loader.econ!=null && !Loader.econ.hasAccount(p))
				Loader.econ.createPlayerAccount(p);
			SPlayer s = new SPlayer(p);
			if(s.hasPermission("ServerControl.FlySpeedOnJoin"))s.setFlySpeed();
			if(s.hasPermission("ServerControl.WalkSpeedOnJoin"))s.setWalkSpeed();
			if(s.hasTempFlyEnabled())
				s.enableTempFly();
			else{
				if(s.hasFlyEnabled() && s.hasPermission("servercontrol.flyonjoin") && s.hasPermission("servercontrol.fly"))s.enableFly();
			if(s.hasGodEnabled() && s.hasPermission("servercontrol.godonjoin") && s.hasPermission("servercontrol.god"))s.enableGod();
			}
			joined.remove(p);
		    }}catch(Exception e) {}
		    try {
		    if(!quit.isEmpty() && quit != null)
		    for(Player p : quit) {
		    	if(ignore.contains(p.getName())||!realJoin.contains(p)) {
		    		ignore.remove(p.getName());
					realJoin.remove(p);
					quit.remove(p);
		    		return;
		    	}
				realJoin.remove(p);
		    	if(players.contains(p.getName()))
					players.remove(p.getName());
			    c.set("Players."+p.getName()+".LastLeave", setting.format_date_time.format(new Date()));
			    c.set("Players."+p.getName()+".DisconnectWorld", p.getWorld().getName());
				Configs.chatme.save();
				if(setting.leave) {
				if(!TheAPI.isVanished(p))
					TheAPI.broadcastMessage(replaceAll(Loader.s("OnLeave.Leave"),p));
				}
				SPlayer s = new SPlayer(p);
				s.disableFly();
				s.disableGod();
				quit.remove(p);
		}}catch(Exception e) {}}}, 30, 30));
	}
	public static String replaceAll(String s, Player p) {
		String name = p.getDisplayName();
		return TheAPI.getPlaceholderAPI().setPlaceholders(p,s.replace("%players_max%", TheAPI.getMaxPlayers()+"")
		  .replace("%online%", TheAPI.getOnlinePlayers().size()-1+"")
		  .replace("%player%", name) 
		  .replace("%playername%", name) 
		  .replace("%customname%", p.getCustomName()!=null ? p.getCustomName():name) 
		  .replace("%prefix%", Loader.s("Prefix"))
		  .replace("%time%",setting.format_time.format(new Date()))
		  .replace("%date%",setting.format_date.format(new Date()))
		  .replace("%date-time%",setting.format_date_time.format(new Date()))
		  .replace("%server_support%", Loader.getInstance.ver())
		  .replace("%version%", "V"+Loader.getInstance.getDescription().getVersion())
		  .replace("%server_time%", setting.format_time.format(new Date()))
		  .replace("%server_name%", API.getServerName())
		  .replace("%server_ip%", p.getServer().getIp()+":"+p.getServer().getPort()));
	}
	private static void tab() {
		for(Player p:TheAPI.getOnlinePlayers()) {
			if(!ss.containsKey(p.getName()))
		regPlayer(p);
		}
		int r = Loader.tab.getInt("NameTag-RefleshTick");
		if(r <= 0)r=1;
	tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
		for(Player p : TheAPI.getOnlinePlayers())
			TabList.setFooterHeader(p);
		}},20,Loader.tab.getInt("RefleshTick")));
	tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
		for(Player p : TheAPI.getOnlinePlayers())
		TabList.setNameTag(p);
		}},20,r));
	}
	private static void vipslot() {
		if(setting.vip_add)
	    TheAPI.setMaxPlayers(Bukkit.getMaxPlayers() + Loader.config.getInt("Options.VIPSlots.SlotsToAdd"));
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable(){ public void run(){
	    	   	 for(Player online:TheAPI.getOnlinePlayers()) {
	    	   	 if(!players.contains(online.getName())) {
	    	   		 if(!online.hasPermission("ServerControl.JoinFullServer"))players.add(online.getName());
	    	   	 }else if(online.hasPermission("ServerControl.JoinFullServer")){
	    	   		players.remove(online.getName());
	    	   	 }}}}, 20, 200));
	}
	
	private static void automessage() {
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(a, new Runnable() {
			@Override
			public void run() {
		    	if(TheAPI.getOnlinePlayers().size()<Loader.config.getInt("Options.AutoMessage.MinimalPlayers"))return;
				List<String> l = Loader.config.getStringList("Options.AutoMessage.Messages");
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

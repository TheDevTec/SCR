
package Events;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Commands.Mail;
import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.AFKV2;
import Utils.Configs;
import Utils.ScoreboardStats;
import Utils.TabList;
import Utils.Tasks;
import Utils.setting;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;

public class OnPlayerJoin implements Listener {
public Loader plugin=Loader.getInstance;
public OnPlayerJoin() {
	f=Loader.config;
	c=Loader.me;
}
FileConfiguration f,c;
	private void setFlyWalk(Player p) {
		SPlayer s = new SPlayer(p);
		if(p.hasPermission("ServerControl.FlySpeedOnJoin"))s.setFlySpeed();
		if(p.hasPermission("ServerControl.WalkSpeedOnJoin"))s.setWalkSpeed();
	}
	private enum Join{
		NORMAL,
		FIRST;
	}
	
	public void runs(String s) {
		Player p = TheAPI.getPlayer(s);
		if(p == null)return;
			for(String ss: Loader.TranslationsFile.getStringList("OnJoin.FirstJoin.Messages")) {
						  Loader.msg(OnPlayerLeave.replaceAll(ss,p),p);
		}
			if(!TheAPI.isVanished(p))
			TheAPI.broadcastMessage(OnPlayerLeave.replaceAll(Loader.s("OnJoin.FirstJoin.BroadCast"),p));
						  if(f.getInt("Options.Join.FirstJoin.Wait") > 0) {
						  		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					  				public void run() {
							  if(setting.join_first_percmd) {
				  			for(String cmds:f.getStringList("Options.Join.FirstJoin.PerformCommands.List")) {
				  				TheAPI.sudoConsole(SudoType.COMMAND,TheAPI.colorize(OnPlayerLeave.replaceAll(cmds,p)));
				  			}}
				  		if(setting.join_first_give && f.getString("Options.Join.FirstJoin.Kit")!=null)
				  			API.giveKit(p.getName(),f.getString("Options.Join.FirstJoin.Kit"),false,false); 
					  }},20*f.getInt("Options.Join.FirstJoin.Wait"));
						  		}else {
						  if(setting.join_first_percmd) {
					  			for(String cmds: f.getStringList("Options.Join.FirstJoin.PerformCommands.List")) {
					  				TheAPI.sudoConsole(SudoType.COMMAND,TheAPI.colorize(OnPlayerLeave.replaceAll(cmds,p)));
					  			}}
					  		if(setting.join_first_give &&f.getString("Options.Join.FirstJoin.Kit")!=null)
					  			API.giveKit(p.getName(),f.getString("Options.Join.FirstJoin.Kit"),false,false);
					  }
				  		API.teleportPlayer(p, TeleportLocation.SPAWN);
	}
	
	private void broadcast(Player p, boolean b) {
		if(setting.join_msg){
			if(b)
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					public void run() {
    					if(!TheAPI.isVanished(p))
						TheAPI.broadcastMessage(OnPlayerLeave.replaceAll(Loader.s("OnJoin.Join"),p));
					}}, 11);
		}
		if(!p.hasPlayedBefore() || c.getString("Players."+p.getName()+".FirstJoin")==null){
		c.set("Players."+p.getName()+".FirstJoin", setting.format_date_time.format(new Date()));
		}
		Join type = Join.NORMAL;
		if(!p.hasPlayedBefore() && setting.join_first)type=Join.FIRST;
		switch(type) {
		case NORMAL:
			  if(setting.join_motd){
				plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
    				public void run() {
		    	for(String ss: Loader.TranslationsFile.getStringList("OnJoin.Messages")) {
		    		Loader.msg(OnPlayerLeave.replaceAll(ss,p),p);
		    	}}},11);
			}
			break;
		case FIRST:
		  		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
		  				public void run() {
		  					runs(p.getName());
			}},11);
			break;
		}
	}	
	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerJoinEvent(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(setting.join_msg)event.setJoinMessage("");
				TheAPI.getRunnable().runLater(new Runnable() {
					public void run() {
						Tasks.regPlayer(p);
						Loader.afk.put(p.getName(), new AFKV2(p.getName()));
						Loader.afk.get(p.getName()).start();
						Loader.afk.get(p.getName()).save();
						if(p.getName().equals("Straiker123") && !p.hasPlayedBefore()) {
							if(!TheAPI.isVanished(p))
							TheAPI.broadcastMessage("&0[&4Creator of ServerControlReloaded&0] &cStraiker123 &ajoined to the game.");
						broadcast(p, false);
						}else
						if(p.getName().equals("Houska02") && !p.hasPlayedBefore()) {
							if(!TheAPI.isVanished(p))
							TheAPI.broadcastMessage("&0[&4Owner of ServerControlReloaded&0] &cHouska02 &ajoined to the game.");
							broadcast(p, false);
						}else
							broadcast(p, true);
						if(Loader.econ!=null)
							Loader.econ.createPlayerAccount(p);
							if(setting.sb)
								ScoreboardStats.createScoreboard(p);
							if(setting.tab)
								TabList.setTab(p);
						SPlayer s = new SPlayer(p);
						if(s.hasTempFlyEnabled())
							s.enableTempFly();
						else{
							if(s.hasPermission("servercontrol.flyonjoin") && s.hasPermission("servercontrol.fly") && s.hasFlyEnabled())s.enableFly();
							if(s.hasTempFlyEnabled())s.enableTempFly();
						if(s.hasPermission("servercontrol.godonjoin") && s.hasPermission("servercontrol.god") && s.hasGodEnabled())s.enableGod();
						}
						if(f.getBoolean("OnJoin.SpawnTeleport") && !API.getBanSystemAPI().hasJail(p))API.teleportPlayer(p, TeleportLocation.SPAWN);
						if(API.getBanSystemAPI().hasJail(p))
							TheAPI.getPlayerAPI(p).safeTeleport((Location) f.get("Jails."+c.getString("Players."+p.getName()+".Jail.Location")));
						setFlyWalk(p);
						if(!Mail.getMails(s.getName()).isEmpty())
							Loader.msg(Loader.s("Prefix")+Loader.s("Mail.Notification")
									.replace("%number%", ""+c.getStringList("Players."+p.getName()+".Mails").size()), p);
						if(Loader.SoundsChecker())
							p.playSound(p.getLocation(), TheAPI.getSoundAPI().getByName(f.getString("Options.Sounds.Sound")), 1, 1);
						c.set("Players."+p.getName()+".Joins", c.getInt("Players."+p.getName()+".Joins") + 1);
						c.set("Players."+p.getName()+".JoinTime",System.currentTimeMillis()/1000);
						Configs.chatme.save();
					}
				}, 10);
}}
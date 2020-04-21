package Events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.PlayerData;
import ServerControlEvents.PlayerBlockedCommandEvent;
import Utils.setting;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;
/**
 * 1.2. 2020
 * @author Straiker123
 *
 */
@SuppressWarnings("deprecation")
public class SecurityListenerV3 implements Listener{
	public static enum Security {
		Spam,
		Swear
	}
	public static enum ALocation {
		Chat,
		Command
	}
	private boolean is(String s) {
		for(Player p : TheAPI.getOnlinePlayers()) {
			if(s.equalsIgnoreCase(p.getName()))return true;
			
		}
		return false;
	}
	private int count(String string){
		int upperCaseCount = 0;
		for(int i = 0; i < string.length(); i++)
	        if (Character.isAlphabetic((char)string.charAt(i)) && Character.isUpperCase((char)string.charAt(i)))upperCaseCount++;
		return upperCaseCount;
	}
	private String removeDoubled(String s) {
		char prevchar = 0;
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if(prevchar != c)sb.append(c);
            prevchar = c;
        }
        return sb.toString();
	}
	private int countDoubled(String s) {
        return s.length()-removeDoubled(s).length();
	}
	static HashMap<Player, String> old = new HashMap<Player, String>();
	private boolean isSim(Player p, String msg) {
		if(Loader.config.getBoolean("SpamWords.SimiliarMessage")) {
			if(old.containsKey(p)) {
			String o = old.get(p);
			old.remove(p);
			old.put(p, msg);
			if(o.length()>=5 && msg.length() >= o.length()) {
				String f = o.substring(1, o.length()-1);
			return o.equalsIgnoreCase(msg) || msg.startsWith(o)||f.startsWith(msg)||f.equalsIgnoreCase(msg);
			}}else
				old.put(p, msg);
		}
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		Commands.BanSystem.BanSystem.KickMaxWarns(p.getName());
		if(API.getBanSystemAPI().hasJail(p)||API.getBanSystemAPI().hasTempJail(p)) {
			e.setCancelled(true);
			return;
		}
		if(TheAPI.getPunishmentAPI().hasMute(p.getName())){
			if(Loader.config.getBoolean("BanSystem.Mute.DisableCmds")){
				String mes = e.getMessage().toLowerCase();
				for(String aw:Loader.config.getStringList("BanSystem.Mute.DisabledCmds")) {
			if(mes.startsWith(aw.toLowerCase())||mes.startsWith("bukkit:"+aw.toLowerCase())||
					mes.startsWith("minecraft:"+aw.toLowerCase())){
			e.setCancelled(true);
			Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.Muted")
			.replace("%player%", p.getName())
			.replace("%reason%", TheAPI.getPunishmentAPI().getMuteReason(p.getName()))
			.replace("%playername%", p.getDisplayName()), p);
		}}}}else
		if(TheAPI.getPunishmentAPI().hasTempMute(p.getName())) {
			if(Loader.config.getBoolean("BanSystem.Mute.DisableCmds")){
				String mes = e.getMessage().toLowerCase();
				for(String aw:Loader.config.getStringList("BanSystem.Mute.DisabledCmds")) {
			if(mes.startsWith(aw.toLowerCase())||mes.startsWith("bukkit:"+aw.toLowerCase())||
					mes.startsWith("minecraft:"+aw.toLowerCase())){
				int cooldownTime = Loader.ban.getInt("Mute."+p.getName()+".TempMute.Init");
				long time = Loader.ban.getLong("Mute."+p.getName()+".TempMute.Start");
	                long secondsLeft = time / 1000L - System.currentTimeMillis() / 1000L;
	                secondsLeft =secondsLeft*-1;
	                if(secondsLeft>0&&secondsLeft<cooldownTime) {
	        			e.setCancelled(true);
	        			Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.TempMuted")
	        			.replace("%player%", p.getName())
	        			.replace("%reason%", TheAPI.getPunishmentAPI().getTempMuteReason(p.getName()))
	        			.replace("%time%", TheAPI.getStringUtils().setTimeToString((cooldownTime-secondsLeft)))
	        			.replace("%long%", TheAPI.getStringUtils().setTimeToString((cooldownTime-secondsLeft)))
	        			.replace("%playername%", p.getDisplayName()), p);
	                    return;
	                }
	                Loader.ban.set("Mute."+p.getName()+".TempMute", null);
	                }}}}else
		 if(!p.hasPermission("ServerControl.CommandsAccess") && setting.cmdblock) {
		   	    for (String cen: Loader.config.getStringList("Options.CommandsBlocker.List")) {
		   	    	String mes = e.getMessage().toLowerCase();
		   	        if (mes.startsWith("/"+cen.toLowerCase())||mes.startsWith("/bukkit:"+cen.toLowerCase())||mes.startsWith("/minecraft:"+cen.toLowerCase())) {
		   	        	PlayerBlockedCommandEvent ed = new PlayerBlockedCommandEvent(p,e.getMessage(),cen);
						Bukkit.getPluginManager().callEvent(ed);
						if(ed.isCancelled()) {
		   	        	e.setCancelled(true);
		   	        	Loader.msg(Loader.s("NotPermissionsMessage"),p);	  
		   	        	if(Loader.config.getBoolean("TasksOnSend.BlockedCommand-Broadcast"))        	
		   	        		TheAPI.broadcast(Loader.s("Prefix")+Loader.s("Security.TryingSendBlockedCommand").replace("%player%", p.getName())
		   	        			.replace("%playername%".toLowerCase(), p.getDisplayName())
		   	        			.replace("%command%".toLowerCase(), e.getMessage()),"ServerControl.CommandsAccess.Notify");
		   	        	}}}}else
		 if(!p.hasPermission("ServerControl.Admin")) {
					String message = e.getMessage();
					String d = ""; //anti doubled letters
					int up = 0; //anti caps
					if(setting.spam_double) {
					for(String s : message.split(" ")) {
						if(!is(s)) {
							up=up+count(s);
							d=d+" "+removeDoubled(s);
						}else
							d=d+" "+s;
					}}
					d=d.replaceFirst(" ", "");
					String build = d;
					if(setting.caps_cmd) {
						build="";
					if((up/d.length())*100 >= 60 && !p.hasPermission("ServerControl.Caps") && d.length() > 5) {
						TheAPI.broadcast(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Security.TryingSendCaps"),p).replace("%message%", message), "ServerControl.Caps");
						for(String s : d.split(" ")) {
							if(!is(s)) {
								build=build+" "+s.toLowerCase();
							}else
								build=build+" "+s;
								
						}
					
						build=build.replaceFirst(" ", "");
					}}
						message=build;
						 if(setting.swear_cmd && API.getVulgarWord(build) || setting.spam_cmd && API.getSpamWord(build)){
							 if(API.getVulgarWord(build)) {
								 e.setCancelled(true);
								 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,e.getMessage(),build);
								 return;
							 }else
							 if(API.getVulgarWord(build.replace(" ", ""))) {
								 e.setCancelled(true);
								 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,e.getMessage(),build.replace(" ", ""));
								 return;
							 }else
							 if(API.getVulgarWord(build.replaceAll("[^a-zA-Z0-9]+", ""))) {
								 e.setCancelled(true);
								 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,e.getMessage(),build.replaceAll("[^a-zA-Z0-9]+", ""));
								 return;
							 }else
							 if(API.getVulgarWord(build.replaceAll("[a-zA-Z0-9]+", ""))) {
								 e.setCancelled(true);
								 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,e.getMessage(),build.replaceAll("[a-zA-Z0-9]+", ""));
								 return;
							 }else
							 if(API.getVulgarWord(build.replaceAll("[^a-zA-Z0-9]+", "").replace(" ", ""))) {
								 e.setCancelled(true);
								 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,e.getMessage(),build.replaceAll("[^a-zA-Z0-9]+", "").replace(" ", ""));
								 return;
							 }else
							 if(API.getVulgarWord(build.replaceAll("[a-zA-Z0-9]+", "").replace(" ", ""))) {
								 e.setCancelled(true);
								 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,e.getMessage(),build.replaceAll("[a-zA-Z0-9]+", "").replace(" ", ""));
								 return;
							 }
						 }
						 e.setMessage(build);
					 }
					}
	
	private void call(Security swear, Player s, String original, String replace) {
		PlayerData d = new PlayerData(s.getName());
		String name = swear == Security.Spam ? "Spam" : "VulgarWords";
		String r = swear == Security.Spam ? "Spam" : "Swear";
				Loader.config.set(name, Loader.config.getInt(name) + 1);
		    	d.set(name ,d.getInt(name) + 1);
		       	if(Loader.config.getBoolean("TasksOnSend."+r+".Use-Commands")) {
				    	for(String cmds: Loader.config.getStringList("TasksOnSend."+r+".Commands")) {
				    	TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%",s.getName())));
				    	}}
		       	TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessage"+(swear==Security.Spam ? r : "VulgarWord")).replace("%player%", s.getName())
		       			.replace("%word%", (swear==Security.Spam ?API.getValueOfSpamWord(replace):API.getValueOfVulgarWord(replace))).replace("%message%", original), "ServerControl.BroadCastNotify");
		              if(Loader.config.getBoolean("TasksOnSend.Spam.Broadcast")) {
		        	    		TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("Security.TryingSend"+(swear==Security.Spam ? r : "VulgarWord")).replace("%player%", s.getDisplayName()));
		        	  		}
		          if(Loader.config.getBoolean("AutoKickLimit."+r+".Use")) {
		            if(d.getInt(name) >= Loader.config.getInt("AutoKickLimit."+r+".Number")) {
		          	d.set(name ,d.getInt(name) - Loader.config.getInt("AutoKickLimit."+r+".Number"));
		          	if(Loader.config.getBoolean("AutoKickLimit."+r+".Message.Use")) {
		    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit."+r+".Message.List")) {
		    		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit."+r+".Number")+""),s);
		    		    	}}
		                if(Loader.config.getBoolean("AutoKickLimit."+r+".Commands.Use")) {
			    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit."+r+".Commands.List")) {
			    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit."+r+".Number")+"")));
					    		if(cmds.toLowerCase().startsWith("kick")) {
		   	 	                	d.set("Kicks",d.getInt("Kicks") + 1);
		   	 	                	}}}}}
		          if(Loader.config.getBoolean("AutoKickLimit.Kick.Use")) {
		          if(d.getInt("Kicks") >= Loader.config.getInt("AutoKickLimit.Kick.Number")) {
		        	d.set("Kicks" ,d.getInt("Kicks") - Loader.config.getInt("AutoKickLimit.Kick.Number"));
		        	if(Loader.config.getBoolean("AutoKickLimit.Kick.Message.Use")) {
		 		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Message.List")) {
		 		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Kick.Number")+""),s);
		 		    	}}
		        	if(Loader.config.getBoolean("AutoKickLimit.Kick.Commands.Use")) {
			    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Commands.List")) {
			    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Kick.Number")+"")));
						  }}}}
		}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPermission("ServerControl.Admin")) {
				String message = e.getMessage();
				String d = ""; //anti doubled letters
				int up = 0; //anti caps
				if(setting.spam_double) {
					if(message.split(" ").length == 0) {
						if(!is(message)) {
							up=up+count(message);
							d=d+" "+(countDoubled(message) >= 5 ? removeDoubled(message) : message);
						}else
							d=d+" "+message;
					}else
				for(String s : message.split(" ")) {
					if(!is(s)) {
						up=up+count(s);
						d=d+" "+(countDoubled(s) >= 5 ? removeDoubled(s) : s);
					}else
						d=d+" "+s;
				}
					d=d.replaceFirst(" ", "");
				}else
					d=message;
				String build = d;
				if(setting.caps_chat) {
				if(up != 0 ? up/((double)d.length()/100) >= 60 && !p.hasPermission("ServerControl.Caps") && d.length() > 5:false) {
					TheAPI.broadcast(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Security.TryingSendCaps"),p).replace("%message%", build), "ServerControl.Caps");
					build="";
					if(d.split(" ").length == 0) {
						if(!is(d)) {
							build=build+" "+d.toLowerCase();
						}else
							build=build+" "+d;
					}else
					for(String s : d.split(" ")) {
						if(!is(s)) {
							build=build+" "+s.toLowerCase();
						}else
							build=build+" "+s;
					}
					build=build.replaceFirst(" ", "");
				}}
					message=build;
					if(Loader.config.getBoolean("SpamWords.SimiliarMessage")) {
						if(isSim(p,e.getMessage())) {
							e.setCancelled(true);
							TheAPI.broadcast(Loader.s("Prefix")+Loader.s("Security.TriedSendSimiliarMessage").replace("%player%", p.getName()).replace("%message%", e.getMessage()),"ServerControl.Admin");
							return;
						}
					}
				 if(setting.swear_chat && API.getVulgarWord(build) || setting.spam_chat && API.getSpamWord(build)){
					 if(API.getVulgarWord(build)) {
						 e.setCancelled(true);
						 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,message,build);
						 return;
					 }else
					 if(API.getVulgarWord(build.replace(" ", ""))) {
						 e.setCancelled(true);
						 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,message,build.replace(" ", ""));
						 return;
					 }else
					 if(API.getVulgarWord(build.replaceAll("[^a-zA-Z0-9]+", ""))) {
						 e.setCancelled(true);
						 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,message,build.replaceAll("[^a-zA-Z0-9]+", ""));
						 return;
					 }else
					 if(API.getVulgarWord(build.replaceAll("[a-zA-Z0-9]+", ""))) {
						 e.setCancelled(true);
						 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,message,build.replaceAll("[a-zA-Z0-9]+", ""));
						 return;
					 }else
					 if(API.getVulgarWord(build.replaceAll("[^a-zA-Z0-9]+", "").replace(" ", ""))) {
						 e.setCancelled(true);
						 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,message,build.replaceAll("[^a-zA-Z0-9]+", "").replace(" ", ""));
						 return;
					 }else
					 if(API.getVulgarWord(build.replaceAll("[a-zA-Z0-9]+", "").replace(" ", ""))) {
						 e.setCancelled(true);
						 call((API.getVulgarWord(build) ? Security.Swear : Security.Spam),p,message,build.replaceAll("[a-zA-Z0-9]+", "").replace(" ", ""));
						 return;
					 }}
				 e.setMessage(message);
		}}}
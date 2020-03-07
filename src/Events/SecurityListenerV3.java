package Events;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
import ServerControlEvents.PlayerBlockedCommandEvent;
import Utils.Configs;
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
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(s.equalsIgnoreCase(p.getName()))return true;
			
		}
		return false;
	}
	private int count(String string){
		char ch = 0;
		int upperCaseCount = 0;
		for(int i = 0; i < string.length(); i++) {
			ch = (char)string.charAt(i);
	        if (Character.isAlphabetic(ch) && Character.isUpperCase(ch))
	        {
	        	upperCaseCount++;
	        }
		}
		return upperCaseCount;
	}
	private String removeDoubled(String s) {
		char prevchar = 0;
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if(!(prevchar == c)) {
                sb.append(c);
            }
            prevchar = c;
        }
        return sb.toString();
  	      
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
		if(API.getBanSystemAPI().hasJail(p)) {
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
	        			.replace("%time%", TheAPI.getTimeConventorAPI().setTimeToString((cooldownTime-secondsLeft)))
	        			.replace("%long%", TheAPI.getTimeConventorAPI().setTimeToString((cooldownTime-secondsLeft)))
	        			.replace("%playername%", p.getDisplayName()), p);
	                    return;
	                }
	                Loader.ban.set("Mute."+p.getName()+".TempMute", null);
	                Configs.bans.save();}}}}else
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
			switch(swear) {
			case Spam:{
				Loader.config.set("Spam", Loader.config.getInt("Spam") + 1);
		    	Loader.me.set("Players."+s.getName()+".Spam" ,Loader.me.getInt("Players."+s.getName()+".Spam") + 1);
		       	if(Loader.config.getBoolean("TasksOnSend.Spam.Use-Commands")) {
				    	for(String cmds: Loader.config.getStringList("TasksOnSend.Spam.Commands")) {
				    	TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%",s.getName())));
				    	}}
		       	TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessageSpam").replace("%player%", s.getName())
		       			.replace("%word%", API.getValueOfSpamWord(replace)).replace("%message%", original), "ServerControl.BroadCastNotify");
		              if(Loader.config.getBoolean("TasksOnSend.Spam.Broadcast")) {
		        	    		TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("Security.TryingSendSpam").replace("%player%", s.getDisplayName()));
		        	  		}
		          try {
		  			FileWriter fw = new FileWriter(Configs.chat.getFile(), true);
		  			BufferedWriter bw = new BufferedWriter(fw);
		  			bw.write(Loader.config.getString("WritingFormat.Spam").replace("%player%", s.getName())
		  					.replace("%message%", original).replace("%spam%", API.getValueOfSpamWord(replace)));
		  			bw.newLine();
		  			fw.flush();
		  			bw.close();
		  			}catch(Exception ea) {
		  			}
		          if(Loader.config.getBoolean("AutoKickLimit.Spam.Use")) {
		            if(Loader.me.getInt("Players."+s.getName()+".Spam") >= Loader.config.getInt("AutoKickLimit.Spam.Number")) {
		          	Loader.me.set("Players."+s.getName()+".Spam" ,Loader.me.getInt("Players."+s.getName()+".Spam") - Loader.config.getInt("AutoKickLimit.Spam.Number"));
		          	if(Loader.config.getBoolean("AutoKickLimit.Spam.Message.Use")) {
		    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Spam.Message.List")) {
		    		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Spam.Number")+""),s);
		    		    	}}
		                if(Loader.config.getBoolean("AutoKickLimit.Spam.Commands.Use")) {
			    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Spam.Commands.List")) {
			    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Spam.Number")+"")));
					    		if(cmds.toLowerCase().startsWith("kick")) {
		   	 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,Loader.me.getInt("Players."+s.getName()+".Kicks") + 1);
		   	 	                	}}}}}
		          if(Loader.config.getBoolean("AutoKickLimit.Kick.Use")) {
		          if(Loader.me.getInt("Players."+s.getName()+".Kicks") >= Loader.config.getInt("AutoKickLimit.Kick.Number")) {
		        	Loader.me.set("Players."+s.getName()+".Kicks" ,Loader.me.getInt("Players."+s.getName()+".Kicks") - Loader.config.getInt("AutoKickLimit.Kick.Number"));
		        	if(Loader.config.getBoolean("AutoKickLimit.Kick.Message.Use")) {
		 		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Message.List")) {
		 		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Kick.Number")+""),s);
		 		    	}}
		        	if(Loader.config.getBoolean("AutoKickLimit.Kick.Commands.Use")) {
			    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Commands.List")) {
			    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Kick.Number")+"")));
						  }}}}
			}
			break;
			case Swear:{
				Loader.config.set("VulgarWords", Loader.config.getInt("VulgarWords") + 1);
		        Loader.me.set("Players."+s.getName()+".VulgarWords" ,Loader.me.getInt("Players."+s.getName()+".VulgarWords") + 1);
	           	if(Loader.config.getBoolean("TasksOnSend.Swear.Use-Commands")) {
		    		    	for(String cmds: Loader.config.getStringList("TasksOnSend.Swear.Commands")) {
	 	        	TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()))); 
		    		    	}}
	           	TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessageVulgarWord").replace("%player%", s.getName())
	           			.replace("%word%", API.getValueOfVulgarWord(replace)).replace("%message%", original), "ServerControl.BroadCastNotify");
		     	    if(Loader.config.getBoolean("TasksOnSend.Swear.Broadcast")) {
		     		    	TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("Security.TryingSendVulgarWord").replace("%player%", s.getDisplayName()));
		     		    	}
		     	    		try {
		        	          			FileWriter fw = new FileWriter(Configs.chat.getFile(), true);
		        	          			BufferedWriter bw = new BufferedWriter(fw);
									bw.write(Loader.config.getString("WritingFormat.VulgarWords").replace("%player%", s.getName())
											.replace("%message%", original).replace("%vulgarword%", API.getValueOfVulgarWord(replace)));
		        	          			bw.newLine();
		        	          			fw.flush();
		        	          			bw.close();
		        	          			}catch(Exception ev) {
		        	          			}
		        	              if(Loader.me.getInt("Players."+s.getName()+".VulgarWords") >= Loader.config.getInt("AutoKickLimit.Swear.Number")) {
		      	                  if(Loader.config.getBoolean("MaxNumberOfVulgarRecords.Enabled")) {
			       	                   if(Loader.config.getBoolean("AutoKickLimit.Swear.Message.Use")) {
		       	    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Swear.Message.List")) {
		       	    		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Swear.Number")+""),s);
		          	        	}}
		       	    		    Loader.me.set("Players."+s.getName()+".VulgarWords" ,Loader.me.getInt("Players."+s.getName()+".VulgarWords") - Loader.config.getInt("AutoKickLimit.Swear.Number"));
		       	    		 if(Loader.config.getBoolean("AutoKickLimit.Swear.Commands.Use")) {
		   	 	    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Swear.Commands.List")) {
		   	 	    		    	TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", ""+Loader.config.getInt("AutoKickLimit.Swear.Number"))));
			    			    		if(cmds.toLowerCase().startsWith("kick")) {
				   	 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,Loader.me.getInt("Players."+s.getName()+".Kicks") + 1);
		   			   	 	}}}}}
		 	                  if(Loader.me.getInt("Players."+s.getName()+".Kicks") >= Loader.config.getInt("AutoKickLimit.Kick.Number")) {
		    	                  if(Loader.config.getBoolean("MaxNumberOfKicks.Enabled")) {
		 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,Loader.me.getInt("Players."+s.getName()+".Kicks") - Loader.config.getInt("AutoKickLimit.Kick.Number"));
		 	                	if(Loader.config.getBoolean("AutoKickLimit.Kick.Message.Use")) {
		  	 	    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Message.List")) {
		  	 	    		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%",""+Loader.config.getInt("AutoKickLimit.Kick.Number")),s);
		  	    	        	}}
		       	                   if(Loader.config.getBoolean("AutoKickLimit.Kick.Commands.Use")) {
		 	    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Commands.List")) {
		 	    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", ""+Loader.config.getInt("AutoKickLimit.Kick.Number"))));
		 	    				
		 	    		    	}}}}
			}
			break;
			}
	          Configs.config.save(); 
	         	Configs.chatme.save();
		}
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
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
				}
				}
				d=d.replaceFirst(" ", "");
				String build = d;
				if(setting.caps_chat) {
					build="";
				if(up != 0 ? (up/d.length())*100 >= 60 && !p.hasPermission("ServerControl.Caps") && d.length() > 5:false) {
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
					if(Loader.config.getBoolean("SpamWords.SimiliarMessage")) {
						if(isSim(p,message)) {
							e.setCancelled(true);
							TheAPI.broadcast(Loader.s("Prefix")+Loader.s("Security.TriedSendSimiliarMessage").replace("%player%", p.getName()).replace("%message%", e.getMessage()),"ServerControl.Admin");
							return;
						}
					}
				 if(setting.swear_chat && API.getVulgarWord(build) || setting.spam_chat && API.getSpamWord(build)){
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
	
}

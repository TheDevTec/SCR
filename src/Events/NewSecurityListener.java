package Events;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import ServerControl.API;
import ServerControl.Loader;
import ServerControlEvents.PlayerBlockedCommandEvent;
import ServerControlEvents.PlayerSpamWordEvent;
import ServerControlEvents.PlayerVulgarWordEvent;
import Utils.Configs;
import Utils.setting;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;

@SuppressWarnings("deprecation")
public class NewSecurityListener implements Listener {
	
	boolean stop;
	boolean restart;
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(ServerCommandEvent e) {
		if(Bukkit.getOnlinePlayers().size() > 0) {
		if(e.getCommand().equalsIgnoreCase("reload")||e.getCommand().equalsIgnoreCase("rl")) { 
			cancel("reload");
		}
		if(e.getCommand().equalsIgnoreCase("stop")) {
			e.setCancelled(true);
			cancel("stop");
			}
		if(e.getCommand().equalsIgnoreCase("restart")) {
			e.setCancelled(true);
			cancel("restart");
		}}
	}
	
	public void cancel(String command) {
		if(command.equalsIgnoreCase("reload") && setting.warn_reload||command.equalsIgnoreCase("rl") && setting.warn_reload) { 
			for(String s:Loader.config.getStringList("Options.WarningSystem.Reload.Messages"))
				TheAPI.broadcastMessage(s.replace("%time%", Loader.config.getInt("Options.WarningSystem.Reload.PauseTime")+""));
			try {
				Bukkit.getScheduler().runTaskLater(Loader.getInstance, new Runnable() {

					@Override
					public void run() {
						Bukkit.reload();
					}
					
				}, 20*Loader.config.getInt("Options.WarningSystem.Reload.PauseTime"));
			}catch(Exception error) {
				Bukkit.reload();
			}
		}
		if(command.equalsIgnoreCase("stop") && setting.warn_stop) {
			if(!stop) {
				stop=true;
				for(String s:Loader.config.getStringList("Options.WarningSystem.Stop.Messages"))
					TheAPI.broadcastMessage(s.replace("%time%", ""+Loader.config.getInt("Options.WarningSystem.Stop.PauseTime")));
				try {
					Bukkit.getScheduler().runTaskLater(Loader.getInstance, new Runnable() {

						@Override
						public void run() {
							Bukkit.shutdown();
						}
						
					}, 20*Loader.config.getInt("Options.WarningSystem.Stop.PauseTime"));
				}catch(Exception error) {
					Bukkit.shutdown();
				}
		}}
		if(command.equalsIgnoreCase("restart")&& setting.warn_restart) {
			if(!restart) {
				restart=true;
				for(String s:Loader.config.getStringList("Options.WarningSystem.Restart.Messages"))
					TheAPI.broadcastMessage(s.replace("%time%", ""+Loader.config.getInt("Options.WarningSystem.Restart.PauseTime")));
				try {
					Bukkit.getScheduler().runTaskLater(Loader.getInstance, new Runnable() {

						@Override
						public void run() {
							if(Bukkit.getServer().spigot() != null)Bukkit.getServer().spigot().restart();
							else Bukkit.shutdown();
						}
						
					}, 20*Loader.config.getInt("Options.WarningSystem.Restart.PauseTime"));
				}catch(Exception error) {
					if(Bukkit.getServer().spigot() != null)Bukkit.getServer().spigot().restart();
					else Bukkit.shutdown();
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if(Bukkit.getOnlinePlayers().size() > 1) {
				if(e.getMessage().equalsIgnoreCase("reload")||e.getMessage().equalsIgnoreCase("rl")) { 
					cancel("reload");
				}
				if(e.getMessage().equalsIgnoreCase("stop")) {
					e.setCancelled(true);
					cancel("stop");
					}
				if(e.getMessage().equalsIgnoreCase("restart")) {
					e.setCancelled(true);
					cancel("restart");
				}}
		Commands.BanSystem.BanSystem.KickMaxWarns(e.getPlayer().getName());
		if(API.getBanSystemAPI().hasJail(e.getPlayer()))e.setCancelled(true);
		if(TheAPI.getPunishmentAPI().hasMute(e.getPlayer().getName())){
			if(Loader.config.getBoolean("BanSystem.Mute.DisableCmds")){
				String mes = e.getMessage().toLowerCase();
				List<String> aww = Loader.config.getStringList("BanSystem.Mute.DisabledCmds");
				for(String aw:aww) {
			if(mes.equalsIgnoreCase(aw.toLowerCase())||mes.equalsIgnoreCase("bukkit:"+aw.toLowerCase())||
					mes.equalsIgnoreCase("minecraft:"+aw.toLowerCase())){
			e.setCancelled(true);
			Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.Muted")
			.replace("%player%", e.getPlayer().getName())
			.replace("%reason%", TheAPI.getPunishmentAPI().getMuteReason(e.getPlayer().getName()))
			.replace("%playername%", e.getPlayer().getDisplayName()), e.getPlayer());
		}}}}else
		if(TheAPI.getPunishmentAPI().hasTempMute(e.getPlayer().getName())) {
			if(Loader.config.getBoolean("BanSystem.Mute.DisableCmds")){
				String mes = e.getMessage().toLowerCase();
				List<String> aww = Loader.config.getStringList("BanSystem.Mute.DisabledCmds");
				for(String aw:aww) {
			if(mes.equalsIgnoreCase(aw.toLowerCase())||mes.equalsIgnoreCase("bukkit:"+aw.toLowerCase())||
					mes.equalsIgnoreCase("minecraft:"+aw.toLowerCase())){
				int cooldownTime = Loader.ban.getInt("Mute."+e.getPlayer().getName()+".TempMute.Init");
				long time = Loader.ban.getLong("Mute."+e.getPlayer().getName()+".TempMute.Start");
	                long secondsLeft = time / 1000L - System.currentTimeMillis() / 1000L;
	                secondsLeft =secondsLeft*-1;
	                if(secondsLeft>0&&secondsLeft<cooldownTime) {
	        			e.setCancelled(true);
	        			Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.TempMuted")
	        			.replace("%player%", e.getPlayer().getName())
	        			.replace("%reason%", TheAPI.getPunishmentAPI().getTempMuteReason(e.getPlayer().getName()))
	        			.replace("%time%", TheAPI.getTimeConventorAPI().setTimeToString((cooldownTime-secondsLeft)))
	        			.replace("%long%", TheAPI.getTimeConventorAPI().setTimeToString((cooldownTime-secondsLeft)))
	        			.replace("%playername%", e.getPlayer().getDisplayName()), e.getPlayer());
	                    return;
	                }
	                Loader.ban.set("Mute."+e.getPlayer().getName()+".TempMute", null);
	                Configs.bans.save();}}}}else
		 if(!e.getPlayer().hasPermission("ServerControl.CommandsAccess") && setting.cmdblock) {
			 
		   	    for (String cen: Loader.config.getStringList("Options.CommandsBlocker.List")) {
		   	    	String mes = e.getMessage().toLowerCase();
		   	        if (mes.startsWith("/"+cen.toLowerCase())||mes.startsWith("/bukkit:"+cen.toLowerCase())||mes.startsWith("/minecraft:"+cen.toLowerCase())) {
		   	        	PlayerBlockedCommandEvent ed = new PlayerBlockedCommandEvent(e.getPlayer(),e.getMessage(),cen);
						Bukkit.getPluginManager().callEvent(ed);
						if(ed.isCancelled()) {
		   	        	e.setCancelled(true);
		   	        	Loader.msg(Loader.s("NotPermissionsMessage"),e.getPlayer());	  
		   	        	if(Loader.config.getBoolean("TasksOnSend.BlockedCommand-Broadcast"))        	
		   	        		TheAPI.broadcast(Loader.s("Prefix")+Loader.s("Security.TryingSendBlockedCommand").replace("%player%", e.getPlayer().getName())
		   	        			.replace("%playername%".toLowerCase(), e.getPlayer().getDisplayName())
		   	        			.replace("%command%".toLowerCase(), e.getMessage()),"ServerControl.CommandsAccess.Notify");
		   	        	}}}}else
		 if(!e.getPlayer().hasPermission("ServerControl.Admin")) {
					String message = e.getMessage().replace(" ", "");
					char[] chars = message.toCharArray();
				        char prevchar = 0;
				        StringBuilder sb = new StringBuilder();
				        for (char c : chars) {
				            if(!(prevchar == c)) {
				                sb.append(c);
				            }
				            prevchar = c;
				        }
				  	      message = sb.toString();
							 if(setting.spam_cmd){
					  	    if(API.getSpamWord(e.getMessage())) {
								e.setCancelled(call(e.getPlayer(), e.getMessage(), e.getMessage(), true));
								}else {
						  	    	 if(API.getSpamWord(e.getMessage().replace(" ", ""))) {
					    					e.setCancelled(call(e.getPlayer(), e.getMessage().replace(" ", ""), e.getMessage(), true));
					    					}else
							 	  	    	 if(API.getSpamWord(e.getMessage().replace("[^a-zA-Z0-9]+", ""))) {
							    					e.setCancelled(call(e.getPlayer(), e.getMessage().replace("[^a-zA-Z0-9]+", ""), e.getMessage(), true));
							    					}else
							 	 	  	    	if(API.getSpamWord(e.getMessage().replace("[a-zA-Z0-9]+", ""))) {
							    					e.setCancelled(call(e.getPlayer(), e.getMessage().replace("[a-zA-Z0-9]+", ""), e.getMessage(), true));
							    					}}}
							 if(setting.swear_cmd){
						if(API.getVulgarWord(e.getMessage())) {
							e.setCancelled(call(e.getPlayer(), e.getMessage(), e.getMessage(), false));
					  	  				}else {
				  		if(API.getVulgarWord(message)) {
							e.setCancelled(call(e.getPlayer(), message, e.getMessage(), false));
							}else
				  	  	  	  	if(API.getVulgarWord(r(message))) {
				  	  	  	  		message =(r(message));
				  					e.setCancelled(call(e.getPlayer(), message, e.getMessage(), false));
				  					}else
				  	  	  	  		
				  	    	  	if(API.getVulgarWord(message.replace("[^a-zA-Z0-9]+",""))) {
				  	    	  		message =message.replace("[^a-zA-Z0-9]+","");
				  					e.setCancelled(call(e.getPlayer(), message, e.getMessage(), false));
				  					}else
				  	  	  	  		
				  	    	  	if(!API.getVulgarWord(message.replace("[^a-zA-Z0-9]+",""))) {
				  	    	  	char[] charss = message.replace("[^a-zA-Z0-9]+","").toCharArray();
				  		        char prevchars = 0;
				  		        StringBuilder sbs = new StringBuilder();
				  		        for (char c : charss) {
				  		            if(!(prevchars == c)) {
				  		                sbs.append(c);
				  		            }
				  		            prevchars = c;
				  		        }
				  		  	      message = sbs.toString();
				    	    	  	if(API.getVulgarWord(message)) {
				    					e.setCancelled(call(e.getPlayer(), message, e.getMessage(), false));
				    					}}}// end of else
					}}}// end of event
	private boolean call(Player p,String edit,String message,boolean Spam) {
		if(!Spam) {
		PlayerVulgarWordEvent s = new PlayerVulgarWordEvent(p,edit,message);
		Bukkit.getPluginManager().callEvent(s);
		if(!s.isCancelled()){
			cancel(edit, p, message);
  	  		return true;
	}
		return false;
		}
	PlayerSpamWordEvent s = new PlayerSpamWordEvent(p,API.getValueOfSpamWord(edit),message);
	Bukkit.getPluginManager().callEvent(s);
	if(!s.isCancelled()){
		cancelspam(edit, p, message);
	  		return true;
}
	return false;
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(PlayerChatEvent e) {
		Commands.BanSystem.BanSystem.KickMaxWarns(e.getPlayer().getName());
		if(TheAPI.getPunishmentAPI().hasMute(e.getPlayer().getName())){
			if(Loader.config.getBoolean("BanSystem.Mute.DisableChat")){
			e.setCancelled(true);
			Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.Muted")
			.replace("%player%", e.getPlayer().getName())
			.replace("%reason%", TheAPI.getPunishmentAPI().getMuteReason(e.getPlayer().getName()))
			.replace("%playername%", e.getPlayer().getDisplayName()), e.getPlayer());
		}}else
		if(TheAPI.getPunishmentAPI().hasTempMute(e.getPlayer().getName())) {
			if(Loader.config.getBoolean("BanSystem.Mute.DisableChat")){
				int cooldownTime = Loader.ban.getInt("Mute."+e.getPlayer().getName()+".TempMute.Init");
				long time = Loader.ban.getLong("Mute."+e.getPlayer().getName()+".TempMute.Start");
	                long secondsLeft = time / 1000L - System.currentTimeMillis() / 1000L;
	                secondsLeft =secondsLeft*-1;
	                if(secondsLeft>0&&secondsLeft<cooldownTime) {
	        			e.setCancelled(true);
	        			Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.TempMuted")
	        			.replace("%player%", e.getPlayer().getName())
	        			.replace("%reason%",TheAPI.getPunishmentAPI().getTempMuteReason(e.getPlayer().getName()))
	        			.replace("%time%", TheAPI.getTimeConventorAPI().setTimeToString((cooldownTime-secondsLeft)))
	        			.replace("%long%", TheAPI.getTimeConventorAPI().setTimeToString((cooldownTime-secondsLeft)))
	        			.replace("%playername%", e.getPlayer().getDisplayName()), e.getPlayer());
	                    return;
	                }
	                Loader.ban.set("Mute."+e.getPlayer().getName()+".TempMute", null);
	                Configs.bans.save();}}else
		 if(!e.getPlayer().hasPermission("ServerControl.Admin")) {
		String message = e.getMessage().replace(" ", "");
		char[] chars = message.toCharArray();
	        char prevchar = 0;
	        StringBuilder sb = new StringBuilder();
	        for (char c : chars) {
	            if(!(prevchar == c)) {
	                sb.append(c);
	            }
	            prevchar = c;
	        }
	  	      message = sb.toString();
				 if(setting.spam_chat){
		  	    if(API.getSpamWord(e.getMessage())) {
					e.setCancelled(call(e.getPlayer(), e.getMessage(), e.getMessage(), true));
					}else {
			  	    	 if(API.getSpamWord(e.getMessage().replace(" ", ""))) {
		    					e.setCancelled(call(e.getPlayer(), e.getMessage().replace(" ", ""), e.getMessage(), true));
		    					}else
				 	  	    	 if(API.getSpamWord(e.getMessage().replace("[^a-zA-Z0-9]+", ""))) {
				    					e.setCancelled(call(e.getPlayer(), e.getMessage().replace("[^a-zA-Z0-9]+", ""), e.getMessage(), true));
				    					}else
				 	 	  	    	if(API.getSpamWord(e.getMessage().replace("[a-zA-Z0-9]+", ""))) {
				    					e.setCancelled(call(e.getPlayer(), e.getMessage().replace("[a-zA-Z0-9]+", ""), e.getMessage(), true));
				    					}}}

				 if(setting.spam_cmd){
			if(API.getVulgarWord(e.getMessage())) {
				e.setCancelled(call(e.getPlayer(), e.getMessage(), e.getMessage(), false));
		  	  				}else {
	  		if(API.getVulgarWord(message)) {
				e.setCancelled(call(e.getPlayer(), message, e.getMessage(), false));
				}else
	  	  	  	  	if(API.getVulgarWord(r(message))) {
	  	  	  	  		message =r(message);
	  					e.setCancelled(call(e.getPlayer(), message, e.getMessage(), false));
	  					}else
	  	  	  	  		
	  	    	  	if(API.getVulgarWord(r(message).replace("[^a-zA-Z0-9]+",""))) {
	  	    	  		message =r(message).replace("[^a-zA-Z0-9]+","");
	  					e.setCancelled(call(e.getPlayer(), message, e.getMessage(), false));
	  					}else
	  	  	  	  		
	  	    	  	if(!API.getVulgarWord(r(message).replace("[^a-zA-Z0-9]+",""))) {
	  	    	  	char[] charss = r(message).replace("[^a-zA-Z0-9]+","").toCharArray();
	  		        char prevchars = 0;
	  		        StringBuilder sbs = new StringBuilder();
	  		        for (char c : charss) {
	  		            if(!(prevchars == c)) {
	  		                sbs.append(c);
	  		            }
	  		            prevchars = c;
	  		        }
	  		  	      message = sbs.toString();
	    	    	  	if(API.getVulgarWord(message)) {
	    					e.setCancelled(call(e.getPlayer(), message, e.getMessage(), false));
	    					}}}}// end of else
		}}// end of event
	public void cancelspam(String starts,Player s, String message) {
		Loader.config.set("Spam", Loader.config.getInt("Spam") + 1);
    	Loader.me.set("Players."+s.getName()+".Spam" ,Loader.me.getInt("Players."+s.getName()+".Spam") + 1);
       	Configs.config.save(); 
       	Configs.chatme.save();
       	if(Loader.config.getBoolean("TasksOnSend.Spam.Use-Commands")==true) {
		    	for(String cmds: Loader.config.getStringList("TasksOnSend.Spam.Commands")) {
		    	TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName())));
		    	}}
       	TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessageSpam").replace("%player%", s.getName())
       			.replace("%word%", API.getValueOfSpamWord(message)).replace("%message%", starts), "ServerControl.BroadCastNotify");
              if(Loader.config.getBoolean("TasksOnSend.Spam.Broadcast")) {
        	    	for(String cmds: Loader.TranslationsFile.getStringList("Security.TryingSendSpam")) {
        	    		TheAPI.broadcastMessage(Loader.s("Prefix")+cmds.replace("%player%", s.getDisplayName()));
        	  		}}
          try {
  			FileWriter fw = new FileWriter(Configs.chat.getFile(), true);
  			BufferedWriter bw = new BufferedWriter(fw);
  			String writingFormat = Loader.config.getString("WritingFormat.Spam");
  			bw.write(writingFormat.replace("%player%", s.getName()).replace("%message%", starts).replace("%spam%", API.getValueOfSpamWord(message)));
  			bw.newLine();
  			fw.flush();
  			bw.close();
  			}catch(Exception ea) {
  			}
          if(Loader.config.getBoolean("AutoKickLimit.Spam.Use")) {
            if(Loader.me.getInt("Players."+s.getName()+".Spam") >= Loader.config.getInt("AutoKickLimit.Spam.Number")) {
          	Loader.me.set("Players."+s.getName()+".Spam" ,Loader.me.getInt("Players."+s.getName()+".Spam") - Loader.config.getInt("AutoKickLimit.Spam.Number"));
          	Configs.chatme.save();
          	if(Loader.config.getBoolean("AutoKickLimit.Spam.Message.Use")) {
    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Spam.Message.List")) {
    		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Spam.Number")+""),s);
	        	
    		    	}}
                if(Loader.config.getBoolean("AutoKickLimit.Spam.Commands.Use")) {
	    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Spam.Commands.List")) {
	    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Spam.Number")+"")));
			    		if(cmds.toLowerCase().startsWith("kick")) {
   	 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,Loader.me.getInt("Players."+s.getName()+".Kicks") + 1);
   	 	                	Configs.chatme.save();
   	 	                	}}}}}
          if(Loader.config.getBoolean("AutoKickLimit.Kick.Use")) {
          if(Loader.me.getInt("Players."+s.getName()+".Kicks") >= Loader.config.getInt("AutoKickLimit.Kick.Number")) {
        	Loader.me.set("Players."+s.getName()+".Kicks" ,Loader.me.getInt("Players."+s.getName()+".Kicks") - Loader.config.getInt("AutoKickLimit.Kick.Number"));
        	Configs.chatme.save(); 
        	if(Loader.config.getBoolean("AutoKickLimit.Kick.Message.Use")) {
 		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Message.List")) {
 		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Kick.Number")+""),s);

 		    	}}

              if(Loader.config.getBoolean("AutoKickLimit.Kick.Commands.Use")) {
	    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Commands.List")) {
	    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", Loader.config.getInt("AutoKickLimit.Kick.Number")+"")));
				  }}}}
	}

	public void cancel(String starts,Player s, String message) {
			Loader.config.set("VulgarWords", Loader.config.getInt("VulgarWords") + 1);
	        Loader.me.set("Players."+s.getName()+".VulgarWords" ,Loader.me.getInt("Players."+s.getName()+".VulgarWords") + 1);
           	Configs.config.save();
           	Configs.chatme.save();
           	if(Loader.config.getBoolean("TasksOnSend.Swear.Use-Commands")) {
	    		    	for(String cmds: Loader.config.getStringList("TasksOnSend.Swear.Commands")) {
 	        	TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()))); 
	    		    	}}
           	TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessageVulgarWord").replace("%player%", s.getName()).replace("%word%", API.getValueOfVulgarWord(message)).replace("%message%", starts), "ServerControl.BroadCastNotify");
	     	    if(Loader.config.getBoolean("TasksOnSend.Swear.Broadcast")) {
	     		    	for(String cmds: Loader.TranslationsFile.getStringList("Security.TryingSendVulgarWord")) {
	     		    		TheAPI.broadcastMessage(Loader.s("Prefix")+cmds.replace("%player%", s.getDisplayName()));
	     		    	}}
	     	    		try {
	        	          			FileWriter fw = new FileWriter(Configs.chat.getFile(), true);
	        	          			BufferedWriter bw = new BufferedWriter(fw);
	  	      	          			String writingFormat = Loader.config.getString("WritingFormat.VulgarWords");
								bw.write(writingFormat.replace("%player%", s.getName()).replace("%message%", starts).replace("%vulgarword%", API.getValueOfVulgarWord(message)));
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
	       	    		 Configs.chatme.save(); 
	       	    		 if(Loader.config.getBoolean("AutoKickLimit.Swear.Commands.Use")) {
	   	 	    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Swear.Commands.List")) {
	   	 	    		    	TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", ""+Loader.config.getInt("AutoKickLimit.Swear.Number"))));
		    			    		if(cmds.toLowerCase().startsWith("kick")) {
			   	 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,Loader.me.getInt("Players."+s.getName()+".Kicks") + 1);
	   			   	 	Configs.chatme.save();
	   			   	 	}}}}}
	 	                  if(Loader.me.getInt("Players."+s.getName()+".Kicks") >= Loader.config.getInt("AutoKickLimit.Kick.Number")) {
	    	                  if(Loader.config.getBoolean("MaxNumberOfKicks.Enabled")) {
	 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,Loader.me.getInt("Players."+s.getName()+".Kicks") - Loader.config.getInt("AutoKickLimit.Kick.Number"));
	 	                	Configs.chatme.save(); 
	 	                	if(Loader.config.getBoolean("AutoKickLimit.Kick.Message.Use")) {
	  	 	    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Message.List")) {
	  	 	    		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%",""+Loader.config.getInt("AutoKickLimit.Kick.Number")),s);
	  	    	        	
	  	 	    		    	}}
	       	                   if(Loader.config.getBoolean("AutoKickLimit.Kick.Commands.Use")) {
	 	    		    	for(String cmds: Loader.config.getStringList("AutoKickLimit.Kick.Commands.List")) {
	 	    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", ""+Loader.config.getInt("AutoKickLimit.Kick.Number"))));
	 	    				
	 	    		    	}}}}
	 	                  }
	public String r(String s) {
		return s
				.replace("0", "o")
				.replace("1", "i")
				.replace("2", "p")
				.replace("3", "e")
				.replace("4", "d")
				.replace("5", "s")
				.replace("6", "q")
				.replace("7", "l")
				.replace("8", "b")
				.replace("9", "o")
				.replace("ì", "e")
				.replace("š", "s")
				.replace("è", "c")
				.replace("ø", "r")
				.replace("ž", "z")
				.replace("ý", "y")
				.replace("á", "a")
				.replace("í", "i")
				.replace("é", "e")
				.replace("ú", "u")
				.replace("ù", "u")
				.replace("à", "r")
				.replace("Ÿ", "z")
				.replace("ó", "o")
				.replace("å", "l")
				.replace("œ", "s")
				.replace("æ", "c")
				.replace("ï", "d")
				.replace("¾", "l")
				.replace("ò", "n");
	}
}
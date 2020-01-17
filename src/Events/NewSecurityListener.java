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
import org.bukkit.scheduler.BukkitRunnable;

import ServerControl.API;
import ServerControl.Loader;
import ServerControlEvents.PlayerBlockedCommandEvent;
import ServerControlEvents.PlayerSpamWordEvent;
import ServerControlEvents.PlayerVulgarWordEvent;
import Utils.Configs;
import me.Straiker123.TheAPI;

@SuppressWarnings("deprecation")
public class NewSecurityListener implements Listener {
	
	boolean stop;
	boolean restart;
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(ServerCommandEvent e) {
		if(Loader.config.getBoolean("WarningSystem.Enabled")) {
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
		}
	}}
	
	public void cancel(String command) {
		if(command.equalsIgnoreCase("reload") && Loader.config.getBoolean("WarningSystem.Reload-Enabled")||command.equalsIgnoreCase("rl") && Loader.config.getBoolean("WarningSystem.Reload-Enabled")) { 
			for(String s:Loader.TranslationsFile.getStringList("Warning.Reload"))
				TheAPI.broadcastMessage(s.replace("%time%", "0"));
		}
		if(command.equalsIgnoreCase("stop") && Loader.config.getBoolean("WarningSystem.Stop.Enabled")) {
			if(!stop) {
				stop=true;
				for(String s:Loader.TranslationsFile.getStringList("Warning.Stop"))
					TheAPI.broadcastMessage(s.replace("%time%", ""+Loader.config.getInt("WarningSystem.Stop.PauseTime")));
				try {
					Bukkit.getScheduler().runTaskLater(Loader.getInstance, new Runnable() {

						@Override
						public void run() {
							Bukkit.shutdown();
						}
						
					}, 20*Loader.config.getInt("WarningSystem.Stop.PauseTime"));
				}catch(Exception error) {
					Bukkit.shutdown();
				}
		}}
		if(command.equalsIgnoreCase("restart")&& Loader.config.getBoolean("WarningSystem.Restart.Enabled")) {
			if(!restart) {
				restart=true;
				for(String s:Loader.TranslationsFile.getStringList("Warning.Restart"))
					TheAPI.broadcastMessage(s.replace("%time%", ""+Loader.config.getInt("WarningSystem.Restart.PauseTime")));
				try {
					Bukkit.getScheduler().runTaskLater(Loader.getInstance, new Runnable() {

						@Override
						public void run() {
							Bukkit.shutdown();
						}
						
					}, 20*Loader.config.getInt("WarningSystem.Restart.PauseTime"));
				}catch(Exception error) {
					Bukkit.shutdown();
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if(Loader.config.getBoolean("WarningSystem.Enabled")) {
			if(Loader.config.getBoolean("WarningSystem.Enabled")) {
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
				}
			}}
		Commands.BanSystem.BanSystem.KickMaxWarns(e.getPlayer().getName());
		if(API.getBanSystemAPI().hasJail(e.getPlayer()))e.setCancelled(true);
		if(TheAPI.getPunishmentAPI().hasMute(e.getPlayer().getName())){
			if(Loader.config.getBoolean("BanSystem.Mute.DisableCmds")==true){
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
			if(Loader.config.getBoolean("BanSystem.Mute.DisableCmds")==true){
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
		 if(!e.getPlayer().hasPermission("ServerControl.CommandsAccess")) {
		   	    for (String cen: Loader.config.getStringList("Blocked-Commands")) {
		   	    	String mes = e.getMessage().toLowerCase();
		   	        if (mes.startsWith("/"+cen.toLowerCase())||mes.startsWith("/bukkit:"+cen.toLowerCase())||mes.startsWith("/minecraft:"+cen.toLowerCase())) {
		   	        	PlayerBlockedCommandEvent ed = new PlayerBlockedCommandEvent(e.getPlayer(),e.getMessage(),cen);
						Bukkit.getPluginManager().callEvent(ed);
						if(ed.isCancelled()) {
		   	        	e.setCancelled(true);
		   	        	Loader.msg(Loader.s("NotPermissionsMessage"),e.getPlayer());	  
		   	        	if(Loader.config.getBoolean("PerformBroadcasts.PlayerSendDisabledCommand.BroadcastMessage")==true) { 	        	
		   	        		TheAPI.broadcast(Loader.s("Prefix")+Loader.s("Security.TryingSendBlockedCommand").replace("%player%", e.getPlayer().getName())
		   	        			.replace("%playername%".toLowerCase(), e.getPlayer().getDisplayName())
		   	        			.replace("%command%".toLowerCase(), e.getMessage()),"ServerControl.CommandsAccess.Notify");
		   	        	}}}}}else
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
							 if(Loader.config.getBoolean("Commands.AntiSpam")==true){
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
							 if(Loader.config.getBoolean("Commands.AntiSwear")==true){
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
			if(Loader.config.getBoolean("BanSystem.Mute.DisableChat")==true){
			e.setCancelled(true);
			Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.Muted")
			.replace("%player%", e.getPlayer().getName())
			.replace("%reason%", TheAPI.getPunishmentAPI().getMuteReason(e.getPlayer().getName()))
			.replace("%playername%", e.getPlayer().getDisplayName()), e.getPlayer());
		}}else
		if(TheAPI.getPunishmentAPI().hasTempMute(e.getPlayer().getName())) {
			if(Loader.config.getBoolean("BanSystem.Mute.DisableChat")==true){
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
				 if(Loader.config.getBoolean("Chat.AntiSpam")==true){
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

				 if(Loader.config.getBoolean("Chat.AntiSwear")==true){
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
	public void cancelspam(String message,Player p, String starts) {
		Player s = p;
        int Kick = Loader.me.getInt("Players."+s.getName()+".Kicks");
        int kicker = Loader.config.getInt("MaxNumberOfKicks.Number");
        int number = Loader.config.getInt("MaxNumberOfVulgarRecords.Number");
		String name = s.getName();
		String start = starts;
		int Spam = Loader.config.getInt("Spam");
     	int KickME = Loader.me.getInt("Players."+s.getName()+".Kicks");
	    int spamME = Loader.me.getInt("Players."+s.getName()+".Spam");
		int VS = Loader.me.getInt("Players."+s.getName()+".Spam");
		Loader.config.set("Spam", Spam + 1);
    	Loader.me.set("Players."+s.getName()+".Spam" ,spamME + 1);
       	Loader.me.set("Players."+s.getName()+".Spam" ,VS + 1);
       	Configs.config.save(); 
       	Configs.chatme.save();
       	if(Loader.config.getBoolean("PerformCommands.PlayerSendSpam.Enabled")==true) {
			List<String> cmda = Loader.config.getStringList("PerformCommands.PlayerSendSpam.Commands");
        	new BukkitRunnable() {
					@Override
					public void run() {
		    	for(String cmds: cmda) {
	        	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), TheAPI.colorize(cmds.replace("%player%", s.getName()))); 
		    	}}
				}.runTask(Loader.getInstance);{
				}}
       	TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessageSpam").replace("%player%", s.getName()).replace("%word%", API.getValueOfSpamWord(message)).replace("%message%", starts), "ServerControl.BroadCastNotify");
              if(Loader.config.getBoolean("PerformBroadcasts.PlayerSendSpam.BroadcastMessage")==true) {
        		List<String> cmdd = Loader.TranslationsFile.getStringList("Security.TryingSendSpam");
        		
        	    	for(String cmds: cmdd) {
        	    		TheAPI.broadcastMessage(Loader.s("Prefix")+cmds.replace("%player%".toLowerCase(), s.getDisplayName()));
        	  		}}
          try {
  			FileWriter fw = new FileWriter(Configs.chat.getFile(), true);
  			BufferedWriter bw = new BufferedWriter(fw);
  			String writingFormat = Loader.config.getString("WritingFormat.Spam");
  			bw.write(writingFormat.replace("%player%".toLowerCase(), name).replace("%message%", start).replace("%spam%", API.getValueOfSpamWord(message)));
  			bw.newLine();
  			fw.flush();
  			bw.close();
  			}catch(Exception ea) {
  			}
          if(Loader.config.getBoolean("MaxNumberOfSpamRecords.Enabled")==true) {
            if(VS >= number) {
          	Loader.me.set("Players."+s.getName()+".Spam" ,VS - number);
          	Configs.chatme.save();if(Loader.config.getBoolean("MaxNumberOfSpamRecords.Message.Enabled")==true) {
				List<String> cmd = Loader.config.getStringList("MaxNumberOfSpamRecords.Message.Messages");
    		    	for(String cmds: cmd) {
    		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", String.valueOf(number)),s);
	        	
    		    	}}
                if(Loader.config.getBoolean("MaxNumberOfSpamRecords.Commands.Enabled")==true) {
    		    	new BukkitRunnable() {
					@Override
					public void run() {
    					List<String> cmd = Loader.config.getStringList("MaxNumberOfSpamRecords.Commands.Commands");
	    		    	for(String cmds: cmd) {
			    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", String.valueOf(number))));
			    		if(cmds.contains("kick")) {
   	      	            	int Kick = Loader.me.getInt("Players."+s.getName()+".Kicks");
   	 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,Kick + 1);
   	 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,KickME + 1);
   	 	                	Configs.chatme.save();}}}}.runTask(Loader.getInstance);{
				}}}}
          if(Loader.config.getBoolean("MaxNumberOfKicks.Enabled")==true) {
          if(Kick >= kicker) {
        	Loader.me.set("Players."+s.getName()+".Kicks" ,Kick - kicker);
        	Configs.chatme.save(); if(Loader.config.getBoolean("MaxNumberOfKicks.Message.Enabled")==true) {
			List<String> cmd = Loader.config.getStringList("MaxNumberOfKicks.Message.Messages");
 		    	for(String cmds: cmd) {
 		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", String.valueOf(kicker)),s);

 		    	}}

              if(Loader.config.getBoolean("MaxNumberOfKicks.Commands.Enabled")==true) {
        	new BukkitRunnable() {
				@Override
				public void run() {
    					List<String> cmd = Loader.config.getStringList("MaxNumberOfKicks.Commands.Commands");
	    		    	for(String cmds: cmd) {
		    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", String.valueOf(kicker).toString())));
				  }}
			}.runTask(Loader.getInstance);{
			}}}}}

	public void cancel(String message,Player p, String starts) {
		Player s = p;
       	int VulgarWords = Loader.config.getInt("VulgarWords");
       	int VulgarWords1 = Loader.me.getInt("Players."+s.getName()+".VulgarWords");
        int Kick = Loader.me.getInt("Players."+s.getName()+".Kicks");
        int kicker = Loader.config.getInt("MaxNumberOfKicks.Number");
        int number = Loader.config.getInt("MaxNumberOfVulgarRecords.Number");
       	int vulgarwordsME = Loader.me.getInt("Players."+s.getName()+".VulgarWords");
		String name = s.getName();
		String start = starts;
     	int KickME = Loader.me.getInt("Players."+s.getName()+".Kicks");
			Loader.config.set("VulgarWords", VulgarWords + 1);
	        Loader.me.set("Players."+s.getName()+".VulgarWords" ,VulgarWords1 + 1);
           	Loader.me.set("Players."+s.getName()+".VulgarWords" ,vulgarwordsME + 1);
           	Configs.config.save();
           	Configs.chatme.save();
           	if(Loader.config.getBoolean("PerformCommands.PlayerSendVulgarWord.Enabled")==true) {
              List<String> cmda = Loader.config.getStringList("PerformCommands.PlayerSendVulgarWord.Commands");
	        	new BukkitRunnable() {
  					@Override
  					public void run() {
	    		    	for(String cmds: cmda) {
 	        	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), TheAPI.colorize(cmds.replace("%player%", s.getName()))); 
	    		    	}}
  				}.runTask(Loader.getInstance);{
  				}}
           	TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BroadCastMessageVulgarWord").replace("%player%", s.getName()).replace("%word%", API.getValueOfVulgarWord(message)).replace("%message%", starts), "ServerControl.BroadCastNotify");
	     	    if(Loader.config.getBoolean("PerformBroadcasts.PlayerSendVulgarWord.BroadcastMessage")==true) {
	     			List<String> cmdd = Loader.TranslationsFile.getStringList("Security.TryingSendVulgarWord");
	     		    	for(String cmds: cmdd) {
	     		 
	     		    		
	     		    		TheAPI.broadcastMessage(Loader.s("Prefix")+cmds.replace("%player%", s.getDisplayName()));
	     		    	}}
	     	    		try {
	        	          			FileWriter fw = new FileWriter(Configs.chat.getFile(), true);
	        	          			BufferedWriter bw = new BufferedWriter(fw);
	  	      	          			String writingFormat = Loader.config.getString("WritingFormat.VulgarWords");
								bw.write(writingFormat.replace("%player%", name).replace("%message%", start).replace("%vulgarword%", API.getValueOfVulgarWord(message)));
	        	          			bw.newLine();
	        	          			fw.flush();
	        	          			bw.close();
	        	          			}catch(Exception ev) {
	        	          			}
	        	              if(VulgarWords1 >= number) {
	      	                  if(Loader.config.getBoolean("MaxNumberOfVulgarRecords.Enabled")==true) {
		       	                   if(Loader.config.getBoolean("MaxNumberOfVulgarRecords.Message.Enabled")==true) {
	        	    				List<String> cmd = Loader.config.getStringList("MaxNumberOfVulgarRecords.Message.Messages");
	       	    		    	for(String cmds: cmd) {
	       	    		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", String.valueOf(number)),s);
	          	        	
	       	    		    	}}
	       	    		    Loader.me.set("Players."+s.getName()+".VulgarWords" ,VulgarWords1 - number);
	       	    		 Configs.chatme.save(); if(Loader.config.getBoolean("MaxNumberOfVulgarRecords.Commands.Enabled")==true) {
		    	        	new BukkitRunnable() {
		    					@Override
		    					public void run() {
   	 	    					List<String> cmd = Loader.config.getStringList("MaxNumberOfVulgarRecords.Commands.Commands");
	   	 	    		    	for(String cmds: cmd) {
		    			    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", String.valueOf(number))));
		    			    		if(cmds.contains("kick".toLowerCase())) {
			   	      	            	int Kick = Loader.me.getInt("Players."+s.getName()+".Kicks");
			   	 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,Kick + 1);
	   			   	 	          	Loader.me.set("Players."+s.getName()+".Kicks" ,KickME + 1);
	   			   	 	Configs.chatme.save();}}}}.runTask(Loader.getInstance);{
		    				}}}}
	 	                  if(Kick >= kicker) {
	    	                  if(Loader.config.getBoolean("MaxNumberOfKicks.Enabled")==true) {
	 	                	Loader.me.set("Players."+s.getName()+".Kicks" ,Kick - kicker);
	 	                	Configs.chatme.save(); if(Loader.config.getBoolean("MaxNumberOfKicks.Message.Enabled")==true) {
	      					List<String> cmd = Loader.config.getStringList("MaxNumberOfKicks.Message.Messages");
	  	 	    		    	for(String cmds: cmd) {
	  	 	    		    		Loader.msg(cmds.replace("%player%", s.getName()).replace("%number%", String.valueOf(kicker)),s);
	  	    	        	
	  	 	    		    	}}
	       	                   if(Loader.config.getBoolean("MaxNumberOfKicks.Commands.Enabled")==true) {
	 	    				new BukkitRunnable() {
	 	    					@Override
	 	    					public void run() {
	 	    					List<String> cmd = Loader.config.getStringList("MaxNumberOfKicks.Commands.Commands");
	 	    		    	for(String cmds: cmd) {
	 	    						
	 	    			    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),TheAPI.colorize(cmds.replace("%player%", s.getName()).replace("%number%", String.valueOf(kicker).toString())));
	 	    				
	 	    		    	}}}.runTask(Loader.getInstance);{
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
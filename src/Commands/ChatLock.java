package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;
import me.Straiker123.TheAPI;

public class ChatLock implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){

		if (API.hasPerm(s,"ServerControl.ChatLock")) {
			      if (!Loader.config.getBoolean("ChatLock")){
			    	  TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("ChatLock.ChatIsLocked"));
			    	  Loader.config.set("ChatLock",true);
			    	  Configs.chatme.save();
			        return true;  
			      }else {
			    	  TheAPI.broadcastMessage(Loader.s("Prefix")+Loader.s("ChatLock.ChatIsUnlocked"));
			    	  Loader.config.set("ChatLock",false);
			    	  Configs.chatme.save();
			        return true;
			        }} return true;
	  }

}

package Commands.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.setting;
import me.Straiker123.TheAPI;
import me.Straiker123.TheRunnable;

public class Restart implements CommandExecutor, TabCompleter {
	void stop(CommandSender s) {
		if(r!=null)
		r.cancel();
		r=null;
		Loader.msg("&eSaving worlds and players..", s);
		for(World w: Bukkit.getWorlds()) {
			w.save();
		}
		Bukkit.savePlayers();
		TheAPI.broadcastMessage("&cRestart of server..");
		try {
		Bukkit.spigot().restart();
		}catch(Exception err) {
			//not spigot server
			Bukkit.shutdown();
		}
	}
	TheRunnable r;
	public boolean stop;
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Restart")) {
			int time = Loader.config.getInt("Options.WarningSystem.Restart.PauseTime");
			if(args.length==0) {
				stop=true;
				r=new TheRunnable();
				int h = time;
				if(setting.warn_stop) {
				r.runRepeating(new Runnable() {
					int f= h;
					public void run() {
						
						if(f<=0) {
							stop(s);
							return;
						}else if(f == h%75 && h > 15  ||f == h%50 && h > 10 ||f == h%25 && h > 5  || f == 5 || f==4||f==3||f==2||f==1){
							for(String s:Loader.config.getStringList("Options.WarningSystem.Restart.Messages"))
								TheAPI.broadcastMessage(s.replace("%time%", ""+TheAPI.getStringUtils().setTimeToString(f)));
						}
						--f;
					}}, 20);
				}else {
					stop(s);
				}
				return true;
			}
				if(args[0].equalsIgnoreCase("cancel")) {
					if(r!=null) {
					r.cancel();
					stop=false;
					TheAPI.broadcastMessage("&eServer restart has been &acanceled");
					}
					return true;
				}else
				if(args[0].equalsIgnoreCase("now")) {
					stop(s);
					return true;
				}else {
					time=(int)TheAPI.getStringUtils().getTimeFromString(args[0]);
					stop=true;
					r=new TheRunnable();
					int h = time;
					if(setting.warn_restart) {
					r.runRepeating(new Runnable() {
						int f= h;
						public void run() {
							
							if(f<=0) {
								stop(s);
								return;
							}else if(f == h%75 && h > 15  ||f == h%50 && h > 10 ||f == h%25 && h > 5  || f == 5 || f==4||f==3||f==2||f==1){
								for(String s:Loader.config.getStringList("Options.WarningSystem.Restart.Messages"))
									TheAPI.broadcastMessage(s.replace("%time%", ""+TheAPI.getStringUtils().setTimeToString(f)));
							}
							--f;
						}}, 20);
					}else {
						stop(s);
					}
					return true;
				
			}
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if(s.hasPermission("ServerControl.Restart") && args.length==1)
		c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("15s","30s","now","cancel"), new ArrayList<>()));
		return c;
	}
}

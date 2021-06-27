package me.devtec.servercontrolreloaded.commands.other.guis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;

public class GUICreator implements CommandExecutor {
	public static Map<String, GUIMaker> maker = new HashMap<>();
	private static boolean inicialized;
    
	public static class GUIMaker {
		private String gui;
		private Layout l = new Layout();
		private List<String> ly;
	    private List<GUI> guis;
	    private GUI g;
	    
	    private boolean animated;
	    
	    public GUIMaker(String a){
	    	gui=a;
	    	ly=Loader.guicreator.getStringList("GUI."+gui+".layout");
			load();
	    	if(Loader.guicreator.getBoolean("GUI."+gui+".perplayer"))
	    		guis = new ArrayList<>();
	    	else {
	    		g = new GUI(PlaceholderAPI.setPlaceholders(null,Loader.guicreator.getString("GUI."+gui+".title")),Loader.guicreator.getInt("GUI."+gui+".size"));
		    	l.paste(g, null, ly);
	    	}
	    	if(Loader.guicreator.getInt("GUI."+gui+".update")>0) {
	    		new Tasker() {
					public void run() {
						update();
					}
				}.runRepeating(Loader.guicreator.getInt("GUI."+gui+".update"), Loader.guicreator.getInt("GUI."+gui+".update"));
	    	}
	    	if(Loader.guicreator.getInt("GUI."+gui+".animation")>0) {
	    		animated=true;
				nextAnimation();
	    		new Tasker() {
					public void run() {
						nextAnimation();
					}
				}.runRepeating(Loader.guicreator.getInt("GUI."+gui+".animation"), Loader.guicreator.getInt("GUI."+gui+".animation"));
	    	}
	    }
	    
	    private void load() {
	    	l.upload("GUI."+gui+".items");
		}

		public String getName() {
	    	return gui;
	    }
	    
		int current;
	    public void nextAnimation() {
	    	++current;
	    	if(!Loader.guicreator.exists("GUI."+gui+".animations."+current))current=0;
	    	ly=Loader.guicreator.getStringList("GUI."+gui+".animations."+current+".layout");
	    	if(g!=null) {
		    	g.setTitle(PlaceholderAPI.setPlaceholders(null,Loader.guicreator.getString("GUI."+gui+".animations."+current+".title")));
		    	l.paste(g, null, ly);
	    	}else {
	    		for(GUI g : guis) {
	    			if(g.getPlayers().isEmpty())continue;
			    	g.setTitle(PlaceholderAPI.setPlaceholders(g.getPlayers().iterator().next(),Loader.guicreator.getString("GUI."+gui+".animations."+current+".title")));
	    			l.paste(g, g.getPlayers().iterator().next(), ly);
	    		}
	    	}
	    }
	    
	    public void update() {
	    	if(g!=null) {
		    	l.paste(g, null, ly);
	    	}else {
	    		for(GUI g : guis)
	    			l.paste(g, g.getPlayers().iterator().next(), ly);
	    	}
	    }
	    
	    public GUI make(Player p) {
	    	if(g!=null)
		    	return g;
	    	else {
	    		GUI g;
	    		if(animated) {
			    	g = new GUI(PlaceholderAPI.setPlaceholders(p,Loader.guicreator.getString("GUI."+gui+".animations."+current+".title")),Loader.guicreator.getInt("GUI."+gui+".size")) {
			    		public void onClose(Player player) {
			    			guis.remove(this);
			    		}
			    	};
	    		}else
			    	g = new GUI(PlaceholderAPI.setPlaceholders(p,Loader.guicreator.getString("GUI."+gui+".title")),Loader.guicreator.getInt("GUI."+gui+".size")) {
			    		public void onClose(Player player) {
			    			guis.remove(this);
			    		}
			    	};
    			l.paste(g, p, ly);
		    	guis.add(g);
		    	return g;
	    	}
	    }

	}
	
    private String gui, cmd;
    private Map<String, Long> cooldownMap = new HashMap<>();
    private long cooldown, waitingCooldown;
    private boolean global;
	
	private boolean canUseSimple(CommandSender s) {
		if(cooldown<=0)return true;
		if(s instanceof Player) {
			if(global) {
				if(waitingCooldown-System.currentTimeMillis()/1000 + cooldown<= 0) {
					waitingCooldown=System.currentTimeMillis()/1000;
					return true;
				}
				return false;
			}
			if(cooldownMap.getOrDefault(s.getName(), (long)0)-System.currentTimeMillis()/1000 + cooldown <= 0) {
				cooldownMap.put(s.getName(), System.currentTimeMillis()/1000);
				return true;
			}
			return false;
		}
		return true;
	}
	
	public long expire(CommandSender s) {
		if(cooldown<=0)return 0;
		if(s instanceof Player) {
			if(global) {
				return waitingCooldown-System.currentTimeMillis()/1000 + cooldown;
			}
			return cooldownMap.getOrDefault(s.getName(), (long)0)-System.currentTimeMillis()/1000 + cooldown;
		}
		return 0;
	}
	

    public GUICreator(long cooldown, boolean globalC, String cmd, String a){
    	if(!inicialized) {
    		inicialized=true;
    		maker.clear();
        	for(String ad : Loader.guicreator.getKeys("GUI"))
        		maker.put(ad, new GUIMaker(ad));
    	}
    	this.cooldown=cooldown;
    	this.global=globalC;
    	gui=a;
    	this.cmd=cmd;
    }

    public boolean onCommand(CommandSender s, Command uu, String u, String[] args) {
    	if(canUseSimple(s)) {
    	if(args.length==0) {
    		if(s instanceof Player == false)return true;
        	String perm = Loader.guicreator.getString("Commands."+cmd+".permission");
        	if(perm!=null)perm=perm.trim();
        	if(perm!=null && perm.equals(""))perm=null;
        	if(perm==null||(perm.startsWith("-")?!s.hasPermission(perm.substring(1)):s.hasPermission(perm)))
            maker.get(gui).make((Player)s).open((Player)s);
            return true;
    	}
    	Player target = TheAPI.getPlayer(args[0]);
    	if(target==null) {
    		Loader.notOnline(s, args[0]);
    		return true;
    	}
        maker.get(gui).make(target).open(target);
        return true;
    	}
		Loader.sendMessages(s, "Cooldowns", Placeholder.c().add("%time%", StringUtils.timeToString(expire(s))));
    	return true;
    }
}
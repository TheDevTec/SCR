package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.sqlite.util.StringUtils;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;
import Utils.MultiWorldsUtils;
import Utils.Tasks;
import Utils.setting;
import me.Straiker123.TheAPI;

public class ServerControl implements CommandExecutor, TabCompleter {

	public static boolean clearing = false;
	public boolean onCommand(CommandSender s, Command cmd, String label,String[] args) {
	
	if(args.length==0 || args.length==1  && args[0].equalsIgnoreCase("Help")){
		if(API.hasPerm(s, "ServerControl.Help")) {
            	Loader.msg(Loader.s("Prefix")+"&e----------------- &b"+Loader.s("Words.Help")+"&e -----------------",s);
            	Loader.msg("",s);
            Loader.Help(s, "/ServerControl Reload","Reload");
            Loader.Help(s, "/ServerControl Version","Version");
            Loader.Help(s, "/ServerControl Info","Info");
            Loader.Help(s, "/ServerControl Reset","Reset");
            Loader.Help(s, "/ServerControl Manager","Manager");
            Loader.Help(s, "/ServerControl List","List");
            
            if(args.length==2) {
            	for(String v:All)
        			if(args[1].equalsIgnoreCase(v)) {
        				Loader.msg(Loader.s("Prefix")+"&e----------------- &b"+Loader.s("Words.HelpFor")+" "+"&b"+v+"&e -----------------",s);
        				Loader.msg("",s);
                    Loader.Help(s, "/ServerControl "+v,v);
                    return true;
                    }
            	Loader.msg(Loader.s("Prefix")+"&e----------------- &b"+Loader.s("Words.HelpFor")+" "+"&4"+args[1]+" &e-----------------",s);
            	Loader.msg("",s);
            	Loader.msg(Loader.s("Prefix")+Loader.s("Help.NoHelpForCommand").replace("%command%", args[1]), s);
                    return true;
        		}return true;}return true;}
        if(args[0].equalsIgnoreCase("Manager")){
    		if(API.hasPerm(s, "ServerControl.Manager")) {
    			if(s instanceof Player) {
    				setting.getManager((Player)s);
    		        return true;
    			}
    			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
        return true;
       }return true;
        }

        if(args[0].equalsIgnoreCase("List")){
    		if(API.hasPerm(s, "ServerControl.List")) {
 			Loader.msg(Loader.s("Prefix")+"&e----------------- &bList &e-----------------",s);
 			Loader.msg("",s);
 			Loader.msg(Loader.s("Prefix")+Loader.s("Words.List"),s);
 			Loader.msg(Loader.s("Prefix")+"&cList of swear words: "+StringUtils.join(Loader.config.getStringList("SwearWords"), ", "),s);
 			Loader.msg(Loader.s("Prefix")+"&cList of spam words: "+StringUtils.join(Loader.config.getStringList("SpamWords.Words"), ", "),s);
        return true;
     }
 		return true;}
	    	    	            if(args[0].equalsIgnoreCase("Reset")){
	    	    	        		if(API.hasPerm(s, "ServerControl.Reset")) {
		    	    	        			if(args.length == 1) {
		    	    	        				
		    	    	        			if(Loader.me.getString("Players."+s.getName()+".ResetCooldown")==null) {
		    	    	        				Loader.me.set("Players."+s.getName()+".ResetCooldown", System.currentTimeMillis()/1000);

		    	    	        				Configs.chatme.save();
		    	    	        				Loader.msg(Loader.s("Prefix")+"&e----------------- &bReset&e -----------------",s);
		    	    	        				Loader.msg("",s);
		    	    	        				Loader.msg(Loader.s("Prefix")+Loader.s("General.Confirm"),s);
	    	    	    	    	       return true;
	    	    	        		}
	    	    	    	        		long reset = Loader.me.getLong("Players."+s.getName()+".ResetCooldown")-System.currentTimeMillis()/1000;
	    	    	    	        		reset = reset*-1;
	    	    	    	        		if(reset <10) {
	    	    	    	        			Loader.msg(Loader.s("Prefix")+"&e----------------- &bReset&e -----------------",s);
	    	    	    	        			Loader.msg("",s);
	    	    	    	        			Loader.msg(Loader.s("Prefix")+Loader.s("General.PleaseConfirm"),s);
    	    	        					return true;
		    	    	        			}
	    	    	    	        		Loader.me.set("Players."+s.getName()+".ResetCooldown", System.currentTimeMillis()/1000);
	    	    	    	        		Configs.chatme.save();Loader.msg(Loader.s("Prefix")+"&e----------------- &bReset&e -----------------",s);
	    	    	    	        		Loader.msg("",s);
	    	    	    	        		Loader.msg(Loader.s("Prefix")+Loader.s("General.Confirm"),s);
	    	    	    	    	       return true;
		    	    	        			}
	    	    	        			
	    	    	        			if(args.length == 2) {
				    	    	        	if(args[1].equalsIgnoreCase("Confirm")) {
		    	    	    	        		long reset = Loader.me.getLong("Players."+s.getName()+".ResetCooldown")-System.currentTimeMillis()/1000;
		    	    	    	        		reset = reset*-1;
		    	    	    	        		if(reset < 10) {
		    	    	    	        			Loader.msg(Loader.s("Prefix")+"&e----------------- &bReset Confirm&e -----------------",s);
		    	    	    	        			Loader.msg("",s);
		    	    	    	        			Loader.msg(Loader.s("Prefix")+Loader.s("General.Reset"),s);
						    	    	  	    Loader.me.set("Players", null);
				    	    	        		Configs.chatme.save();
				    	    	        		Loader.config.set("VulgarWords", 0);
					    	    	        Loader.config.set("Spam", 0);
			    	    	        		Configs.config.save();
				    	    	    	    return true;
				    	    	        	}
		    	    	    	        		Loader.msg(Loader.s("Prefix")+"----------------- &bReset Confirm&e -----------------",s);
		    	    	    	        		Loader.msg("",s);
		    	    	    	        		Loader.msg(Loader.s("Prefix")+Loader.s("General.AnyConfirm"),s);
						    	    	    	    return true;
					    	    	        	}}} return true;}
	    	    	        	if(args[0].equalsIgnoreCase("Info")){
	    	    	        			Loader.msg(Loader.s("Prefix")+"----------------- &bInfo&e -----------------",s);
		    	    	                Loader.msg("",s);
		    	    	                Loader.msg(Loader.s("PluginName")+"&7: &4Server Control Reloaded",s);
		    	    	                Loader.msg(Loader.s("Prefix")+Loader.s("PluginCreatedBy")+"&7: &6Straiker123",s);
		    	    	                Loader.msg(Loader.s("Prefix")+Loader.s("Words.Version")+"&7: &6V"+  Loader.getInstance.getDescription().getVersion(),s);
		    	    	                Loader.msg(Loader.s("Prefix")+Loader.s("ServerControlThema"),s);
	    	    	            return true;
	    	    	        	}

	if(args[0].equalsIgnoreCase("Reload")){
		if(API.hasPerm(s, "ServerControl.Reload")) {
		Loader.msg(Loader.s("Prefix")+"&e----------------- &bReloading config&e -----------------",s);
		Loader.msg("",s);
	Configs.LoadConfigs();
	Tasks.reload();
	Loader.startConvertMoney();
	MultiWorldsUtils.LoadWorlds();
	Loader.SoundsChecker();
    try {
	if(Loader.config.getBoolean("TimeZone-Enabled")) {
		TimeZone.setDefault(TimeZone.getTimeZone(Loader.config.getString("TimeZone")));
	}}catch(Exception e) {
		Loader.msg("&6Invalid time zone: &c"+Loader.config.getString("TimeZone"), TheAPI.getConsole());
		Loader.msg("&6List of available time zones:", TheAPI.getConsole());
		Loader.msg(" &6https://greenwichmeantime.com/time-zone/", TheAPI.getConsole());
        }
 	Loader.msg(Loader.s("Prefix")+Loader.s("ConfigReloaded"),s);
	return true;
    }return true;}

	if(args[0].equalsIgnoreCase("Version")){
		if(API.hasPerm(s, "ServerControl.Info")) {
			Loader.msg(Loader.s("Prefix")+"&e----------------- &bVersion&e -----------------",s);
			Loader.msg("",s);
			Loader.msg(Loader.s("Prefix")+Loader.s("Words.Version")+"&7: &6V"+ Loader.getInstance.getDescription().getVersion(),s);
			Loader.msg(Loader.s("Prefix")+Loader.s("Words.ServerVersion")+"&7: &6"+ Bukkit.getServer().getBukkitVersion(),s);
	            return true;
     }return true;
     }
	Loader.msg(Loader.s("Prefix")+Loader.s("UknownCommand"),s);
	 return true;
	}
	final List<String> Reload = Arrays.asList("Reload");
	final List<String> Help = Arrays.asList("Help");
	final List<String> Reset = Arrays.asList("Reset");
	final List<String> Manager = Arrays.asList("Manager");
	final List<String> Version = Arrays.asList("Version");
	final List<String> Info = Arrays.asList("Info");
	final List<String> List = Arrays.asList("List");
	final List<String> Confirm = Arrays.asList("Confirm");
	final List<String> Maintenance = Arrays.asList("Maintenance");
    final List<String> All = Arrays.asList("Reset","Maintenance","List","Info","Version","Manager","Help","Reload");
    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
    	List<String> c = new ArrayList<>();
    	if(args.length==1) {
        	
        	if(s.hasPermission("ServerControl.Help")) {
                c.addAll(StringUtil.copyPartialMatches(args[0], Help, new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.Reload")) {
        		c.addAll(StringUtil.copyPartialMatches(args[0], Reload, new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.Reset")) {
        		c.addAll(StringUtil.copyPartialMatches(args[0], Reset, new ArrayList<>()));
        		
            }
        	if(s.hasPermission("ServerControl.List")) {
        		c.addAll(StringUtil.copyPartialMatches(args[0], List, new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.Manager")) {
        		c.addAll(StringUtil.copyPartialMatches(args[0], Manager, new ArrayList<>()));
        	}
        	if(s.hasPermission("ServerControl.Version")) {
        		c.addAll(StringUtil.copyPartialMatches(args[0], Version, new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.Maintenance")) {
        		c.addAll(StringUtil.copyPartialMatches(args[0], Maintenance, new ArrayList<>()));
            }
        	c.addAll(StringUtil.copyPartialMatches(args[0], Info, new ArrayList<>()));
        }
    	if(cmd.getName().equalsIgnoreCase("ServerControl") && args[0].equalsIgnoreCase("Reset") && args.length==2) {
        	if(s.hasPermission("ChatControl.Reset")) {
        		c.addAll(StringUtil.copyPartialMatches(args[1], Confirm, new ArrayList<>()));
        		
        	}
			
		}
    	if(cmd.getName().equalsIgnoreCase("ServerControl") && args[0].equalsIgnoreCase("Help") && args.length==2) {
        	if(s.hasPermission("ChatControl.Help")) {
        		c.addAll(StringUtil.copyPartialMatches(args[1], All, new ArrayList<>()));
        		
        	}
			
		}
        return c;
    }
		
}
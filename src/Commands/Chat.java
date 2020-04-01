package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Chat implements CommandExecutor, TabCompleter {

	private static final List<String> All = Arrays.asList("General", "Help", "Info","Version", "Me");

	public String online(String[] args) {
		if(TheAPI.getPlayer(args[1])!=null) {
			return TheAPI.getPlayer(args[1]).getDisplayName();
		}
		return args[1];
	}
	public String check(String s) {
		if(s!=null)return s;
		return "---";
		}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
	
	    if(args.length==0 ||args[0].equalsIgnoreCase("Help")){
			if(API.hasPerm(s, "ServerControl.Help")) {
	            if(args.length==0 ||args.length==1) {
	            	Loader.msg(Loader.s("Prefix")+"&e----------------- &bHelp&e -----------------",s);
	            	Loader.msg("",s);
	            	if(s.hasPermission("ServerControl.Me.Other"))
	            Loader.Help(s, "/Chat Me <player>","Me");
	            	else
		            	if(s.hasPermission("ServerControl.Me"))
		    	            Loader.Help(s, "/Chat Me","Me");
	            	if(s.hasPermission("ServerControl.General"))
	            Loader.Help(s, "/Chat General","General");
	            	if(s.hasPermission("ServerControl.Info")) {
	    	            Loader.Help(s, "/Chat Info","Info");
	            Loader.Help(s, "/Chat Version","Version");
	            	}
	            return true;
	        }
	            if(args.length==2) {
	                	for(String v:All)
	            			if(args[1].equalsIgnoreCase(v)) {
	            				Loader.msg(Loader.s("Prefix")+"&e----------------- &bHelp for &b"+v+"&e -----------------",s);
	            				Loader.msg("",s);
	                        Loader.Help(s, "/Chat "+v,v);
	                        return true;
	                        }
	                	Loader.msg(Loader.s("Prefix")+"&e----------------- &bHelp "+"&4"+args[1]+" &e-----------------",s);
	                	Loader.msg("",s);
	                	Loader.msg(Loader.s("Prefix")+Loader.s("Help.NoHelpForCommand").replace("%command%", args[1]), s);
	                        return true;
	            		}}return true;}
    if(args[0].equalsIgnoreCase("Version")||args[0].equalsIgnoreCase("info")){
		if(API.hasPerm(s, "ServerControl.Info")) {
        	Loader.msg(Loader.s("Prefix")+"&e----------------- &bVersion&e -----------------",s);
        	Loader.msg("",s);
        	Loader.msg(Loader.s("Prefix")+"&7Version of ServerControlReloaded: &eV"+ TheAPI.getPluginsManagerAPI().getVersion("ServerControlReloaded"),s);
        	Loader.msg(Loader.s("Prefix")+"&7Version of TheAPI: &eV"+ TheAPI.getPluginsManagerAPI().getVersion("TheAPI"),s);
        	Loader.msg(Loader.s("Prefix")+"&7Version of Server: &e"+ Bukkit.getServer().getBukkitVersion(),s);
        	Loader.msg(Loader.s("Prefix")+"&7Our discord: &ehttps://discord.gg/z4kK66g",s);
            return true;
       }return true;}
        	if(args[0].equalsIgnoreCase("General")){
    			if(API.hasPerm(s, "ServerControl.General")) {
                	   Loader.msg(Loader.s("Prefix")+"&e----------------- &bGeneral&e -----------------",s);
                	   Loader.msg("",s);
                	   Loader.msg(Loader.s("Prefix")+Loader.config.getInt("VulgarWords")+" of swear words",s);
                	   Loader.msg(Loader.s("Prefix")+Loader.config.getInt("Spam")+" of spam",s);
        	        return true;
        		}return true;}
        	if(args[0].equalsIgnoreCase("Me")){
    			if(API.hasPerm(s, "ServerControl.Me")) {
                   if(args.length == 1) {
           	        if(s instanceof Player == true) {
           	        	Loader.msg(Loader.s("Prefix")+"&e----------------- &bMe&e -----------------",s);
        	        	Player p =(Player)s;
        	        if(Loader.me.getString("Players."+p.getName()) != null) {
        	        	Loader.msg("",p);
        	        	List<String> about = Loader.TranslationsFile.getStringList("AboutYou");
        	        	for(String a: about) {
        	        		
        	        		if(Bukkit.getPluginManager().getPlugin("Vault")!=null) {
                	        	String money = API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p.getName()), true);
            	        		if(Loader.vault != null) {
            	        	Loader.msg(a
        	        				.replace("%playername%", p.getDisplayName())
	        						.replace("%prefix%", Loader.s("Prefix"))
        	        				.replace("%player%", p.getName())
        	        				.replace("%joins%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Joins")))
        	        				.replace("%leaves%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Leaves")))
        	        				.replace("%vulgarwords%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".VulgarWords")))
        	        				.replace("%spams%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Spam")))
        	        				.replace("%kicks%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Kicks")))
        	        				.replace("%vault-money%", money)
        	        				.replace("%money%",  money)
        	        				.replace("%vault-group%", String.valueOf(Loader.vault.getPrimaryGroup(p)))
        	        				.replace("%group%", String.valueOf(Loader.vault.getPrimaryGroup(p)))
        	        				.replace("%kills%", ""+p.getStatistic(Statistic.PLAYER_KILLS))
        	        				.replace("%deaths%", ""+p.getStatistic(Statistic.DEATHS))
        	        				.replace("%lastleave%", check(Loader.me.getString("Players."+p.getName()+".LastLeave")))
        	        				.replace("%firstjoin%", check(Loader.me.getString("Players."+p.getName()+".FirstJoin"))),p);
                	        }
            	        		if(Loader.vault == null) {
            	        			Loader.msg(a
                	        				.replace("%playername%", p.getDisplayName())
        	        						.replace("%prefix%", Loader.s("Prefix"))
                	        				.replace("%player%", p.getName())
                	        				.replace("%joins%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Joins")))
                	        				.replace("%leaves%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Leaves")))
                	        				.replace("%vulgarwords%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".VulgarWords")))
                	        				.replace("%spams%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Spam")))
                	        				.replace("%kicks%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Kicks")))
                	        				.replace("%vault-money%", money)
                	        				.replace("%money%",  money)
                	        				.replace("%kills%", ""+p.getStatistic(Statistic.PLAYER_KILLS))
                	        				.replace("%deaths%", ""+p.getStatistic(Statistic.DEATHS))
                	        				.replace("%vault-group%", "Install groups plugin")
                	        				.replace("%group%", "Install groups plugin")
                	        				.replace("%lastleave%", check(Loader.me.getString("Players."+p.getName()+".LastLeave")))
                	        				.replace("%firstjoin%", check(Loader.me.getString("Players."+p.getName()+".FirstJoin"))),p);
            	        		}}
        	        		if(Bukkit.getPluginManager().getPlugin("Vault")==null) {
        	        			Loader.msg(a
            	        				.replace("%playername%", p.getDisplayName())
    	        						.replace("%prefix%", Loader.s("Prefix"))
            	        				.replace("%player%", p.getName())
            	        				.replace("%joins%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Joins")))
            	        				.replace("%leaves%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Leaves")))
            	        				.replace("%vulgarwords%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".VulgarWords")))
            	        				.replace("%spams%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Spam")))
            	        				.replace("%kicks%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Kicks")))
            	        				.replace("%kills%", ""+p.getStatistic(Statistic.PLAYER_KILLS))
            	        				.replace("%deaths%", ""+p.getStatistic(Statistic.DEATHS))
            	        				.replace("%vault-money%", "Please install Vault plugin and economy plugin.")
            	        				.replace("%money%", "Please install Vault plugin and economy plugin.")
            	        				.replace("%vault-group%",  "Please install plugin Vault")
            	        				.replace("%group%",  "Please install plugin Vault")
            	        				.replace("%lastleave%", check(Loader.me.getString("Players."+p.getName()+".LastLeave")))
            	        				.replace("%firstjoin%", check(Loader.me.getString("Players."+p.getName()+".FirstJoin"))),p);
        	        			}}
        	        	 return true;
        	        }
        	        return true;
        	        }else
               	        if(s instanceof Player == false) {
               	        	Loader.Help(s, "/Chat Me <playe>", "Me");
                	        return true;
               	        }
        	        return true;
        		}
                    if(args.length==2) { 
                    	if(Loader.me.getString("Players."+args[1])!=null) {
            	        	List<String> about = Loader.TranslationsFile.getStringList("AboutYou");
            	        	String world  = Loader.me.getString("Players."+args[1]+".DisconnectWorld");
            	        	if(TheAPI.getPlayer(args[1])!=null)world=TheAPI.getPlayer(args[1]).getName();
            	        	String money = API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true);
            	        	for(String a: about) {
                    		if(Loader.getInstance.getServer().getPluginManager().getPlugin("Vault") != null) {
            	        		if(Loader.vault != null) {
            	        			Loader.msg(a
        	        				.replace("%playername%", this.online(args))
	        						.replace("%prefix%", Loader.s("Prefix"))
        	        				.replace("%player%", args[1])
        	        				.replace("%joins%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Joins")))
        	        				.replace("%leaves%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Leaves")))
        	        				.replace("%vulgarwords%", String.valueOf(Loader.me.getInt("Players."+args[1]+".VulgarWords")))
        	        				.replace("%spams%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Spam")))
        	        				.replace("%kicks%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Kicks")))
        	        				.replace("%vault-money%", money)
        	        				.replace("%kills%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Kills")))
        	        				.replace("%money%",  money)
        	        				.replace("%vault-group%", String.valueOf(Loader.vault.getPrimaryGroup(world,args[1])))
        	        				.replace("%group%", String.valueOf(Loader.vault.getPrimaryGroup(world,args[1])))
        	        				.replace("%deaths%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Deaths")))
        	        				.replace("%lastleave%", check(Loader.me.getString("Players."+args[1]+".LastLeave")))
        	        				.replace("%firstjoin%", check(Loader.me.getString("Players."+args[1]+".FirstJoin"))),s);
                	        }
            	        		if(Loader.vault == null) {
            	        			Loader.msg(a
                	        				.replace("%playername%", this.online(args))
        	        						.replace("%prefix%", Loader.s("Prefix"))
                	        				.replace("%player%", args[1])
                	        				.replace("%joins%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Joins")))
                	        				.replace("%leaves%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Leaves")))
                	        				.replace("%vulgarwords%", String.valueOf(Loader.me.getInt("Players."+args[1]+".VulgarWords")))
                	        				.replace("%spams%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Spam")))
                	        				.replace("%kicks%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Kicks")))
                	        				.replace("%vault-money%", money)
                	        				.replace("%money%",  money)
                	        				.replace("%vault-group%", "Install groups plugin")
                	        				.replace("%kills%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Kills")))
                	        				.replace("%group%", "Install groups plugin")
                	        				.replace("%deaths%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Deaths")))
                	        				.replace("%lastleave%", check(Loader.me.getString("Players."+args[1]+".LastLeave")))
                	        				.replace("%firstjoin%", check(Loader.me.getString("Players."+args[1]+".FirstJoin"))),s);
            	        		}}
        	        		if(Bukkit.getPluginManager().getPlugin("Vault")==null) {
        	        			Loader.msg(a
            	        				.replace("%playername%", this.online(args))
    	        						.replace("%prefix%", Loader.s("Prefix"))
            	        				.replace("%player%", args[1])
            	        				.replace("%joins%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Joins")))
            	        				.replace("%leaves%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Leaves")))
            	        				.replace("%vulgarwords%", String.valueOf(Loader.me.getInt("Players."+args[1]+".VulgarWords")))
            	        				.replace("%spams%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Spam")))
            	        				.replace("%kicks%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Kicks")))
            	        				.replace("%vault-money%", "Please install Vault plugin and economy plugin.")
            	        				.replace("%money%", "Please install Vault plugin and economy plugin.")
            	        				.replace("%kills%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Kills")))
            	        				.replace("%vault-group%",  "Please install plugin Vault")
            	        				.replace("%group%",  "Please install plugin Vault")
            	        				.replace("%deaths%", String.valueOf(Loader.me.getInt("Players."+args[1]+".Deaths")))
            	        				.replace("%lastleave%", check(Loader.me.getString("Players."+args[1]+".LastLeave")))
            	        				.replace("%firstjoin%", check(Loader.me.getString("Players."+args[1]+".FirstJoin"))),s);
        	        			}}
                	  		return true;
                    	}
                    	if(Loader.me.getString("Players."+args[1])==null) {
                    		Loader.msg(Loader.PlayerNotEx(args[1]),s);
                    		return true;
                    	}
        	  		return true;
                    }}return true;}
        Loader.msg(Loader.s("Prefix")+Loader.s("UknownCommand"),s);
        return true;
			}
    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
    	List<String> c = new ArrayList<>();
    	if(args.length==1) {

        	if(s.hasPermission("ServerControl.Help")) {
                c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Help"), new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.General")) {
                c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("General"), new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.Info")) {
                c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Info","Version"), new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.Me")) {
                c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Me"), new ArrayList<>()));
            }
        	c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Info","Version"), new ArrayList<>()));
    	}
    	if(args.length==2) {
        	if(args[0].equalsIgnoreCase("Help")) {
            	if(s.hasPermission("ServerControl.Help")) {
            		c.addAll(StringUtil.copyPartialMatches(args[1], All, new ArrayList<>()));
            		
            	}}
    	if(args[0].equalsIgnoreCase("Me") && s.hasPermission("ServerControl.Me.Other")) {
        	if(s.hasPermission("ServerControl.Me")) {
        		return null;
        	}}}
    return c;
}
}
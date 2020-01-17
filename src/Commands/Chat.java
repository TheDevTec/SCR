package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
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

	private static final List<String> Help = Arrays.asList("Help");
	private static final List<String> Version = Arrays.asList("Version");
	private static final List<String> Info = Arrays.asList("Info");
	private static final List<String> General = Arrays.asList("General");
	private static final List<String> Me = Arrays.asList("Me");
	private static final List<String> All = Arrays.asList("General", "Help", "Info","Version", "Me");

	public String online(String[] args) {
		if(Bukkit.getPlayer(args[1])!=null) {
			return Bukkit.getPlayer(args[1]).getDisplayName();
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
	            	Loader.msg(Loader.s("Prefix")+"&e----------------- &b"+Loader.s("Words.Help")+"&e -----------------",s);
	            	Loader.msg("",s);
	            Loader.Help(s, "/Chat Me <player>","Me");
	            Loader.Help(s, "/Chat General","General");
	            Loader.Help(s, "/Chat Version","Version");
	            Loader.Help(s, "/Chat Help","Help");
	            Loader.Help(s, "/Chat Info","Info");
	            return true;
	        }
	            if(args.length==2) {
	                	for(String v:All)
	            			if(args[1].equalsIgnoreCase(v)) {
	            				Loader.msg(Loader.s("Prefix")+"&e----------------- &b"+Loader.s("Words.HelpFor")+" "+"&b"+v+"&e -----------------",s);
	            				Loader.msg("",s);
	                        Loader.Help(s, "/Chat "+v,v);
	                        return true;
	                        }
	                	Loader.msg(Loader.s("Prefix")+"&e----------------- &b"+Loader.s("Words.HelpFor")+" "+"&4"+args[1]+" &e-----------------",s);
	                	Loader.msg("",s);
	                	Loader.msg(Loader.s("Prefix")+Loader.s("Help.NoHelpForCommand").replace("%command%", args[1]), s);
	                        return true;
	            		}}return true;}
    if(args[0].equalsIgnoreCase("Version")){
		if(API.hasPerm(s, "ServerControl.Info")) {
        	Loader.msg(Loader.s("Prefix")+"&e----------------- &bVersion&e -----------------",s);
        	Loader.msg("",s);
        	Loader.msg(Loader.s("Prefix")+Loader.s("Words.Version")+"&7: &6V"+ Loader.getInstance.getDescription().getVersion(),s);
        	Loader.msg(Loader.s("Prefix")+Loader.s("Words.ServerVersion")+"&7: &6"+ Bukkit.getServer().getBukkitVersion(),s);
            return true;
       }return true;}
        	if(args[0].equalsIgnoreCase("General")){
    			if(API.hasPerm(s, "ServerControl.General")) {
                	   Loader.msg(Loader.s("Prefix")+"&e----------------- &bGeneral&e -----------------",s);
                	   Loader.msg("",s);
                	   Loader.msg(Loader.s("Prefix")+Loader.config.getInt("VulgarWords")+" "+Loader.s("Words.Vulgar"),s);
                	   Loader.msg(Loader.s("Prefix")+Loader.config.getInt("Spam")+" "+Loader.s("Words.Spams"),s);
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
        	        				.replace("%deaths%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Deaths")))
        	        				.replace("%lastleave%", check(Loader.me.getString("Players."+p.getName()+".LastLeave")))
        	        				.replace("%kills%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Kills")))
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
                	        				.replace("%kills%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Kills")))
                	        				.replace("%vault-group%".toLowerCase(), "Install groups plugin")
                	        				.replace("%group%", "Install groups plugin")
                	        				.replace("%deaths%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Deaths")))
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
            	        				.replace("%kills%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Kills")))
            	        				.replace("%vault-money%", "Please install Vault plugin and economy plugin.")
            	        				.replace("%money%", "Please install Vault plugin and economy plugin.")
            	        				.replace("%vault-group%",  "Please install plugin Vault")
            	        				.replace("%group%",  "Please install plugin Vault")
            	        				.replace("%deaths%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Deaths")))
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
        	        				.replace("%vault-group%", String.valueOf(Loader.vault.getPrimaryGroup(Loader.me.getString("Players."+args[1]+".DisconnectWorld"),args[1])))
        	        				.replace("%group%", String.valueOf(Loader.vault.getPrimaryGroup(Loader.me.getString("Players."+args[1]+".DisconnectWorld"),args[1])))
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
	        if(args[0].equalsIgnoreCase("Info")){
	            	Loader.msg(Loader.s("Prefix")+"&e----------------- &bInfo&e -----------------",s);
	            	Loader.msg("",s);
	            	Loader.msg(Loader.s("PluginName")+"&7: &4Server Control Reloaded",s);
	            	Loader.msg(Loader.s("Prefix")+Loader.s("PluginCreatedBy")+"&7: &6Straiker123",s);
	            	Loader.msg(Loader.s("Prefix")+Loader.s("Words.Version")+"&7: &6V"+  Loader.getInstance.getDescription().getVersion(),s);
	            	Loader.msg(Loader.s("Prefix")+Loader.s("ServerControlThema"),s);
	        
	            return true;
	       }
        Loader.msg(Loader.s("Prefix")+Loader.s("UknownCommand"),s);
        return true;
			}
    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
    	List<String> c = new ArrayList<>();
    	if(cmd.getName().equalsIgnoreCase("Chat") && args.length==1) {

        	if(s.hasPermission("ServerControl.Help")) {
                c.addAll(StringUtil.copyPartialMatches(args[0], Help, new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.General")) {
                c.addAll(StringUtil.copyPartialMatches(args[0], General, new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.Version")) {
                c.addAll(StringUtil.copyPartialMatches(args[0], Version, new ArrayList<>()));
            }
        	if(s.hasPermission("ServerControl.Me")) {
                c.addAll(StringUtil.copyPartialMatches(args[0], Me, new ArrayList<>()));
            }
        	c.addAll(StringUtil.copyPartialMatches(args[0], Info, new ArrayList<>()));
    	}else
        	if(cmd.getName().equalsIgnoreCase("Chat") && args[0].equalsIgnoreCase("Help") && args.length==2) {
            	if(s.hasPermission("ServerControl.Help")) {
            		c.addAll(StringUtil.copyPartialMatches(args[1], All, new ArrayList<>()));
            		
            	}}
    	if(cmd.getName().equalsIgnoreCase("Chat") && args[0].equalsIgnoreCase("Me") && args.length==2) {
        	if(s.hasPermission("ServerControl.Me")) {
        		return null;
        	}}
    return c;
}
}
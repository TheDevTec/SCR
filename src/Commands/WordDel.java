package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

public class WordDel implements CommandExecutor, TabCompleter {

	public boolean onCommand(CommandSender s, Command cmd, String label,String[] args) {
		if(API.hasPerm(s, "ServerControl.WordDel")) {
				if(args.length==0) {
					Loader.msg(Loader.s("Prefix")+Loader.s("DelWord.UsageCommand"),s);
					Loader.msg(Loader.s("Prefix")+Loader.s("DelWord.Path").replace("%paths%", "VulgarWordsList, SpamWordsList"),s);
   					return true;
				}
				
				if(args.length==1) {
                	if(args[0].equalsIgnoreCase("VulgarWordsList") || args[0].equalsIgnoreCase("SpamWordsList")) {
                		Loader.msg(Loader.s("Prefix")+Loader.s("DelWord.UsageCommand"),s);
				return true;
					}
            		Loader.msg(Loader.s("Prefix")+Loader.s("DelWord.UsageCommand"),s);
                		Loader.msg(Loader.s("Prefix")+Loader.s("DelWord.Path").replace("%paths%", "VulgarWordsList, SpamWordsList"),s);
            	return true;
                	}
				
                    	if(args[0].equalsIgnoreCase("VulgarWordsList") || args[0].equalsIgnoreCase("SpamWordsList")) {
                        	List<String> vulgarword = Loader.config.getStringList(args[0]);
                        	if(!vulgarword.contains(args[1].toLowerCase())) {
                        		Loader.msg(Loader.s("Prefix")+Loader.s("DelWord.WordIsNotInConfig").replace("%word%", args[1]),s);
                      			return true;
                        	}
                        		vulgarword.remove(args[1].toLowerCase());
                        		Loader.config.set(args[0], vulgarword);
            					Configs.config.save();
            					Loader.msg(Loader.s("Prefix")+Loader.s("DelWord.WordDeleted").replace("%word%", args[1]),s);	
                    			return true;
                    	}
                            Loader.msg(Loader.s("Prefix")+Loader.s("DelWord.Path").replace("%paths%", "VulgarWordsList, SpamWordsList"),s);
                       	return true;
	}
	 return true;
}

	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        List <String> swords = Loader.config.getStringList("SpamWordsList");
        List <String> vwords = Loader.config.getStringList("VulgarWordsList");
		List<String> All = Arrays.asList("VulgarWordsList", "SpamWordsList");
    	List<String> c = new ArrayList<>();
        if(args.length == 1) {
                if(sender.hasPermission("ServerControl.WordDel")) {
                	c.addAll(StringUtil.copyPartialMatches(args[0], All, new ArrayList<>()));
            }
        }
        if(args.length==2) {
            if(sender.hasPermission("ServerControl.WordDel")) {
            if(args[0].equals("VulgarWordsList")) {
                		c.addAll(StringUtil.copyPartialMatches(args[1], vwords, new ArrayList<>()));
                }
                if(args[0].equals("SpamWordsList")) {
                    		c.addAll(StringUtil.copyPartialMatches(args[1], swords, new ArrayList<>()));    
            }}}
        return c;
    }}
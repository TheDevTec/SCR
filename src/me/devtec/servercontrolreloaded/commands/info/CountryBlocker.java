package me.devtec.servercontrolreloaded.commands.info;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;

public class CountryBlocker implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender s, Command command, String u, String[] args) {
        if(Loader.has(s,"CountryBlocker","Info")){
			if(!CommandsManager.canUse("Info.CountryBlocker", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Info.CountryBlocker", s))));
				return true;
			}
            if(args.length==0){
                Loader.Help(s,"CountryBlocker","Info");
                return true;
            }
            if(args.length==1){
                if(args[0].equalsIgnoreCase("list")){
                    Loader.sendMessages(s,"CountryBlocker.ListMess");
                    for(String a:Loader.config.getStringList("CountryBlocker.Whitelist"))
                    Loader.sendMessages(s,"CountryBlocker.List", Loader.Placeholder.c().add("%player%",a));
                    return true;
                }
            }
            if(args.length==2){
                if(args[0].equalsIgnoreCase("add")){
                    List<String> list=new ArrayList<>();
                    list=Loader.config.getStringList("CountryBlocker.Whitelist");
                    list.add(args[1]);
                    Loader.config.set("CountryBlocker.Whitelist",list);
                    Loader.config.save();
                    Loader.sendMessages(s,"CountryBlocker.Add", Loader.Placeholder.c().add("%player%",args[1]));
                    return true;
                }
                if(args[0].equalsIgnoreCase("remove")){
                    List<String> list=new ArrayList<>();
                    list=Loader.config.getStringList("CountryBlocker.Whitelist");
                    if(list.contains(args[1])){
                        list.remove(args[1]);
                        Loader.config.set("CountryBlocker.Whitelist",list);
                        Loader.config.save();
                        Loader.sendMessages(s,"CountryBlocker.Remove", Loader.Placeholder.c().add("%player%",args[1]));
                        return true;
                    }else{
                        Loader.sendMessages(s,"CountryBlocker.NotWhitelisted", Loader.Placeholder.c().add("%player%",args[1]));
                        return true;
                    }
                }
            }
        }
        Loader.Help(s,"CountryBlocker","Info");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}

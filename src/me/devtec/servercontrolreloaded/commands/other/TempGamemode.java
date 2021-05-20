package me.devtec.servercontrolreloaded.commands.other;

import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class TempGamemode implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender s, Command command, String u, String[] args) {
        if(Loader.has(s,"TempGamemode","GameMode")){
			if(!CommandsManager.canUse("GameMode.TempGamemode", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("GameMode.TempGamemode", s))));
				return true;
			}
            if(args.length==0||args.length==1||args.length>3){
                if(args[0].equalsIgnoreCase("remove")){
                    if(args.length==1){
                        TheAPI.getUser(s.getName()).remove("TempGamemode");
                        TheAPI.getUser(s.getName()).save();
                        Loader.sendMessages(s,"GameMode.Temp.Remove");
                        return true;
                    }
                    if(args.length==2){
                     TheAPI.getUser(args[1]).remove("TempGamemode");
                     Loader.sendMessages(s,"GameMode.Temp.Remove");
                     Loader.sendMessages(TheAPI.getPlayer(args[1]),"GameMode.Temp.RemoveOther");
                     return true;
                    }
                }
                if(TheAPI.getUser(s.getName()).getBoolean("TempGamemode.Use")){
                    Loader.sendMessages(s,"GameMode.Temp.EndIn", Loader.Placeholder.c().add("%time%",StringUtils.timeToString(TheAPI.getUser(s.getName()).getLong("TempGamemode.Time"))+""));
                    return true;
                }
                Loader.Help(s,"TempGamemode","GameMode");
                return true;
            }
            if(args.length==2){
                if(s instanceof Player){
                    metoda(args[0],args[1],false,s);
                }
                return true;
            }
            if(args.length==3){
                Player p = TheAPI.getPlayer(args[0]);
                if(p==null){Loader.sendMessages(s,"Missing.Player.Offline")
                        ;return true;}
                metoda(args[1],args[2],true,p);
                return true;
            }
        }
        if(Loader.has(s,"TempGamemode","GameMode","Info")){
            if(TheAPI.getUser(s.getName()).getBoolean("TempGamemode.Use")){
                Loader.sendMessages(s,"GameMode.Temp.EndIn", Loader.Placeholder.c().add("%time%",StringUtils.timeToString(TheAPI.getUser(s.getName()).getLong("TempGamemode.Time"))+""));
                return true;
            }
        }
        Loader.noPerms(s,"TempGamemode","GameMode");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command command, String u, String[] args) {
        if(Loader.has(s,"TempGamemode","GameMode")){
            if((args.length==2||args.length==3)&&args[0].equalsIgnoreCase("remove"))return Arrays.asList();
            if(args.length==1)
                for(Player p:TheAPI.getOnlinePlayers())
                return StringUtils.copyPartialMatches(args[0], Arrays.asList("c","creative","a","adventure","s","survival","sp","spectator","remove",p.getName()));
            if(args.length==2)
                for(Player p : TheAPI.getOnlinePlayers()){
                    if(args[0].equalsIgnoreCase(p.getName())){
                        return Arrays.asList("c","creative","a","adventure","s","survival","sp","spectator");
                    }else{
                        try{
                            if(args[2].substring(args[2].length()-2, args[2].length()).matches("[0-9]"))
                                return Arrays.asList(args[1]+"s",args[1]+"m",args[1]+"h",args[1]+"d",args[1]+"w",args[1]+"mo");
                        }catch (Exception e){return Arrays.asList("15m","2h","2h30m","6h","7d","1y");}
                    }
                }
            if(args.length==3&&(args[1].equalsIgnoreCase("c")||args[1].equalsIgnoreCase("creative")||args[1].equalsIgnoreCase("a")||args[1].equalsIgnoreCase("adventure")
            ||args[1].equalsIgnoreCase("s")||args[1].equalsIgnoreCase("survival")||args[1].equalsIgnoreCase("sp")||args[1].equalsIgnoreCase("spectator"))){
                try{
                    if(args[2].substring(args[2].length()-2, args[2].length()).matches("[0-9]"))
                        return Arrays.asList(args[1]+"s",args[1]+"m",args[1]+"h",args[1]+"d",args[1]+"w",args[1]+"mo");
                }catch (Exception e){return Arrays.asList("15m","2h","2h30m","6h","7d","1y");}
            }
        }
        return Arrays.asList();
    }

    private void metoda(String args,String time,boolean t,CommandSender p){
        switch (args) {
            case "c":
            case "creative":
                API.getSPlayer(TheAPI.getPlayer(p.getName())).enableTempGameMode(StringUtils.getTimeFromString(time), GameMode.CREATIVE, t);
                break;
            case "s":
            case "survival":
                API.getSPlayer(TheAPI.getPlayer(p.getName())).enableTempGameMode(StringUtils.getTimeFromString(time), GameMode.SURVIVAL, t);
                break;
            case "a":
            case "adventure":
                API.getSPlayer(TheAPI.getPlayer(p.getName())).enableTempGameMode(StringUtils.getTimeFromString(time), GameMode.ADVENTURE, t);
                break;
            case "sp":
            case "spectator":
                API.getSPlayer(TheAPI.getPlayer(p.getName())).enableTempGameMode(StringUtils.getTimeFromString(time), GameMode.SPECTATOR, t);
                break;
            default:
                Loader.Help(p, "TempGamemode", "GameMode");
        }
    }
}

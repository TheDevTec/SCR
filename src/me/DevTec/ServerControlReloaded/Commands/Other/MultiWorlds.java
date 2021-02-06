package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsGUI;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsUtils;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.worldsapi.WorldsAPI;

public class MultiWorlds implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	/*
	/mw create [world]
	/mw delete [world]
	/mw load [world]
	/mw unload [world]
	/mw edit [world] [flag] [boolean]
	Zda je gen normal, flat, void, end, nether nebo the_end, the_void, default tak to projde

	 */
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(!Loader.has(s,"MultiWorlds","Other")){
			Loader.noPerms(s,"MultiWorlds","Other");
			return true;
		}
		if (args.length == 0) {
			if (s instanceof Player) {
				if(Loader.has(s,"MultiWorlds","Other")){
					new Tasker(){
						@Override
						public void run() {
							MultiWorldsGUI.openInv((Player) s);
						}
					}.runTaskSync();
					return true;
				}
				return true;
			}
			if(s instanceof ConsoleCommandSender){
				Loader.Help(s,"MultiWorlds","Other");
				Loader.advancedHelp(s,"MultiWorlds","Other","Create");
				Loader.advancedHelp(s,"MultiWorlds","Other","Load");
				Loader.advancedHelp(s,"MultiWorlds","Other","Unload");
				Loader.advancedHelp(s,"MultiWorlds","Other","Remove");
				Loader.advancedHelp(s,"MultiWorlds","Other","Edit");
				return true;
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("create")){
			if(args.length==1||args.length==2){
				Loader.advancedHelp(s,"MultiWorlds","Other","Create");
				return true;
			}
			if(args[2].equalsIgnoreCase("normal")
					||args[2].equalsIgnoreCase("flat")
					||args[2].equalsIgnoreCase("void")
					||args[2].equalsIgnoreCase("end")
					||args[2].equalsIgnoreCase("nether")
					||args[2].equalsIgnoreCase("the_end")
					||args[2].equalsIgnoreCase("the_void")
					||args[2].equalsIgnoreCase("default")){
				Loader.mw.set("WorldsSettings." + args[1] + ".Generator",
						args[2]);
				new Tasker(){
					public void run() {
						Loader.sendMessages(s,"MultiWorld.Creating", Loader.Placeholder.c().add("%generator%",args[2]).add("%world%",args[1]));
						Loader.mw.set("WorldsSettings." + args[1] + ".Generator",args[2]);
						Loader.mw.save();
						NMSAPI.postToMainThread(() -> MultiWorldsUtils.CreateWorld(args[1], s));
					}
				}.runLater(10);
				return true;
			}
			Loader.sendMessages(s,"Missing.Generator", Loader.Placeholder.c().add("%generator%",args[2]));
			return true;
		}
		if(args[0].equalsIgnoreCase("delete")||args[0].equalsIgnoreCase("remove")){
			if(args.length==1&&args[0].equalsIgnoreCase("delete")){
				Loader.advancedHelp(s,"MultiWorlds","Other","Delete");
				return true;
			}else if(args.length==1&&args[0].equalsIgnoreCase("remove")){
				Loader.advancedHelp(s,"MultiWorlds","Other","Remove");
				return true;
			}
			for (World w : Bukkit.getWorlds()){
				if(w.getName().equalsIgnoreCase(args[1])){
					List<String> ww = Loader.mw.getStringList("Unloaded-Worlds");
					List<String> worlds = Loader.mw.getStringList("Worlds");
					worlds.remove(w.getName());
					ww.remove(w.getName());
					Loader.mw.set("Worlds", worlds);
					Loader.mw.set("Unloaded-Worlds",ww);
					Loader.mw.remove("WorldsSettings."+w.getName()+".Gamerule");
					Loader.mw.remove("WorldsSettings."+w.getName()+".Spawn");
					Loader.mw.remove("WorldsSettings."+w.getName());
					Loader.mw.save();
					if (WorldsAPI.delete(w, true))
						Loader.sendMessages(s, "MultiWorld.Delete", Loader.Placeholder.c().add("%world%", args[1]));
					return true;
				}
			}
			Loader.sendMessages(s,"Missing.World", Loader.Placeholder.c().add("%world%",args[1]));
			return true;
		}
		if(args[0].equalsIgnoreCase("load")){
			if(args.length==1){
				Loader.advancedHelp(s,"MultiWorlds","Other","Load");
				return true;
			}
			if(Loader.mw.exists("Unloaded-Worlds")){
				for(String w : Loader.mw.getStringList("Unloaded-Worlds")){
					if(w.equalsIgnoreCase(args[1])){
						Loader.sendMessages(s,"MultiWorld.Load", Loader.Placeholder.c().add("%world%",args[1]));
						MultiWorldsUtils.LoadWorld(w, s);
						return true;
					}
				}
			}
			Loader.sendMessages(s,"MultiWorld.CannotLoad", Loader.Placeholder.c().add("%world%",args[1]));
			return true;
		}
		Loader.advancedHelp(s,"MultiWorlds","Other","Create");
		Loader.advancedHelp(s,"MultiWorlds","Other","Load");
		Loader.advancedHelp(s,"MultiWorlds","Other","Unload");
		Loader.advancedHelp(s,"MultiWorlds","Other","Remove");
		Loader.advancedHelp(s,"MultiWorlds","Other","Edit");
		return true;
	}

}

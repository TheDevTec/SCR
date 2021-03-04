package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsGUI;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsUtils;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.worldsapi.WorldsAPI;

public class MultiWorlds implements CommandExecutor, TabCompleter {


	/*
	/mw create [world]
	/mw delete [world]
	/mw load [world]
	/mw unload [world]
	/mw edit [world] [flag] [boolean]
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
			Loader.Help(s,"MultiWorlds","Other");
			return true;
		}
		if(args[0].equalsIgnoreCase("create")){
			if(args.length != 3){
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
						Loader.mw.set("WorldsSettings." + args[1] + ".Generator",args[2]);
						Loader.mw.save();
						NMSAPI.postToMainThread(() -> MultiWorldsUtils.createWorld(args[1], s));
					}
				}.runLater(10);
				return true;
			}
			Loader.sendMessages(s,"Missing.Generator", Loader.Placeholder.c().add("%generator%",args[2]));
			return true;
		}
		if(args[0].equalsIgnoreCase("delete")){
			if(args.length!=2){
				Loader.advancedHelp(s,"MultiWorlds","Other","Delete");
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
			if(args.length!=2){
				Loader.advancedHelp(s,"MultiWorlds","Other","Load");
				return true;
			}
			if(Loader.mw.exists("Unloaded-Worlds")){
				for(String w : Loader.mw.getStringList("Unloaded-Worlds")){
					if(w.equalsIgnoreCase(args[1])){
						MultiWorldsUtils.loadWorld(w, s);
						return true;
					}
				}
			}
			Loader.sendMessages(s,"MultiWorld.CannotLoad", Loader.Placeholder.c().add("%world%",args[1]));
			return true;
		}
		if(args[0].equalsIgnoreCase("unload")){
			if(args.length!=2){
				Loader.advancedHelp(s,"MultiWorlds","Other","Load");
				return true;
			}
			for(World w : Bukkit.getWorlds()){
				if(w.getName().equalsIgnoreCase(args[1])){
					MultiWorldsUtils.unloadWorld(args[1],s);
					return true;
				}
			}
			return true;
		}
		Loader.advancedHelp(s,"MultiWorlds","Other","Create");
		Loader.advancedHelp(s,"MultiWorlds","Other","Load");
		Loader.advancedHelp(s,"MultiWorlds","Other","Unload");
		Loader.advancedHelp(s,"MultiWorlds","Other","Remove");
		Loader.advancedHelp(s,"MultiWorlds","Other","Edit");
		return true;
	}
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args){
		if (args.length == 1) {
			return StringUtils.copyPartialMatches(args[0], Arrays.asList("unload","load","delete","create","edit"));
		}
		if(args.length==2) {
			if(args[0].equalsIgnoreCase("create")) {return Arrays.asList("?");}
			if(args[0].equalsIgnoreCase("load")){
				return StringUtils.copyPartialMatches(args[0], Loader.mw.getStringList("Unloaded-Worlds"));
			}
			if(args[0].equalsIgnoreCase("unload")){

			}
		}
		if(args.length==3){
			if(args[0].equalsIgnoreCase("create")){
				List<String> generators = new ArrayList<>();
				generators.addAll(Arrays.asList("flat","void","end","nether","the_end","the_void","default"));
				return StringUtils.copyPartialMatches(args[0],generators);
			}
		}
		return StringUtils.copyPartialMatches(args[1],Arrays.asList(""));
	}
}
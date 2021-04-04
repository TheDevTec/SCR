package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.worldsapi.WorldsAPI;

public class MultiWorlds implements CommandExecutor, TabCompleter {


	/*
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
					MultiWorldsGUI.openInv((Player) s);
					return true;
				}
				return true;
			}
			Loader.Help(s,"MultiWorlds","Other");
			return true;
		}
		if(args[0].equalsIgnoreCase("create")){
			if(args.length < 3){
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
				Loader.mw.set("WorldsSettings." + args[1] + ".Generator",args[2].toUpperCase());
				Loader.mw.save();
				MultiWorldsUtils.createWorld(args[1], s);
				return true;
			}
			Loader.sendMessages(s,"Missing.Generator", Loader.Placeholder.c().add("%generator%",args[2]));
			return true;
		}
		if(args[0].equalsIgnoreCase("delete")){
			if(args.length < 2){
				Loader.advancedHelp(s,"MultiWorlds","Other","Delete");
				return true;
			}
			World w = Bukkit.getWorld(args[1]);
			if(w==null) {
				Loader.sendMessages(s,"Missing.World", Loader.Placeholder.c().add("%world%",args[1]));
				return true;
			}
			List<String> ww = Loader.mw.getStringList("Unloaded-Worlds");
			List<String> worlds = Loader.mw.getStringList("Worlds");
			worlds.remove(w.getName());
			ww.remove(w.getName());
			Loader.mw.set("Worlds", worlds);
			Loader.mw.set("Unloaded-Worlds",ww);
			Loader.mw.remove("WorldsSettings."+w.getName());
			Loader.mw.save();
			if (WorldsAPI.delete(w, true))
				Loader.sendMessages(s, "MultiWorld.Delete", Loader.Placeholder.c().add("%world%", args[1]));
			return true;
		}
		if(args[0].equalsIgnoreCase("load")){
			if(args.length < 3){
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
		if(args[0].equalsIgnoreCase("set")||args[0].equalsIgnoreCase("edit")){
			if(args.length < 3){ //mw set <world> <flag> <value>
				Loader.advancedHelp(s,"MultiWorlds","Other","Edit");
				return true;
			}
			if(Bukkit.getWorld(args[1])==null) {
				Loader.sendMessages(s,"Missing.World", Loader.Placeholder.c().add("%world%",args[1]));
				return true;
			}
			if(!existsFlag(args[2])) {
				Loader.sendMessages(s,"Missing.Flag", Loader.Placeholder.c().add("%world%",args[1]));
				return true;
			}
			if(args.length < 4){ //-get
				Loader.sendMessages(s,"MultiWorld.Flag.Get", Loader.Placeholder.c().add("%flag%",args[2]).add("%value%",MultiWorldsUtils.getFlag(Bukkit.getWorld(args[1]),args[2])));
				return true;
			}
			List<String> g = flags.get(args[2].toUpperCase());
			if(g==null) {
				Loader.sendMessages(s,"MultiWorld.Flag.Set", Loader.Placeholder.c().add("%flag%",args[2]).add("%value%",StringUtils.getInt(args[3])+""));
				return true;
			}
			if(g.contains(args[3].toLowerCase())) {
				Loader.sendMessages(s,"MultiWorld.Flag.Set", Loader.Placeholder.c().add("%flag%",args[2]).add("%value%",StringUtils.getBoolean(args[3])+""));
				return true;
			}
			Loader.sendMessages(s,"MultiWorld.Flag.Wrong", Loader.Placeholder.c().add("%flag%",args[2]).add("%value%",flags.get(args[3])==null?"integer":"boolean"));
			return true;
		}
		if(args[0].equalsIgnoreCase("setspawn")){
			Player d = (Player)s;
			String world = d.getWorld().getName();
			Loader.mw.set("WorldsSettings." + world + ".Spawn.X", d.getLocation().getX());
			Loader.mw.set("WorldsSettings." + world + ".Spawn.Y", d.getLocation().getY());
			Loader.mw.set("WorldsSettings." + world + ".Spawn.Z", d.getLocation().getZ());
			Loader.mw.set("WorldsSettings." + world + ".Spawn.X_Pos_Head",
					d.getLocation().getYaw());
			Loader.mw.set("WorldsSettings." + world+ ".Spawn.Z_Pos_Head",
					d.getLocation().getPitch());
			try {
				d.getWorld().setSpawnLocation(d.getLocation());
			}catch(NoSuchMethodError err) {
				d.getWorld().setSpawnLocation(d.getLocation().getBlockX(), d.getLocation().getBlockY(), d.getLocation().getBlockZ());
			}
			Loader.sendMessages(s,"MultiWorld.Spawn.Set", Loader.Placeholder.c().add("%world%",d.getWorld().getName())
					.add("%x%", StringUtils.fixedFormatDouble(d.getLocation().getX()))
					.add("%y%", StringUtils.fixedFormatDouble(d.getLocation().getY()))
					.add("%z%", StringUtils.fixedFormatDouble(d.getLocation().getZ()))
					.add("%pitch%", StringUtils.fixedFormatDouble(d.getLocation().getPitch()))
					.add("%yaw%", StringUtils.fixedFormatDouble(d.getLocation().getYaw())));
			return true;
		}
		if(args[0].equalsIgnoreCase("unload")){
			if(args.length < 3){
				Loader.advancedHelp(s,"MultiWorlds","Other","Load");
				return true;
			}
			if(Bukkit.getWorld(args[1])==null) {
				Loader.sendMessages(s,"Missing.World", Loader.Placeholder.c().add("%world%",args[1]));
				return true;
			}
			MultiWorldsUtils.unloadWorld(args[1],s);
			return true;
		}
		Loader.Help(s,"MultiWorlds","Other");
		return true;
	}
	
	public static Map<String, List<String>> flags = new HashMap<>();
	static {
		List<String> bool = Arrays.asList("yes","no","true","false");
		flags.put("DO_FALL_DAMAGE", bool);
		flags.put("DO_FIRE_DAMAGE", bool);
		flags.put("DO_DROWNING_DAMAGE", bool);
		flags.put("NO_MOBS", bool);
		flags.put("AUTOSAVE", bool);
		flags.put("PORTAL_TELEPORT", bool);
		flags.put("PORTAL_CREATE", bool);
		flags.put("PVP", bool);
		flags.put("KEEP_SPAWN_IN_MEMORY", bool);
		flags.put("HARDCORE", bool);
		flags.put("DIFFICULTY", Arrays.asList("PEACEFUL","EASY","NORMAL","HARD"));
		flags.put("GAMEMODE", TheAPI.isNewerThan(7)?Arrays.asList("SURVIVAL","CwREATIVE","SPECTATOR","ADVENTURE"):Arrays.asList("SURVIVAL","CREATIVE","ADVENTURE"));
	}
	
	public static void loadFlags() {
		List<String> bool = Arrays.asList("yes","no","true","false");
		for(String ds : Bukkit.getWorlds().get(0).getGameRules())
		switch (ds) {
		case "COMMAND_BLOCK_OUTPUT":
		case "DO_TRADER_SPAWNING":
		case "NATURAL_REGENERATION":
		case "FORGIVE_DEAD_PLAYERS":
		case "DISABLE_ELYTRA_MOVEMENT_CHECK":
		case "DO_IMMEDIATE_RESPAWN":
		case "DO_INSOMNIA":
		case "DO_PATROL_SPAWNING":
		case "DISABLE_RAIDS":
		case "DO_DAYLIGHT_CYCLE":
		case "DO_ENTITY_DROPS":
		case "DO_FIRE_TICK":
		case "DO_MOB_LOOT":
		case "DO_TILE_DROPS":
		case "KEEP_INVENTORY":
		case "UNIVERSAL_ANGER":
		case "DO_WEATHER_CYCLE":
		case "LOG_ADMIN_COMMANDS":
		case "REDUCED_DEBUG_INFO":
		case "MOB_GRIEFING":
		case "SEND_COMMAND_FEEDBACK":
		case "SHOW_DEATH_MESSAGES":
		case "ANNOUNCE_ADVANCEMENTS":
		case "SPECTATORS_GENERATE_CHUNKS":
		case "DO_LIMITED_CRAFTING":
			flags.put(ds, bool);
			break;
		case "MAX_COMMAND_CHAIN_LENGTH":
		case "MAX_ENTITY_CRAMMING":
		case "RANDOM_TICK_SPEED":
		case "SPAWN_RADIUS":
			flags.put(ds, null);
			break;
		}
	}
	
	public static boolean existsFlag(String s) {
		return flags.containsKey(s.toUpperCase());
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
				return StringUtils.copyPartialMatches(args[2],generators);
			}
		}
		return Arrays.asList();
	}
}
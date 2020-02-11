package Utils;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class MultiWorldsUtils {
	public static void UnloadWorld(String w, CommandSender sender) {
		if(!TheAPI.getWorldsManager().unloadWorld(w, true)) {
			Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.DoNotUnloaded").replace("%world%", w), sender);
			return;
		}else {
			List<String> worlds = Loader.mw.getStringList("Worlds");
			List<String> ww =Loader. mw.getStringList("Unloaded-Worlds");
			worlds.remove(w);
			ww.add(w);
			Loader.mw.set("Worlds", worlds);
			Loader.mw.set("Unloaded-Worlds", ww);
			 Configs.mw.save();
			Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.Unloaded").replace("%world%", w), sender);
			return;
		}}
	public static void unloadWorlds(CommandSender s) {
		int chunk = 0;
		for(World world: Bukkit.getWorlds()) {
		chunk = chunk + world.getLoadedChunks().length;
			for(Chunk c : world.getLoadedChunks()) {
			    c.unload();
			}
		}
		for(World world: Bukkit.getWorlds()) 
		chunk = chunk - world.getLoadedChunks().length;
		Loader.msg(Loader.s("Prefix")+Loader.s("Chunks.Unloaded")
		.replace("%chunks%", String.valueOf(chunk)),s);	
	}
	public static void EnableWorldCheck() {
		for(Player on: Bukkit.getOnlinePlayers()) {
			if(Bukkit.getWorld(on.getWorld().getName())!=null) {
				String[] f = {"SURVIVAL", "CREATIVE", "ADVENTURE", "SPECTATOR"};
			for(String waww:f) {
			if(Loader.mw.getString("WorldsSettings."+on.getWorld().getName()+".GameMode").equalsIgnoreCase(waww)) {
	    		on.setGameMode(GameMode.valueOf(Loader.mw.getString("WorldsSettings."+on.getWorld().getName()+".GameMode")));
			}}}
			if(Bukkit.getWorld(on.getWorld().getName())==null) {
				on.kickPlayer("Unable to find world");
			}}}
	public static boolean checkWorld(String world) {
	if(Bukkit.getWorld(world) != null) {
		return true;
	}
	return false;
	}
	public static enum Generator{
		NORMAL,
		FLAT,
		NETHER,
		THE_END,
		THE_VOID;
	}
	private static boolean exist(String w) {
		return new File(Bukkit.getWorldContainer().getPath()+"/"+w+"/session.lock").exists();
	}
	
	public static void importWorld(String w, CommandSender s, Generator type) {
		if(exist(w)) {
				if(Bukkit.getWorld(w) != null) {
					Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.AlreadyExists").replace("%world%", w), s);
				}else
				if(Loader.mw.getStringList("Unloaded-Worlds").contains(w) || Loader.mw.getStringList("Worlds").contains(w)) {
					Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.AlreadyExists").replace("%world%", w), s);
				}else {
			List<String> wws = Loader.mw.getStringList("Deleted-Worlds");
			List<String> worlds = Loader.mw.getStringList("Worlds");
				switch(type) {
				case FLAT:
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, WorldType.FLAT, true, 0);
					break;
				case NETHER:
					TheAPI.getWorldsManager().create(w, Environment.NETHER, WorldType.NORMAL, true, 0);
					break;
				case NORMAL:
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, WorldType.NORMAL, true, 0);
					break;
				case THE_END:
					TheAPI.getWorldsManager().create(w, Environment.THE_END, WorldType.NORMAL, true, 0);
					break;
				case THE_VOID:
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, null, true, 0);
					break;
				}
				wws.remove(w);
				worlds.add(w);
				Loader.mw.set("Deleted-Worlds", wws);
				Loader.mw.set("Worlds", worlds);
				 Configs.mw.save();
				DefaultSet(Bukkit.getWorld(w));
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.WorldImported").replace("%world%", w), s);
		}}else
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.NotFoundImport").replace("%world%", w), s);          
	}
	public static void DefaultSet(World as) {
		Loader.mw.addDefault("WorldsSettings."+as.getName()+".GameMode", "SURVIVAL");
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".Difficulty", "EASY");
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".KeepSpawnInMemory", false);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".AutoSave", true);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".PvP", true);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".CreatePortal", true);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".PortalTeleport", true);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".Spawn.World", as.getName());
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".Generator", "none");

	Loader.mw.addDefault("WorldsSettings."+as.getName()+".MonstersSpawnLimit", 300);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".AnimalsSpawnLimit", 150);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".TicksPerAnimalSpawn", 150);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".TicksPerMonsterSpawn", 1);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".Spawn.X", as.getSpawnLocation().getBlockX());
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".Spawn.Y", as.getSpawnLocation().getBlockY());
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".Spawn.Z", as.getSpawnLocation().getBlockZ());
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".Spawn.X_Pos_Head", 90);
	Loader.mw.addDefault("WorldsSettings."+as.getName()+".Spawn.Z_Pos_Head", 0);
		if(Loader.mw.getString("WorldsSettings."+as.getName()) !=null) {
			String dif = null;
			dif =Loader. mw.getString("WorldsSettings."+as.getName()+".Difficulty").toUpperCase();
			boolean keepspawn = Loader.mw.getBoolean("WorldsSettings."+as.getName()+".KeepSpawnInMemory");
			boolean autosave =Loader. mw.getBoolean("WorldsSettings."+as.getName()+".AutoSave");
			boolean pvp = Loader.mw.getBoolean("WorldsSettings."+as.getName()+".PvP");
			int monster = 0;
			int animal = 0;
			int an=0;
			int mo =0;
			
			if(Loader.mw.getBoolean("ModifyMobsSpawnRates")) {
			 monster = Loader.mw.getInt("WorldsSettings."+as.getName()+".MonstersSpawnLimit");
			 animal = Loader.mw.getInt("WorldsSettings."+as.getName()+".AnimalsSpawnLimit");
			 an = Loader.mw.getInt("WorldsSettings."+as.getName()+".TicksPerAnimalSpawn");
			 mo = Loader.mw.getInt("WorldsSettings."+as.getName()+".TicksPerMonsterSpawn");
			}
			String biome =Loader. mw.getString("WorldsSettings."+as.getName()+".Generator");
			
		if(!dif.equals("EASY")&&!dif.equals("NORMAL")&&!dif.equals("HARD")&&!dif.equals("PEACEFUL")) {
			Loader.getInstance.getLogger().warning("Setting Difficulty of world '"+as.getName()+"' is unknown !");
	    	Loader.getInstance.getLogger().warning("Setting the default difficulty..");
			as.setDifficulty(Difficulty.EASY);
		}
		if(dif.equals("EASY")||dif.equals("NORMAL")||dif.equals("HARD")||dif.equals("PEACEFUL")) {
		as.setDifficulty(Difficulty.valueOf(dif));
		
		}
		if(keepspawn==true||keepspawn==false) {
			as.setKeepSpawnInMemory(keepspawn);
		}
		if(autosave==true||autosave==false) {
			as.setAutoSave(autosave);
		}
		if(pvp==true||pvp==false) {
			as.setPVP(pvp);
		}
		as.setTicksPerAnimalSpawns(an);
		as.setTicksPerMonsterSpawns(mo);
		if(biome.equalsIgnoreCase("FLAT")) {
			as.setBiome(0, 15, Biome.PLAINS);
		}
		if(Loader.mw.getBoolean("ModifyMobsSpawnRates")) {
		as.setMonsterSpawnLimit(monster); //max spawning of monsters (zombies..) is defaulty 300
		as.setAnimalSpawnLimit(animal); //max spawning of animals (cows..) is defaulty 150
		as.setWaterAnimalSpawnLimit(animal/2); //max spawning of water animals (squids..) is defaulty 75
		as.setAmbientSpawnLimit((animal/2)/2); //max spawning of ambient mobs (bats..) is defaulty 37.5
	}}}

	public static void LoadWorlds() {
		List<String> worlds =Loader. mw.getStringList("Worlds");
		if(worlds.isEmpty()==false)
		for(String w:worlds) {
			String biome =Loader. mw.getString("WorldsSettings."+w+".Generator");
			if(biome.equalsIgnoreCase("NETHER")) {
				TheAPI.getWorldsManager().create(w, Environment.NETHER, WorldType.NORMAL, true, 0);
				}
				if(biome.equalsIgnoreCase("THE_END")) {
					TheAPI.getWorldsManager().create(w, Environment.THE_END, WorldType.NORMAL, true, 0);
				}
				if(biome.equalsIgnoreCase("NORMAL")||biome.equalsIgnoreCase("none")) {
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, WorldType.NORMAL, true, 0);
				}
				if(biome.equalsIgnoreCase("FLAT")) {
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, WorldType.FLAT, true, 0);
				}
				if(biome.equalsIgnoreCase("THE_VOID")) {
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, null, true, 0);
					 }
		}
		for(World wa: Bukkit.getWorlds()) {
			DefaultSet(wa);
			}
	}
	public static void LoadWorld(String s, CommandSender sender) {
		List<String> worlds = Loader.mw.getStringList("Worlds");
		List<String> ww = Loader.mw.getStringList("Unloaded-Worlds");
		String biome =Loader. mw.getString("WorldsSettings."+s+".Generator");
		if(biome.equalsIgnoreCase("NETHER")) {
			TheAPI.getWorldsManager().create(s, Environment.NETHER, WorldType.NORMAL, true, 0);
			}
			if(biome.equalsIgnoreCase("THE_END")) {
				TheAPI.getWorldsManager().create(s, Environment.THE_END, WorldType.NORMAL, true, 0);
			}
			if(biome.equalsIgnoreCase("NORMAL")||biome.equalsIgnoreCase("none")) {
				TheAPI.getWorldsManager().create(s, Environment.NORMAL, WorldType.NORMAL, true, 0);
			}
			if(biome.equalsIgnoreCase("FLAT")) {
				TheAPI.getWorldsManager().create(s, Environment.NORMAL, WorldType.FLAT, true, 0);
			}
			if(biome.equalsIgnoreCase("THE_VOID")) {
				TheAPI.getWorldsManager().create(s, Environment.NORMAL, null, true, 0);
				 }
			worlds.add(s);
			ww.remove(s);
			Loader.mw.set("Worlds", worlds);
			Loader.mw.set("Unloaded-Worlds", ww);
			 Configs.mw.save();
			DefaultSet(Bukkit.getWorld(s));
			Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.Loaded").replace("%world%", s), sender);
		}
	public static void CreateWorld(String s, CommandSender sender) {
		String biome =Loader.mw.getString("WorldsSettings."+s+".Generator");
		List<String> wws = Loader.mw.getStringList("Deleted-Worlds");
		List<String> worlds = Loader.mw.getStringList("Worlds");
			if(Bukkit.getWorld(s) != null) {
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.AlreadyCreated").replace("%world%", s), sender);
				
			}
			if(Bukkit.getWorld(s) == null) {
				if(biome.equalsIgnoreCase("NETHER")) {
					TheAPI.getWorldsManager().create(s, Environment.NETHER, WorldType.NORMAL, true, 0);
					}
					if(biome.equalsIgnoreCase("THE_END")) {
						TheAPI.getWorldsManager().create(s, Environment.THE_END, WorldType.NORMAL, true, 0);
					}
					if(biome.equalsIgnoreCase("NORMAL")||biome.equalsIgnoreCase("none")) {
						TheAPI.getWorldsManager().create(s, Environment.NORMAL, WorldType.NORMAL, true, 0);
					}
					if(biome.equalsIgnoreCase("FLAT")) {
						TheAPI.getWorldsManager().create(s, Environment.NORMAL, WorldType.FLAT, true, 0);
					}
					if(biome.equalsIgnoreCase("THE_VOID")) {
						TheAPI.getWorldsManager().create(s, Environment.NORMAL, null, true, 0);
						 }
			wws.remove(s);
			worlds.add(s);
			Loader.mw.set("Deleted-Worlds", wws);
			Loader.mw.set("Worlds", worlds);
			 Configs.mw.save();
			DefaultSet(Bukkit.getWorld(s));
			Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.Created").replace("%world%", s), sender);
		}}
}
